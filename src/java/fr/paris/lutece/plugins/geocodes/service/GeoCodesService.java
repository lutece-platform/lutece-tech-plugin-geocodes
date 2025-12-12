package fr.paris.lutece.plugins.geocodes.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
import fr.paris.lutece.plugins.geocodes.business.CityMapper;
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
    	@Deprecated
	public List<City> getCityByCode( String strCode )
	{
		GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
		IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
		List<City> cities = geoCodeLocal.getCityByCode( strCode );
		
		return cities;
	}
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	@Deprecated
	public List<City> getCitiesListByName( String strSearchVal )
	{
		GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByName( strSearchVal );
	    	
	    	return lstCities;
	}
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	@Deprecated
	public List<City> getCitiesListByNameLike( String strSearchBeginningVal )
	{
		GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
	    	List<City> lstCities = new ArrayList<>( );
	        
	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
	    	lstCities = geoCodeLocal.getCitiesListByNameLike( strSearchBeginningVal );
	    	
	    	return lstCities;
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
		List<City> lstCities = ( List<City> ) _cacheGeoCode.getFromCityCache( strSearchBeginningVal + dateCity );
		
		if ( lstCities == null || lstCities.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
			
        	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
        	    	lstCities = geoCodeLocal.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
        	    	
        	    	if ( !lstCities.isEmpty( ) )
        	    	{
        	    	    _cacheGeoCode.putCityInCache( strSearchBeginningVal + dateCity, lstCities );
        	    	}
        	}
		
		return lstCities;
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
		List<City> lstCities = ( List<City> ) _cacheGeoCodeLike.getFromCache( strSearchBeginningVal + dateCity);
		
		if ( lstCities == null || lstCities.isEmpty( ) )
		{
			GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
        	    	
        	    	IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
        	    	lstCities = geoCodeLocal.getCitiesListByNameAndDateLike( strSearchBeginningVal, dateCity );
        	    	
        	    	if ( !lstCities.isEmpty( ) )
        	    	{
        	    	    _cacheGeoCodeLike.putInCache( strSearchBeginningVal + dateCity, lstCities );
        	    	}
		}
		
		return lstCities;
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
		
		List<String> listCityCodes = (List<String>) _cacheGeoCode.getFromCityCache( strDate );
 		
		if ( listCityCodes == null || listCityCodes.isEmpty( ) )
		{
		    final GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
		    final IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
		    listCityCodes = geoCodeLocal.getCitiesCodesListByDate( dateCity );
		    if ( listCityCodes != null && !listCityCodes.isEmpty( ) )
		    {
			_cacheGeoCode.putCitiesCodesByDateInCache( strDate, listCityCodes );
		    }    
		}
		
		return listCityCodes;
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
	@Deprecated
	public Optional<Country> getCountryByCode( String strCode, boolean bAttached )
	{
	    return CountryHome.findByCode( strCode, bAttached );
	}
	
	/**
	 * search countries by name
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	public List<Country> getCountriesListByNameAndDate(String strSearchBeginningVal, Date dateRef)
	{
		List<Country> lstCountries = ( List<Country> ) _cacheGeoCode.getFromCountryCache( strSearchBeginningVal + dateRef );
		
		if ( lstCountries == null || lstCountries.isEmpty( ) )
		{
			lstCountries =  CountryHome.getCountriesListByNameAndDate(strSearchBeginningVal, dateRef);
			_cacheGeoCode.putCountryInCache( strSearchBeginningVal + dateRef, lstCountries );
		}
		
		return lstCountries;
	}

	/**
	 * get country list by name
	 * 
	 * @param strCountryName
	 * @return the list
	 */
	@Deprecated
	public List<Country> getCountriesListByName(String strCountryName)
	{
	    return CountryHome.getCountriesListByName(strCountryName);
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
		City city = ( City ) _cacheGeoCode.getFromCityCache( strCode + dateCity );
		
		if ( city != null )
		{
		    return Optional.of ( city );
		}
		
		GeoCodeProviderService instance = GeoCodeProviderService.getInstance( );
		IGeoCodeProvider geoCodeLocal = instance.find( Constants.ID_PROVIDER_GEOCODE_LOCAL );
		Optional<City> cityOpt = geoCodeLocal.getCityByDateAndCode( dateCity, strCode );
			
		if ( cityOpt.isPresent( ) )
		{
		    _cacheGeoCode.putCityInCache( strCode + dateCity, cityOpt.get( ) );
		}
		
		return cityOpt;
	}
	
	/**
	 * get all cities order by lastUpdateDate
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
	public static void addCityChanges(City cityUpdate, String author, String status )
	{
		CityHome.addCityChanges(cityUpdate, author, status);
	}

	/**
	 * update the city changes informations
	 *
	 * @param cityUpdate : the city to update
	 */
	public static void updateCityChanges(City cityUpdate, String author, String status )
	{
		CityHome.updateChanges(CityMapper.cityIntoCityChanges( cityUpdate, author, status));
	}
	
	/**
	 * create the new city change
	 * 
	 * @param cityNew : the city to create
	 */
	public static void createCity( City cityNew, String author, String applied )
	{
		CityHome.addCityChanges(cityNew, author, applied);
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

	public static boolean checkChangesExistence( City city )
	{
		return CityHome.getChangesExistence( city );
	}
	
	public static boolean checkDateFormat( String strDateCity )
	{
		if ( strDateCity.length( ) != 10 || !strDateCity.matches( Constants.DATE_FORMAT_PATTERN ) )
        {
            return false;
        }
		return true;
	}
}
