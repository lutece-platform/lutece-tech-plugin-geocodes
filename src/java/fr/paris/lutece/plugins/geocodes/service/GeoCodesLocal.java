package fr.paris.lutece.plugins.geocodes.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.CityHome;
import fr.paris.lutece.plugins.geocodes.business.Country;
import fr.paris.lutece.plugins.geocodes.business.CountryHome;
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
	public Optional<City> getCityByCode( String strCode ) {
		return  CityHome.findByCode( strCode );
	}

	@Override
	public List<City> getCitiesListByNameAndDate( String strSearchBeginningVal, Date dateCity ) {
		return  CityHome.getCitiesListByNameAndDate( strSearchBeginningVal, dateCity );
	}
	
	@Override
	public List<City> getCitiesListByNameAndDateLike( String strSearchBeginningVal, Date dateCity ) {
		return  CityHome.getCitiesListByNameAndDateLike( strSearchBeginningVal, dateCity );
	}
	
	@Override
	public List<City> getCitiesListByNameLike( String strSearchBeginningVal ) {
		return  CityHome.getCitiesListByNameLike( strSearchBeginningVal );
	}
	
	@Override
	public List<City> getCitiesListByName( String strSearchBeginningVal ) {
		return  CityHome.getCitiesListByName( strSearchBeginningVal );
	}
	
	@Override
	public Optional<Country> getCountryByCodeAndDate ( Date dateCountry, String strCodeCountry )
	{
		return CountryHome.findByCode( strCodeCountry );
	}

}
