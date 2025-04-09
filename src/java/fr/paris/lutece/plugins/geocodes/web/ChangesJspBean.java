package fr.paris.lutece.plugins.geocodes.web;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityChanges;
import fr.paris.lutece.plugins.geocodes.business.CityGroup;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
import fr.paris.lutece.plugins.geocodes.business.GeocodesChangesStatusEnum;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.AbstractPaginator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller( controllerJsp = "ManageChanges.jsp", controllerPath = "jsp/admin/plugins/geocodes/", right = "GEOCODES_MANAGEMENT" )
public class ChangesJspBean extends AbstractManageGeoCodesJspBean <String, CityGroup>
{

    // Templates
    private static final String TEMPLATE_MANAGE_CHANGES = "/admin/plugins/geocodes/manage_changes.html";

    // Parameters
    private static final String PARAMETER_CODE_CHANGES = "codeChanges";
    private static final String PARAMETER_DATE_CHANGES = "dateChanges";

    // Views
    private static final String VIEW_MANAGE_CHANGES = "manageChanges";

    //Actions
    private static final String ACTION_APPLY_CITY_CHANGES = "applyCityChanges";
    private static final String ACTION_DENY_CHANGES = "denyChanges";
    private static final String ACTION_APPLY_NEW_CITY = "applyNewCity";
    private static final String ACTION_DENY_NEW_CITY = "denyNewCity";

    // Errors
    private static final String ERROR_NO_CITY_RETURNED = "No city returned";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CHANGES = "geocodes.manage_changes.pageTitle";
    private static final String PROPERTY_TITLE_MANAGE_CHANGES = "geocodes.manage_changes.title";
    private static final String PROPERTY_PAGE_TAB = "changes";
    private static final String PROPERTY_PAGE_SEARCH = "ManageChanges";

    // Markers
    private static final String MARK_CITY_LIST = "city_group_list";
    private static final String MARK_PAGE_TITLE = "page_title";
    private static final String MARK_PAGE_TAB = "page_tab";
    private static final String MARK_PAGE = "page";
    private static final String MARK_STATUS_LIST = "status_list";
    private static final String MARK_STATUS_SEARCH = "status_search";

    private static final String JSP_MANAGE_CHANGES = "jsp/admin/plugins/geocodes/ManageChanges.jsp";

    private List<String> _listCityCodesChanges = new ArrayList<>();
    private String _status;

