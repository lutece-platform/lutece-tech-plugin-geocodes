package fr.paris.lutece.plugins.geocodes.provider;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.Country;

public interface IGeoCodeProvider {
	
	/**
     * Gives the id.
     * The id is used to find which geoCode providers have to be used in the task. The id must be unique among the geoCode providers.
     * @return the id
     */
    String getId( );
	
    /**
	 * get city by code and date
	 * 
	 * @param strCode
	 * @param dateCity
	 * @return the city (as Optional)
	 */
	Optional<City> getCityByDateAndCode( Date dateCity, String strCode );
	
	/**
	 * search cities by name beginning with a string and a date
	 * 
	 * @param strSearchBeginningVal
	 * @param dateCity
	 * @return the list
	 */
	List<City> getCitiesListByNameAndDate( String strSearchBeginningVal, Date dateCity );

	/**
	 * get country by code and date
	 * 
	 * @param strCodeCountry
	 * @param dateCountry
	 * @return the country (as Optional)
	 */
	Optional<Country> getCountryByCodeAndDate(Date dateCountry, String strCodeCountry);

	/**
	 * search cities by name beginning with a string and a date
	 * 
	 * @param strSearchBeginningVal
	 * @param dateCity
	 * @return the list
	 */
	List<City> getCitiesListByNameAndDateLike(String strSearchBeginningVal, Date dateCity);
	
	/**
	 * search cities by name beginning with a string
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	List<City> getCitiesListByNameLike( String strSearchBeginningVal );

	/**
	 * search cities by exact name 
	 * 
	 * @param strSearchBeginningVal
	 * @return the list
	 */
	List<City> getCitiesListByName( String strSearchBeginningVal );

	/**
	 * get city by code
	 * 
	 * @param strCode
	 * @return the city (as Optional)
	 */
	Optional<City> getCityByCode( String strCode );

}
