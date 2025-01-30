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

/**
 * Rest Constants
 */
public final class Constants 
{
    public static final String API_PATH = "geocodes/api";
    public static final String VERSION_PATH = "/v{" + Constants.VERSION + "}";
    public static final String ID_PATH = "/{" + Constants.ID + "}";
    public static final String VERSION = "version";
    public static final String ID = "id";
    public static final String SEARCH_PATH = "/search/{" + Constants.SEARCHED_STRING + "}";
    public static final String SEARCH_DATE_PATH = "/searchdate/{" + Constants.SEARCHED_STRING + "}";
    public static final String SEARCH_LIKE = "/list";
    public static final String SEARCHED_STRING = "search";
    public static final String SEARCH_PATH_COUNTRY = "/search";
    public static final String VALUE_STRING = "value";
    public static final String DATE = "dateref";
    public static final String SEARCH_DATE_AND_CODE = "/{" + Constants.ID + "}";
    
    public static final String SWAGGER_DIRECTORY_PATH = "/plugins/";
    public static final String SWAGGER_PATH = "/swagger";
    public static final String SWAGGER_VERSION_PATH = "/v";
    public static final String SWAGGER_REST_PATH = "rest/";
    public static final String SWAGGER_JSON = "/swagger.json";
    
    public static final String EMPTY_OBJECT = "{}";
    public static final String ERROR_NOT_FOUND_VERSION = "Version not found";
    public static final String ERROR_NOT_FOUND_RESOURCE = "Resource not found";
    public static final String ERROR_FORMAT_DATE_RESOURCE = "Date not in correct format yyyy-MM-dd";
    public static final String ERROR_SEARCH_STRING = "Search string must contain 3 chars at least";
    public static final String ERROR_BAD_REQUEST_EMPTY_PARAMETER = "Empty parameter";
    
    public static final String CITY_PATH = "/cities";
    public static final String CITY_CODES_PATH = "/codes";
    public static final String CITY_ATTRIBUTE_CODE_COUNTRY = "code_country";
    public static final String CITY_ATTRIBUTE_CODE = "code";
    public static final String CITY_ATTRIBUTE_VALUE = "value";
    public static final String CITY_ATTRIBUTE_CODE_ZONE = "code_zone";
    
    public static final String COUNTRY_PATH = "/countries";
    public static final String COUNTRY_ATTRIBUTE_CODE = "code";
    public static final String COUNTRY_ATTRIBUTE_VALUE = "value";
    
    public static final String ID_PROVIDER_GEOCODE_LOCAL = "geocodes.geoCodeProviderLocal";
    public static final String ID_PROVIDER_GEOCODE_INSEE = "geocodes.geoCodeProviderINSEE";
    
    public static final String CONST_DATE_MIN = "1943-01-01";
    public static final String FORMAT_DATE_REF_V1 = "yyyy-MM-DD";
    public static final String FORMAT_DATE_REF_V2 = "yyyy-MM-dd";
    public static final String ERROR_DATE_RESOURCE = "The date dateRef must be entered";  
    
    /**
     * Private constructor
     */
    private Constants(  )
    {
    }
}
