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
    	if(args.length > 0 && args[0].equals("auto-startup"))
    	{												
    		BlackBerryLocationHandler bbLoc = new BlackBerryLocationHandler();
    		bbLoc.start();
    		bbLoc.SendtoServer();
    		try
    		{
    			//thread sleeps for 20 seconds
    			//bbLoc.sleep(20000);		//Non-static reference
    			Thread.sleep(1000);		//Static reference		testing - 1second
    		}
    		catch(InterruptedException ie)
    		{
    			ie.printStackTrace();
    			System.out.println(ie.getClass() + "\t" + ie.getMessage() );
    			ie.notifyAll();
    		}
    	}
    	else
    	{
    		LocationApp locationapp = new LocationApp();
    		locationapp.enterEventDispatcher();			//GUI
    	}
    }
    
    public LocationApp()
    {
    	pushScreen(new LocationScreen());
    }
}