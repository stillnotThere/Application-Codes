package com.phone.Cellular;

import net.rim.blackberry.api.phone.Phone;
import net.rim.blackberry.api.phone.phonelogs.CallLog;
import net.rim.blackberry.api.phone.phonelogs.PhoneCallLog;
import net.rim.blackberry.api.phone.phonelogs.PhoneCallLogID;
import net.rim.blackberry.api.phone.phonelogs.PhoneLogs;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class PhoneScreen extends MainScreen
{
    public String PhoneNumber = ""; 
	public CallLog callLog = null;
	public static PhoneLogActive phoneLogListener = null;
	public PhoneLogs phoneLogs = null;

	public int numbNormalCalls = 0;
	public PhoneCallLogID call_participant = null;
	public int call_time = 0;
	public int call_duration = 0;
	public int call_sum = 0;
	public String call_number = "";
	
    /**
     * Creates a new MyScreen object
     */
    public PhoneScreen()
    {        
    	//super(MainScreen.DEFAULT_MENU);
    	// Set the displayed title of the screen
    	PhoneNumber = Phone.getDevicePhoneNumber(true);
        setTitle("Phone Usage Tracker of "+ Phone.getDevicePhoneNumber(true));
       
        TraceLoggers();
        LabelField lf1 = new LabelField ("Number of Seconds used: " +call_sum
	        								+ "\n\rLast call made from/to: "+call_number);
        add(lf1);

        MissCallHandlers();
        add(new RichTextField("Number of missed calls: "+numbMissedCalls));
        add(new RichTextField("Number of normal calls: "+numbNormalCalls));
        
        add(new RichTextField("Sent to Seerver:"+ (sendToServer() ? "Yes":"No") ));
	    /*LabelField lf2 = new LabelField ("2:Cell Minutes used: " + call_duration2
        								+ "\n\r2:Last Number called: "+call_number2);
        add(lf2);*/
        
       	DeRegister();
    }
    
	public void TraceLoggers()
	{
		phoneLogs = PhoneLogs.getInstance();			//current call log instance
		//phoneLogs.addListener(new PhoneLogActive());
		numbNormalCalls = phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS) - 1;
		call_duration =  phoneLogs.callAt(numbNormalCalls,
				PhoneLogs.FOLDER_NORMAL_CALLS).getDuration();
		
		if( call_sum < call_duration)
		{
			call_sum = call_duration;
			call_number = ((PhoneCallLog) (phoneLogs.callAt(phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS) - 1,
					PhoneLogs.FOLDER_NORMAL_CALLS))).getParticipant().getNumber();
		}
		else
		{
			call_duration = phoneLogs.callAt(phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS),
					PhoneLogs.FOLDER_NORMAL_CALLS).getDuration();
			call_sum += call_duration;
			call_number = ((PhoneCallLog) (phoneLogs.callAt(phoneLogs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS) - 1,
				PhoneLogs.FOLDER_NORMAL_CALLS))).getParticipant().getNumber();
		}
		
	}
	
	public int numbMissedCalls = 0;
	public PhoneLogs missedcallsLogs = null;
	public void MissCallHandlers()
	{
		//@SuppressWarnings("Signing Required")
		missedcallsLogs = PhoneLogs.getInstance();
		numbMissedCalls = missedcallsLogs.numberOfCalls(PhoneLogs.FOLDER_MISSED_CALLS) -1;
	}
	
	public String streamData = "";
	public boolean sendToServer()
    {
    	streamData = "Client's Number:" + PhoneNumber +
    						"| Call from/to Phone Number:" + String.valueOf(call_number) +
    						"| Call Duration:" + String.valueOf(call_sum) +
    						"| Number of Missed Calls:"+ String.valueOf(numbMissedCalls) +
    						"| Number of Answered Calls:"+ String.valueOf(numbNormalCalls);
    	new ServerCommunication(streamData);
    	return true;
    }
	    
	public void DeRegister()
	{
		phoneLogs.removeListener(new PhoneLogActive());
		//System.exit(0); Causing random 3xx series App-Error/Uncaught Exception 
		//					as System.exit causes to clean all values from JVM causing variables
		//					to be Null. 
		//TODO				Better consider Runtime cleaning with gc() [Garbage Collection]
	}
	
	/*
	public boolean onClose()
	{
		/*
		phoneLogs.removeListener(new PhoneLogActive());
		Runtime run = Runtime.getRuntime();
		run.freeMemory();
		*/
		//System.exit(0);
		//Runtime.getRuntime().exit(0);
		//return true;
	//}
	
	
}