package com.http.test;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class Screen extends MainScreen //implements KeyListener
{
	
	//InputScreen() method
	BasicEditField bef;
	String str;
	
	//HTTPPost() method
	public final String Https = "https://";
	public final String Http = "http://";
	public final String HttpURL = "cphcloud.biz";
	public final String Port = ":8080"; //":80";
	public final String DeviceSideParams = "/deviceside=true";
	public HttpConnection httpConn;
	int responsecode;
	OutputStream output;
	byte[] byteArray = new byte[10];
	
    /**
     * Creates a new MyScreen object
     */
    public Screen()
    {        
    	super( MainScreen.USE_ALL_WIDTH | MainScreen.USE_ALL_HEIGHT |
    			MainScreen.HORIZONTAL_SCROLLBAR | MainScreen.VERTICAL_SCROLLBAR );
    	
    	// Set the displayed title of the screen       
        setTitle("HTTP Post App (Tester)");
        
        InputScreen();
        HTTPPost();
    }
    
    public void InputScreen()
    {
    	HorizontalFieldManager hfm = new HorizontalFieldManager(HORIZONTAL_SCROLL);
//    	bef = new BasicEditField("Type in some message for server: " ,"", .""|BasicEditField.NO_NEWLINE| 
//										BasicEditField.FOCUSABLE);
//    	add(bef);
    	str = bef.getText();
    	byteArray = str.getBytes();
    }
    
    public void HTTPPost(){
    	try{
    	httpConn = (HttpConnection) Connector.open(Http + HttpURL + Port);
    	responsecode = httpConn.getResponseCode();
    	if (responsecode != HttpConnection.HTTP_OK)
    		throw new IOException ("Exception Caught HTTP Response Code : " + responsecode );
    	
    	//httpConn.setRequestMethod(HttpConnection.POST);
    	output = httpConn.openOutputStream();
    	output.write(byteArray, 0, byteArray.length);
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    		System.out.println(ioe.getClass() + "\t" + ioe.getMessage());
    	}
    	
    }
    
}
