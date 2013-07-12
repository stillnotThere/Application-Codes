package com.phone.Location;

import javax.microedition.location.AddressInfo;
import javax.microedition.location.Criteria;
import javax.microedition.location.Landmark;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import net.rim.device.api.lbs.Locator;
import net.rim.device.api.system.RadioInfo;

public class LocationHandler {

	
	
	
	
	
	/*
	*//**
	 * Variables
	 *//*
	public String city = "";
    public double latitude = 0;
    public double longitude = 0;
    //public Location location = null;
    //public LocationProvider locationProvider = null;
    //public AddressInfo addressInfo = null;
    //public Criteria criteria = null;
    //public QualifiedCoordinates qualifiedCoordinates = null;
    //public long timestamp = 0;
    //public float speed = 0;
    
    //public boolean LocationValid = false;
	//public int LocationMethod = 0;
	
    public Landmark[] landmark = null;
    public int responsetime = 0;
    public int powerconsumption = 0;
    public int horizontalaccuracy = 0;
    public int verticalaccuracy = 0;

    public String streamData = "";
    
	public LocationHandler()
	{
		
	}

	*//**
	 * Checks whether the device is Roaming by looking over whether out of "home territory" or 
	 * "operator-campus"
	 * @return True on Roaming
	 *//*
    public boolean isRoaming()
	{
    	boolean roaming = false;
		if(RadioInfo.getActiveWAFs() > 1 || RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING)
		{
			if(RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING_OFF_CAMPUS || RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING)
				roaming=true;
			
			if (RadioInfo.getEnabledWAFs() == RadioInfo.WAF_3GPP ||RadioInfo.getEnabledWAFs() == RadioInfo.WAF_CDMA 
					|| RadioInfo.getEnabledWAFs() == RadioInfo.WAF_IDEN ||RadioInfo.getEnabledWAFs() == RadioInfo.WAF_WLAN)
			{
				if(RadioInfo.getNetworkService(RadioInfo.getActiveWAFs()) == RadioInfo.NETWORK_SERVICE_ROAMING)
				roaming=true;
			}
			else 
				roaming=false;
		}
		else if(RadioInfo.getActiveWAFs()== 1)
		{
			if(RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING_OFF_CAMPUS || RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING)
				roaming = true;
			
			if (RadioInfo.getEnabledWAFs() == RadioInfo.WAF_3GPP ||RadioInfo.getEnabledWAFs()  == RadioInfo.WAF_CDMA 
					|| RadioInfo.getEnabledWAFs()  == RadioInfo.WAF_IDEN || RadioInfo.getEnabledWAFs()  == RadioInfo.WAF_WLAN)
			{
				if(RadioInfo.getNetworkService(RadioInfo.getActiveWAFs()) == RadioInfo.NETWORK_SERVICE_ROAMING)
				roaming=true;
			}
			else
				roaming=false;
		}
			
		else
			roaming = false;
		
		return roaming;
	}
    
    *//**
     * Uses defined "Criteria" to find the current coordinates("Qualified Coordinates") , 
     * Current Velocity (in m/s)
     * @category - Location handling (Reverse Geo-Coding)
     *//*
    public String[] ReverseGeocoding()
    {
    	LocationListener locationListener = null;
    	QualifiedCoordinates qualifiedCoordinates = null;
    	LocationProvider locationProvider = null;
    	Location location = null;
    	//AddressInfo addressInfo = null;
    	String city = "";
    	Landmark[] landmark = new Landmark[12];
    	String[] array = new String[10];
    	try{
    		Criteria criteria = new Criteria();
	    	criteria.isAddressInfoRequired();
	    	criteria.isSpeedAndCourseRequired();
	    	criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
	    	criteria.setPreferredResponseTime(-1);//Criteria.NO_REQUIREMENT);
	    	criteria.setHorizontalAccuracy(5000);
	    	criteria.setVerticalAccuracy(5000);
	    	criteria.setAltitudeRequired(true);
	    	criteria.setCostAllowed(true);
	    	
	    	responsetime = criteria.getPreferredResponseTime();
	    	powerconsumption = criteria.getPreferredPowerConsumption();
	    	horizontalaccuracy = criteria.getHorizontalAccuracy();
	    	verticalaccuracy = criteria.getVerticalAccuracy();
	    	
	    	
	    	locationProvider = LocationProvider.getInstance(criteria);
	    	location = locationProvider.getLocation(-1);			//default parameters according to the operator
	    	//locationProvider.setLocationListener(locationListener, -1, -1, -1);		//default parameters according to the operator
	    	array[0] = String.valueOf(location.isValid());
	    	array[1] = String.valueOf( location.getLocationMethod() );
	    	if(array[1].equals("1"))
	    	{
		    	qualifiedCoordinates = location.getQualifiedCoordinates();
		    	array[2] = String.valueOf(qualifiedCoordinates.getLatitude());
		    	array[3] = String.valueOf(qualifiedCoordinates.getLongitude());
		    	//array[4] = String.valueOf(location.getTimestamp());
		    	array[4] = String.valueOf(location.getSpeed());
	    	}
	    	//addressInfo = location.getAddressInfo();
	    	
	    	//addressInfo.setField(AddressInfo.CITY, "CITY");
	    	
	    	landmark = Locator.reverseGeocode(qualifiedCoordinates, Locator.CITY);
	    	array[5] = landmark[0].getAddressInfo().getField(AddressInfo.CITY);
	    	//landmark = Locator.geocode(addressInfo, qualifiedCoordinates);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return array;
    }
    */
}
