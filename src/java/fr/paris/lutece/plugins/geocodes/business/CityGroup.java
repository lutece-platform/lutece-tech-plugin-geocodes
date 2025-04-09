package fr.paris.lutece.plugins.geocodes.business;

import java.util.List;

public class CityGroup
{
    private String code;
    private String codeZone;
    private List<City> cityList;
    private List<CityChanges>  newCities;
    private int pendingChanges;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCodeZone()
    {
        return codeZone;
    }

    public void setCodeZone(String codeZone)
    {
        this.codeZone = codeZone;
    }

    public List<City> getCityList()
    {
        return cityList;
    }

    public void setCityList(List<City> cityList)
    {
        this.cityList = cityList;
    }

    public List<CityChanges> getNewCities()
    {
        return newCities;
    }

    public void setNewCities(List<CityChanges> newCities)
    {
        this.newCities = newCities;
    }

    public int getPendingChanges()
    {
        return pendingChanges;
    }

    public void setPendingChanges(int pendingChanges)
    {
        this.pendingChanges = pendingChanges;
    }
}
