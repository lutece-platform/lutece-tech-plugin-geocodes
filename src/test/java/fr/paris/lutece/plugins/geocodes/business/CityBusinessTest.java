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
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.geocodes.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Optional;


/**
 * This is the business class test for the object City
 */
public class CityBusinessTest extends LuteceTestCase
{
    private static final String CODECOUNTRY1 = "CodeCountry1";
    private static final String CODECOUNTRY2 = "CodeCountry2";
    private static final String CODE1 = "Code1";
    private static final String CODE2 = "Code2";
    private static final String VALUE1 = "Value1";
    private static final String VALUE2 = "Value2";
    private static final String CODEZONE1 = "CodeZone1";
    private static final String CODEZONE2 = "CodeZone2";

	/**
	* test City
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        City city = new City();
        city.setCodeCountry( CODECOUNTRY1 );
        city.setCode( CODE1 );
        city.setValue( VALUE1 );
        city.setCodeZone( CODEZONE1 );

        // Create test
        CityHome.create( city );
        Optional<City> optCityStored = CityHome.findByPrimaryKey( city.getId( ) );
        City cityStored = optCityStored.orElse( new City ( ) );
        assertEquals( cityStored.getCodeCountry( ) , city.getCodeCountry( ) );
        assertEquals( cityStored.getCode( ) , city.getCode( ) );
        assertEquals( cityStored.getValue( ) , city.getValue( ) );
        assertEquals( cityStored.getCodeZone( ) , city.getCodeZone( ) );

        // Update test
        city.setCodeCountry( CODECOUNTRY2 );
        city.setCode( CODE2 );
        city.setValue( VALUE2 );
        city.setCodeZone( CODEZONE2 );
        CityHome.update( city );
        optCityStored = CityHome.findByPrimaryKey( city.getId( ) );
        cityStored = optCityStored.orElse( new City ( ) );
        
        assertEquals( cityStored.getCodeCountry( ) , city.getCodeCountry( ) );
        assertEquals( cityStored.getCode( ) , city.getCode( ) );
        assertEquals( cityStored.getValue( ) , city.getValue( ) );
        assertEquals( cityStored.getCodeZone( ) , city.getCodeZone( ) );

        // List test
        CityHome.getCitiesList( );

        // Delete test
        CityHome.remove( city.getId( ) );
        optCityStored = CityHome.findByPrimaryKey( city.getId( ) );
        cityStored = optCityStored.orElse( null );
        assertNull( cityStored );
        
    }
    
    
     

}