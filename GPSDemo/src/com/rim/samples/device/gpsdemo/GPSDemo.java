/**
 *  GPSDemo.java
 * 
 * Copyright � 1998-2010 Research In Motion Ltd.
 * 
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

package com.rim.samples.device.gpsdemo;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import javax.microedition.io.*;
import java.util.*;
import java.io.*;
import javax.microedition.location.*;
import net.rim.device.api.util.*;

/**
 * This application acts as a simple travel computer, recording route coordinates,
 * speed and altitude. Recording begins as soon as the application is invoked. When
 * using the BlackBerry Smartphone Simulator you will need to select 'GPS location' 
 * from the 'Simulate' menu and specify GPS locations or generate a route. The server
 * application included in the JDE will use the co-ordinates it recieves to construct 
 * route, speed and altitude plots.  These plots will be saved as JPEG files in the 
 * 'samples' directory of the JDE installation. 
 */
public class GPSDemo extends UiApplication 
{    
    // Constants -----------------------------------------------------------------------------------------------------------------
    private static final int GRADE_INTERVAL=5;  // Seconds - represents the number of updates over which alt is calculated.
    private static final long ID = 0x5d459971bb15ae7aL; //com.rim.samples.device.gpsdemo.GPSDemo.ID
    private static final int CAPTURE_INTERVAL=5;    // We record a location every 5 seconds.
    private static final int SENDING_INTERVAL=30;   // The interval in seconds after which the information is sent to the server.

    
    // When running this application, select options from the menu and replace <server name here>
    // with the name of the computer which is running the GPSServer application found in 
    // com.rim.samples.server, typically the local machine.  Alternatively, the _hostName variable
    // can be hard-coded below with no need to further modify the server name while running the application.
    
    private static String _hostName = "<server name here>:5555";    // E.g "ComputerName.DomainName:5555".
    private static int _interval = 1;   // Seconds - this is the period of position query.
    private static Vector _previousPoints;    
    private static float[] _altitudes;
    private static float[] _horizontalDistances;
    private static PersistentObject _store;
        
    /**
     * Initialize or reload our persistent store.
     */
    static
    {
        _store = PersistentStore.getPersistentObject(ID);
        
        if(_store.getContents()==null)
        {
            _previousPoints= new Vector();
            _store.setContents(_previousPoints);
        }
        
        _previousPoints=(Vector)_store.getContents();
    }
    
