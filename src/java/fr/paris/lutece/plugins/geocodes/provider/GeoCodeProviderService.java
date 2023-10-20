/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.plugins.geocodes.provider;

import java.util.List;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * <p>
 * This class is a service for geocodes providers
 * </p>
 * <p>
 * Designed as a singleton
 * </p>
 *
 */
public final class GeoCodeProviderService
{
    private final List<IGeoCodeProvider> _listGeoCodeProviders;

    /**
     * Constructor
     */
    private GeoCodeProviderService( )
    {
        _listGeoCodeProviders = SpringContextService.getBeansOfType( IGeoCodeProvider.class );
    }

    /**
     * Gives the instance of the singleton
     * 
     * @return the instance
     */
    public static GeoCodeProviderService getInstance( )
    {
        return GeoCodeProviderServiceHolder._instance;
    }

    /**
     * Gives the geocodes providers
     * 
     * @return the list of geocodes providers
     */
    public List<IGeoCodeProvider> getGeoCodeProviders( )
    {
        return _listGeoCodeProviders;
    }

    /**
     * Finds a NotifyGru marker provider with the specified id
     * 
     * @param strId
     *            the NotifyGru marker provider id
     * @return the NotifyGru marker provider
     */
    public IGeoCodeProvider find( String strId )
    {
        IGeoCodeProvider geoCodeProvider = null;

        for ( IGeoCodeProvider geoCodeProviderItem : _listGeoCodeProviders )
        {
            if ( geoCodeProviderItem.getId( ).equals( strId ) )
            {
                geoCodeProvider = geoCodeProviderItem;
                break;
            }
        }

        if ( geoCodeProvider == null )
        {
            AppLogService.error( "Unable to find the GeoCodeProvider: " + strId );
        }

        return geoCodeProvider;
    }

    /**
     * This class holds the {@link GeoCodeProviderService} instance
     *
     */
    private static class GeoCodeProviderServiceHolder
    {
        private static GeoCodeProviderService _instance = new GeoCodeProviderService( );
    }
}
