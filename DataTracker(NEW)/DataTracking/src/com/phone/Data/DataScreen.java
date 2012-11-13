package com.phone.Data;

import java.util.Calendar;
import java.util.TimerTask;

import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class DataScreen extends MainScreen
{
	double downloaded = 0;
	double uploaded = 0;
	DataHandler data;
	RichTextField datacounterText;
	RichTextField sendServer;
	String streamData;
	Calendar calendar;
	
	public DataScreen()
	{
		super(MainScreen.HORIZONTAL_SCROLLBAR |
				MainScreen.VERTICAL_SCROLLBAR);
		
		data = new DataHandler();
		
		datacounterText = new RichTextField("",RichTextField.NON_FOCUSABLE);
		add(datacounterText);
		
		sendServer = new RichTextField("",RichTextField.NON_FOCUSABLE);
		add(sendServer);
	}
	
	public boolean onClose()
	{
		close();			//pops the screen if the stack is empty calls System.exit(0)
		return false;
	}
	
	public class Updater extends TimerTask
	{
		public Updater()
		{
		}
		
		public void run()
		{
			synchronized(DataApp.getEventLock())
			{
				downloaded = data.getDownloaded();
				uploaded = data.getUploaded();
				
				datacounterText.setText("Downloaded Bytes:\t" + downloaded +
						"\r\nUploade:\t " + uploaded);
				
				if(SendtoServer())
					sendServer.setText("Sent to server");
				else
					sendServer.setText("Could not connect with server, Problem occurred!!");
			}
		}
		
		 public boolean SendtoServer()
	        {
			 	calendar = Calendar.getInstance();
	        	streamData = "Downloaded: " + String.valueOf(downloaded) +
						"\t| Uploaded: " + String.valueOf(uploaded) +
						"\t| Time: " + calendar.getTime().toString();
	    		if(new ServerCommunication(streamData).getPrompt() == 0)
	    			return true;
	    		else
	    			return false;
	        }
		
	}
}
	
	
	/*
    *//**
     * Creates a new MyScreen object
     *//*
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
    
}*/