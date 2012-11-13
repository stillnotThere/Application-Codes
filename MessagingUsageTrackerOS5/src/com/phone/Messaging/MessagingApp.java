
package com.phone.Messaging;

import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class MessagingApp extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        
        MessagingApp theApp = new MessagingApp();
        
        
       if(args!=null && args.length>0 && args[0].equals("interface"))
       {
    	   theApp.enterEventDispatcher();		// Create a new instance of the application and make the currently
           										// running thread the application's event dispatch thread.
       }
       else
       {
    	   new MessagingRunnable().startApp();
       }
    }
    

    /**
     * Creates a new MyApp object
     */
    public MessagingApp()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new MessagingScreen());
    }    
}

