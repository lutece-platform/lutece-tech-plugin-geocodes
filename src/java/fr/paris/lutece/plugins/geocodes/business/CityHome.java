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


 package fr.paris.lutece.plugins.geocodes.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;


import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for City objects
 */
public final class CityHome
{
    // Static variable pointed at the DAO instance
    private static ICityDAO _dao = SpringContextService.getBean( "geocodes.cityDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "geocodes" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CityHome(  )
    {
    }

    /**
     * Create an instance of the city class
     * @param city The instance of the City which contains the informations to store
     * @return The  instance of city which has been created with its primary key.
     */
    public static City create( City city )
    {
        _dao.insert( city, _plugin );

        return city;
    }

    /**
     * Update of the city which is specified in parameter
     * @param city The instance of the City which contains the data to store
     * @return The instance of the  city which has been updated
     */
    public static City update( City city )
    {
        _dao.store( city, _plugin );

        return city;
    }

    /**
     * Remove the city whose identifier is specified in parameter
     * @param nKey The city Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a city whose identifier is specified in parameter
     * @param nKey The city primary key
     * @return an instance of City
     */
    public static Optional<City> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a city whose identifier is specified in parameter
     * @param nKey The city primary key
     * @return an instance of City
     */
    public static Optional<City> findByCode( String strCode )
    {
        return _dao.loadByCode( strCode, _plugin );
    }

    /**
     * Load the data of all the city objects and returns them as a list
     * @return the list which contains the data of all the city objects
     */
    public static List<City> getCitiesList( )
    {
        return _dao.selectCitiesList( _plugin );
    }
    
    /**
     * Load the data of all the city objects and returns them as a list
     * @return the list which contains the data of all the city objects
     */
    public static List<City> getCitiesListByName( String strVal )
    {
        return _dao.selectCitiesListByValue( strVal, _plugin );
    }
    
    /**
     * Load the id of all the city objects and returns them as a list
     * @return the list which contains the id of all the city objects
     */
    public static List<Integer> getIdCitiesList( )
    {
        return _dao.selectIdCitiesList( _plugin );
    }
    
    /**
     * Load the data of all the city objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the city objects
     */
    public static ReferenceList getCitiesReferenceList( )
    {
        return _dao.selectCitiesReferenceList( _plugin );
    }
    
	
    /**
     * Load the data of all the avant objects and returns them as a list
     * @param listIds liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<City> getCitiesListByIds( List<Integer> listIds )
    {
        return _dao.selectCitiesListByIds( _plugin, listIds );
    }

}

