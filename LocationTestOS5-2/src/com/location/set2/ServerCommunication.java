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
	public String UnformatedPhoneNumber = "";
	public String Name = "";
	public boolean ERROR = false;
	
	/** 7x2 matrix **/
	public String[][] Name_Phone = {{"Gianni" , "6479819606"} , 
										{"Mary" , "6479835125"} ,
										{"Kumar" , "4167173533"} ,
										{"Vinni" , "6472977855"} ,
										{"Dimple" , "4162594680"} ,
										{"Rohan" , "6474648458"} ,
 										{"Ashwin" , "4166700180"} };

	public ServerCommunication()
	{
		
	}
	
	public void StartandSend(String str) {
		try{
			this.str = str;
		/**<socket://99.229.28.101:53;deviceside=true/>**/
			stream = (StreamConnection) Connector.open(ip+DirectTCP);		//Direct TCP SocketConnection 
			
			Dialog.inform("Connection made successfully!!!");
			
		/**Open DataOutputStream*/
			dataout = stream.openDataOutputStream();
			this.str+= "\tPhone Number:" + PhoneNumber + "\tPhoneHolder: Mary-BB9700";//Rohan-BB9790";
			
			PhoneNumber = Phone.getDevicePhoneNumber(true);			//formatted device phone number
			UnformatedPhoneNumber = Phone.getDevicePhoneNumber(false);
			//IMPORTANT
			//very in-efficient method of array abstraction
			for(int i=0;i<7;i++) {
				if(Name_Phone[i][0].equals(UnformatedPhoneNumber)) {
						Name = Name_Phone[i][1];
						break;
				}
			}
			Dialog.alert("Phone number of an Non-employee of CPH Inc.");
		/**Writing on DataOutputStream*/
			dataout.write(str.getBytes(), 0, str.length());
			
		} catch(IOException ioe) {
			Dialog.alert("Error occured while communicating.."+ioe.getClass()+"\t"+ioe.getMessage());
			ioe.printStackTrace();
			System.err.println(ioe.getClass() + "\t" + ioe.getMessage());
		}
	}
	
}