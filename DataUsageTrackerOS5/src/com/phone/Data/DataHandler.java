package com.phone.Data;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioStatusListener;
import net.rim.device.api.ui.UiApplication;

/**
 * 
 */

/**
 * @author Rohan K.M [rohan.mahendroo@gmail.com]
 *
 */
public class DataHandler extends Thread 
{
	public boolean DataStatus = false;
	public double Downloaded = 0;
	public double Uploaded = 0;
	
	/**
	 * Starts the thread
	 */
	public DataHandler()
	{
		if(DataisWorking())	//checks MDS/PDP state
		{
			StartTracking();
		}
		else
		{
			//FUTURE do something
		}
}
	
	/**
	 * Checks whether MDS/PDP is active or not
	 * works for both GSM and CDMA
	 * @return Data status
	 */
	public boolean DataisWorking()
	{
		if(RadioInfo.getState() == RadioInfo.WAF_3GPP ||
				RadioInfo.getState() == RadioInfo.WAF_CDMA)
			DataStatus = true;
		else
			DataStatus = false;
		
		return DataStatus;
	}
	
	/**
	 * Starts tracking data In/Out
	 */
	public void StartTracking()
	{
		Downloaded =+ RadioInfo.getNumberOfPacketsReceived();
		Uploaded =+ RadioInfo.getNumberOfPacketsSent();
	}

	/**
	 * @return Bytes Uploaded
	 */
	public double getUploaded()
	{
		return Uploaded;
	}

	/**
	 * @return Bytes Downloaded
	 */
	public double getDownloaded() 
	{
		return Downloaded;
	}
	
}
