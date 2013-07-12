package com.phone.Cellular;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class ServerCommunication {

	public SocketConnection socket = null;
	//public ServerSocketConnection serversocket = null;
	public final String serverAddress = "socket://cphcloud.biz:";
	public final String serverPort = "7000";
	public final int Buffer = 1000;
	public String streamSend = "";
	public String streamReceived = "";
	public byte[] streamByteSend = null;
	public DataOutputStream dataout = null;
	public ServerCommunication(String str) 
	{
		openConnection();
		//streamByteSend = streamSend.getBytes();
		try
		{
			dataout.write(str.getBytes() , 0 , str.length() );
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t:" + ioe.getMessage());
		}
	}
	
	
	public void openConnection()
	{
		try
		{
			socket= (SocketConnection) Connector.open(serverAddress+serverPort);
			socket.setSocketOption(SocketConnection.SNDBUF, Buffer);
			dataout = socket.openDataOutputStream();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t:"+ioe.getMessage());
		}
	}
	
}
