/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
 	
 
package fr.paris.lutece.plugins.geocodes.web;

import fr.paris.lutece.plugins.geocodes.business.CountryChanges;
import fr.paris.lutece.plugins.geocodes.business.GeocodesChangesStatusEnum;
import fr.paris.lutece.plugins.geocodes.service.CsvExportService;
import fr.paris.lutece.plugins.geocodes.utils.Batch;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.geocodes.business.Country;
import fr.paris.lutece.plugins.geocodes.business.CountryHome;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.util.Locale;

/**
 * This class provides the user interface to manage Country features ( manage, create, modify, remove )
 */
@SessionScoped
@Named
@Controller( controllerJsp = "ManageCountries.jsp", controllerPath = "jsp/admin/plugins/geocodes/", right = "GEOCODES_MANAGEMENT", securityTokenEnabled = true )
public class CountryJspBean extends AbstractManageGeoCodesJspBean <Integer, Country>
{
    // Templates
    private static final String TEMPLATE_MANAGE_COUNTRYS = "/admin/plugins/geocodes/manage_countries.html";
    private static final String TEMPLATE_CREATE_COUNTRY = "/admin/plugins/geocodes/create_country.html";
    private static final String TEMPLATE_MODIFY_COUNTRY = "/admin/plugins/geocodes/modify_country.html";

    // Parameters
    private static final String PARAMETER_ID_COUNTRY = "id";
    private static final String PARAMETER_ID_CHANGES = "idChanges";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_COUNTRYS = "geocodes.manage_countries.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_COUNTRY = "geocodes.modify_country.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_COUNTRY = "geocodes.create_country.pageTitle";

    // Markers
    private static final String MARK_COUNTRY_LIST = "country_list";
    private static final String MARK_COUNTRY = "country";

    private static final String JSP_MANAGE_COUNTRYS = "jsp/admin/plugins/geocodes/ManageCountries.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_COUNTRY = "geocodes.message.confirmRemoveCountry";
    private static final String MESSAGE_RESOURCE_NOT_FOUND = "geocodes.message.resourceNotFound";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "geocodes.model.entity.country.attribute.";

    // Views
    private static final String VIEW_CONFIRM_APPLY_COUNTRY_CHANGES = "confirmApplyCountryChanges";
    private static final String VIEW_CONFIRM_DENY_CHANGES = "confirmDenyChanges";
    private static final String MESSAGE_CONFIRM_APPLY_CHANGES = "geocodes.message.confirmApplyChanges";
    private static final String MESSAGE_CONFIRM_DENY_CHANGES = "geocodes.message.confirmDenyChanges";
    private static final String VIEW_MANAGE_COUNTRYS = "manageCountries";
    private static final String VIEW_CREATE_COUNTRY = "createCountry";
    private static final String VIEW_MODIFY_COUNTRY = "modifyCountry";
    private static final String VIEW_CONFIRM_REMOVE_COUNTRY = "confirmRemoveCountry";

    // Actions
    private static final String ACTION_CREATE_COUNTRY = "createCountry";
    private static final String ACTION_MODIFY_COUNTRY = "modifyCountry";
    private static final String ACTION_REMOVE_COUNTRY = "removeCountry";
    private static final String ACTION_APPLY_COUNTRY_CHANGES = "applyCountryChanges";
    private static final String ACTION_DENY_CHANGES = "denyChanges";

    // Infos
    private static final String INFO_COUNTRY_CREATED = "geocodes.info.country.created";
    private static final String INFO_COUNTRY_UPDATED = "geocodes.info.country.updated";
    private static final String INFO_COUNTRY_REMOVED = "geocodes.info.country.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    private static final String ERROR_MULTIPLE_COUNTRIES_RETURNED = "Multiple countries returned";
    
    // Session variable to store working values
    private Country _country;
    private List<Integer> _listIdCountries;

