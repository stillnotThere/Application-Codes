package com.phone.Location;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

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
	public OutputStreamWriter output;
	public boolean confirm = false;
	public int Prompt = 0;
	
	public ServerCommunication(String str) 
	{
		try{
			System.out.println("Opening Connection");
			openConnection();
			output.write(str, 0, str.length());
			//dataout.write(str.getBytes() , 0 , streamSend.length() );
			//dataout.write(str.getBytes());
			System.out.println("Writing stream to server");
			//closeConnection();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
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
			socket = (SocketConnection) Connector.open(serverAddress+serverPort);
			socket.setSocketOption(SocketConnection.KEEPALIVE,Buffer);//SNDBUF, Buffer);
			output = new OutputStreamWriter( socket.openOutputStream() );
			//dataout = socket.openDataOutputStream();
			System.out.print("Connected to "+serverAddress +" at "+ serverPort);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println(ioe.getClass() + "\t:"+ioe.getMessage());
		}
	}
	/*
	public void closeConnection()
	{
		try 
		{
			socket.close();
			//dataout.close();
			//dataout.flush();
			output.close();
			output.flush();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	*/
}
