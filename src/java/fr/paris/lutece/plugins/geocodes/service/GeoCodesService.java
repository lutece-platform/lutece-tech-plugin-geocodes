package fr.paris.lutece.plugins.geocodes.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
import fr.paris.lutece.plugins.geocodes.business.Country;
import fr.paris.lutece.plugins.geocodes.business.CountryHome;
import fr.paris.lutece.plugins.geocodes.provider.GeoCodeProviderService;
import fr.paris.lutece.plugins.geocodes.provider.IGeoCodeProvider;
import fr.paris.lutece.plugins.geocodes.rs.Constants;

public class GeoCodesService 
{
	
	/**
	 * get city by code
	 * 
	 * @param strCodel
	 * @return the city (as Optional)
	 */
	public static Optional<City> getCityByCode( String strCode )
	{
		return  CityHome.findByCode( strCode );
	}
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	public static List<City> getCitiesListByName( String strSearchBeginningVal )
	{
		return  CityHome.getCitiesListByName( strSearchBeginningVal );
	}
	
	/**
	 * search cities by name beginning with a string and a date
	 * 
	 * @param strSearchBeginningVal
	 * @param dateCity
	 * @return the list
	 */
	public static List<City> getCitiesListByNameAndDate( String strSearchBeginningVal, Date dateCity )
	{
		dateCity = checkDateValidityStart( dateCity );
		GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
    	List<City> lstCities = new ArrayList<>( );
        
    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
    	lstCities = geoCodeLocal.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
    	return lstCities;
	}
	
	/**
	 * get country by code
	 * 
	 * @param strCode
	 * @return  the country (as Optional)
	 */
	public static Optional<Country> getCountryByCode( String strCode )
	{
		return  CountryHome.findByCode( strCode );
	}
	
	/**
	 * search countries by name
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	public static List<Country> getCountriesListByName( String strSearchBeginningVal )
	{
		return  CountryHome.getCountriesListByName( strSearchBeginningVal );
	}
	
	/**
	 * get city by code and date
	 * 
	 * @param strCodel
	 * @param dateCity
	 * @return the city (as Optional)
	 */
	public static Optional<City> getCityByDateAndCode( Date dateCity, String strCode )
	{
		dateCity = checkDateValidityStart( dateCity );
		GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
		IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
		return geoCodeLocal.getCityByDateAndCode( dateCity, strCode );
	}
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @return the list
	 */
	public static List<City> getCitiesListByLastDateUpdate( )
	{
		return  CityHome.getCitiesListByLastDate( );
	}
	
	/**
	 * update the city informations
	 * 
	 * @param cityUpdate : the city to update
	 */
	public static void updateCity( City cityUpdate )
	{
		CityHome.update( cityUpdate );
	}
	
	/**
	 * create the new city
	 * 
	 * @param cityNew : the city to create
	 */
	public static void createCity( City cityNew )
	{
		CityHome.create( cityNew );
	}
	
	public static Date checkDateValidityStart( Date dateCheck )
	{
		Date dateMin = Date.valueOf( Constants.CONST_DATE_MIN );
		if ( dateCheck.before( dateMin ) )
		{
			return dateMin;
		}
		return dateCheck;
	}
}