    // Country mapping
    private static final String COUNTRY_CODE = "code";
    private static final String COUNTRY_VALUE = "value";
    private static final String COUNTRY_VALUE_MIN_COMPLETE = "valueMinComplete";
    private static final String COUNTRY_DATE_VALIDITY_START = "date_validity_start";
    private static final String COUNTRY_DATE_VALIDITY_END = "date_validity_end";
    private static final String COUNTRY_ATTACHED = "attached";
    private static final String COUNTRY_DEPRECATED = "deprecated";

    // Export properties
    private static final String EXPORT_FILE_NAME = "countries.zip";
    private static final String EXPORT_CONTENT_TYPE = "application/zip";
    private static final int EXPORT_BATCH_PARTITION_SIZE = AppPropertiesService.getPropertyInt("geocodes.export.countries.batch.size", 100);

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_COUNTRYS, defaultView = true )
    public String getManageCountries( HttpServletRequest request, Models model )
    {
        _country = null;
        final Map<String, String> queryParameters = this.getQueryParameters( request );
        final String countryLabel = queryParameters.get( QUERY_PARAM_INSEE_COUNTRY_LABEL );
        final String countryCode = queryParameters.get( QUERY_PARAM_INSEE_COUNTRY_CODE );
        final boolean approximate = Boolean.parseBoolean( queryParameters.get( QUERY_PARAM_APPROXIMATE ) );

        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) == null || _listIdCountries == null || _listIdCountries.isEmpty( ) )
        {
            _listIdCountries = CountryHome.getIdCountriesList( this.cleanLabel( countryLabel ), countryCode, approximate );
        }

        getPaginatedListModel( request, MARK_COUNTRY_LIST, _listIdCountries, JSP_MANAGE_COUNTRYS, model, null, null,
                this.cleanLabel( countryLabel ), countryCode, null, approximate );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_COUNTRYS, TEMPLATE_MANAGE_COUNTRYS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<Country> getItemsFromIds( List<Integer> listIds ) 
	{
		List<Country> listCountry = new ArrayList<>();

        for( Country country : CountryHome.getCountriesListByIds( listIds ) )
        {
            country.setListChanges(CountryHome.getListChangesFromCountryId(country.getId()));
            country.setPendingChanges(0);
            for(CountryChanges countryChanges : country.getListChanges())
            {
                country.setPendingChanges(country.getPendingChanges() + (Strings.CS.equals(countryChanges.getStatus(), GeocodesChangesStatusEnum.PENDING.toString()) ? 1 : 0));
            }
            listCountry.add( country );
        }
		// keep original order
        return listCountry.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdCountries list
    */
    public void resetListId( )
    {
    	_listIdCountries = new ArrayList<>( );
    }

    /**
     * Returns the form to create a country
     *
     * @param request The Http request
     * @return the html code of the country form
     */
    @View( VIEW_CREATE_COUNTRY )
    public String getCreateCountry( HttpServletRequest request, Models model )
    {
        _country = ( _country != null ) ? _country : new Country(  );

        model.put( MARK_COUNTRY, _country );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_COUNTRY, TEMPLATE_CREATE_COUNTRY, model );
    }

    /**
     * Process the data capture form of a new country
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_COUNTRY )
    public String doCreateCountry( HttpServletRequest request ) throws AccessDeniedException, ParseException
    {
        this.populateCountry( _country, request);
        


        // Check constraints
        if ( !validateBean( _country, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_COUNTRY );
        }

        CountryHome.create( _country );
        CountryHome.addCountryChanges( _country, new Date(), this.getUser().getLastName(), GeocodesChangesStatusEnum.APPLIED.toString());
        addInfo( INFO_COUNTRY_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_COUNTRYS );
    }

    /**
     * Manages the removal form of a country whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_COUNTRY, securityTokenAction = ACTION_REMOVE_COUNTRY )
    public String getConfirmRemoveCountry( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_COUNTRY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_COUNTRY ) );
        url.addParameter( PARAMETER_ID_COUNTRY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_COUNTRY, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a country
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage countries
     */
    @Action( ACTION_REMOVE_COUNTRY )
    public String doRemoveCountry( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_COUNTRY ) );
        
        
        CountryHome.remove( nId );
        addInfo( INFO_COUNTRY_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_COUNTRYS );
    }

    /**
     * Returns the form to update info about a country
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_COUNTRY )
    public String getModifyCountry( HttpServletRequest request, Models model )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_COUNTRY ) );

        if ( _country == null || ( _country.getId(  ) != nId ) )
        {
            Optional<Country> optCountry = CountryHome.findByPrimaryKey( nId );
            if ( optCountry.isEmpty( ) )
            {
                addError( MESSAGE_RESOURCE_NOT_FOUND, getLocale( ) );
                return redirectView( request, VIEW_MANAGE_COUNTRYS );
            }
            _country = optCountry.get( );
        }


        model.put( MARK_COUNTRY, _country );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_COUNTRY, TEMPLATE_MODIFY_COUNTRY, model );
    }

    /**
     * Process the change form of a country
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_COUNTRY )
    public String doModifyCountry( HttpServletRequest request ) throws AccessDeniedException, ParseException
    {
        this.populateCountry( _country, request );
		
		

        // Check constraints
        if ( !validateBean( _country, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_COUNTRY, PARAMETER_ID_COUNTRY, _country.getId( ) );
        }

        CountryHome.update( _country );
        CountryHome.addCountryChanges( _country, new Date(), this.getUser().getLastName(), GeocodesChangesStatusEnum.APPLIED.toString());
        addInfo( INFO_COUNTRY_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_COUNTRYS );
    }

    /**
     * Asks confirmation before applying a pending change of a country.
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the confirmation message
     */
    @View( value = VIEW_CONFIRM_APPLY_COUNTRY_CHANGES, securityTokenAction = ACTION_APPLY_COUNTRY_CHANGES )
    public String getConfirmApplyCountryChanges( HttpServletRequest request )
    {
        return redirectToConfirmation( request, MESSAGE_CONFIRM_APPLY_CHANGES, ACTION_APPLY_COUNTRY_CHANGES, PARAMETER_ID_CHANGES );
    }

    @Action(ACTION_APPLY_COUNTRY_CHANGES)
    public String doApplyCountryModification ( HttpServletRequest request )
    {
        CountryChanges countryChanges = CountryHome.getChangesFromChangesId( Integer.parseInt(request.getParameter(PARAMETER_ID_CHANGES) ) );

        List<Integer> listCityId = new ArrayList<>();
        listCityId.add( countryChanges.getId() );
        List<Country> listCountry = CountryHome.getCountriesListByIds(listCityId);

        if(listCountry.size() == 1 )
        {
            Country country = this.updateCountry(listCountry.get( 0 ), countryChanges);

            CountryHome.update( country );
            countryChanges.setDateLastUpdate(new Date());
            countryChanges.setStatus(GeocodesChangesStatusEnum.APPLIED.toString());
            CountryHome.updateChanges(countryChanges);
        }
        else
        {
            throw new AppException( ERROR_MULTIPLE_COUNTRIES_RETURNED);
        }

        return redirectView( request, VIEW_MANAGE_COUNTRYS );
    }

    /**
     * Asks confirmation before refusing a pending change of a country.
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the confirmation message
     */
    @View( value = VIEW_CONFIRM_DENY_CHANGES, securityTokenAction = ACTION_DENY_CHANGES )
    public String getConfirmDenyChanges( HttpServletRequest request )
    {
        return redirectToConfirmation( request, MESSAGE_CONFIRM_DENY_CHANGES, ACTION_DENY_CHANGES, PARAMETER_ID_CHANGES );
    }

    @Action( ACTION_DENY_CHANGES )
    public String doRefuseModification ( HttpServletRequest request )
    {
        CountryChanges countryChanges = CountryHome.getChangesFromChangesId( Integer.parseInt(request.getParameter(PARAMETER_ID_CHANGES) ) );

        countryChanges.setStatus(GeocodesChangesStatusEnum.REFUSED.toString());
        CountryHome.updateChanges(countryChanges);

        return redirectView( request, VIEW_MANAGE_COUNTRYS );
    }

    /**
     * Streams the countries of the current listing as a zip of CSV files, outside the admin page chrome.
     *
     * @param request
     *            the HTTP request
     * @param response
     *            the HTTP response the zip is written to
     * @return an empty string, the content being written to the response
     * @throws AccessDeniedException
     *             if the user does not have the right of the feature
     */
    public String getExportCountries( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {
        init( request, RIGHT_MANAGEGEOCODES );

        final List<Integer> listIds = ( _listIdCountries == null || _listIdCountries.isEmpty( ) ) ? CountryHome.getIdCountriesList( null, null, true ) : _listIdCountries;
        MVCUtils.addDownloadHeaderToResponse( response, EXPORT_FILE_NAME, EXPORT_CONTENT_TYPE );

        try ( ZipOutputStream zipOut = new ZipOutputStream( response.getOutputStream( ) ) )
        {
            int i = 0;
            for ( final List<Integer> batch : Batch.ofSize( listIds, EXPORT_BATCH_PARTITION_SIZE ) )
            {
                zipOut.putNextEntry( new ZipEntry( "countries-" + ++i + ".csv" ) );
                zipOut.write( CsvExportService.instance( ).writeCountries( CountryHome.getCountriesListByIds( batch ) ) );
                zipOut.closeEntry( );
            }
        }
        catch( final IOException e )
        {
            throw new AppException( e.getMessage( ), e );
        }

        return StringUtils.EMPTY;
    }

    private void populateCountry( final Country country, final HttpServletRequest request ) throws ParseException
    {
        country.setCode( request.getParameter( COUNTRY_CODE ) );
        country.setValue( request.getParameter( COUNTRY_VALUE ) );
        country.setValueMinComplete( request.getParameter( COUNTRY_VALUE_MIN_COMPLETE ) );
        country.setAttached( Objects.equals( request.getParameter( COUNTRY_ATTACHED ), "true" ) );
        final String dateValidityStart = request.getParameter( COUNTRY_DATE_VALIDITY_START );
        country.setDateValidityStart( parseSubmittedDate( dateValidityStart, request.getLocale( ) ) );
        final String dateValidityEnd = request.getParameter( COUNTRY_DATE_VALIDITY_END );
        country.setDateValidityEnd(parseSubmittedDate( dateValidityEnd, request.getLocale( ) ));
        country.setDeprecated( Objects.equals( request.getParameter( COUNTRY_DEPRECATED ), "true" ) );
        
    }

    private Country updateCountry(Country country, CountryChanges changes)
    {
        if(!Strings.CS.equals(country.getCode(), changes.getCode()))
        {
            country.setCode( changes.getCode() );
        }
        if(!Strings.CS.equals(country.getValue(), changes.getValue()))
        {
            country.setValue( changes.getValue() );
        }
        if(!Strings.CS.equals(country.getValueMinComplete(), changes.getValue()))
        {
            country.setValue( changes.getValueMinComplete() );
        }
        if(country.isAttached() != changes.isAttached())
        {
            country.setAttached( changes.isAttached( ) );
        }
        if(!country.getDateValidityStart().equals(changes.getDateValidityStart()))
        {
            country.setDateValidityStart( changes.getDateValidityStart() );
        }
        if(!country.getDateValidityEnd().equals(changes.getDateValidityEnd()))
        {
            country.setDateValidityEnd( changes.getDateValidityEnd() );
        }
        if(country.isDeprecated() != changes.isDeprecated())
        {
            country.setDeprecated( changes.isDeprecated() );
        }

        return country;
    }

    /**
     * Reads a date submitted by the back-office date picker.
     *
     * The v8 picker displays the date in the admin's locale but submits its hidden field in the ISO pattern
     * whatever that locale is, so the ISO pattern is read first. The locale short format stays accepted for a
     * value posted by anything else.
     *
     * @param strDate
     *            the submitted value
     * @param locale
     *            the request locale
     * @return the date, or null when neither pattern matches
     */
    private static Date parseSubmittedDate( String strDate, Locale locale )
    {
        Date date = DateUtil.parseIsoDate( strDate );

        return date != null ? date : DateUtil.formatDate( strDate, locale );
    }

}
