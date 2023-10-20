package fr.paris.lutece.plugins.geocodes.provider;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;

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

}
