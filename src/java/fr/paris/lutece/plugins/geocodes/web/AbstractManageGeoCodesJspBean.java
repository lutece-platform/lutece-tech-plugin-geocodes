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

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.url.UrlItem;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * ManageGeoCodes JSP Bean abstract class for JSP Bean
 */
public abstract class AbstractManageGeoCodesJspBean <S, T> extends MVCAdminJspBean
{
    // Rights
    public static final String RIGHT_MANAGEGEOCODES = "GEOCODES_MANAGEMENT";
    
    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "geocodes.listItems.itemsPerPage";
    
    // Parameters
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // Infos
    public static final String QUERY_PARAM_INSEE_CITY_LABEL = "insee_city_label";
    public static final String QUERY_PARAM_INSEE_CITY_CODE = "insee_city";
    public static final String QUERY_PARAM_INSEE_COUNTRY_LABEL = "insee_country_label";
    public static final String QUERY_PARAM_INSEE_COUNTRY_CODE = "insee_country";
    public static final String QUERY_PARAM_INSEE_PLACE_CODE = "insee_place";

    // Markers
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    //Variables
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Return a model that contains the list and paginator infos
     * @param request The HTTP request
     * @param strBookmark The bookmark
     * @param list The list of item
     * @param strManageJsp The JSP
     * @return The model
     */
    protected <T> Map<String, Object> getPaginatedListModel( HttpServletRequest request, String strBookmark, List<S> list,
        String strManageJsp, String cityLabel, String cityCode, String countryLabel, String countryCode, String placeCode )
    {
        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, nDefaultItemsPerPage );

        UrlItem url = new UrlItem( strManageJsp );
        String strUrl = url.getUrl(  );

        // PAGINATOR
        LocalizedPaginator<S> paginator = new LocalizedPaginator<>( list, _nItemsPerPage, strUrl, PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        Map<String, Object> model = getModel(  );

        model.put( QUERY_PARAM_INSEE_CITY_LABEL, cityLabel );
        model.put( QUERY_PARAM_INSEE_CITY_CODE, cityCode );
        model.put( QUERY_PARAM_INSEE_COUNTRY_LABEL, countryLabel );
        model.put( QUERY_PARAM_INSEE_COUNTRY_CODE, countryCode );
        model.put( QUERY_PARAM_INSEE_PLACE_CODE, placeCode );
        model.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
        model.put( MARK_PAGINATOR, paginator );
        model.put( strBookmark, getItemsFromIds ( paginator.getPageItems( ) ) );

        return model;
    }
    
    /**
     * Get Items from Ids list
     * @param <T>
     *
     * @param <S> the generic type of the Ids
     * @param <T> the generic type of the items
     * @param <S>
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
     abstract  List<T> getItemsFromIds ( List<S> listIds ) ;

    /**
     * Return the possible parameters that can be searched
     * @param request
     * @return
     */
    protected Map<String, String> getQueryParameters( final HttpServletRequest request )
    {
        final Map<String, String> parameters = new HashMap<>( );
        final String cityLabel = request.getParameter( QUERY_PARAM_INSEE_CITY_LABEL );
        if ( cityLabel != null )
        {
            parameters.put( QUERY_PARAM_INSEE_CITY_LABEL, cityLabel );
        }
        final String cityCode = request.getParameter( QUERY_PARAM_INSEE_CITY_CODE );
        if ( cityCode != null )
        {
            parameters.put( QUERY_PARAM_INSEE_CITY_CODE, cityCode );
        }
        final String countryLabel = request.getParameter( QUERY_PARAM_INSEE_COUNTRY_LABEL );
        if ( countryLabel != null )
        {
            parameters.put( QUERY_PARAM_INSEE_COUNTRY_LABEL, countryLabel );
        }
        final String countryCode = request.getParameter( QUERY_PARAM_INSEE_COUNTRY_CODE );
        if ( countryCode != null )
        {
            parameters.put( QUERY_PARAM_INSEE_COUNTRY_CODE, countryCode );
        }
        final String placeCode = request.getParameter( QUERY_PARAM_INSEE_PLACE_CODE );
        if ( placeCode != null )
        {
            parameters.put( QUERY_PARAM_INSEE_PLACE_CODE, placeCode );
        }
        return parameters;
    }

    /**
     * Clear the parameters list
     * @param request
     */
    protected void clearParameters( final HttpServletRequest request )
    {
        final String cityLabel = request.getParameter(QUERY_PARAM_INSEE_CITY_LABEL);
        if (cityLabel != null)
        {
            request.removeAttribute(QUERY_PARAM_INSEE_CITY_LABEL);
        }
        final String cityCode = request.getParameter(QUERY_PARAM_INSEE_CITY_CODE);
        if (cityCode != null)
        {
            request.removeAttribute(QUERY_PARAM_INSEE_CITY_CODE);
        }
        final String countryLabel = request.getParameter(QUERY_PARAM_INSEE_COUNTRY_LABEL);
        if (countryLabel != null)
        {
            request.removeAttribute(QUERY_PARAM_INSEE_COUNTRY_LABEL);
        }
        final String countryCode = request.getParameter(QUERY_PARAM_INSEE_COUNTRY_CODE);
        if (countryCode != null)
        {
            request.removeAttribute(QUERY_PARAM_INSEE_COUNTRY_CODE);
        }
        final String placeCode = request.getParameter(QUERY_PARAM_INSEE_PLACE_CODE);
        if (placeCode != null)
        {
            request.removeAttribute(QUERY_PARAM_INSEE_PLACE_CODE);
        }
    }

    protected String cleanLabel(String label)
    {
        if(label != null)
        {
            return StringUtils.toRootUpperCase(StringUtils.stripAccents(label.replace('-', ' ')));
        }
        else {
            return null;
        }
    }
}