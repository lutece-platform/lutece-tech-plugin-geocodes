package fr.paris.lutece.plugins.geocodes.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import fr.paris.lutece.plugins.geocodes.rs.Constants;

import java.util.Date;

public class CsvCity {

    @CsvBindByName( column =  "geocodes.manage_cities.columnCodeCountry" )
    @CsvBindByPosition( position = 0 )
    private String _strCodeCountry;

    @CsvBindByName( column =  "geocodes.manage_cities.columnCode" )
    @CsvBindByPosition( position = 1 )
    private String _strCode;

    @CsvBindByName( column =  "geocodes.manage_cities.columnValue" )
    @CsvBindByPosition( position = 2 )
    private String _strValue;

    @CsvBindByName( column =  "geocodes.manage_cities.columnCodeZone" )
    @CsvBindByPosition( position = 3 )
    private String _strCodeZone;

    @CsvBindByName( column =  "geocodes.manage_cities.columnDateValidityStart" )
    @CsvBindByPosition( position = 4 )
    @CsvDate( Constants.CSV_DATE_FORMAT )
    private Date _dateValidityStartDate;

    @CsvBindByName( column =  "geocodes.manage_cities.columnDateValidityEnd" )
    @CsvBindByPosition( position = 5 )
    @CsvDate( Constants.CSV_DATE_FORMAT )
    private Date _dateValidityEndDate;

    @CsvBindByName( column =  "geocodes.manage_cities.labelValueMin" )
    @CsvBindByPosition( position = 6 )
    private String _strValueMin;

    @CsvBindByName( column =  "geocodes.manage_cities.labelValueMinComplete" )
    @CsvBindByPosition( position = 7 )
    private String _strValueMinComplete;

    @CsvBindByName( column =  "geocodes.manage_cities.columnDateLastUpdate" )
    @CsvBindByPosition( position = 8 )
    @CsvDate( Constants.CSV_DATE_FORMAT )
    private Date _dateLastUpdateDate;

    @CsvBindByName( column =  "geocodes.manage_cities.columnDeprecated" )
    @CsvBindByPosition( position = 9 )
    private String _strDeprecated;

    public String getCodeCountry() {
        return _strCodeCountry;
    }

    public void setCodeCountry(final String _strCodeCountry) {
        this._strCodeCountry = _strCodeCountry;
    }

    public String getCode() {
        return _strCode;
    }

    public void setCode(final String _strCode) {
        this._strCode = _strCode;
    }

    public String getValue() {
        return _strValue;
    }

    public void setValue(final String _strValue) {
        this._strValue = _strValue;
    }

    public String getCodeZone() {
        return _strCodeZone;
    }

    public void setCodeZone(final String _strCodeZone) {
        this._strCodeZone = _strCodeZone;
    }

    public Date getValidityStartDate() {
        return _dateValidityStartDate;
    }

    public void setValidityStartDate(final Date _dateValidityStartDate) {
        this._dateValidityStartDate = _dateValidityStartDate;
    }

    public Date getValidityEndDate() {
        return _dateValidityEndDate;
    }

    public void setValidityEndDate(final Date _dateValidityEndDate) {
        this._dateValidityEndDate = _dateValidityEndDate;
    }

    public String getValueMin() {
        return _strValueMin;
    }

    public void setValueMin(final String _strValueMin) {
        this._strValueMin = _strValueMin;
    }

    public String getValueMinComplete() {
        return _strValueMinComplete;
    }

    public void setValueMinComplete(final String _strValueMinComplete) {
        this._strValueMinComplete = _strValueMinComplete;
    }

    public Date getLastUpdateDate() {
        return _dateLastUpdateDate;
    }

    public void setLastUpdateDate(final Date _dateLastUpdateDate) {
        this._dateLastUpdateDate = _dateLastUpdateDate;
    }

    public String getDeprecated() {
        return _strDeprecated;
    }

    public void setDeprecated(final String _strDeprecated) {
        this._strDeprecated = _strDeprecated;
    }
}
