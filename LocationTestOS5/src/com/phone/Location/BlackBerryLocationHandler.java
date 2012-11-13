package com.phone.Location;

import javax.microedition.location.AddressInfo;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.system.RadioInfo;

public class BlackBerryLocationHandler extends Thread{

	
	//public BlackBerryCriteria bbcriteria = null;
	//public BlackBerryLocation bblocation = null;
	//public BlackBerryLocationProvider bblocationProvider = null;

	//public QualifiedCoordinates coordinates = null;
	
	public int signalQuality = 0;
	public int LocStatus = 0;
	
	public float Course = 0;
	public float Alt = 0;
	public double LAT = 0;
	public double LONG = 0;
	public float Accuracy = 0;
	
	public float Speed = 0;
	public String CurrentAddress = "";
	public String city = "";
	public static AddressInfo addressInfo = null;
	
	public int interval = 2;

	public String stream = null;
	public int Prompt = 0;
	
	public final String NMEA_MIME = "application/X-jsr179-location-nmea";
	public final String Satellite = "Satellites"; 
	public String NumbSat = "";
	
	public BlackBerryLocationHandler()
	{
		signalQuality = 0;
		LocStatus = 0;
		LAT = 0;
		LONG = 0;
		
		Speed = 0;
		CurrentAddress = "";
		city = "";
		addressInfo = null;
		
		if(CurrentLocationValid())
		{
			/*if(getPrompt() != 0){
				SendtoServer();
			}
			else{
				//nothing at all
			}*/
		}
		else{
			
		}
	}

	public boolean CurrentLocationValid()
	{
		try
		{
			LocationProvider locationprovider = LocationProvider.getInstance(null);
			if(locationprovider!=null)
				locationprovider.setLocationListener(new LocationListening(), interval, 1, 1);
		}
		catch(LocationException le)
		{
			
		}
		
		return true;
	}
	
	private class LocationListening implements LocationListener
	{
		public void locationUpdated(LocationProvider provider, Location location) 
		{
			if(location.isValid())
			{
				Course = location.getCourse();
				LAT = location.getQualifiedCoordinates().getLatitude();
				LONG = location.getQualifiedCoordinates().getLongitude();
				//Alt = location.getQualifiedCoordinates().getAltitude();
				Speed = location.getSpeed();
				
				NumbSat = location.getExtraInfo(Satellite);
				if(NumbSat == null)
				{
					NumbSat = location.getExtraInfo(NMEA_MIME);
				}
				Accuracy = location.getQualifiedCoordinates().getHorizontalAccuracy();
			}
		}

		public void providerStateChanged(LocationProvider provider, int newState) 
		{
		}
		
	}

	public String isRoaming()
	{
		boolean roaming = false;
		if(RadioInfo.getNetworkService(RadioInfo.getActiveWAFs()) == RadioInfo.NETWORK_SERVICE_ROAMING)
		{
			roaming = true; 
		}
		else
			roaming = false;
		
		return (roaming ? "Yes" : "No");
	}
	
	/**
	 * @return the Accuracy
	 */
	public float getAccuracy()
	{
		return Accuracy;
	}

	/**
	 * @return the Number of Satellites
	 */
	public String getNumbSat()
	{
		return NumbSat;
	}

	/**
	 * @return the Speed
	 */
	public float getSpeed() 
	{
		return Speed;
	}

	/**
	 * @return the Altitude
	 *//*
	public float getAlt() {
		return Alt;
	}*/

	/**
	 * @return the Longitude
	 */
	public double getLONG() 
	{
		return LONG;
	}

	/**
	 * @return the Latitude
	 */
	public double getLAT() 
	{
		return LAT;
	}

	/**
	 * @return the Course
	 */
	public float getCourse()
	{
		return Course;
	}
	
