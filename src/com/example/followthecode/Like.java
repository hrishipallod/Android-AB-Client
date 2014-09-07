package com.example.followthecode;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Like extends ActionBarActivity {
	TalkToServer tObj;
	ConversionRate obj;
	int ab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_like);
		tObj = new TalkToServer(this);
		obj = new ConversionRate(this);
		obj.insert("Like", "button1");
		new MyAsyncTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.like, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void Click1(View v)
    {
    	obj.updateCount("Like", "button1", ab);
    }
    
    private class MyAsyncTask extends AsyncTask<String, String, String>
	{

    	protected void onPostExecute(String str) {

    		if (str.equals("Could not connect"))
    		{
    			//No Connectivity or server side issue 
    			ab = 0;
    		}
    		else
    		{	
	    		ab = Integer.parseInt(str);
    		}
    		
    		obj.updateTotalCount("Like", "button1", ab);
    		Button bt1 = (Button) findViewById(R.id.button1);
    		if(ab == 0)
			{
    			bt1.setText("Like");
		  	}
		  	else
		  	{
		  		bt1.setText("+1");
		  	}
			
    	}
    	
		@Override
		protected String doInBackground(String... uris) {
			
			return tObj.getAorB();
		}

	}
}
