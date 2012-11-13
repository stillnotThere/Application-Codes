package com.location.set2;
import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

public class LocationTest2Screen extends MainScreen {
	LocationTest2App locationtest2;
	Timer timer;
	RichTextField txtlocationtest2;
	RichTextField test;
	ServerCommunication server;
	
	public LocationTest2Screen(){
		locationtest2 = new LocationTest2App();
		timer = new Timer();
		timer.schedule(new Checklocationtest2(), 1000 , 2000);		//only for testing	period-2sec delay-1sec
//		timer.schedule(new Checklocationtest2(), 1000,86400000);	//check for locationtest2 every 864000000 second(24hours);
//		timer.schedule(new Checklocationtest2(), 1000, 432000000);	//once in 432000000 (12hours)
//		timer.schedule(new Checklocationtest2() , 1000 , 3600000); 	//period of 60mins and delay of 1second
		
		String textlocationtest2 = "";
		txtlocationtest2 = new RichTextField(textlocationtest2, RichTextField.NON_FOCUSABLE);
		add(txtlocationtest2);
		
		test = new RichTextField("",RichTextField.NON_FOCUSABLE);
		add(test);

	}
	public boolean onClose()
	{
		timer.cancel();  //cleanup
		this.close();
		return true;
	}

	public class Checklocationtest2 extends TimerTask{
		public Checklocationtest2() {
		}
	
		public void run() {
			double lat;
			double lng;
			boolean roam;
			String stream;
			stream = "";
			lat = 0;
			lng = 0;
			roam = false;
//			server = new ServerCommunication();
			lat = locationtest2.getLatitude();
			lng = locationtest2.getLongitude();
			roam = locationtest2.isRoaming();
			if (lat != 0.0 & lng != 0.0) {
				synchronized (LocationTest2.getEventLock()) {
					double acc = locationtest2.getAccuracy();
					stream = "Latitude:"+lat + ", \r\nLongitude:" + lng + ",\r\nTowers:" +
							locationtest2.getSatCount() + "\r\nAccuracy:" + String.valueOf(acc) + 
							"\r\nRoaming:" + (roam ? "Yes":"No") ;

					server = new ServerCommunication(stream);
					
//					if(server.SendStream(stream))
//						test.setText("Not able to communicate with server");
//					else
//						test.setText("Location sent to server");
					
					txtlocationtest2.setText(stream);
				}			

			}
			else
			{
				String thetxt = txtlocationtest2.getText();
				synchronized (LocationTest2.getEventLock()) {
					if(thetxt.length() > 25)
						if(thetxt.length() > 45)
							txtlocationtest2.setText("Waiting for Co-ordinates.");
						else
							txtlocationtest2.setText(thetxt + ".");
					else
						txtlocationtest2.setText("Waiting for Co-ordinates.");
				}
			}
		}
	}
}
