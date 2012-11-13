package com.phone.Data;

import net.rim.device.api.system.RadioStatusListener;
import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class DataApp extends UiApplication
{
	public static RadioActiveHandler RadioHandler = null;
	static boolean RadioOFF = false;
	int APNID = 0;
	int Cause = 0;
	boolean NetworkChange = false;
	int SignalLevel = 0;
	/**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
    	DataApp theApp = new DataApp();
    	theApp.addRadioListener(RadioHandler);
    	if(args.length > 0 && args[0].equals("auto-start"))
        {
    		if(!isRadioOFF())
    		{
    			DataHandler data = new DataHandler();
    			data.start();
    		}
        }
        else
        {
 	        theApp.enterEventDispatcher();
        }
    }
    
    /**
     * Creates a new MyApp object
     */
    public DataApp()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new DataScreen(isRadioOFF()));
    }   
    
	public class RadioHandler implements RadioStatusListener
	{
		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#baseStationChange()
		 */
		public void baseStationChange() 
		{
			
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#networkScanComplete(boolean)
		 */
		public void networkScanComplete(boolean success) 
		{
			
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#networkServiceChange(int, int)
		 */
		public void networkServiceChange(int networkId, int service) 
		{
			
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#networkStarted(int, int)
		 */
		public void networkStarted(int networkId, int service) 
		{
			
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#networkStateChange(int)
		 */
		public void networkStateChange(int state) 
		{
			if(state == 0)
				NetworkChange = false;
			else
				NetworkChange = true;
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#pdpStateChange(int, int, int)
		 */
		public void pdpStateChange(int apn, int state, int cause)
		{
			APNID = apn;
			Cause = cause;
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#radioTurnedOff()
		 */
		public void radioTurnedOff()
		{
			RadioOFF = true;
		}

		/** (non-Javadoc)
		 * @see net.rim.device.api.system.RadioStatusListener#signalLevel(int)
		 */
		public void signalLevel(int level) 
		{
			SignalLevel = level;
		}
		
	}

	/**
	 * @return The APN ID
	 */
	public int getAPNID() 
	{
		return APNID;
	}

	/**
	 * @return Is Radio OFF
	 */
	public static boolean isRadioOFF() 
	{
		return RadioOFF;
	}

	/**
	 * @return Change in Network
	 */
	public boolean isNetworkChange() 
	{
		return NetworkChange;
	}

	/**
	 * @return Signal Level
	 */
	public int getSignalLevel() 
	{
		return SignalLevel;
	}

	/**
	 * @return The cause of PDP change
	 */
	public int getCause() {
		return Cause;
	}


}