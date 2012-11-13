package com.phone.Data;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import net.rim.device.api.system.EventLogger;

public class ServerCommunication {

	public SocketConnection socket = null;
	//public ServerSocketConnection serversocket = null;
	public final String serverAddress = "socket://cphcloud.biz:";
	public final String serverPort = "7000";
	public final int Buffer = 4096;
	public String streamSend = "";
	public String streamReceived = "";
	public byte[] streamByteSend = null;
	public DataOutputStream dataout = null;
	public boolean confirm = false;
	public int Prompt = 0;
	public ServerCommunication(String str) 
	{
		System.out.println("Opening Connection");
		openConnection();
		/*EventLogger event = null;
		event.*/
		
		//streamByteSend = streamSend.getBytes();
		try
		{
			//dataout.write(str.getBytes() , 0 , streamSend.length() );
			if(confirm)
			{
				Prompt = 0;
				dataout.write(str.getBytes());
				System.out.println("Writing stream to server");
			}
			else
			{
				Prompt = -1;
			}
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t:" + ioe.getMessage());
		}
	}
	
	public int getPrompt()
	{
		return Prompt;
	}
	
	public void openConnection()
	{
		try
		{
			socket= (SocketConnection) Connector.open(serverAddress+serverPort);
			socket.setSocketOption(SocketConnection.SNDBUF, Buffer);
			dataout = socket.openDataOutputStream();
			confirm = (dataout != null) ? true:false;
			if(confirm)
				System.out.print("Connected to "+serverAddress +" at "+ serverPort);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t:"+ioe.getMessage());
		}
	}
	
}
