package com.phone.Location;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class LocationScreen extends MainScreen
{
	public double latitude = 0;
	public double longitude = 0;
	public float accuracy = 0;
	public int satellite_count = 0;
	public float speed = 0;
	public String city_Loc = "";
	
	public String address = "";
    public String streamData = "";
    public boolean roaming = false;

    public Timer timer;
    public TimerTask timerTask;
    public BlackBerryLocationHandler bbLoc;
    
    public RichTextField text;
    public LocationScreen()
    {
    	bbLoc = new BlackBerryLocationHandler();

    	timer = new Timer();
    	timer.schedule(new Updater(), 1000, 5000);		//updates and checks the location
    													//Period- 5 second
    													//Delay - 1 second
    	
    	text = new RichTextField("", RichTextField.NON_FOCUSABLE);
    	add(text);
    }
  
    public boolean onClose()
    {
    	//timer.cancel();
    	close();
    	return false;
    }
    
    public class Updater extends TimerTask
    {
    	public Updater(){
    	}
    	public void run() 
		{
    		latitude=0;
    		longitude=0;
    		accuracy=0;
    		satellite_count=0;
    		
    		latitude = bbLoc.getLAT();
    		longitude = bbLoc.getLONG();
    		if(latitude!=.00 & longitude !=0.0)
    		{
    			synchronized(LocationApp.getEventLock()) //Locks the screen thread 
    													//IMPORTANT- should always be serialized and synchronized
    			{
    				accuracy = bbLoc.getAccuracy();
    				text.setText("Longitude --- \t" + longitude + 
    						"\r\nLatitude --- \t" + latitude +
    						"\r\nAccuracy --- \t" + accuracy +
    						"\r\nNumber of Towers --- \t" + bbLoc.getNumbSat() +
    						"\r\nSpeed --- \t" + bbLoc.getSpeed() +
    						"\r\nRoaming --- \t" +  bbLoc.isRoaming());
    				
    				if(SendtoServer())
    					add(new RichTextField("Sent to server --- \tYes"));
    				else
    					add(new RichTextField("Could not send to server, Problem occured"));
    			}
    		}
    		else
    		{
    			String thetext = text.getText();
    			synchronized(LocationApp.getEventLock())
    			{
    				if(thetext.length() > 50)
    					if(thetext.length() > 90)
    						text.setText("Waiting for Co-ordinates");
    					else
    						text.setText(".");
    				else
    					text.setText("Waiting for Co-ordinates");
    			}
    		}
		}
    	
        public boolean SendtoServer()
        {
        	streamData = "Latitude: " + String.valueOf(latitude) +
					"\t| Longitude: " + String.valueOf(longitude) +
					"\t| Accuracy: " + String.valueOf(accuracy) + 
					"\t| Speed(m/s): " + String.valueOf(speed) + 
					"\t| Roaming: "+(bbLoc.isRoaming()
					//"| Current City :" + city
							);
    		if(new ServerCommunication(streamData).getPrompt() == 0)
    			return true;
    		else
    			return false;
        }
        
    }
}
    
    /*
     * 	IMPORTANT -- OLD CODE 
     * public LocationScreen()
    {        
    	super(MainScreen.HORIZONTAL_SCROLLBAR | 
    			MainScreen.VERTICAL_SCROLLBAR |
    			MainScreen.DEFAULT_CLOSE );
    	
        setTitle("Location and Phone Usage Tracker");
        //font[0] = Font.getDefault();
        //font[1] = Font.getDefault().derive(Font.BOLD);
        
        //att[0] = 0;
        //att[1] = 1;
        Handling();			//gives city -- NPE on 1350 24thOct
        BBHandling();
    }
    
    public void Handling()
    {
    	LocationHandler Handler = new LocationHandler();
    	String[] array = new String[10];
    	
    	//off[0] = 11;
    	//off[1] = 20;
    	add(new RichTextField ("***\"Geo-Coding\"***"));
    											//off,
    											//att,
    											//font,
    											//RichTextField.FOCUSABLE));
    	
    	array = Handler.ReverseGeocoding();
    	
    	if(array.length>0)
    	{
    		LocationValid = array[0];
    		
    		LocationMethod = Integer.parseInt( array[1] );
    		if(LocationMethod == Location.MTE_SATELLITE)
	    		LocationMethodology = "Satelittes based";
	    	else if(LocationMethod == Location.MTE_TIMEDIFFERENCE)
	    		LocationMethodology = "Time difference based";
	    	else if(LocationMethod == Location.MTE_TIMEOFARRIVAL)
	    		LocationMethodology = "Time of arrival based";
	    	else if(LocationMethod == Location.MTE_CELLID)
	    		LocationMethodology = "Cell-ID based";
	    	else if(LocationMethod == Location.MTE_SHORTRANGE)
	    		LocationMethodology = "Short Ranged based";
	    	else if(LocationMethod == Location.MTE_ANGLEOFARRIVAL)
	    		LocationMethodology = "Angle of Arrival based";
	    	else if(LocationMethod == Location.MTY_TERMINALBASED)
	    		LocationMethodology = "Terminal based";
	    	else if(LocationMethod == Location.MTY_NETWORKBASED)
	    		LocationMethodology = "Network based";
	    	else if(LocationMethod == Location.MTA_ASSISTED)
	    		LocationMethodology = "Assisted based";
	    	else if(LocationMethod == Location.MTA_UNASSISTED)
	    		LocationMethodology = "Un-assisted based";
    		
    		latitude = Double.parseDouble(array[2]);
	    	longitude = Double.parseDouble(array[3]);
	    	speed = Integer.parseInt(array[4]);
	    	//address = array[5];
	    	
	    	add(new RichTextField ("Location fetching Methodology:"+LocationStatus));
	    	
	    	LabelField lf0 = new LabelField ("Average Signal Quality:"+ signalQ +
	    										"\r\nCurrent Address:"+ address );
	    	add(lf0);
	    	
	    	roaming = Handler.isRoaming();
	    	
	     	LabelField lf1 = new LabelField ("Longitude: "+longitude +  
	 				"\n\rLatitude: " + latitude +
	 				"\n\rSpeed: "+speed + 
	 				"\n\rRoaming:" + (roaming ? "Yes":"No")
	 				//+
	 				//"\n\rCity: "+city
	 				);
	     	
	     	add(lf1);
	
	     	
	     	 // XXX CITY from both BlackBerryLocation and Locator
	     	 
	     	//city = array[5];
	     	//add(new RichTextField("Present City(BBLocProv):"+array[5]));
	     	//add(new RichTextField("Presnt City(Landmark):"+array[6]));
	     	
	     	//add(new RichTextField ("Sent to Server:"+(sendToServer("Geo-Coding",roaming) ? "Yes":"No")) );
    	}
    	else
    	{
    		add(new RichTextField ("Sent to Server:No"));
    		add(new RichTextField ("Some problem occured with Geo-Coding"));
    	}
    }

    public void BBHandling()
    {
    	BlackBerryLocationHandler bbHandler = new BlackBerryLocationHandler();
    	off[0] = 11;
    	off[1] = 22; 
    	add(new RichTextField ("***\"Geo-Location\"***" ));
    													off,
    													att,
    													font,
    													RichTextField.FOCUSABLE));
    		
    	String[] BBarray = new String[10];
    	
    	BBarray = bbHandler.GeoLocation();
    	
    	if(BBarray!=null)
    	{
	    	latitude = Double.parseDouble(BBarray[0]);
	    	longitude = Double.parseDouble(BBarray[1]);
	    	
	    	//address = bbHandler.CurrentAddress;
	    	//String addressinfoStr = BBarray[2];
	    	signalQ = Integer.parseInt(BBarray[2]);
	    	BBLocationStatus = Integer.parseInt(BBarray[3]);
	    	speed = Float.parseFloat(BBarray[4]);
	    	
	    	//city_Loc = BBarray[5];
	    	//city_Land = BBarray[7];
	    	
	    	if(BBLocationStatus == BlackBerryLocation.SUBSEQUENT_MODE_ON)
	    		LocationStatus = "Subsequent Mode ON";
	    	else if(BBLocationStatus == BlackBerryLocation.FAILOVER_MODE_ON)
	    		LocationStatus = "Fialover Mode ON";
	    	else if(BBLocationStatus == BlackBerryLocation.GPS_ERROR)
	    		LocationStatus = "GPS Error";
	    	else if(BBLocationStatus == BlackBerryLocation.GPS_FIX_COMPLETE)
	    		LocationStatus = "GPS Fix Complete";
	    	else if(BBLocationStatus == BlackBerryLocation.GPS_FIX_PARTIAL)
	    		LocationStatus = "GPS Fix Partial";
	    	else if(BBLocationStatus == BlackBerryLocation.GPS_FIX_UNAVAILABLE)
	    		LocationStatus = "GPS Fix Unavailable";
	    		 
	    	add(new RichTextField ("Location Status:"+LocationStatus));
	    	
	    	LabelField lf0 = new LabelField ("Average Signal Quality:"+ signalQ +
	    										"\r\nCurrent Address:"+ address );
	    	add(lf0);
	    	roaming = bbHandler.isRoaming();
	     	LabelField lf1 = new LabelField ("Longitude: "+longitude +  
	 				"\n\rLatitude: " + latitude +
	 				"\n\rSpeed: "+speed + 
	 				"\n\rRoaming:" + (roaming ? "Yes":"No")
	 				//+
	 				//"\n\rCity: "+city
	 				);
	     	add(lf1);
	     	
	     	//sadd(new RichTextField("Present City(BBLocProv):"+city_Loc));
	     	//add(new RichTextField("Present City(Landmark):"+city_Land));
	     	
	     	add(new RichTextField ("Sent to Server:"+(sendToServer("Geo-Location",roaming) ? "Yes":"No")) );
    	}
    	else
    	{
    		add(new RichTextField("Location information not valid"));
    	}
    }
    
	public boolean sendToServer(String str, boolean roam)
    {
    	streamData = "Geo-position Method :" + str +
    					"| Latitude:" + String.valueOf(latitude) +
    					"| Longitude:" + String.valueOf(longitude) +
						//"|TimeStamp:" + String.valueOf(timestamp)+
						"| Speed(m/s):" + String.valueOf(speed) + 
						"| Roaming:"+(roam ?"Yes":"No"
						//"| Current City :" + city
								);
    	new ServerCommunication(streamData);
    	return true;
    }
    
    
    public boolean onClose()
    {
    	//System.exit(1); 	XXX	 
    	// IMPORTANT causes ABNORMAL TEMINATION				
    	*//** Causing NULL POINTER EXCEPTION as
    	 * System.exit() terminates the application
    	 * while removing everything from RAM itself
    	 * and making all variables null at next 
    	 * start-up
    	 **//*
//    	Runtime runtime = Runtime.getRuntime();			
//    	runtime.gc();

//    	System.exit(0);
//    	Runtime.getRuntime().exit(0);
    	return true;
	}
}
*/