    private long _startTime;
    private float _wayHorizontalDistance;
    private float _horizontalDistance;
    private float _verticalDistance;    
    private EditField _status;    
    private StringBuffer _messageString;    
    private LocationProvider _locationProvider;    
    private ServerConnectThread _serverConnectThread;   

    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        new GPSDemo().enterEventDispatcher();
    }

    // Constructor 
    public GPSDemo()
    {
        // Used by waypoints, represents the time since the last waypoint.
        _startTime = System.currentTimeMillis();
        _altitudes = new float[GRADE_INTERVAL];
        _horizontalDistances = new float[GRADE_INTERVAL];
        _messageString = new StringBuffer();
        
        GPSDemoScreen screen = new GPSDemoScreen();
        screen.setTitle("GPS Demo");
        
        _status = new EditField(Field.NON_FOCUSABLE);
        screen.add(_status);
        
        // Try to start the GPS thread that listens for updates.
        if ( startLocationUpdate() )
        {
            // If successful, start the thread that communicates with the server.
            startServerConnectionThread(); 
        }
        
        // Render our screen.
        pushScreen(screen);
    }
    
   
    /**
     * Update the GUI with the data just received.
     * @param msg The message to display
     */
    private void updateLocationScreen(final String msg)
    {
        invokeLater(new Runnable()
        {
            public void run()
            {
                _status.setText(msg);
            }
        });
    }

    // Menu items --------------------------------------------------------------------------------------------
    /**
     * Mark the current point as a waypoint
     */
    private MenuItem _markWayPoint = new MenuItem("Mark waypoint" , 110, 10) 
    {        
        public void run()
        {
            GPSDemo.this.markPoint();
        }
    };

    /**
     * View the marked waypoints
     */
    private MenuItem _viewWayPoints = new MenuItem("View waypoints" , 110, 11) 
    {
        public void run()
        {
            GPSDemo.this.viewPreviousPoints();
        }
    };
    
    /**
     * Display the options
     */
    private MenuItem _options = new MenuItem("Options" , 110, 10) 
    {        
        public void run()
        {
            GPSDemo.this.viewOptions();
        }
    };    
    
    
    /**
     * Invokes the Location API with the default criteria.
     * 
     * @return True if the Location Provider was successfully started; false otherwise.
     */
    private boolean startLocationUpdate()
    {
        boolean retval = false;
        
        try
        {
            _locationProvider = LocationProvider.getInstance(null);
            
            if ( _locationProvider == null )
            {
                // We would like to display a dialog box indicating that GPS isn't supported, 
                // but because the event-dispatcher thread hasn't been started yet, modal 
                // screens cannot be pushed onto the display stack.  So delay this operation
                // until the event-dispatcher thread is running by asking it to invoke the 
                // following Runnable object as soon as it can.
                Runnable showGpsUnsupportedDialog = new Runnable() 
                {
                    public void run() {
                        Dialog.alert("GPS is not supported on this platform, exiting...");
                        System.exit( 1 );
                    }
                };
                
                invokeLater( showGpsUnsupportedDialog );  // Ask event-dispatcher thread to display dialog ASAP.
            }
            else
            {
                // Only a single listener can be associated with a provider, and unsetting it 
                // involves the same call but with null, therefore, no need to cache the listener
                // instance request an update every second.
                _locationProvider.setLocationListener(new LocationListenerImpl(), _interval, 1, 1);
                retval = true;
            }
        }
        catch (LocationException le)
        {
            System.err.println("Failed to instantiate the LocationProvider object, exiting...");
            System.err.println(le); 
            System.exit(0);
        }        
        return retval;
    }
    
    /**
     * Invokes a separate thread used to send data to the server.
     */
    private void startServerConnectionThread()
    {
        _serverConnectThread = new ServerConnectThread(_hostName);
        _serverConnectThread.start();
    }

    /**
     * Marks a point in the persistent store, calculations are based on all data 
     * collected since the previous way point, or from the start of the application
     * of no previous waypoints exist.
     */
    private void markPoint()
    {
        long current = System.currentTimeMillis();
        WayPoint p= new WayPoint(_startTime, current, _wayHorizontalDistance, _verticalDistance);
        
        addWayPoint(p);
        
        // Reset the waypoint vars.
        _startTime = current;
        _wayHorizontalDistance = 0;
        _verticalDistance = 0;
    }
    
    /**
     * View the various options for this application.
     */
    private void viewOptions() 
    {
        OptionScreen optionScreen = new OptionScreen();
        pushScreen(optionScreen);
    }
    
    /**
     * View the various saved waypoints.
     */
    private void viewPreviousPoints()
    {
        PointScreen pointScreen = new PointScreen(_previousPoints);
        pushScreen(pointScreen);
    }
    
    /**
     * Adds a new WayPoint and commits the set of saved waypoints to flash.
     * @param p The point to add.
     */
    private synchronized static void addWayPoint(WayPoint p) 
    {
        _previousPoints.addElement(p);
        commit();
    }
    
    /**
     * Removes a waypoint from the set of saved points and commits the modifed set to
     * flash.
     * @param p The point to remove.
     */
    synchronized static void removeWayPoint(WayPoint p)
    {
        _previousPoints.removeElement(p);
        commit();
    }
    
    /**
     * Commit the waypoint set to flash.
     */
    private static void commit()
    {
        _store.setContents(_previousPoints);
        _store.commit();
    }    
    
    /**
     * Rounds off a given double to the provided number of decimal places.
     * @param d The double to round off.
     * @param decimal The number of decimal places to retain.
     * @return A double with the number of decimal places specified.
     */
    private static double round(double d, int decimal) 
    {
        double powerOfTen = 1;
        
        while (decimal-- > 0)
        {
            powerOfTen *= 10.0;
        }
        
        double d1 = d * powerOfTen;
        int d1asint = (int)d1; // Clip the decimal portion away and cache the cast, this is a costly transformation.
        double d2 = d1 - d1asint; // Get the remainder of the double.
        
        // Is the remainder > 0.5? if so, round up, otherwise round down (lump in .5 with > case for simplicity).
        return ( d2 >= 0.5 ? (d1asint + 1)/powerOfTen : (d1asint)/powerOfTen);
    }

    /**
     * Implementation of the LocationListener interface.
     */
    private class LocationListenerImpl implements LocationListener
    {
        // Members ----------------------------------------------------------------------------------------------
        private int captureCount;
        private int sendCount;
        
        // Methods ----------------------------------------------------------------------------------------------
        /**
         * @see javax.microedition.location.LocationListener#locationUpdated(LocationProvider,Location)
         */
        public void locationUpdated(LocationProvider provider, Location location)
        {
            if(location.isValid())
            {
                float heading = location.getCourse();
                double longitude = location.getQualifiedCoordinates().getLongitude();
                double latitude = location.getQualifiedCoordinates().getLatitude();
                float altitude = location.getQualifiedCoordinates().getAltitude();
                float speed = location.getSpeed();                
                
                // Horizontal distance to send to server.
                float horizontalDistance = speed * _interval;
                _horizontalDistance += horizontalDistance;
                
                // Horizontal distance for this waypoint.
                _wayHorizontalDistance += horizontalDistance;
                                
                // Distance over the current interval.
                float totalDist = 0; 
                               
                // Moving average grade.
                for(int i = 0; i < GRADE_INTERVAL - 1; ++i)
                {
                    _altitudes[i] = _altitudes[i+1];
                    _horizontalDistances[i] = _horizontalDistances[i+1];
                    totalDist = totalDist + _horizontalDistances[i];
                }
                
                _altitudes[GRADE_INTERVAL-1] = altitude;
                _horizontalDistances[GRADE_INTERVAL-1] = speed*_interval;
                totalDist= totalDist + _horizontalDistances[GRADE_INTERVAL-1];
                float grade = (totalDist==0.0F)? Float.NaN : ( (_altitudes[4] - _altitudes[0]) * 100/totalDist);
                
                // Running total of the vertical distance gain.
                float altGain = _altitudes[GRADE_INTERVAL-1] - _altitudes[GRADE_INTERVAL-2];
                
                if (altGain > 0) 
                {
                    _verticalDistance = _verticalDistance + altGain;
                }
                
                captureCount += _interval;
                
                // If we're mod zero then it's time to record this data.
                captureCount %= CAPTURE_INTERVAL;
                
                // Information to be sent to the server.
                if ( captureCount == 0 )
                {
                    // Minimize garbage creation by appending only character primitives, no extra String objects created that way.
                    _messageString.append(round(longitude,4));
                    _messageString.append(';');
                    _messageString.append(round(latitude,4));
                    _messageString.append(';');
                    _messageString.append(round(altitude,2));
                    _messageString.append(';');
                    _messageString.append(_horizontalDistance);
                    _messageString.append(';');
                    _messageString.append(round(speed,2));
                    _messageString.append(';');
                    _messageString.append(System.currentTimeMillis());
                    _messageString.append(':');
                    sendCount += CAPTURE_INTERVAL;
                    _horizontalDistance = 0;
                }
                
                // If we're mod zero then it's time to send.
                sendCount %= SENDING_INTERVAL;
                
                synchronized(this)
                {
                    if (sendCount == 0 && _messageString.length() != 0)
                    {
                        _serverConnectThread.sendUpdate(_messageString.toString()); 
                        _messageString.setLength(0);                        
                    }                    
                }
                
                // Information to be displayed on the device.
                StringBuffer sb = new StringBuffer();
                sb.append("Longitude: ");
                sb.append(longitude);
                sb.append("\n");
                sb.append("Latitude: ");
                sb.append(latitude);
                sb.append("\n");
                sb.append("Altitude: ");
                sb.append(altitude);
                sb.append(" m");
                sb.append("\n");
                sb.append("Heading relative to true north: ");
                sb.append(heading);
                sb.append("\n");
                sb.append("Speed : ");
                sb.append(speed);
                sb.append(" m/s");
                sb.append("\n");
                sb.append("Grade : ");
                if(Float.isNaN(grade))sb.append(" Not available");
                else sb.append(grade+" %");
                GPSDemo.this.updateLocationScreen(sb.toString());
            }
        }
  
        public void providerStateChanged(LocationProvider provider, int newState)
        {
            // Not implemented.
        }        
    }
    
    /**
     * The main screen to display the current GPS information
     */
    private final class GPSDemoScreen extends MainScreen
    {
        
        // Constructor
        GPSDemoScreen()
        {
            RichTextField instructions = new RichTextField("Waiting for location update...",Field.NON_FOCUSABLE);
            this.add(instructions);
            
            addMenuItem(_markWayPoint);
            addMenuItem(_viewWayPoints);
            addMenuItem(_options);            
        }
        
        
        /**
         * @see net.rim.device.api.ui.Screen#close()
         */
        public void close()
        {
            if ( _locationProvider != null ) 
            {
                _locationProvider.reset();
                _locationProvider.setLocationListener(null, -1, -1, -1);
            }
            if ( _serverConnectThread != null )
            {
                _serverConnectThread.stop();
            }
            
            super.close();
        }
    }
    
    /**
     * Options for the application.
     */
    private class OptionScreen extends MainScreen 
    {
        private BasicEditField _serverAddressField;
        
        OptionScreen()
        {
            super();
            
            LabelField title = new LabelField("Options", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
            setTitle(title);
            
            _serverAddressField = new BasicEditField("Server Address: ", GPSDemo._hostName, 128, Field.EDITABLE);
            add(_serverAddressField);
            
            addMenuItem(_save);            
        }         
        
        private MenuItem _save = new MenuItem("Save" , 200000, 10) 
        {
            public void run()
            {                
                String hostname = _serverAddressField.getText();
                GPSDemo._hostName = hostname;
                GPSDemo.this._serverConnectThread.setHost(hostname);
                GPSDemo.this.popScreen(OptionScreen.this);
            }
        };
    }
    
    /**
     * WayPoint describes a way point, a marker on a journey or point of interest.
     * WayPoints are persistable.
     */
    static class WayPoint implements Persistable
    {
        long _startTime;
        long _endTime;
        float _distance;
        float _verticalDistance;
        
        WayPoint(long startTime,long endTime,float distance,float verticalDistance)
        {
            _startTime=startTime;
            _endTime=endTime;
            _distance=distance;
            _verticalDistance=verticalDistance;
        }
    }
    
    /**
     * ServerConnectThread is responsible for sending data periodically to the server.
     * This thread runs for the lifetime of the application instance, data is queued 
     * up for send. Failed transactions are retried at the next send period.
     */
    private final class ServerConnectThread  extends Thread
    {        
        // Members ------------------------------------------------------------------------------------------------
        
        private InputStreamReader _in;
        private OutputStreamWriter _out;
        private String _url;        
        private boolean _go; 
        private Vector _data;
        private int _delay; 
        private static final int DEFAULT = 5000; // Retry delay for communication with the server after a failed attempt.
        private static final int TWENTY_FOUR_HOURS = 24 * 3600 * 1000;
                
        /**
         * Creates a thread to connect to a server
         * @param host The server host to connect to
         */
        ServerConnectThread(String host)
        {
            setHost(host);
            _go = true;
            _data = new Vector();
            _delay = DEFAULT; // 5 second backoff to start.
        }
        
        /**
         * Sets the url pointing to the server to connect to based on the 
         * server's hostname.
         * @param host The hostname of the server to connect to
         */
        private synchronized void setHost(String host)
        {
            // Don't use direct TCP.
            _url = "socket://" + host; // + ";deviceside=false";
            
            // Wake up this thread in case it was sleeping so pending data can 
            // be resent.
            this.notify(); 
        }
        
        /**
         * Stops the connection
         */
        private synchronized void stop()
        {
            _go = false;
        }
        
        /**
         * Queues the updated information to send to the server
         * @param data The updated information to send
         */
        private synchronized void sendUpdate(String data)
        {
            _data.addElement(data);
            this.notify();
        }
        
        /**
         * Increase the time interval in which the server is updated with the 
         * current data.
         * @param delay The current update delay (in milliseconds) to increase
         * @return The new delay which is equal to double the old delay with an upper bound of twenty-four hours (in milliseconds)
         */
        private int increaseDelay(int delay)
        {            
            if ( delay >= TWENTY_FOUR_HOURS )
            {
                 return TWENTY_FOUR_HOURS; // 24 hr max backoff.
            }
            else 
            {
                return delay << 1; // Otherwise just double the current delay.
            }
        }

        /**
         * Periodically send data to the server
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            boolean error = false;
            
            while ( _go )
            {
                String data = null;
                synchronized(this)
                {
                    // If there is nothing to send or there is an error then
                    // delay the upload.
                    if ( _data.size() == 0 || error )
                    {
                        try 
                        {
                            this.wait(_delay);
                        } 
                        catch (InterruptedException e) 
                        {
                        }
                    }
                    
                    if ( !_go ) 
                    {
                        break; // Check for exit status.
                    }
                    
                    if ( _data.size() == 0 ) 
                    {
                        continue; // Nothing to do.
                    }
                    
                    data = (String)_data.elementAt(0); // Pop the first element.
                    _data.removeElement(data);
                }              
                
                // Try to connect and write data to the server
                StreamConnection connection = null;
                try
                {
                    connection = (StreamConnection)Connector.open(_url, Connector.READ_WRITE, false);
                    _in = new InputStreamReader(connection.openInputStream());
                    _out = new OutputStreamWriter(connection.openOutputStream());
                    
                    // Write the data to the server.
                    _out.write(data, 0, data.length());   
                    _out.write('z'); // Write a terminator.
                    
                    // Wait for an acknowledgement, in this case an 'R' character, for 'Received'.                    
                    char c = (char)_in.read();
                    
                    // Debug
                    System.out.println("GPSDemo: Debug: exchange(): received acknowledgement char:" + c);
                    _delay = DEFAULT; // Reset the backoff delay .
                    error = false; // Clear any error.
                }
                catch (IOException e)
                {
                    error = true;
                    _delay = increaseDelay(_delay);
                    GPSDemo.this.updateLocationScreen(e.toString());
                    
                    // Push this data back on the stack, it's still pending.
                    synchronized (this)
                    {
                        _data.insertElementAt(data, 0);
                    }
                }
                finally
                {
                    try
                    {
                        if(_in!=null) _in.close();
                        if(_out!=null) _out.close();
                        if(connection!=null) connection.close();
                    }
                    catch (IOException ioe)
                    {
                        // No-op - we don't care on close.
                    }
                }
            }            
        }
    }     
}
