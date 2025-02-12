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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides Data Access methods for City objects
 */
public final class CityDAO implements ICityDAO
{
    // Constants
	private static final String SQL_QUERY_SELECT_BY_ID = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE id_city = ?";
	private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE code = ? AND date_validity_start <= ? AND date_validity_end > ? ";
	private static final String SQL_QUERY_SELECT_BETWEEN_DATE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE date_validity_start <= ? AND date_validity_end > ? and code = ? ";
	private static final String SQL_QUERY_SELECT_BY_VALUE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE LOWER(value) = LOWER(?) AND date_validity_start <= ? AND date_validity_end > ? order and deprecated = 0 by value ";
	private static final String SQL_QUERY_SELECT_BY_VALUE_LIKE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE ( LOWER(value_min) like LOWER(?) OR LOWER(value_min_complete) like LOWER(?) ) AND date_validity_start <= ? AND date_validity_end > ? and deprecated = 0 order by value ";
	private static final String SQL_QUERY_SELECT_BY_VALUE_AND_DATE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE  TRANSLATE(REPLACE(REPLACE(LOWER(value), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ', 'aaaeeeeiioouuuycn') = TRANSLATE(REPLACE(REPLACE(LOWER(?), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ', 'aaaeeeeiioouuuycn') AND date_validity_start <= ? AND date_validity_end > ? and deprecated = 0 order by value ";
	private static final String SQL_QUERY_SELECT_CODES_BY_DATE = "SELECT code FROM geocodes_city WHERE date_validity_start <= ? AND date_validity_end > ? and deprecated = 0";
	private static final String SQL_QUERY_SELECT_BY_VALUE_LIKE_AND_DATE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE (   TRANSLATE(REPLACE(REPLACE(LOWER(value_min), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ-', 'aaaeeeeiioouuuycn ')  like TRANSLATE(REPLACE(REPLACE(LOWER(?), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ-', 'aaaeeeeiioouuuycn ') OR  TRANSLATE(REPLACE(REPLACE(LOWER(value_min_complete), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ-', 'aaaeeeeiioouuuycn ') like TRANSLATE(REPLACE(REPLACE(LOWER(?), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ-', 'aaaeeeeiioouuuycn ') ) AND date_validity_start <= ? AND date_validity_end > ? and deprecated = 0 order by value ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO geocodes_city ( code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM geocodes_city WHERE id_city = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE geocodes_city SET code_country = ?, code = ?, value = ?, code_zone = ?, date_validity_start = ?, date_validity_end = ?, value_min = ?, value_min_complete = ?, date_last_update = ?, deprecated = ? WHERE id_city = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_city FROM geocodes_city";
    private static final String SQL_QUERY_SEARCH_ID = "SELECT city.id_city FROM geocodes_city AS city WHERE (${cityLabel} OR ${cityLabelMin} OR ${cityLabelMinComplete}) AND ${cityCode} AND ${placeCode}";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city WHERE id_city IN (  ";
    private static final String SQL_QUERY_SELECTALL_BY_LAST_DATE = "SELECT id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, date_last_update, deprecated FROM geocodes_city order by date_last_update, code limit 5";
	private static final String SQL_QUERY_INSERT_HISTORY = "INSERT INTO geocodes_city_changes (id_city, code_country, code, value, code_zone, date_validity_start, date_validity_end, value_min, value_min_complete, deprecated, date_update, author, status ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	private static final String SQL_QUERY_SELECT_HISTORY_BY_CITY_ID = "SELECT * FROM geocodes_city_changes WHERE id_city = ?";
	private static final String SQL_QUERY_SELECT_HISTORY_BY_HISTORY_ID = "SELECT * FROM geocodes_city_changes WHERE id_city_history = ?";
	private static final String SQL_QUERY_UPDATE_HISTORY = "UPDATE geocodes_city_changes SET code_country = ?, code = ?, value = ?, code_zone = ?, date_validity_start = ?, date_validity_end = ?, value_min = ?, value_min_complete = ?, date_update = ?, deprecated = ?, author = ?, status = ? WHERE id_city_history = ?";


	/**
     * {@inheritDoc }
     */
    @Override
    public City insert( City city, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , city.getCodeCountry( ) );
            daoUtil.setString( nIndex++ , city.getCode( ) );
            daoUtil.setString( nIndex++ , city.getValue( ) );
            daoUtil.setString( nIndex++ , city.getCodeZone( ) );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateValidityStart( ).getTime( ) ) );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateValidityEnd( ).getTime() ) );
            daoUtil.setString( nIndex++ , city.getValueMin( ) );
            daoUtil.setString( nIndex++ , city.getValueMinComplete( ) );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateLastUpdate( ).getTime( ) ) );
            daoUtil.setBoolean( nIndex++, city.isDeprecated( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                city.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        return city;
    }

	/**
     * {@inheritDoc }
     */
    @Override
    public void addCityChanges(City city, String author, String status, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_HISTORY, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
			daoUtil.setInt(nIndex++, city.getId( ) );
            daoUtil.setString( nIndex++ , city.getCodeCountry( ) );
            daoUtil.setString( nIndex++ , city.getCode( ) );
            daoUtil.setString( nIndex++ , city.getValue( ) );
            daoUtil.setString( nIndex++ , city.getCodeZone( ) );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateValidityStart( ).getTime( ) ) );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateValidityEnd( ).getTime() ) );
            daoUtil.setString( nIndex++ , city.getValueMin( ) );
            daoUtil.setString( nIndex++ , city.getValueMinComplete( ) );
            daoUtil.setBoolean( nIndex++, city.isDeprecated( ) );
			daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateLastUpdate( ).getTime( ) ) );
			daoUtil.setString( nIndex++ , author );
			daoUtil.setString( nIndex, status);

            daoUtil.executeUpdate( );
        }
    }

	/**
	 * {@inheritDoc }
	 */
	@Override
	public List<CityChanges> selectCityChangesListByCityId(int idCity, Plugin plugin )
	{
		List<CityChanges> cityList = new ArrayList<>(  );
		try( DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_HISTORY_BY_CITY_ID, plugin ) )
		{
			daoUtil.setInt( 1 , idCity );

			daoUtil.executeQuery(  );

			while ( daoUtil.next(  ) )
			{
				CityChanges cityChanges = new CityChanges(  );
				int nIndex = 1;

				cityChanges.setIdChanges( daoUtil.getInt( nIndex++ ) );
				cityChanges.setId( daoUtil.getInt( nIndex++ ) );
				cityChanges.setCodeCountry( daoUtil.getString( nIndex++ ) );
				cityChanges.setCode( daoUtil.getString( nIndex++ ) );
				cityChanges.setCodeZone( daoUtil.getString( nIndex++ ) );
				cityChanges.setValue( daoUtil.getString( nIndex++ ) );
				cityChanges.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
				cityChanges.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
				cityChanges.setValueMin( daoUtil.getString( nIndex++ ) );
				cityChanges.setValueMinComplete( daoUtil.getString( nIndex++ ) );
				cityChanges.setDeprecated( daoUtil.getBoolean( nIndex++ ) );
				cityChanges.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
				cityChanges.setAuthor( daoUtil.getString( nIndex++ ) );
				cityChanges.setStatus( daoUtil.getString( nIndex ) );

				cityList.add(cityChanges);
			}

			return cityList;
		}
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public CityChanges selectCityChanges(int idHistoryCity, Plugin plugin)
	{
		CityChanges cityChanges = new CityChanges(  );
		try( DAOUtil daoUtil = new DAOUtil(SQL_QUERY_SELECT_HISTORY_BY_HISTORY_ID, plugin ) )
		{
			daoUtil.setInt( 1 , idHistoryCity );

			daoUtil.executeQuery(  );

			while ( daoUtil.next(  ) )
			{
				int nIndex = 1;

				cityChanges.setIdChanges( daoUtil.getInt( nIndex++ ) );
				cityChanges.setId( daoUtil.getInt( nIndex++ ) );
				cityChanges.setCodeCountry( daoUtil.getString( nIndex++ ) );
				cityChanges.setCode( daoUtil.getString( nIndex++ ) );
				cityChanges.setCodeZone( daoUtil.getString( nIndex++ ) );
				cityChanges.setValue( daoUtil.getString( nIndex++ ) );
				cityChanges.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
				cityChanges.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
				cityChanges.setValueMin( daoUtil.getString( nIndex++ ) );
				cityChanges.setValueMinComplete( daoUtil.getString( nIndex++ ) );
				cityChanges.setDeprecated( daoUtil.getBoolean( nIndex++ ) );
				cityChanges.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
				cityChanges.setAuthor( daoUtil.getString( nIndex++ ) );
				cityChanges.setStatus( daoUtil.getString( nIndex ) );

			}
			return cityChanges;
		}
	}

    /**
     * {@inheritDoc }
     */
    @Override
	public CityChanges storeChanges(CityChanges cityChanges, Plugin plugin)
	{
		try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_HISTORY, plugin ) )
		{
			int nIndex = 1;

			daoUtil.setString( nIndex++ , cityChanges.getCodeCountry( ) );
			daoUtil.setString( nIndex++ , cityChanges.getCode( ) );
			daoUtil.setString( nIndex++ , cityChanges.getValue( ) );
			daoUtil.setString( nIndex++ , cityChanges.getCodeZone( ) );
			daoUtil.setDate( nIndex++, new java.sql.Date( cityChanges.getDateValidityStart( ).getTime( ) ) );
			daoUtil.setDate( nIndex++, new java.sql.Date( cityChanges.getDateValidityEnd( ).getTime( ) ) );
			daoUtil.setString( nIndex++ , cityChanges.getValueMin( ) );
			daoUtil.setString( nIndex++ , cityChanges.getValueMinComplete() );
			daoUtil.setDate( nIndex++, new java.sql.Date( cityChanges.getDateLastUpdate( ).getTime( ) ) );
			daoUtil.setBoolean( nIndex++, cityChanges.isDeprecated( ) );
			daoUtil.setString( nIndex++, cityChanges.getAuthor());
			daoUtil.setString( nIndex++, cityChanges.getStatus());
			daoUtil.setInt( nIndex , cityChanges.getIdChanges( ) );

			daoUtil.executeUpdate( );
		}
		return cityChanges;
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
    public Optional<City> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        City city = null;
	
	        if ( daoUtil.next( ) )
	        {
	            city = new City();
	            int nIndex = 1;
	            
	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );
	        }

	        return Optional.ofNullable( city );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<City> loadByCode( String strCode, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin ) )
        {
	        daoUtil.setString( 1 , strCode );
	        daoUtil.setDate( 2, new java.sql.Date(System.currentTimeMillis( ) ) );
	        daoUtil.setDate( 3, new java.sql.Date(System.currentTimeMillis( ) ) );
	        daoUtil.executeQuery( );
	        City city = null;

	        if ( daoUtil.next( ) )
	        {
	            city = new City();
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );
	        }

	        return Optional.ofNullable( city );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<City> loadByDateAndCode( Date dateCode, String strCode, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BETWEEN_DATE, plugin ) )
        {
	        daoUtil.setDate( 1 , new java.sql.Date( dateCode.getTime( ) ) );
	        daoUtil.setDate( 2 , new java.sql.Date( dateCode.getTime( ) ) );
	        daoUtil.setString( 3, strCode );
	        daoUtil.executeQuery( );
	        City city = null;

	        if ( daoUtil.next( ) )
	        {
	            city = new City();
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );
	        }

	        return Optional.ofNullable( city );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( City city, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;

        	daoUtil.setString( nIndex++ , city.getCodeCountry( ) );
        	daoUtil.setString( nIndex++ , city.getCode( ) );
        	daoUtil.setString( nIndex++ , city.getValue( ) );
        	daoUtil.setString( nIndex++ , city.getCodeZone( ) );
        	daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateValidityStart( ).getTime( ) ) );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateValidityEnd( ).getTime( ) ) );
            daoUtil.setString( nIndex++ , city.getValueMin( ) );
            daoUtil.setString( nIndex++ , city.getValueMinComplete() );
            daoUtil.setDate( nIndex++, new java.sql.Date( city.getDateLastUpdate( ).getTime( ) ) );
            daoUtil.setBoolean( nIndex++, city.isDeprecated( ) );
	        daoUtil.setInt( nIndex , city.getId( ) );

	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<City> selectCitiesList( Plugin plugin )
    {
        List<City> cityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            City city = new City(  );
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );

	            cityList.add( city );
	        }

	        return cityList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<City> selectCitiesListByValue( String strVal, Plugin plugin )
    {
        List<City> cityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VALUE, plugin ) )
        {
        	daoUtil.setString( 1 , strVal );
        	daoUtil.setDate( 2, new java.sql.Date(System.currentTimeMillis( ) ) );
        	daoUtil.setDate( 3, new java.sql.Date(System.currentTimeMillis( ) ) );

	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            City city = new City(  );
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );

	            cityList.add( city );
	        }

	        return cityList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<City> selectCitiesListByValueLike( String strVal, Plugin plugin )
    {
        List<City> cityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VALUE_LIKE, plugin ) )
        {
        	daoUtil.setString( 1 , strVal + "%"  );
        	daoUtil.setString( 2 , strVal + "%"  );
        	daoUtil.setDate( 3, new java.sql.Date(System.currentTimeMillis( ) ) );
        	daoUtil.setDate( 4, new java.sql.Date(System.currentTimeMillis( ) ) );

	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            City city = new City(  );
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );

	            cityList.add( city );
	        }

	        return cityList;
        }
    }

	/**
	 * {@inheritDoc }
	 */
	@Override
	public List<String> selectCitiesCodesListByDate( Date dateCity, Plugin plugin )
	{
		List<String> codesList = new ArrayList<>( );
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CODES_BY_DATE, plugin ) )
		{
			daoUtil.setDate( 1, new java.sql.Date( dateCity.getTime( ) ) );
			daoUtil.setDate( 2, new java.sql.Date( dateCity.getTime( ) ) );

			daoUtil.executeQuery( );

			while ( daoUtil.next( ) )
			{
				codesList.add( daoUtil.getString( 1 ) );
			}

			return codesList;
		}
	}

    /**
     * {@inheritDoc }
     */
    @Override
    public List<City> selectCitiesListByValueAndDate( String strVal, Date dateCity, Plugin plugin )
    {
        List<City> cityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VALUE_AND_DATE, plugin ) )
        {
        	daoUtil.setString( 1 , strVal );
        	daoUtil.setDate( 2, new java.sql.Date( dateCity.getTime( ) ) );
        	daoUtil.setDate( 3, new java.sql.Date( dateCity.getTime( ) ) );

	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            City city = new City(  );
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );

	            cityList.add( city );
	        }

	        return cityList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<City> selectCitiesListByValueAndDateLike( String strVal, Date dateCity, Plugin plugin )
    {
        List<City> cityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VALUE_LIKE_AND_DATE, plugin ) )
        {
        	daoUtil.setString( 1 , strVal + "%"  );
        	daoUtil.setString( 2 , strVal + "%"  );
        	daoUtil.setDate( 3, new java.sql.Date( dateCity.getTime( ) ) );
        	daoUtil.setDate( 4, new java.sql.Date( dateCity.getTime( ) ) );

	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            City city = new City(  );
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );

	            cityList.add( city );
	        }

	        return cityList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdCitiesList( final Plugin plugin, final String cityLabel, final String cityCode, final String placeCode, final boolean approximateSearch )
    {
        final String sql;
        if (approximateSearch) {
            sql = SQL_QUERY_SEARCH_ID.replace( "${cityLabel}", ( StringUtils.isNotBlank( cityLabel ) ? "city.value LIKE '%" + cityLabel + "%'" : "1=1" ) )
                .replace( "${cityLabelMin}", ( StringUtils.isNotBlank( cityLabel ) ? "city.value_min LIKE '%" + cityLabel + "%'" : "1=1" ) )
                .replace( "${cityLabelMinComplete}", ( StringUtils.isNotBlank( cityLabel ) ? "city.value_min_complete LIKE '%" + cityLabel + "%'" : "1=1" ) )
                .replace( "${cityCode}", ( StringUtils.isNotBlank( cityCode ) ? "city.code = '" + cityCode + "'" : "1=1" ) )
                .replace( "${placeCode}", ( StringUtils.isNotBlank( placeCode ) ? "city.code_zone = '" + placeCode + "'" : "1=1" ) );
        } else {
            sql = SQL_QUERY_SEARCH_ID.replace( "${cityLabel}", ( StringUtils.isNotBlank( cityLabel ) ? "city.value = '" + cityLabel + "'" : "1=1" ) )
                .replace( "${cityLabelMin}", ( StringUtils.isNotBlank( cityLabel ) ? "city.value_min = '" + cityLabel + "'" : "1=1" ) )
                .replace( "${cityLabelMinComplete}", ( StringUtils.isNotBlank( cityLabel ) ? "city.value_min_complete = '" + cityLabel + "'" : "1=1" ) )
                .replace( "${cityCode}", ( StringUtils.isNotBlank( cityCode ) ? "city.code = '" + cityCode + "'" : "1=1" ) )
                .replace( "${placeCode}", ( StringUtils.isNotBlank( placeCode ) ? "city.code_zone = '" + placeCode + "'" : "1=1" ) );
        }

        List<Integer> cityList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( sql, plugin ) )
        {
	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            cityList.add( daoUtil.getInt( 1 ) );
	        }

	        return cityList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectCitiesReferenceList( Plugin plugin )
    {
        ReferenceList cityList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            cityList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }

	        return cityList;
    	}
    }

    /**
     * {@inheritDoc }
     */
	@Override
	public List<City> selectCitiesListByIds( Plugin plugin, List<Integer> listIds ) {
		List<City> cityList = new ArrayList<>(  );

		StringBuilder builder = new StringBuilder( );

		if ( !listIds.isEmpty( ) )
		{
			for( int i = 0 ; i < listIds.size(); i++ ) {
			    builder.append( "?," );
			}

			String placeHolders =  builder.deleteCharAt( builder.length( ) -1 ).toString( );
			String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";


	        try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
	        {
	        	int index = 1;
				for( Integer n : listIds ) {
					daoUtil.setInt(  index++, n );
				}

	        	daoUtil.executeQuery(  );
	        	while ( daoUtil.next(  ) )
		        {
		        	City city = new City(  );
		            int nIndex = 1;

		            city.setId( daoUtil.getInt( nIndex++ ) );
				    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
				    city.setCode( daoUtil.getString( nIndex++ ) );
				    city.setValue( daoUtil.getString( nIndex++ ) );
				    city.setCodeZone( daoUtil.getString( nIndex++ ) );
				    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
				    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
				    city.setValueMin( daoUtil.getString( nIndex++ ) );
				    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
				    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
				    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );

		            cityList.add( city );
		        }

		        daoUtil.free( );

	        }
	    }
		return cityList;

	}

	/**
     * {@inheritDoc }
     */
    @Override
    public List<City> selectCitiesListByLastDate( Plugin plugin )
    {
        List<City> cityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_LAST_DATE, plugin ) )
        {
	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            City city = new City(  );
	            int nIndex = 1;

	            city.setId( daoUtil.getInt( nIndex++ ) );
			    city.setCodeCountry( daoUtil.getString( nIndex++ ) );
			    city.setCode( daoUtil.getString( nIndex++ ) );
			    city.setValue( daoUtil.getString( nIndex++ ) );
			    city.setCodeZone( daoUtil.getString( nIndex++ ) );
			    city.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
			    city.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    city.setValueMin( daoUtil.getString( nIndex++ ) );
			    city.setValueMinComplete( daoUtil.getString( nIndex++ ) );
			    city.setDateLastUpdate( daoUtil.getDate( nIndex++ ) );
			    city.setDeprecated( daoUtil.getBoolean( nIndex++ ) );
	
	            cityList.add( city );
	        }
	
	        return cityList;
        }
    }
}
