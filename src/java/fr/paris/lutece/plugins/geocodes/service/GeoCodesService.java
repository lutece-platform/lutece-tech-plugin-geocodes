package fr.paris.lutece.plugins.geocodes.service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import fr.paris.lutece.plugins.geocodes.service.cache.GeoCodeCacheServiceLike;
import fr.paris.lutece.util.date.DateUtil;

public class GeoCodesService 
{
	private GeoCodeCacheService _cacheGeoCode = GeoCodeCacheService.getInstance( );
	private GeoCodeCacheServiceLike _cacheGeoCodeLike = GeoCodeCacheServiceLike.getInstance( );
	private static final Date dateMin = DateUtil.parseIsoDate( "1943-01-01 00:00:00" );
	private static final SimpleDateFormat citiesCodesDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	private static GeoCodesService _singleton;
	
	
	/** Creates a new instance of CalendarService */
    private GeoCodesService( )
    {
        init( );
    }
    
    /**
     * Initialize the CalendarService
     */
    private void init( )
    {
        _cacheGeoCode.initCache( );
        _cacheGeoCodeLike.initCache( );
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
		Optional<City> cityCache = (Optional<City>) _cacheGeoCode.getFromCityCache( strCode );
		
		if ( cityCache == null || cityCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
			IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
			Optional<City> city = geoCodeLocal.getCityByCode( strCode );
			_cacheGeoCode.putCityInCache( strCode , city );
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
		List<City> lstCitiesCache = ( List<City> ) _cacheGeoCode.getFromCityCache( strSearchBeginningVal );
		
		if ( lstCitiesCache == null || lstCitiesCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByName( strSearchBeginningVal );
	    	_cacheGeoCode.putCityInCache( strSearchBeginningVal, lstCitiesCache );
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
		List<City> lstCitiesCache = ( List<City> ) _cacheGeoCode.getFromCityCache( strSearchBeginningVal + dateCity);
		
		if ( lstCitiesCache == null || lstCitiesCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
	    	_cacheGeoCode.putCityInCache( strSearchBeginningVal + dateCity, lstCities );
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
	 * search cities codes by date
	 *
	 * @param dateCity
	 * @return the list
	 */
	public List<String> getCitiesCodesListByDate( Date dateCity )
	{
		dateCity = checkDateValidityStart( dateCity );
		final String strDate = citiesCodesDateFormat.format( dateCity );
		final List<String> result = (List<String>) _cacheGeoCode.getFromCityCache( strDate );

		if ( result == null || result.isEmpty( ) )
		{
			final GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
			final IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
			final List<String> lstCities = geoCodeLocal.getCitiesCodesListByDate( dateCity );
			_cacheGeoCode.putCitiesCodesByDateInCache( strDate, lstCities );
			return lstCities;
		}
		return result;
	}
	
	/**
	 * get country by code
	 * 
	 * @param strCode
	 * @return  the country (as Optional)
	 */
	public Optional<Country> getCountryByCode( String strCode )
	{
		Optional<Country> country = ( Optional<Country> ) _cacheGeoCode.getFromCountryCache( strCode );
		
		if ( country == null || country.isEmpty( ) )
		{
			country =  CountryHome.findByCode( strCode );
			_cacheGeoCode.putCountryInCache( strCode, country );
		}
		
		return country;
	}
	
	/**
	 * get country by code
	 * 
	 * @param strCode
	 * @return  the country (as Optional)
	 */
	public Optional<Country> getCountryByCode( String strCode, boolean bAttached )
	{
		Optional<Country> country = ( Optional<Country> ) _cacheGeoCode.getFromCountryCache( strCode );
		
		if ( country == null || country.isEmpty( ) )
		{
			country =  CountryHome.findByCode( strCode, bAttached );
			_cacheGeoCode.putCountryInCache( strCode, country );
		}
		
		return country;
	}
	
	/**
	 * search countries by name
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	public List<Country> getCountriesListByName( String strSearchBeginningVal, Date dateRef )
	{
		List<Country> lstCountries = ( List<Country> ) _cacheGeoCode.getFromCountryCache( strSearchBeginningVal + dateRef );
		
		if ( lstCountries == null || lstCountries.isEmpty( ) )
		{
			lstCountries =  CountryHome.getCountriesListByName( strSearchBeginningVal, dateRef );
			_cacheGeoCode.putCountryInCache( strSearchBeginningVal + dateRef, lstCountries );
		}
		
		return lstCountries;
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
		Optional<City> cityCache = (Optional<City>) _cacheGeoCode.getFromCityCache( strCode + dateCity );
		
		if ( cityCache == null || cityCache.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
			IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
			Optional<City> city = geoCodeLocal.getCityByDateAndCode( dateCity, strCode );
			_cacheGeoCode.putCityInCache( strCode + dateCity, city );
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
	public static void updateCity( City cityUpdate, String author, String applied )
	{
		CityHome.addCityChanges(cityUpdate, author, applied);
	}
	
	/**
	 * create the new city
	 * 
	 * @param cityNew : the city to create
	 */
	public static void createCity( City cityNew, String author, String applied )
	{
		City city = CityHome.create( cityNew );
		CityHome.addCityChanges(city, author, applied);
	}
	
	public static Date checkDateValidityStart( Date dateCheck )
	{
		//Date dateMin = Date.valueOf( Constants.CONST_DATE_MIN );
		Date dateMin = DateUtil.parseIsoDate("1943-01-01 00:00:00");
		if ( dateCheck.before( dateMin ) )
		{
			return dateMin;
		}
		return dateCheck;
	}
	
}
