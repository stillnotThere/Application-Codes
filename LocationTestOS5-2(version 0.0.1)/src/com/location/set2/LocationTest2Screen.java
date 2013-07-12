package com.location.set2;
import java.util.Timer;
import java.util.TimerTask;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

public class LocationTest2Screen extends MainScreen {
	LocationTest2App locationtest2;
	Timer timer;
	RichTextField txtlocationtest2;

	public LocationTest2Screen(){
		locationtest2 = new LocationTest2App();
		timer = new Timer();
		timer.schedule(new Checklocationtest2(), 1000,10000);  //check for locationtest2 every 10 second;

		String textlocationtest2 = "";
		txtlocationtest2 = new RichTextField(textlocationtest2, RichTextField.NON_FOCUSABLE);

		add(txtlocationtest2);

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
			lat = 0;
			lng = 0;

			lat = locationtest2.getLatitude();
			lng = locationtest2.getLongitude();

			if (lat != 0.0 & lng != 0.0) {
				synchronized (LocationTest2.getEventLock()) {
					double acc = locationtest2.getAccuracy();
					txtlocationtest2.setText("Latitude:"+lat + ", \r\nLongitude:" + lng + ",\r\nTowers" +
														locationtest2.getSatCount() + ",Accuracy" + (int) acc + ")");
				}			

			}
			else
			{
				String thetxt = txtlocationtest2.getText();
				synchronized (LocationTest2.getEventLock()) {
					if(thetxt.length() > 20)
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
