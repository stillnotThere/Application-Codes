package com.phone.Location;

import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class LocationApp extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)		//Location thread
    {
    	LocationApp locationapp = new LocationApp();
    	
    	if(args.length > 0 && args[0].equals("auto-startup"))
    	{												
    		BlackBerryLocationHandler bbLoc = new BlackBerryLocationHandler();
    		bbLoc.start();
    	}
    	else
    	{
    	  locationapp.enterEventDispatcher();			//GUI
    	}
    }
    
    public LocationApp()
    {
    	pushScreen(new LocationScreen());
    }
}