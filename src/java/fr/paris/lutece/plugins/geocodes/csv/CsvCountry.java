package fr.paris.lutece.plugins.geocodes.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import fr.paris.lutece.plugins.geocodes.rs.Constants;

import java.util.Date;

public class CsvCountry {

    @CsvBindByName( column =  "geocodes.manage_countries.columnCode" )
    @CsvBindByPosition( position = 0 )
    private String _strCode;

    @CsvBindByName( column =  "geocodes.manage_countries.columnValue" )
    @CsvBindByPosition( position = 1 )
    private String _strValue;

    @CsvBindByName( column =  "geocodes.manage_countries.columnValueMinComplete" )
    @CsvBindByPosition( position = 2 )
    private String _strValueMinComplete;

    @CsvBindByName( column =  "geocodes.manage_countries.columnAttached" )
    @CsvBindByPosition( position = 3 )
    private String _strAttached;

    @CsvBindByName( column =  "geocodes.manage_countries.columnDateValidityStart" )
    @CsvBindByPosition( position = 4 )
    @CsvDate( Constants.CSV_DATE_FORMAT )
    private Date _dateValidityStartDate;

    @CsvBindByName( column =  "geocodes.manage_countries.columnDateValidityEnd" )
    @CsvBindByPosition( position = 5 )
    @CsvDate( Constants.CSV_DATE_FORMAT )
    private Date _dateValidityEndDate;

    @CsvBindByName( column =  "geocodes.manage_countries.columnDeprecated" )
    @CsvBindByPosition( position = 6 )
    private String _strDeprecated;

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

    public String getValueMinComplete() {
        return _strValueMinComplete;
    }

    public void setValueMinComplete(final String _strValueMinComplete) {
        this._strValueMinComplete = _strValueMinComplete;
    }

    public String getAttached() {
        return _strAttached;
    }

    public void setAttached(final String _strAttached) {
        this._strAttached = _strAttached;
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

    public String getDeprecated() {
        return _strDeprecated;
    }

    public void setDeprecated(final String _strDeprecated) {
        this._strDeprecated = _strDeprecated;
    }
}
