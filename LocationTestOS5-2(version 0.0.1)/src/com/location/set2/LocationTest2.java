/**
 * 
 */
package com.location.set2;

import net.rim.device.api.ui.UiApplication;

/**
 * @author Rohan K.M [rohan.mahendroo@gmail.com]
 *
 */
public class LocationTest2 extends UiApplication{
	
	public static void main(String args[])
	{
		LocationTest2 location2 = new LocationTest2();
		
		if(args.length >0 && args[0].equals("auto-startup"))
		{
			LocationTest2App location2app = new LocationTest2App();
			location2app.start();
		}
		else
		{
			location2.enterEventDispatcher();
		}
	}

	public LocationTest2()
	{
		pushScreen(new LocationTest2Screen());
	}

}