    @View( value = VIEW_MANAGE_CHANGES, defaultView = true )
    public String getManageChanges( HttpServletRequest request )
    {
        final Map<String, String> queryParameters = this.getQueryParameters( request );
        final String cityLabel = queryParameters.get( QUERY_PARAM_INSEE_CITY_LABEL );
        final String cityCode = queryParameters.get( QUERY_PARAM_INSEE_CITY_CODE );
        final String placeCode = queryParameters.get( QUERY_PARAM_INSEE_PLACE_CODE );
        _status = (queryParameters.get( QUERY_PARAM_STATUS_SEARCH ) == null ? GeocodesChangesStatusEnum.PENDING.name() : queryParameters.get( QUERY_PARAM_STATUS_SEARCH ) );
        final boolean approximate = Boolean.parseBoolean( queryParameters.get( QUERY_PARAM_APPROXIMATE ) );

        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) == null || _listCityCodesChanges.isEmpty( ) )
        {
            _listCityCodesChanges = CityHome.getAllCitiesWithChanges( this.cleanLabel( cityLabel ), cityCode, placeCode, approximate, _status );
        }

        final Map<String, Object> model = getPaginatedListModel( request, MARK_CITY_LIST, _listCityCodesChanges, JSP_MANAGE_CHANGES, this.cleanLabel( cityLabel ),
                cityCode, null, null, placeCode, approximate );

        model.put(MARK_PAGE_TITLE, "#i18n{"+PROPERTY_TITLE_MANAGE_CHANGES+"}");
        model.put(MARK_PAGE_TAB, PROPERTY_PAGE_TAB);
        model.put(MARK_PAGE, PROPERTY_PAGE_SEARCH);
        model.put(MARK_STATUS_SEARCH, _status);
        model.put(MARK_STATUS_LIST, Arrays.stream(GeocodesChangesStatusEnum.values()).collect(Collectors.toList()));

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CHANGES, TEMPLATE_MANAGE_CHANGES, model );
    }

    @Override
    List<CityGroup> getItemsFromIds(List<String> listCityCodesChanges)
    {
        List<CityGroup> cityGroupList = new ArrayList<>(  );

        listCityCodesChanges.forEach( code -> {
            CityGroup cityGroup = new CityGroup( );
            cityGroup.setCode( code );

            List<City> cityList = CityHome.getCitiesListByCode( code );
            List<CityChanges> cityChangesList = CityHome.getChangesListByCode( code );
            cityGroup.setCodeZone( cityList.isEmpty() ? null : cityList.get( 0 ).getCodeZone( ) );
            cityList.forEach( city -> {
                List<CityChanges> cityChangesListCopy = new ArrayList<>(cityChangesList);
                int iterator = 0;
                for( CityChanges cityChanges : cityChangesListCopy )
                {
                    if(StringUtils.equals( city.getCode(), cityChanges.getCode() ) && city.getDateValidityStart().compareTo(cityChanges.getDateValidityStart()) == 0 )
                    {
                        city.setCityChanges(cityChanges);
                        cityChangesList.remove( iterator );
                        cityGroup.setPendingChanges(cityGroup.getPendingChanges() + (StringUtils.equals(cityChanges.getStatus(), GeocodesChangesStatusEnum.PENDING.toString()) ? 1 : 0));
                    }
                    iterator++;
                }
            });
            cityGroup.setCityList(cityList);
            cityGroup.setNewCities(new ArrayList<>());
            if(!cityChangesList.isEmpty())
            {
                cityGroup.getNewCities().addAll(cityChangesList);
                cityGroup.setPendingChanges(cityGroup.getPendingChanges() + cityChangesList.size());
            }
            cityGroupList.add( cityGroup );
        });

        return cityGroupList;
    }

    @Action(ACTION_APPLY_CITY_CHANGES)
    public String doApplyCityModification ( HttpServletRequest request )
    {
        String code = request.getParameter(PARAMETER_CODE_CHANGES);
        Date date = this.extractDate(request.getParameter(PARAMETER_DATE_CHANGES));
        CityChanges cityChanges = CityHome.getCityChangesByCodeAndDate( code, date  );

        City city = CityHome.getCityByCodeAndDate( code, date );

        if(city != null )
        {
            city = this.updateCity(city, cityChanges);

            CityHome.update( city );
            cityChanges.setDateLastUpdate(new Date());
            cityChanges.setAuthor(this.getUser( ).getEmail( ) );
            cityChanges.setStatus(GeocodesChangesStatusEnum.APPLIED.toString());
            CityHome.updateChanges(cityChanges);
        }
        else
        {
            throw new AppException(ERROR_NO_CITY_RETURNED);
        }

        return redirectView( request, VIEW_MANAGE_CHANGES);
    }

    @Action( ACTION_DENY_CHANGES )
    public String doRefuseModification ( HttpServletRequest request )
    {
        String code = request.getParameter(PARAMETER_CODE_CHANGES);
        Date date = this.extractDate(request.getParameter(PARAMETER_DATE_CHANGES));
        CityChanges cityChanges = CityHome.getCityChangesByCodeAndDate( code, date  );

        cityChanges.setStatus(GeocodesChangesStatusEnum.REFUSED.toString());
        CityHome.updateChanges(cityChanges);

        return redirectView( request, VIEW_MANAGE_CHANGES);
    }

    @Action( ACTION_APPLY_NEW_CITY )
    public String doApplyNewCity ( HttpServletRequest request )
    {
        String code = request.getParameter(PARAMETER_CODE_CHANGES);
        Date date = this.extractDate(request.getParameter(PARAMETER_DATE_CHANGES));
        CityChanges cityChanges = CityHome.getCityChangesByCodeAndDate( code, date  );

        List<City> cityList = CityHome.getCitiesListByCode( code );
        cityList.sort(Comparator.comparing(City::getDateValidityStart));
        City oldCity = cityList.get( cityList.size() - 1 );
        oldCity.setDateValidityEnd( cityChanges.getDateValidityStart());
        oldCity.setDateLastUpdate(new Date());
        CityHome.update( oldCity );
        CityHome.addCityChanges( oldCity, this.getUser( ).getEmail( ), GeocodesChangesStatusEnum.APPLIED.toString() );

        cityChanges.setDateLastUpdate(new Date());
        CityHome.create( cityChanges );
        cityChanges.setAuthor(this.getUser( ).getEmail( ) );
        cityChanges.setStatus(GeocodesChangesStatusEnum.APPLIED.toString());
        CityHome.updateChanges(cityChanges);

        return redirectView( request, VIEW_MANAGE_CHANGES);
    }

    @Action( ACTION_DENY_NEW_CITY )
    public String doDenyNewCity ( HttpServletRequest request )
    {
        String code = request.getParameter(PARAMETER_CODE_CHANGES);
        Date date = this.extractDate(request.getParameter(PARAMETER_DATE_CHANGES));
        CityChanges cityChanges = CityHome.getCityChangesByCodeAndDate( code, date  );

        cityChanges.setDateLastUpdate(new Date());
        cityChanges.setStatus(GeocodesChangesStatusEnum.REFUSED.toString());
        CityHome.updateChanges(cityChanges);

        return redirectView( request, VIEW_MANAGE_CHANGES);
    }
}
