package fr.paris.lutece.plugins.geocodes.business;

public class CityChanges extends City
{
    private int _nIdChanges;

    private String _strAuthor;

    private String _strApplied;

    public int getIdChanges()
    {
        return _nIdChanges;
    }

    public void setIdChanges(int _nIdChanges)
    {
        this._nIdChanges = _nIdChanges;
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
        return _strApplied;
    }

    public void setStatus(String _strStatus)
    {
        this._strApplied = _strStatus;
    }
}
