import net.rim.device.api.system.RadioInfo;

/**
 * 
 */

/**
 * @author Rohan K.M [rohan.mahendroo@gmail.com]
 *
 */
public class DataHandler extends Thread 
{
	public boolean DataStatus = false;
	public double Downloaded = 0;
	public double Uploaded = 0;
	
	public DataHandler()
	{
		if(DataisWorking())
		{
			StartTracking();
		}
		else
		{
		
		}
	}
	
	public boolean DataisWorking()
	{
		if(RadioInfo.getState() == RadioInfo.WAF_3GPP ||
				RadioInfo.getState() == RadioInfo.WAF_CDMA)
			DataStatus = true;
		else
			DataStatus = false;
		
		return DataStatus;
	}
	
	public void StartTracking()
	{
		
	}
	
}
