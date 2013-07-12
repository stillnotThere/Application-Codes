package com.phone.Messaging;

// Sample message listener program.
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;

import net.rim.blackberry.api.sms.OutboundMessageListener;

public class MessagingRunnable extends MIDlet implements MessageListener,OutboundMessageListener
{
    MessageConnection messconn;
    MessageConnection mmsconn;
    boolean done;
    public Reader reader;

    // Initial tests setup and execution.
    public void startApp() 
    {
        try
        {
            // Start a message-reading thread.
            done = false;
            reader = new Reader();
            new Thread(reader).start();
            // Get our receiving port connection.
            messconn = (MessageConnection) Connector.open("sms://:6222");		//sms Port No. 6222
            mmsconn = (MessageConnection) Connector.open("mms://8080");			//mms Port No. 8080
            // Register a listener for inbound messages.
            messconn.setMessageListener(this);
            
            
        }
        catch (IOException e) 
        {
            // Handle startup errors
        }
        Reader reader = null;
         
        Display display = Display.getDisplay(this);
        /*
         * TODO 
         * Check for UI for Hybrid applications
         */
        Form form = new Form("Messaging Layout");
        form.append("Incoming Message Count"+reader.getIncomingCount());
        form.append("Outgoing Message Count"+reader.getOutgoingCount());
        
        display.setCurrent(form);
        
        //UiEngine.class..add(new RichTextField("Incoming Message:"+ reader.getIncomingCount()));
        //add(new RichTextField("Outgoing Message:"+ reader.getOutgoingCount()));
    }
    
    //from OutboundMessageListener
    public void notifyOutgoingMessage(Message message)
    {
		if(message != null)
		{
			 reader.handleOutgoingMessage();
		}
	}
    //from MessageListener
    // Asynchronous callback for inbound message.
    public void notifyIncomingMessage(MessageConnection conn)
    {
        if (conn == messconn) 
        {
            reader.handleIncomingMessage();
        }
        if(conn == mmsconn)
        {
        	reader.handleIncomingMessage();
        	//reader.handleOutgoingMessage();
        }
    }

    // Required MIDlet method - release the connection and signal the reader thread to terminate.
    public void pauseApp() 
    {
        done = true;
        try {
            messconn.close();
        } catch (IOException e) {
            // Handle errors
        }
    }

    // Required MIDlet method - shutdown.
    // @param unconditional forced shutdown flag
    public void destroyApp(boolean unconditional) 
    {
        done = true;
        try {
            messconn.setMessageListener(null);
            messconn.close();
        } catch (IOException e) {
            // Handle shutdown errors.
        }
    }

    // Isolate blocking I/O on a separate thread, so callback can return immediately.
    public class Reader implements Runnable 
    {
    	public int IncomingMessages = 0;
    	public int OutgoingMessages = 0;

        // The run method performs the actual message reading.
        public void run() 
        {
            while (!done) 
            {
                synchronized (this)
                {
                    if (IncomingMessages == 0) 
                    {
                        try 
                        {
                            wait();
                        } 
                        catch (Exception e) 
                        {
                            // Handle interruption
                        }
                    }
                    IncomingMessages--;
                }
                // The benefit of the MessageListener is here. This thread could via similar
                // triggers be handling other kind of events as well in addition to just receiving
                // the messages.
                try 
                {
                    Message inmess = messconn.receive();
                } 
                catch (IOException ioe) 
                {
                    // Handle reading errors
                }
            }
        }

        // Invoked from notifyIncomingMessage() to handle the incoming message.
        public synchronized void handleIncomingMessage() 
        {
            IncomingMessages++;
            notify();
        }
        
        public synchronized void handleOutgoingMessage()
        {
        	OutgoingMessages++;
        	notify();
        }
     
        int getIncomingCount()
        {
        	return IncomingMessages;
        }
        int getOutgoingCount()
        {
        	return OutgoingMessages;
        }
        
    }


}
/*

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessagePart;
import javax.wireless.messaging.TextMessage;

import net.rim.blackberry.api.mms.MMS;
import net.rim.blackberry.api.sms.SMS;

public class MessagingRunnable implements Runnable {

	public long messagingCount = 0;
	public long smsCount = 0;
	public long mmsCount = 0;
	
	public BinaryMessage binaryMessage = null;
	public TextMessage textmessage = null;
	public Message multimediamessage = null;
	public MessageConnection textmessageConnection = null;
	public MessageConnection multimediamessageConnection = null;
	public MessagingActiveHandler textmessageListener = null;
	public MessagingActiveHandler multimediamessageListener = null;
	public MessagePart messagePart = null;

	public SMS sms_text = null;
	public MMS mms_multimedia = null;
	
	public void run() 
	{
		openMessagingConnection();
		TrackMessages();
		//Runnable.class.
	}
	
	public void openMessagingConnection()
    {
    	try
    	{
	    	textmessageConnection = (MessageConnection) Connector.open("sms://");
	    	multimediamessageConnection = (MessageConnection) Connector.open("mms://");
	    	
	    	textmessageConnection.setMessageListener(textmessageListener);
	    	textmessageConnection.setMessageListener(multimediamessageListener);
	    	
	    	textmessage = (TextMessage)textmessageConnection.receive();
	    	multimediamessage = multimediamessageConnection.receive();
	    	
	    	
	    	
	    	if(textmessageListener.IncomingMessage || textmessage!=null)
	    	{
	    		smsCount++;
	    	}
	    	
	    	if(multimediamessageListener.IncomingMessage || multimediamessage != null)
	    	{
	    		mmsCount++;
	    	}
	    	
	    	messagingCount = smsCount + mmsCount;
	    	
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
     *
    public void TrackMessages()
    {
    	LabelField sms = new LabelField("SMS received/sent")
    }

}
*/
