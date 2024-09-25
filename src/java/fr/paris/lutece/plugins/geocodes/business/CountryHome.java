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

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for Country objects
 */
public final class CountryHome
{
    // Static variable pointed at the DAO instance
    private static ICountryDAO _dao = SpringContextService.getBean( "geocodes.countryDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "geocodes" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CountryHome(  )
    {
    }

    /**
     * Create an instance of the country class
     * @param country The instance of the Country which contains the informations to store
     * @return The  instance of country which has been created with its primary key.
     */
    public static Country create( Country country )
    {
        _dao.insert( country, _plugin );

        return country;
    }

    /**
     * Update of the country which is specified in parameter
     * @param country The instance of the Country which contains the data to store
     * @return The instance of the  country which has been updated
     */
    public static Country update( Country country )
    {
        _dao.store( country, _plugin );

        return country;
    }

    /**
     * Remove the country whose identifier is specified in parameter
     * @param nKey The country Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a country whose identifier is specified in parameter
     * @param nKey The country primary key
     * @return an instance of Country
     */
    public static Optional<Country> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a country whose identifier is specified in parameter
     * @param nKey The country primary key
     * @return an instance of Country
     */
    public static Optional<Country> findByCode( String strCode )
    {
        return _dao.loadByCode( strCode, _plugin );
    }
    
    /**
     * Returns an instance of a country whose identifier is specified in parameter
     * @param strCode The country primary key
     * @param bAttached is the  country attached
     * @return an instance of Country
     */
    public static Optional<Country> findByCode( String strCode, boolean bAttached )
    {
        return _dao.loadByCode( strCode, bAttached, _plugin );
    }
    
    /**
     * Load the data of all the country objects and returns them as a list
     * @return the list which contains the data of all the country objects
     */
    public static List<Country> getCountriesList( )
    {
        return _dao.selectCountriesList( _plugin );
    }
 
    /**
     * Load the data of all the country objects and returns them as a list
     * @return the list which contains the data of all the country objects
     */
    public static List<Country> getCountriesListByName( String strSearch, Date dateRef )
    {
        return _dao.selectCountriesListByValue( strSearch, dateRef, _plugin );
    }

    
    /**
     * Load the id of all the country objects and returns them as a list
     * @return the list which contains the id of all the country objects
     */
    public static List<Integer> getIdCountriesList( String cityLabel, String cityCode, String countryLabel, String countryCode, String placeCode )
    {
        return _dao.selectIdCountriesList( _plugin, cityLabel, cityCode, countryLabel, countryCode,  placeCode );
    }
    
    /**
     * Load the data of all the country objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the country objects
     */
    public static ReferenceList getCountriesReferenceList( )
    {
        return _dao.selectCountriesReferenceList( _plugin );
    }
    
	
    /**
     * Load the data of all the avant objects and returns them as a list
     * @param listIds liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<Country> getCountriesListByIds( List<Integer> listIds )
    {
        return _dao.selectCountriesListByIds( _plugin, listIds );
    }

}

