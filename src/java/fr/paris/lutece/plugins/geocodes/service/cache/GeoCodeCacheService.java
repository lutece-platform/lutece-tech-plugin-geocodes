package fr.paris.lutece.plugins.geocodes.service.cache;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.util.AppLogService;

public final class GeoCodeCacheService extends AbstractCacheableService 
{

	// CONSTANTS
	private static final String SERVICE_NAME = "GeoCode Cache Service";
	
	// VARIABLES
    private static GeoCodeCacheService _singleton;
    
    private GeoCodeCacheService( )
    {
    	
    }
	
    /**
     * Get the instance of CalendarCacheService
     * @return an instance of CalendarCacheService
     */
    public static GeoCodeCacheService getInstance(  )
    {
    	if ( _singleton == null )
    	{
    		_singleton = new GeoCodeCacheService(  );
    	}
    	return _singleton;
    }
    
	/**
     * Get the service name
     * @return the service name
     */
	@Override
	public String getName() {
		return SERVICE_NAME;
	}
	
	/**
     * Remove the cache by a key
     * @param strKey the cache key
     */
    public void removeCache( String strKey )
    {
        try
        {
            if ( isCacheEnable(  ) && ( getCache(  ) != null ) )
            {
                getCache(  ).remove( strKey );
            }
        }
        catch ( IllegalStateException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

}
