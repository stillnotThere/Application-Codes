package com.test.module.Messaging;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;

import net.rim.blackberry.api.mms.MMS;
import net.rim.blackberry.api.mms.SendListener;
import net.rim.blackberry.api.sms.OutboundMessageListener;
/**
 * @author Rohan K.M
 * @email rohan.mahendroo@gmail.com
 */
public class Handler extends Thread {

	Message message;
	MessageConnection messageconnection;
	boolean incomingTextMessage = false;		//SMS
	boolean outgoingTextMessage = false;
	public boolean incomingMMS = false;			//MMS
	public boolean outgoingMMS = false;
	
	int Textmessages = 0;
	int MMSmessages = 0;
	String Type;
	public final String smsURL = "sms://:3590";				//SMS Port 3590
	public final String mmsURL = "mms://";					//MMS Port TODO
	
	public Handler(){
		message = null;
		messageconnection = null;
		
		Type = "";
		
		HandleTextMessages();
		HandleMultiMediaMessages();
	}

	/**
	 * @category Text Message
	 * @see javax.wireless.message.MessageListener
	 * @see rim.blackberry.api.sms.OutboundMessageListner
	 * Makes connection to MessageConnection with device on Port 3590 for SMS/Text Messages
	 * Registers OutboundMessageListener (extension of javax.wireless.messaging.MessageListener)
	 * for incoming and outgoing message handling.
	 */
	public void HandleTextMessages(){
		try{
			/**Incoming and Outgoing (Both)**/
			messageconnection = (MessageConnection) Connector.open(smsURL);
			messageconnection.setMessageListener(new TextMessages());	//register message listener (OutboundMessageListener) for in and out
																		//message listener de-register ABSENT
			if(incomingTextMessage || outgoingTextMessage)
				CountSMS();
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
	}
	
	/**
	 *	Implementing interface OutboundMessageListener (net.rim.blackberry.api.sms)
	 *	for listening incoming and outgoing messages
	 *	@method notifyOutgoingMessage(Message message)
	 *	@method notifyIncomingMessage(MessageConnection conn)   
	 */
	public class TextMessages implements OutboundMessageListener {
		public void notifyOutgoingMessage(Message message){
			outgoingTextMessage = true;
		}
		public void notifyIncomingMessage(MessageConnection conn){
			incomingTextMessage = true;
		}
	}
	
	/**
	 * @category Multi Media Message
	 */
	public void HandleMultiMediaMessages(){
		MMS.addSendListener(new MultiMediaSent());
		MultiMediaReceived();
		
		if(outgoingMMS || incomingMMS)
			CountMMS();
		
	}
	
	/**
	 * OUTGOING MMS
	 *	Implements interface SendListner(net.rim.blackberry.api.mms)
	 *	for listening MMS sending event instances
	 *	@met sendMessage(Message message)
	 */
	public class MultiMediaSent implements SendListener{
		public boolean sendMessage(Message message){
			outgoingMMS = true;
			return outgoingMMS;
		}
	}

	/**
	 * INCOMING MMS
	 */
	public void MultiMediaReceived(){
		try{
			messageconnection = (MessageConnection) Connector.open(mmsURL);
			message = messageconnection.receive();
			if(message.getAddress() != null)
				incomingMMS = true;
			else
				incomingMMS = false;
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
	}

	/**
	 * to be initialized only when onClose() is called from Application
	 */
	public void DeRegisterListeners()
	{
		MMS.removeSendListener(new MultiMediaSent());
		try {
			messageconnection.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
	}
	
	public void CountSMS(){
		++Textmessages;
	}
	
	public void CountMMS(){
		++MMSmessages;
	}
	
	public int GetTextMessageCount(){
		return Textmessages;
	}
	
	public int GetMultiMediaMessageCount(){
		return MMSmessages;
	}
	
}