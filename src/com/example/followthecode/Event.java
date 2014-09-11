/*
 * Usage : new Event(this, abMode, str eventName, str paramName, <values>);
 * 
 * this       : context
 * abMode     : 0 or 1
 * eventName  : any
 * eventValue : Calculated for standard events. Or provided as an argument.
 * paramName  : orientation, connection type, volume, <more to be added later>
 * paramValue : Calculated for standard parameters. Or provided as an argument.
 * 
 */
package com.example.followthecode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Event {
	static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	Context context;
	String timestamp;
	String eventName;
	String eventValue;
	String paramName;
	String paramValue;
	int abMode;
	
	public Event(Context context, int abMode, String...args) throws JSONException {

		this.context = context;
		this.abMode = abMode;
		this.timestamp = getUtcTimestamp();
		this.eventName = args[0];
		this.eventValue = args[2];
		this.paramName = args[1];
		if(paramName.equals("orientation"))
		{
			this.paramValue = getCurrentOrientation();
		}
		else if(paramName.equals("volume"))
		{
			this.paramValue = getCurrentVolume();
		}
		else if(paramName.equals("brightness"))
		{
			this.paramValue = getCurrentBrightness();
		}
		else if(paramName.equals("connectivity"))
		{
			this.paramValue = getCurrentWifiStatus();
		}
		else
		{
			this.paramValue = args[3];
		}
		addToSQLite();
	       
	}
	
	public static String getUtcTimestamp()
	{
		
	    SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return sdf.format(new Date());
	}
	
	private String getCurrentWifiStatus() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
		{
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return "WIFI";
			else if(activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX)
				return "WIMAX";
			else if(activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
				return "ETHERNET";
			else if(activeNetwork.getType() == ConnectivityManager.TYPE_BLUETOOTH)
				return "BLUETOOTH";
			else
				return "MOBILE INTERNET";
		}
		else
		{
			return "NOT CONNECTED";
		}
	}

	private String getCurrentVolume() {
		// TODO Auto-generated method stub
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
		return String.valueOf(currentVolume);
	}
	

	private String getCurrentOrientation()
	{
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = display.getRotation();
		if(rotation == 0)
			return "PORTRAIT";
		else
			return "LANDSCAPE";
	}
	
	private String getCurrentBrightness() {
		/* ADD LATER */
		return "5";
	}
	
	private void addToSQLite()
	{
		try
	    {
			new EventDb(context).addToDB(createJSONString());
			Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
	    }
	    catch(Exception e)
	    {
	        	Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
	    }
	}
	
	private String createJSONString() throws JSONException
	{
		//Read data
		//Create JSON object
		//Send data
		//Delete table contents
		JSONObject jObj = new JSONObject();
		jObj.put("developer_id", 200);
		jObj.put("app_id", 1);
		jObj.put("timestamp", timestamp);
		jObj.put("eventName", eventName);
		jObj.put("eventValue", eventValue);
		jObj.put("paramName", paramName);
		jObj.put("paramValue", paramValue);
		return jObj.toString();
	}	
	
	/*
	public static Date GetUTCdatetimeAsDate()
	{
	    //note: doesn't check for null
	    return StringDateToDate(GetUTCdatetimeAsString());
	}
	
	public static Date StringDateToDate(String StrDate)
	{
	    Date dateToReturn = null;
	    SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

	    try
	    {
	        dateToReturn = (Date)dateFormat.parse(StrDate);
	    }
	    catch (ParseException e)
	    {
	        e.printStackTrace();
	    }

	    return dateToReturn;
	    
	}
	*/
	
}