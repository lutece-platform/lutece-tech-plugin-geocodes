package fr.paris.lutece.plugins.geocodes.service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import fr.paris.lutece.plugins.geocodes.business.City;
import fr.paris.lutece.plugins.geocodes.business.Country;
import fr.paris.lutece.plugins.geocodes.csv.CsvCity;
import fr.paris.lutece.plugins.geocodes.csv.CsvCountry;
import fr.paris.lutece.plugins.geocodes.csv.CustomMappingStrategy;
import fr.paris.lutece.portal.service.util.AppException;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class CsvExportService {
    
    private static CsvExportService instance;

    private CsvExportService( )
    {
    }

    public static CsvExportService instance( )
    {
        if (instance == null)
        {
            instance = new CsvExportService();
        }
        return instance;
    }

    public byte[] writeCountries(final List<Country> countries) throws AppException
    {
        try
        {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final Writer writer = new OutputStreamWriter(out);
            final CustomMappingStrategy<CsvCountry> mappingStrategy = new CustomMappingStrategy<>();
            mappingStrategy.setType(CsvCountry.class);
            StatefulBeanToCsv<CsvCountry> countriesWriter = (new StatefulBeanToCsvBuilder(writer)).withMappingStrategy(mappingStrategy).withOrderedResults(true).withSeparator(';').withApplyQuotesToAll(false).build();
            countriesWriter.write(this.extractCsvCountries(countries));
            writer.close();
            return out.toByteArray();
        }
        catch (final Exception e)
        {
            throw new AppException("An error occurred while exporting csv countries. ", e);
        }
    }

    public byte[] writeCities(final List<City> cities) throws AppException
    {
        try
        {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final Writer writer = new OutputStreamWriter(out);
            final CustomMappingStrategy<CsvCity> mappingStrategy = new CustomMappingStrategy<>();
            mappingStrategy.setType(CsvCity.class);
            StatefulBeanToCsv<CsvCity> citiesWriter = (new StatefulBeanToCsvBuilder(writer)).withMappingStrategy(mappingStrategy).withOrderedResults(true).withSeparator(';').withApplyQuotesToAll(false).build();
            citiesWriter.write(this.extractCsvCities(cities));
            writer.close();
            return out.toByteArray();
        }
        catch (final Exception e)
        {
            throw new AppException("An error occurred while exporting csv cities. ", e);
        }
    }

    private List<CsvCountry> extractCsvCountries(final List<Country> countries) {
        return countries.stream().map(country -> {
            final CsvCountry csvCountry = new CsvCountry();
            csvCountry.setCode( country.getCode( ) );
            csvCountry.setValue(country.getValue( ) );
            csvCountry.setValueMinComplete( country.getValueMinComplete( ) );
            csvCountry.setAttached( String.valueOf( country.isAttached( ) ) );
            csvCountry.setValidityStartDate( country.getDateValidityStart( ) );
            csvCountry.setValidityEndDate( country.getDateValidityEnd( ) );
            csvCountry.setDeprecated( String.valueOf( country.isDeprecated( ) ) );
            return csvCountry;
        }).collect(Collectors.toList());
    }

    private List<CsvCity> extractCsvCities(final List<City> cities) {
        return cities.stream().map(city -> {
            final CsvCity csvCity = new CsvCity();
            csvCity.setCodeCountry( city.getCodeCountry( ) );
            csvCity.setCode( city.getCode( ) );
            csvCity.setValue( city.getValue( ) );
            csvCity.setCodeZone( city.getCodeZone( ) );
            csvCity.setValueMin( city.getValueMin( ) );
            csvCity.setValueMinComplete( city.getValueMinComplete( ) );
            csvCity.setValidityStartDate( city.getDateValidityStart( ) );
            csvCity.setValidityEndDate( city.getDateValidityEnd( ) );
            csvCity.setDeprecated( String.valueOf( city.isDeprecated( ) ) );
            return csvCity;
        }).collect(Collectors.toList());
    }

}
