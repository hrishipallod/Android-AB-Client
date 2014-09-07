package com.example.followthecode;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class Resolution extends ActionBarActivity {

	int ab;
	TalkToServer tObj;
	ConversionRate obj;
	Spinner spinner;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resolution);
		tObj = new TalkToServer(this);
		obj = new ConversionRate(this);
		obj.insert("Resolution", "mode");
		String[] strings={"360p","480p","720p","1080p"};
		spinner=new Spinner(this);
	    spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,strings));
		new MyAsyncTask().execute();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resolution, menu);
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
		obj.updateCount("Resolution", "mode", ab);
    	Intent i = new Intent(this,Like.class);
    	startActivity(i);
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
    		obj.updateTotalCount("Resolution", "mode", ab);
    		Button bt1 = (Button) findViewById(R.id.button1);
    		if(ab == 0)
      		{
    			
	  			bt1.setText("Auto");
	  			
	  		}
	  		else
	  		{
	  			bt1.setText("Manual");
	  			RelativeLayout root_layout=(RelativeLayout)findViewById(R.id.root_layout);
	  			root_layout.addView(spinner);
	  		    setContentView(root_layout);
	  		}

			
    	}

		@Override
		protected String doInBackground(String... uris) {
			
			return tObj.getAorB();
		}

	}
}
