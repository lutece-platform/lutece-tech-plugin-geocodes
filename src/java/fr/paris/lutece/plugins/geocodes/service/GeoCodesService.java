package fr.paris.lutece.plugins.geocodes.service;

import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
import fr.paris.lutece.plugins.geocodes.business.Country;
import fr.paris.lutece.plugins.geocodes.business.CountryHome;

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
	
	
}
