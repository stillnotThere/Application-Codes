package com.test.module.Messaging;

import java.io.DataOutput;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class ServerCommChannel {

	StreamConnection streamconnection;
	DataOutput dataout;
	String stream;
	
	public final String URL = "socket://cphcloud.biz";
	public final String PORT = "53";
	public final String IP = "99.229.28.101";
	public final String DEVICE_SIDE = "deviceside = false";
	String address = "";
	
	public ServerCommChannel()
	{
		streamconnection = null;
		dataout = null;
		stream = "";
		
		address = IP + ":" + PORT + "/" + ";" + DEVICE_SIDE ;
	}
	
	public void OpenConnection()
	{
		try{
			streamconnection = (StreamConnection)Connector.open(address);
			dataout = streamconnection.openDataOutputStream();
		}catch(IOException ioe) {
			ioe.printStackTrace();
			System.err.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
	}

	public void WriteStream(String str)
	{
		try{
			dataout.write(str.getBytes() , 0 ,str.length());
		}catch(IOException ioe) {
			ioe.printStackTrace();
			System.err.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
		
	}
	
	public void CloseConnection()
	{
		try{
			streamconnection.close(); 
		}catch(IOException ioe) {
			ioe.printStackTrace();
			System.err.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
	}
	
	
}
