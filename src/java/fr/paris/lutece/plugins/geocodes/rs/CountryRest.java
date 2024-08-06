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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.geocodes.business.Country;
import fr.paris.lutece.plugins.geocodes.service.GeoCodesService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

/**
 * CountryRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.COUNTRY_PATH )
public class CountryRest
{
    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;
    
    /**
     * Get Country List
     * @param nVersion the API version
     * @return the Country List
     */
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCountryList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return getCountryListV1( );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Country List V1
     * @return the Country List for the version 1
     */
    private Response getCountryListV1( )
    {
    	GeoCodesService geoCodesService = GeoCodesService.getInstance( );
    	Date date = new Date();
    	List<Country> listCountrys = geoCodesService.getCountriesListByName( "%", date );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( listCountrys ) ) )
                .build( );
    }
    
 
    /**
     * Get Country
     * @param nVersion the API version
     * @param id the code
     * @return the Country
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCountry(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) String id )
    {
        if ( nVersion == VERSION_1 )
        {
            return getCountryV1( id );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Country V1
     * @param id the id
     * @return the Country for the version 1
     */
    private Response getCountryV1( String code )
    {
    	GeoCodesService geoCodesService = GeoCodesService.getInstance( );
        Optional<Country> optCountry = geoCodesService.getCountryByCode( code, false );
        if ( !optCountry.isPresent( ) )
        {
            AppLogService.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( optCountry.get( ) ) ) )
                .build( );
    }
    
    /**
     * Search Countries
     * @param nVersion the API version
     * @param search the searched string
     * @return the Countries
     */
    @GET
    @Path( Constants.SEARCH_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCountiesByName(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.SEARCHED_STRING ) String strVal,
    @QueryParam( Constants.DATE ) String strDateCountry )
    {
    	if ( strDateCountry == null || strDateCountry.isEmpty( ) )
        {
        	AppLogService.error( Constants.ERROR_DATE_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_DATE_RESOURCE ) ) )
                    .build( );
        }
    	
    	DateFormat formatter = null;
    	if ( nVersion == VERSION_1 )
        {
        	formatter = new SimpleDateFormat( Constants.FORMAT_DATE_REF_V1 ); 
        }
        else if ( nVersion == VERSION_2 )
        {
        	formatter = new SimpleDateFormat( Constants.FORMAT_DATE_REF_V2 ); 
        }
    	if ( formatter != null )
    	{ 
        	Date dateref = new Date( );
			try {
				dateref = (Date)formatter.parse(strDateCountry);
			} catch (ParseException e) {
				AppLogService.error( Constants.ERROR_FORMAT_DATE_RESOURCE );
	            return Response.status( Response.Status.NOT_FOUND )
	                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_FORMAT_DATE_RESOURCE ) ) )
	                    .build( );
			}
        	return getCountriesByNameV1( strVal, dateref );
        }
        AppLogService.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Country V1
     * @param id the id
     * @return the Country for the version 1
     */
    private Response getCountriesByNameV1( String strSearchBeginningVal, Date dateRef )
    {
        if ( strSearchBeginningVal == null || strSearchBeginningVal.length( ) < 3 )
        {
            AppLogService.error( Constants.ERROR_SEARCH_STRING );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_SEARCH_STRING ) ) )
                    .build( );
        }
        
        GeoCodesService geoCodesService = GeoCodesService.getInstance( );
        List<Country> listCountries = geoCodesService.getCountriesListByName( strSearchBeginningVal, dateRef );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( listCountries ) ) )
                .build( );
    }
}