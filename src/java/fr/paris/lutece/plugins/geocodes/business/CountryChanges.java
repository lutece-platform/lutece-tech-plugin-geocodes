package fr.paris.lutece.plugins.geocodes.business;

import java.util.Date;

public class CountryChanges extends Country
{
    private int _nIdChanges;

    private Date _dateLastUpdate;

    private String _strAuthor;

    private String _strStatus;

    public int getIdChanges()
    {
        return _nIdChanges;
    }

    public void setIdChanges(int _nIdChanges)
    {
        this._nIdChanges = _nIdChanges;
    }

    public Date getDateLastUpdate() {
        return _dateLastUpdate;
    }

    public void setDateLastUpdate(Date dateLastUpdate) {
        this._dateLastUpdate = dateLastUpdate;
    }

    public String getAuthor()
    {
        return _strAuthor;
    }

    public void setAuthor(String _strAuthor)
    {
        this._strAuthor = _strAuthor;
    }

    public String getStatus()
    {
        return _strStatus;
    }

    public void setStatus(String _strStatus)
    {
        this._strStatus = _strStatus;
    }

}
