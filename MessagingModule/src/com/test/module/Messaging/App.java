package com.test.module.Messaging;

import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class App extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        App theApp = new App();       

        if(args.length > 0 && args[0].equals("auto-start"))
    	{
        	Handler handler = new Handler();
        	handler.start();
    	}
    	else
    	{
    		 theApp.enterEventDispatcher();
    	}
    }
    
    public App()
    {        
        pushScreen(new Screen());
    }    
    
}