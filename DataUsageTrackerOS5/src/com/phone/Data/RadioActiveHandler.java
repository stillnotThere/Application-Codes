package com.phone.Data;

import net.rim.device.api.system.RadioStatusListener;

public class RadioActiveHandler implements RadioStatusListener{

	public boolean RadioOFF = false;
	public boolean NetworkState = false;
	public boolean StationChange = false;
	public boolean NetworkActive = false;
	public boolean PDPStateChanged = false;
	public int APN = 0;
	
	public void baseStationChange() {
		// TODO Auto-generated method stub
		
	}

	public void networkScanComplete(boolean success) {
		// TODO Auto-generated method stub
		
	}

	public void networkServiceChange(int networkId, int service) {
		// TODO Auto-generated method stub
		
	}

	public void networkStarted(int networkId, int service) {
		// TODO Auto-generated method stub
		
	}

	public void networkStateChange(int state) {
		// TODO Auto-generated method stub
		
	}

	public void pdpStateChange(int apn, int state, int cause) {
		// TODO Auto-generated method stub
		
	}

	public void radioTurnedOff() {
		// TODO Auto-generated method stub
		
	}

	public void signalLevel(int level) {
		// TODO Auto-generated method stub
		
	}

}
