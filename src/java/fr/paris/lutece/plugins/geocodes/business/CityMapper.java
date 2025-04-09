package fr.paris.lutece.plugins.geocodes.business;

public class CityMapper
{
    public static CityChanges cityIntoCityChanges( City city, String author, String status )
    {
        CityChanges changes = new CityChanges();
        changes.setCode(city.getCode( ));
        changes.setCodeCountry( city.getCodeCountry( ) );
        changes.setCodeZone( city.getCodeZone( ) );
        changes.setDateValidityStart( city.getDateValidityStart( ) );
        changes.setDateValidityEnd( city.getDateValidityEnd( ) );
        changes.setValue( city.getValue( ) );
        changes.setValueMin( city.getValueMin( ) );
        changes.setValueMinComplete( city.getValueMinComplete( ) );
        changes.setDateLastUpdate( city.getDateLastUpdate( ) );
        changes.setDeprecated( city.isDeprecated( ) );
        changes.setAuthor( author );
        changes.setStatus( status );

        return changes;
    }
}
