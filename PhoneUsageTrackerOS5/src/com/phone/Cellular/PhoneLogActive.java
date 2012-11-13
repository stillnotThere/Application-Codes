package com.phone.Cellular;

import net.rim.blackberry.api.phone.phonelogs.CallLog;
import net.rim.blackberry.api.phone.phonelogs.PhoneLogListener;

public class PhoneLogActive implements PhoneLogListener{

	public boolean LogStatusAddition = false;
	public boolean LogReseted = false;
	
	public void callLogAdded(CallLog cl) 
	{
		LogStatusAddition = true;
	}

	public void callLogRemoved(CallLog cl)
	{
		LogStatusAddition = false;
	}

	public void callLogUpdated(CallLog cl, CallLog oldCl) 
	{
		LogStatusAddition = true;		
	}

	public void reset()
	{
		LogReseted = true;		
	}

}
