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

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;

/**
 * This class provides the user interface to manage City features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCities.jsp", controllerPath = "jsp/admin/plugins/geocodes/", right = "GEOCODES_MANAGEMENT" )
public class CityJspBean extends AbstractManageGeoCodesJspBean <Integer, City>
{
    // Templates
    private static final String TEMPLATE_MANAGE_CITYS = "/admin/plugins/geocodes/manage_cities.html";
    private static final String TEMPLATE_CREATE_CITY = "/admin/plugins/geocodes/create_city.html";
    private static final String TEMPLATE_MODIFY_CITY = "/admin/plugins/geocodes/modify_city.html";

    // Parameters
    private static final String PARAMETER_ID_CITY = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CITYS = "geocodes.manage_cities.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CITY = "geocodes.modify_city.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CITY = "geocodes.create_city.pageTitle";

    // Markers
    private static final String MARK_CITY_LIST = "city_list";
    private static final String MARK_CITY = "city";

    private static final String JSP_MANAGE_CITYS = "jsp/admin/plugins/geocodes/ManageCities.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CITY = "geocodes.message.confirmRemoveCity";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "geocodes.model.entity.city.attribute.";

    // Views
    private static final String VIEW_MANAGE_CITYS = "manageCities";
    private static final String VIEW_CREATE_CITY = "createCity";
    private static final String VIEW_MODIFY_CITY = "modifyCity";

    // Actions
    private static final String ACTION_CREATE_CITY = "createCity";
    private static final String ACTION_MODIFY_CITY = "modifyCity";
    private static final String ACTION_REMOVE_CITY = "removeCity";
    private static final String ACTION_CONFIRM_REMOVE_CITY = "confirmRemoveCity";

    // Infos
    private static final String INFO_CITY_CREATED = "geocodes.info.city.created";
    private static final String INFO_CITY_UPDATED = "geocodes.info.city.updated";
    private static final String INFO_CITY_REMOVED = "geocodes.info.city.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private City _city;
    private List<Integer> _listIdCities;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CITYS, defaultView = true )
    public String getManageCities( HttpServletRequest request )
    {
        _city = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdCities.isEmpty( ) )
        {
        	_listIdCities = CityHome.getIdCitiesList(  );
        }
        
        Map<String, Object> model = getPaginatedListModel( request, MARK_CITY_LIST, _listIdCities, JSP_MANAGE_CITYS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CITYS, TEMPLATE_MANAGE_CITYS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<City> getItemsFromIds( List<Integer> listIds ) 
	{
		List<City> listCity = CityHome.getCitiesListByIds( listIds );
		
		// keep original order
        return listCity.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdCities list
    */
    public void resetListId( )
    {
    	_listIdCities = new ArrayList<>( );
    }

    /**
     * Returns the form to create a city
     *
     * @param request The Http request
     * @return the html code of the city form
     */
    @View( VIEW_CREATE_CITY )
    public String getCreateCity( HttpServletRequest request )
    {
        _city = ( _city != null ) ? _city : new City(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CITY, _city );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_CITY ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CITY, TEMPLATE_CREATE_CITY, model );
    }

    /**
     * Process the data capture form of a new city
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_CITY )
    public String doCreateCity( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _city, request, getLocale( ) );
        

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_CITY ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _city, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CITY );
        }

        CityHome.create( _city );
        addInfo( INFO_CITY_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_CITYS );
    }

    /**
     * Manages the removal form of a city whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CITY )
    public String getConfirmRemoveCity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CITY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CITY ) );
        url.addParameter( PARAMETER_ID_CITY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CITY, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a city
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage cities
     */
    @Action( ACTION_REMOVE_CITY )
    public String doRemoveCity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CITY ) );
        
        
        CityHome.remove( nId );
        addInfo( INFO_CITY_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_CITYS );
    }

    /**
     * Returns the form to update info about a city
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CITY )
    public String getModifyCity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CITY ) );

        if ( _city == null || ( _city.getId(  ) != nId ) )
        {
            Optional<City> optCity = CityHome.findByPrimaryKey( nId );
            _city = optCity.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }


        Map<String, Object> model = getModel(  );
        model.put( MARK_CITY, _city );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_CITY ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CITY, TEMPLATE_MODIFY_CITY, model );
    }

    /**
     * Process the change form of a city
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_CITY )
    public String doModifyCity( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _city, request, getLocale( ) );
		
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_CITY ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _city, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CITY, PARAMETER_ID_CITY, _city.getId( ) );
        }

        CityHome.update( _city );
        addInfo( INFO_CITY_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_CITYS );
    }
}
