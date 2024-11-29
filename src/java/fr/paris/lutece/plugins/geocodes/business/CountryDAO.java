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

import java.sql.Statement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This class provides Data Access methods for Country objects
 */
public final class CountryDAO implements ICountryDAO
{
    // Constants
	private static final String SQL_QUERY_SELECT_BY_ID = "SELECT id_country, code, value, is_attached, date_validity_start, date_validity_end, deprecated FROM geocodes_country WHERE id_country = ?";
	private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id_country, code, value, is_attached, deprecated FROM geocodes_country WHERE code = ?";
	private static final String SQL_QUERY_SELECT_BY_CODE_AND_ATTACHED = "SELECT id_country, code, value, is_attached, deprecated FROM geocodes_country WHERE code = ? and is_attached = ?";
	private static final String SQL_QUERY_SELECT_BY_VALUE = "SELECT id_country, code, value, is_attached, deprecated FROM geocodes_country WHERE LOWER(value) like LOWER(?) and deprecated = 0 order by value ";
	private static final String SQL_QUERY_SELECT_BY_VALUE_AND_DATE = "SELECT id_country, code, value, is_attached, deprecated FROM geocodes_country WHERE  TRANSLATE(REPLACE(REPLACE(LOWER(value), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ', 'aaaeeeeiioouuuycn')  like TRANSLATE(REPLACE(REPLACE(LOWER(?), 'œ', 'oe'), 'æ', 'ae'), 'àâäéèêëîïôöùûüÿçñ', 'aaaeeeeiioouuuycn') AND date_validity_start <= ? AND date_validity_end >= ? and deprecated = 0 order by value ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO geocodes_country ( code, value, is_attached, date_validity_start, date_validity_end, deprecated ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM geocodes_country WHERE id_country = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE geocodes_country SET code = ?, value = ?, is_attached = ?, date_validity_start = ?, date_validity_end = ?, deprecated = ? WHERE id_country = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_country, code, value, is_attached, deprecated FROM geocodes_country";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_country FROM geocodes_country";
    private static final String SQL_QUERY_SEARCH_ID = "SELECT country.id_country FROM geocodes_country as country WHERE ${countryLabel} AND ${countryCode}";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id_country, code, value, is_attached, date_validity_start, date_validity_end, deprecated FROM geocodes_country WHERE id_country IN (  ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Country country, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , country.getCode( ) );
            daoUtil.setString( nIndex++ , country.getValue( ) );
            daoUtil.setBoolean( nIndex++ , country.isAttached( ) );
			daoUtil.setDate( nIndex++ , new java.sql.Date( country.getDateValidityStart( ).getTime( ) ) );
			daoUtil.setDate( nIndex++ , new java.sql.Date( country.getDateValidityEnd( ).getTime( ) ) );
            daoUtil.setBoolean( nIndex, country.isDeprecated( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                country.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<Country> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        Country country = null;
	
	        if ( daoUtil.next( ) )
	        {
	            country = new Country();
	            int nIndex = 1;
	            
	            country.setId( daoUtil.getInt( nIndex++ ) );
			    country.setCode( daoUtil.getString( nIndex++ ) );
			    country.setValue( daoUtil.getString( nIndex++ ) );
			    country.setAttached( daoUtil.getBoolean( nIndex++ ) );
				country.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
				country.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
			    country.setDeprecated( daoUtil.getBoolean( nIndex ) );
	        }
	
	        return Optional.ofNullable( country );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<Country> loadByCode( String strCode, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin ) )
        {
	        daoUtil.setString( 1 , strCode );
	        daoUtil.executeQuery( );
	        Country country = null;
	
	        if ( daoUtil.next( ) )
	        {
	            country = new Country();
	            int nIndex = 1;
	            
	            country.setId( daoUtil.getInt( nIndex++ ) );
			    country.setCode( daoUtil.getString( nIndex++ ) );
			    country.setValue( daoUtil.getString( nIndex++ ) );
			    country.setAttached( daoUtil.getBoolean( nIndex++ ) );
			    country.setDeprecated( daoUtil.getBoolean( nIndex ) );
	        }
	
	        return Optional.ofNullable( country );
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<Country> loadByCode( String strCode, boolean bAttached, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE_AND_ATTACHED, plugin ) )
        {
	        daoUtil.setString( 1 , strCode );
	        daoUtil.setBoolean( 2 , bAttached );
	        daoUtil.executeQuery( );
	        Country country = null;
	
	        if ( daoUtil.next( ) )
	        {
	            country = new Country();
	            int nIndex = 1;
	            
	            country.setId( daoUtil.getInt( nIndex++ ) );
			    country.setCode( daoUtil.getString( nIndex++ ) );
			    country.setValue( daoUtil.getString( nIndex++ ) );
			    country.setAttached( daoUtil.getBoolean( nIndex++ ) );
			    country.setDeprecated( daoUtil.getBoolean( nIndex ) );
	        }
	
	        return Optional.ofNullable( country );
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
    public void store( Country country, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
            	daoUtil.setString( nIndex++ , country.getCode( ) );
            	daoUtil.setString( nIndex++ , country.getValue( ) );
            	daoUtil.setBoolean( nIndex++ , country.isAttached( ) );
            	daoUtil.setDate( nIndex++ , new java.sql.Date( country.getDateValidityStart( ).getTime( ) ) );
            	daoUtil.setDate( nIndex++ , new java.sql.Date( country.getDateValidityEnd( ).getTime( ) ) );
            	daoUtil.setBoolean( nIndex++, country.isDeprecated( ) );
	        daoUtil.setInt( nIndex , country.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Country> selectCountriesList( Plugin plugin )
    {
        List<Country> countryList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            Country country = new Country(  );
	            int nIndex = 1;
	            
	            country.setId( daoUtil.getInt( nIndex++ ) );
			    country.setCode( daoUtil.getString( nIndex++ ) );
			    country.setValue( daoUtil.getString( nIndex++ ) );
			    country.setAttached( daoUtil.getBoolean( nIndex++ ) );
			    country.setDeprecated( daoUtil.getBoolean( nIndex ) );
	
	            countryList.add( country );
	        }
	
	        return countryList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Country> selectCountriesListByValue( String strVal, Date dateRef, Plugin plugin )
    {
        List<Country> countryList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_VALUE_AND_DATE, plugin ) )
        {
        	daoUtil.setString( 1 , strVal + "%" );
        	daoUtil.setDate( 2 , new java.sql.Date( dateRef.getTime( ) ) );
	        daoUtil.setDate( 3 , new java.sql.Date( dateRef.getTime( ) ) );
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            Country country = new Country(  );
	            int nIndex = 1;
	            
	            country.setId( daoUtil.getInt( nIndex++ ) );
			    country.setCode( daoUtil.getString( nIndex++ ) );
			    country.setValue( daoUtil.getString( nIndex++ ) );
			    country.setAttached( daoUtil.getBoolean( nIndex++ ) );
			    country.setDeprecated( daoUtil.getBoolean( nIndex ) );
	
	            countryList.add( country );
	        }
	
	        return countryList;
        }
    }
  
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdCountriesList( Plugin plugin, String countryLabel, String countryCode   )
    {

		String sql = SQL_QUERY_SEARCH_ID
				.replace( "${countryLabel}", ( StringUtils.isNotBlank( countryLabel ) ? "country.value LIKE '%" + countryLabel + "%'" : "1=1" ) )
				.replace( "${countryCode}", ( StringUtils.isNotBlank( countryCode ) ? "country.code = '" + countryCode + "'" : "1=1" ) );

        List<Integer> countryList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( sql, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            countryList.add( daoUtil.getInt( 1 ) );
	        }
	
	        return countryList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectCountriesReferenceList( Plugin plugin )
    {
        ReferenceList countryList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            countryList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        return countryList;
    	}
    }
    
    /**
     * {@inheritDoc }
     */
	@Override
	public List<Country> selectCountriesListByIds( Plugin plugin, List<Integer> listIds ) {
		List<Country> countryList = new ArrayList<>(  );
		
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
		        	Country country = new Country(  );
		            int nIndex = 1;
		            
		            country.setId( daoUtil.getInt( nIndex++ ) );
				    country.setCode( daoUtil.getString( nIndex++ ) );
				    country.setValue( daoUtil.getString( nIndex++ ) );
				    country.setAttached( daoUtil.getBoolean( nIndex++ ) );
				    country.setDateValidityStart( daoUtil.getDate( nIndex++ ) );
				    country.setDateValidityEnd( daoUtil.getDate( nIndex++ ) );
				    country.setDeprecated( daoUtil.getBoolean( nIndex ) );
		            
		            countryList.add( country );
		        }
		
		        daoUtil.free( );
		        
	        }
	    }
		return countryList;
		
	}
}
