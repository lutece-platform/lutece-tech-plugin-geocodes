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
package fr.paris.lutece.plugins.geocodes.service.cache;

import javax.cache.CacheException;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.util.AppLogService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * geocodes.geoCodeCacheService
 */
@ApplicationScoped
public class GeoCodeCacheService extends AbstractCacheableService<String, Object>
{
    private static final String SERVICE_NAME = "geocodes.geoCodeCacheService";
    private static final String CONST_PREFIX_CITY = "city_";
    private static final String CONST_PREFIX_COUNTRY = "country_";
    private static final String CONST_PREFIX_CITY_CODES = "city_codes_";

    /**
     * Stores a city.
     *
     * @param strKey
     *            the city key
     * @param object
     *            the city
     */
    public void putCityInCache( String strKey, Object object )
    {
        put( CONST_PREFIX_CITY + strKey, object );
    }

    /**
     * Stores the city codes of a date.
     *
     * @param strKey
     *            the date key
     * @param object
     *            the codes
     */
    public void putCitiesCodesByDateInCache( String strKey, Object object )
    {
        put( CONST_PREFIX_CITY_CODES + strKey, object );
    }

    /**
     * Stores a country.
     *
     * @param strKey
     *            the country key
     * @param object
     *            the country
     */
    public void putCountryInCache( String strKey, Object object )
    {
        put( CONST_PREFIX_COUNTRY + strKey, object );
    }

    /**
     * Reads a city.
     *
     * @param strKey
     *            the city key
     * @return the city, or null
     */
    public Object getFromCityCache( String strKey )
    {
        return get( CONST_PREFIX_CITY + strKey );
    }

    /**
     * Reads the city codes of a date.
     *
     * @param strKey
     *            the date key
     * @return the codes, or null
     */
    public Object getFromCitiesCodesByDateInCache( String strKey )
    {
        return get( CONST_PREFIX_CITY_CODES + strKey );
    }

    /**
     * Reads a country.
     *
     * @param strKey
     *            the country key
     * @return the country, or null
     */
    public Object getFromCountryCache( String strKey )
    {
        return get( CONST_PREFIX_COUNTRY + strKey );
    }

    /**
     * Removes a key.
     *
     * @param strKey
     *            the key
     */
    public void removeCache( String strKey )
    {
        remove( strKey );
    }

    /**
     * Creates the cache once the bean is built.
     */
    @PostConstruct
    public void init( )
    {
        initCache( SERVICE_NAME, String.class, Object.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * {@inheritDoc}
     *
     * Guarded: the inherited method dereferences the cache, which is null while the cache is disabled.
     */
    @Override
    public void put( String strKey, Object object )
    {
        if ( isCacheEnable( ) )
        {
            try
            {
                super.put( strKey, object );
            }
            catch( CacheException | IllegalStateException e )
            {
                AppLogService.error( "GeoCodeCacheService : error putting key {} in cache", strKey, e );
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * Guarded: the inherited method dereferences the cache, which is null while the cache is disabled.
     */
    @Override
    public Object get( String strKey )
    {
        if ( isCacheEnable( ) )
        {
            try
            {
                return super.get( strKey );
            }
            catch( CacheException | IllegalStateException e )
            {
                AppLogService.error( "GeoCodeCacheService : error getting key {} from cache", strKey, e );
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * Guarded: the inherited method dereferences the cache, which is null while the cache is disabled.
     */
    @Override
    public boolean remove( String strKey )
    {
        if ( isCacheEnable( ) )
        {
            try
            {
                return super.remove( strKey );
            }
            catch( CacheException | IllegalStateException e )
            {
                AppLogService.error( "GeoCodeCacheService : error removing key {} from cache", strKey, e );
            }
        }
        return false;
    }
}
