package com.phone.Data;

import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioListener;
import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class DataApp extends UiApplication
{
	public static RadioActiveHandler RadioHandler = null;
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
    	DataApp theApp = new DataApp();
    	if(args.length > 0 && args[0].equals("autostart"))
        {
 	        theApp.requestBackground();
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
        pushScreen(new DataScreen());
    }    
}
