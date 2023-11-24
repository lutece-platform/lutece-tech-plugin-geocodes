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
import fr.paris.lutece.plugins.geocodes.service.cache.GeoCodeCacheService;

public class GeoCodesService 
{
	private GeoCodeCacheService _cacheGeoCode = GeoCodeCacheService.getInstance( );
	private GeoCodeCacheService _cacheGeoCodeLike = GeoCodeCacheService.getInstance( );
	private static GeoCodesService _singleton;
	
	
	/** Creates a new instance of CalendarService */
    public GeoCodesService( )
    {
        init( );
    }
    
    /**
     * Initialize the CalendarService
     */
    private void init( )
    {
        _cacheGeoCode.initCache( );
    }
    
    /**
     * Get the instance of GeoCodeService
     * @return an instance of GeoCodeService
     */
    public static GeoCodesService getInstance(  )
    {
    	if ( _singleton == null )
    	{
    		_singleton = new GeoCodesService(  );
    	}
    	return _singleton;
    }
	
	/**
	 * get city by code
	 * 
	 * @param strCodel
	 * @return the city (as Optional)
	 */
	public Optional<City> getCityByCode( String strCode )
	{
		Optional<City> cityCache = (Optional<City>) _cacheGeoCode.getFromCache( strCode );
		
		if ( cityCache == null || cityCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
			IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
			Optional<City> city = geoCodeLocal.getCityByCode( strCode );
			_cacheGeoCode.putInCache( strCode , city );
			return city;
		}
		else return cityCache;
	}
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	public List<City> getCitiesListByName( String strSearchBeginningVal )
	{
		List<City> lstCitiesCache = ( List<City> ) _cacheGeoCode.getFromCache( strSearchBeginningVal );
		
		if ( lstCitiesCache == null || lstCitiesCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByName( strSearchBeginningVal );
	    	_cacheGeoCodeLike.putInCache( strSearchBeginningVal, lstCities );
	    	return lstCities;
		}
		return lstCitiesCache;
	}
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	public List<City> getCitiesListByNameLike( String strSearchBeginningVal )
	{
		List<City> lstCitiesCache = ( List<City> ) _cacheGeoCodeLike.getFromCache( strSearchBeginningVal );
		
		if ( lstCitiesCache == null || lstCitiesCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByNameLike( strSearchBeginningVal );
	    	_cacheGeoCodeLike.putInCache( strSearchBeginningVal, lstCities );
	    	return lstCities;
		}
		return lstCitiesCache;
	}
	
	/**
	 * search cities by name beginning with a string and a date
	 * 
	 * @param strSearchBeginningVal
	 * @param dateCity
	 * @return the list
	 */
	public List<City> getCitiesListByNameAndDate( String strSearchBeginningVal, Date dateCity )
	{
		dateCity = checkDateValidityStart( dateCity );
		List<City> lstCitiesCache = ( List<City> ) _cacheGeoCode.getFromCache( strSearchBeginningVal + dateCity);
		
		if ( lstCitiesCache == null || lstCitiesCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
	    	_cacheGeoCode.putInCache( strSearchBeginningVal + dateCity, lstCities );
	    	return lstCities;
		}
		return lstCitiesCache;
	}
	
	/**
	 * search cities by name beginning with a string and a date
	 * 
	 * @param strSearchBeginningVal
	 * @param dateCity
	 * @return the list
	 */
	public List<City> getCitiesListByNameAndDateLike( String strSearchBeginningVal, Date dateCity )
	{
		dateCity = checkDateValidityStart( dateCity );
		List<City> lstCitiesCache = ( List<City> ) _cacheGeoCodeLike.getFromCache( strSearchBeginningVal + dateCity);
		
		if ( lstCitiesCache == null || lstCitiesCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByNameAndDateLike( strSearchBeginningVal, dateCity );
	    	_cacheGeoCodeLike.putInCache( strSearchBeginningVal + dateCity, lstCities );
	    	return lstCities;
		}
		return lstCitiesCache;
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
	public Optional<City> getCityByDateAndCode( Date dateCity, String strCode )
	{
		dateCity = checkDateValidityStart( dateCity );
		Optional<City> cityCache = (Optional<City>) _cacheGeoCode.getFromCache( strCode + dateCity );
		
		if ( cityCache == null || cityCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
			IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
			Optional<City> city = geoCodeLocal.getCityByDateAndCode( dateCity, strCode );
			_cacheGeoCode.putInCache( strCode + dateCity, city );
			return city;
		}
		else return cityCache;
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
