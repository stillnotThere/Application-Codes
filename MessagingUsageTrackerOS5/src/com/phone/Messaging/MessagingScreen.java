package com.phone.Messaging;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessagePart;
import javax.wireless.messaging.TextMessage;

import net.rim.blackberry.api.mms.MMS;
import net.rim.blackberry.api.sms.SMS;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MessagingScreen extends MainScreen
{
	
	/**
     * Creates a new MessagingScreen object
     */
    public MessagingScreen()
    {        
        // Set the displayed title of the screen       
        setTitle("MyTitle");
    }
    	
    public boolean onClose()
    {
    	Dialog.alert("Exiting app");
    	System.exit(0);
    	return true;
    }
    
    class MessageBG extends Thread
    {
    	
    }
    
}

class MessageBG extends Thread
{
	public long messagingCount = 0;
	public long smsCount = 0;
	public long mmsCount = 0;
	
	public BinaryMessage binaryMessage = null;
	public Message textmessage = null;
	public Message multimediamessage = null;
	public MessageConnection textmessageConnection = null;
	public MessageConnection multimediamessageConnection = null;
	public MessagingActiveHandler textmessageListener = null;
	public MessagingActiveHandler multimediamessageListener = null;
	public TextMessage textMessage = null;
	public MessagePart messagePart = null;

	public SMS sms_text = null;
	public MMS mms_multimedia = null;
	
	public void openMessagingConnection()
    {
    	try
    	{
	    	textmessageConnection = (MessageConnection) Connector.open("sms://");
	    	multimediamessageConnection = (MessageConnection) Connector.open("mms://");
	    	
	    	textmessageConnection.setMessageListener(textmessageListener);
	    	textmessageConnection.setMessageListener(multimediamessageListener);
	    	
	    	textmessage = textmessageConnection.receive();
	    	multimediamessage = multimediamessageConnection.receive();
	    	
	    	if(textmessageListener.IncomingMessage || textmessage!=null)
	    	{
	    		smsCount++;
	    	}
	    	
	    	if(multimediamessageListener.IncomingMessage || multimediamessage != null)
	    	{
	    		mmsCount++;
	    	}
	    	
    	}
    	catch(IOException ioe)
    	{
    		ioe.printStackTrace();
    		System.out.println(ioe.getClass() + "\t:"+ioe.getMessage());
    	}
    }
	
	/**
     * @return void
     * @param N/A
     * @desc Keeps track of messaging (SMS and MMS not BBM or E-Mail)
     * sends counter values to MessagingScreen() method for 
     * interface presentation
     */
    public void TrackMessages()
    {
    	
    }
    
	
	
}