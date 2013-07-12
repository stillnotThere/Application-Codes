package com.phone.Messaging;

import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;

public class MessagingActiveHandler implements MessageListener{

	public boolean IncomingMessage = false;
	
	public void notifyIncomingMessage(MessageConnection conn) {
		// TODO Auto-generated method stub
		IncomingMessage = true;
	}

}
