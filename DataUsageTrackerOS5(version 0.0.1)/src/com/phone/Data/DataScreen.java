package com.phone.Data;

import java.io.DataOutputStream;

import javax.microedition.io.Datagram;

import net.rim.device.api.io.DatagramBase;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.util.DataBuffer;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class DataScreen extends MainScreen
{
	public RadioInfo radioinfo = null;
	public long DownloadBytes = 0;
	public long UploadBytes = 0;
	
	public String Radio = "ON";
	public DataOutputStream dataoutStream = null;
	public DatagramBase datagramBase = null;
	public DataBuffer dataBuffer = null;
	public Datagram datagram = null;
	
	public int CurrentWAF = 0;
	
	public int ColumnWidth = 0;
	public final int Column = 2;
	public final int Row = 4;
	
    /**
     * Creates a new MyScreen object
     */
    public DataScreen()
    {        
        setTitle("Data Usage Tracker");

        TrackData();
        
        GridFieldManager grid = new GridFieldManager											//Grid of 4x2
        							(Row,Column,GridFieldManager.PREFERRED_SIZE_WITH_MAXIMUM);			//Using full screen size
        
        grid.setBorder(
        		BorderFactory.createRoundedBorder											//NEW Rounded edged borders
        		(																				//(Bevel style with Dash borders)
        		new XYEdges(0 ,0 ,0 ,0 ) , 															//with Co-ordinates according 
        		Border.STYLE_DASHED	)															//to the GridFieldManager
        																						//surface area specs
        		)																				
        		;
     
        grid.setCellPadding(2);

        grid.add( new LabelField("Radio:",Field.FIELD_LEFT) );									//Col 1 Row 1
        grid.add( new LabelField(Radio,Field.FIELD_RIGHT | Field.FOCUSABLE));					//Col 2 Row 1
        
        grid.add (new LabelField("WAF:",Field.FIELD_LEFT) );									//Col 1 Row 2
        grid.add( new LabelField(String.valueOf(CurrentWAF),Field.FIELD_RIGHT					//Col2 Row 2 
        							| Field.FOCUSABLE));			
        
        grid.add( new LabelField("Downloaded Bytes:", Field.FIELD_LEFT));						//Col1 Row 3
        grid.add( new LabelField(String.valueOf(DownloadBytes) , Field.FIELD_RIGHT				//Col2 Row 3
        							| Field.FOCUSABLE));			
        
        grid.add( new LabelField("Uploaded Bytes:", Field.FIELD_LEFT));							//Col1 Row 4			
        grid.add( new LabelField(String.valueOf(UploadBytes) , Field.FIELD_RIGHT 				//Col2 Row 4
        							| Field.FOCUSABLE));
        ColumnWidth = (Display.getWidth()/Column) - grid.getColumnPadding();
        
        grid.setColumnProperty(0, GridFieldManager.AUTO_SIZE, ColumnWidth);
        grid.setColumnProperty(1, GridFieldManager.AUTO_SIZE, ColumnWidth);

        add(grid);
    }
    
    public void TrackData()
    {
    	CurrentWAF = RadioInfo.getActiveWAFs();
    	
    	if( RadioInfo.getState() == RadioInfo.STATE_ON )
    	{
    		Radio = "ON";
	    	if( CurrentWAF == RadioInfo.WAF_3GPP || 
	    				CurrentWAF == RadioInfo.WAF_CDMA 
	    					&& CurrentWAF != RadioInfo.WAF_WLAN )
	    	{
	    			while( RadioInfo.isDataServiceOperational() || 
	    								(DownloadBytes != -1&&UploadBytes !=-1) )
	    			{
	    				DownloadBytes =+ RadioInfo.getNumberOfPacketsSent();
	    				UploadBytes =+ RadioInfo.getNumberOfPacketsSent();
		    	}
	    	}
			else
			{
    			DownloadBytes = 0;
    			UploadBytes = 0;
			}
    	}
    	else
    	{
    		Radio = "OFF";
    	}
    	
    }
    
  
   public boolean onClose()
   {
	   Dialog.alert("Exiting app");
	   return true;
   }
    
}