package com.test.module.Messaging;

import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class Screen extends MainScreen
{

	Handler handler;
	RichTextField Recv,Sent,messageField;
	int SMSmessageCount = 0;
	int MMSmessageCount = 0;
	int DownCount = 0;
	int UpCount = 0;
	
	public Screen()
    {        
        // Set the displayed title of the screen       
        setTitle("Message tracker");

        handler = new Handler();
        handler.start();
        
        SMSmessageCount = handler.GetTextMessageCount();
        MMSmessageCount = handler.GetMultiMediaMessageCount();
        
        Recv = new RichTextField("Text Messages received/sent : " + SMSmessageCount,RichTextField.NON_FOCUSABLE);
        add(Recv);

        Sent = new RichTextField("Multi Media Messages received/sent : " + MMSmessageCount,RichTextField.NON_FOCUSABLE);
        add(Sent);
        
    }
	
}
