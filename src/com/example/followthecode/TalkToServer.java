package com.example.followthecode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TalkToServer {

	public static final String getURL = "http://192.168.1.2/ONE/get.php";
	public static final String postURL = "http://192.168.1.2/ONE/post.php";
	
	private Context context;
	public TalkToServer(Context context)
	{
		this.context = context;
	}
	public boolean checkConnectivity() {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public String sendJSONdata(String jString)
	{
		String result;
		if(checkConnectivity()==false)
		{
			return "No Connectivity";
		}
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(postURL);
		    StringEntity se = new StringEntity(jString);
		    httpPost.setEntity(se);
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-type", "application/json");
	        HttpResponse httpResponse = httpclient.execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();
	        result = convertStreamToString(inputStream);
	    
		} catch (Exception e) {
	            //Log.d("InputStream", e.getLocalizedMessage());
	        	result = "Could not connect";
	        }
			return result;
	 
	}
	public String getAorB()
	{
		String str = "0";
		if(checkConnectivity()==false)
		{
			return "Could not connect";
		}
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(getURL);
		try 
		{
			HttpResponse response=httpclient.execute(httpget);
		    if(response != null) 
		    {
		        InputStream inputstream = response.getEntity().getContent();
		  		str = convertStreamToString(inputstream);
		    }
		} 
		catch (Exception e) {
			str = "Could not connect";
		}
		return str;
	}
	
	private String convertStreamToString(InputStream is) 
	{
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    try {
	        while ((line = rd.readLine()) != null) {
	            total.append(line);
	        }
	    } catch (Exception e) {
	        //
	    }
	    return total.toString();
	}
}
