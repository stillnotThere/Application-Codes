package com.location.set2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.StreamConnection;

import net.rim.blackberry.api.phone.Phone;
import net.rim.device.api.ui.component.Dialog;

public class ServerCommunication {
	String str;
	SocketConnection socket;
	StreamConnection stream;
	OutputStreamWriter out;
	DataOutputStream dataout;
	char[] charArray;
	public boolean Connected = true;
	public final String ProxyTCP = ";deviceside=false"; //default using Proxy TCP in MDS
	public final String DirectTCP = ";deviceside=true"; //Direct TCP connection in MDS
	public final String url = "socket://cphcloud.biz:53";
	public final String ip = "socket://99.229.28.101:53";
	public String PhoneNumber = "";
	public boolean ERROR = false;
	
	public ServerCommunication(String str)
	{
		this.str = str;
		startConnection();
		if(!ERROR)
		{
			openDataOutputStream();
			writingDataOutputStream();
		}
		else
		{
			//do nothing 
				//TODO check if required anything
		}
	}
	
	public void startConnection()
	{
		this.str = str;
		try{
	/**<socket://99.229.28.101:53;deviceside=true/>**/
			stream = (StreamConnection) Connector.open(ip+DirectTCP);		//Direct TCP SocketConnection 
			Dialog.inform("Connection made successfully!!!");
		} catch (IOException e) {
			ERROR = true;
			Dialog.alert("Connection could not be established!!!");
			e.printStackTrace();
		}
			/*socket = (SocketConnection) Connector.open(url);
			socket.setSocketOption(SocketConnection.KEEPALIVE,str.length() +1 );//SNDBUF,1024);//.KEEPALIVE, 4096);
			
			dataout = socket.openDataOutputStream();
			
			dataout.write(str.getBytes() , 0, str.length());*/
			
			/**
			out = new OutputStreamWriter(socket.openOutputStream());
//			charArray = str.toCharArray();
//			out.write(charArray);
			//out.write(str);
			out.write(str, 0, str.length());
			//out.close();
			//socket.close();
			 */
			
		//return Connected;
	}
	
	public void openDataOutputStream() {
		/**Open DataOutputStream*/
		try{
			dataout = stream.openDataOutputStream();
			if(dataout==null)
				Connected = false;
			else
				Connected = true;
			PhoneNumber = Phone.getDevicePhoneNumber(true);			//formatted device phone number
		} catch(IOException ioe) {
			Dialog.alert("Error while communicating with the server URL:"+url);
			ioe.printStackTrace();
			System.err.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
			this.str+= PhoneNumber;
	}
	
	
	public void writingDataOutputStream(){
		/**Writing on DataOutputStream*/
		try {
			dataout.write(str.getBytes(), 0, str.length());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getClass() + "\t" + e.getMessage());
		}
	}
	
	
//	public boolean ConnectionStatus()
//	{
//		return Connected;
//	}
	
}