	public void SendtoServer()
	{
		stream = "Roaming:\t" + isRoaming() + 
				"| Latitude:\t" + getLAT() + 
				"| Longitude:\t" + getLONG() +
				"| Accuracy:\t" + getAccuracy() +
				"| Speed:\t" + getSpeed() +
				"| # of Towers:\t" + getNumbSat();
		//Prompt = new ServerCommunication(stream).getPrompt();	
	}
	
//	public int getPrompt() 
//	{
//		return Prompt;
//	}
	
	
/*	
	
	public boolean isRoaming()
	{
		if(RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING ||
				RadioInfo.getNetworkService() == RadioInfo.NETWORK_SERVICE_ROAMING_OFF_CAMPUS)
			return true;
		else
			return false;
				
	}
	
	public String[] GeoLocation()
	{
		String[] array = new String[10];
		//bbcriteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_ASSIST);
		
		BlackBerryCriteria bbcriteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_ASSIST);
		
		bbcriteria.setAltitudeRequired(true);
		bbcriteria.setCostAllowed(true);	//by default is True..if set false 
											//and device has a cost the returned value 
											//will only be LocationProvider
//		bbcriteria.setFailoverMode(GPSInfo.GPS_MODE_ASSIST, 2, 30);
											//Using GPS-Assist -- to fetch information even in poor signal
											//MaxRetry -- (all b/w 0-3) allowed for 2 retries
											//Timeout -- (all b/w 30-300 sec) above or below is adjusted to min/max values
		bbcriteria.setHorizontalAccuracy(5000);
		bbcriteria.setVerticalAccuracy(5000);
											//Range set to 3000 meters approximation/accuracy
		//bbcriteria.setMode(GPSInfo.GPS_MODE_ASSIST); not required set in constructor
											//By default mode is set to GPS Assist
		bbcriteria.setPreferredResponseTime(-1);
											//Timeout -- 10secs
		bbcriteria.setPreferredPowerConsumption(BlackBerryCriteria.POWER_USAGE_HIGH);
											//Uses full power of antenna to fetch data
		bbcriteria.setSpeedAndCourseRequired(true);
		bbcriteria.setAddressInfoRequired(true);
											//Speed and Course can be fetched
		try
		{
			BlackBerryLocationProvider bblocationProvider =(BlackBerryLocationProvider)
									BlackBerryLocationProvider.getInstance((Criteria)bbcriteria);
		
			BlackBerryLocation bblocation = (BlackBerryLocation) bblocationProvider.getLocation(-1);
			if(bblocation != null)
			{
				QualifiedCoordinates coordinates = bblocation.getQualifiedCoordinates();
				LAT = coordinates.getLatitude();
				array[0] = String.valueOf(LAT);
				LONG = coordinates.getLongitude();
				array[1] = String.valueOf(LONG);
				
				//addressInfo = bblocation.getAddressInfo();
				//if(addressInfo!=null)
				//if(addressInfo == null)
				{
					//array[2] = String.valueOf(addressInfo);
					//array[2] = "INVALID!!!";
					signalQuality = bblocation.getAverageSatelliteSignalQuality();
					array[2] = String.valueOf(signalQuality);
					LocStatus = bblocation.getStatus();
					array[3] = String.valueOf(LocStatus);
					
					Speed = bblocation.getSpeed();
					array[4] = String.valueOf(Speed);
					CurrentAddress = "Country:"+ addressInfo.getField(addressInfo.COUNTRY) +
										"\r\nProvince:"+ addressInfo.getField(addressInfo.STATE) +
										"\r\nCity:"+ addressInfo.getField(addressInfo.CITY);
										
					
					//city = addressInfo.getField(AddressInfo.CITY);
					//array[5] = city;
					
					//AddressInfo addr = bblocation.getAddressInfo();
					//city = addr.getField(AddressInfo.CITY);
					//array[6] = city;
					//array[6] = addr.getField(AddressInfo.CITY);
					//AddressInfo addrD = bblocation.getAddressInfo();
					
//					Landmark[] landmark = Locator.reverseGeocode(coordinates, Locator.CITY);
//					array[7] = landmark[0].getAddressInfo().getField(AddressInfo.CITY);
				}
//				else
//				{
//					array[2] = "INVALID!!!";
//				}
			}
			else
			{
				array = null;
			}
		}	
		catch(NullPointerException npe)
		{
			Dialog.alert("NPE caught" + npe.getClass() + "\n\r" + npe.getMessage()
					);
			npe.printStackTrace();
			System.out.println(npe.getClass() + "\t:" + npe.getMessage());
		}
		catch(LocationException le)
		{
			le.printStackTrace();
			System.out.println(le.getClass() + "\t:" + le.getMessage());
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
			System.out.println(ie.getClass() + "\t:" + ie.getMessage());
		}
		
		catch(LocatorException loce)
		{
			loce.printStackTrace();
			System.out.println(loce.getClass() + "\t:" + loce.getMessage());
		}
		
		return array;
	}
	*/
}