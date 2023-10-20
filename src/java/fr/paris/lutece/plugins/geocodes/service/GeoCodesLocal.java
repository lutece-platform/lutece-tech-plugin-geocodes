package fr.paris.lutece.plugins.geocodes.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
import fr.paris.lutece.plugins.geocodes.provider.IGeoCodeProvider;
import fr.paris.lutece.plugins.geocodes.rs.Constants;

public class GeoCodesLocal implements IGeoCodeProvider{
	
	@Override
	public String getId() {
		return Constants.ID_PROVIDER_GEOCODE_LOCAL;
	}
	
	@Override
	public Optional<City> getCityByDateAndCode( Date dateCity, String strCode ) {
		return  CityHome.findByDateAndCode( dateCity, strCode);
	}

	@Override
	public List<City> getCitiesListByNameAndDate( String strSearchBeginningVal, Date dateCity ) {
		return  CityHome.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
	}

}
