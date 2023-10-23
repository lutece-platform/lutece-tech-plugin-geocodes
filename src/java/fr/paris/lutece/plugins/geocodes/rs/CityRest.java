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

package fr.paris.lutece.plugins.geocodes.rs;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.service.GeoCodesService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

/**
 * CityRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.CITY_PATH )
public class CityRest
{
    private static final int VERSION_1 = 1;
    
    
    /**
     * Get City List with date
     * @param nVersion the API version
     * @return the City List
     */
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCityListByDate( @PathParam( Constants.VERSION ) Integer nVersion,
    							@QueryParam( Constants.SEARCHED_STRING ) String strVal,
    							@QueryParam( Constants.DATE ) Date dateCity ) 
    {
        if ( nVersion == VERSION_1 )
        {
            return getCityListV1ByNameAndDate( strVal, dateCity);
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get City List V1
     * @return the City List for the version 1
     */
    private Response getCityListV1ByNameAndDate( String strSearchBeginningVal, Date dateCity )
    {
        if ( strSearchBeginningVal == null || strSearchBeginningVal.length( ) < 3 )
        {
            AppLogService.error( Constants.ERROR_SEARCH_STRING );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_SEARCH_STRING ) ) )
                    .build( );
        }
        
        GeoCodesService geoCodesService = GeoCodesService.getInstance( );
        List<City> lstCities = new ArrayList<>( );
        lstCities = geoCodesService.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
        
        if ( lstCities.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
        }
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( lstCities ) ) )
                .build( );
    }
    
    /**
     * Get City by date
     * @param nVersion the API version
     * @param id the id
     * @return the City
     */
    @GET
    @Path( Constants.SEARCH_DATE_AND_CODE )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCityByDate(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) String code,
    @QueryParam( Constants.DATE ) Date dateCity )
    {
        if ( nVersion == VERSION_1 )
        {
            return getCityByDateAndCodeV1( dateCity, code );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get City V1
     * @param strCode the code of the city
     * @param dateCity the date
     * @return the City for the version 1
     */
    private Response getCityByDateAndCodeV1( Date dateCity, String strCode )
    {
    	Optional<City> optCity = Optional.empty();
    	GeoCodesService geoCodesService = GeoCodesService.getInstance( );
    	optCity = geoCodesService.getCityByDateAndCode ( dateCity, strCode );
    	
        if ( !optCity.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( optCity.get( ) ) ) )
                .build( );
    }
}