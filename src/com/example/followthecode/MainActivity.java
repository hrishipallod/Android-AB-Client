package com.example.followthecode;
import org.json.JSONException;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	ConversionRate obj;
	TalkToServer tObj;
	int ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tObj = new TalkToServer(this);
        obj = new ConversionRate(this);
        try {
			new MyAsyncTask().execute("0", obj.createJSONObject(200,1));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.insert("MainActivity", "button1");
		obj.insert("MainActivity", "button2");
        new MyAsyncTask().execute("1");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    	Intent i = new Intent(this,Resolution.class);
    	obj.updateCount("MainActivity", "button1", ab);
    	startActivity(i);
    }
    
    public void Click2(View v)
    {
    	Intent i = new Intent(this,Resolution.class);
    	obj.updateCount("MainActivity", "button2", ab);
    	startActivity(i);
    }
    private class MyAsyncTask extends AsyncTask<String, String, String>
	{
    	protected void onPostExecute(String str) 
    	{
    		if(str.equals("DO NOTHING"))
    		{
    			//Toast message - local db deleted
    		}
    		else if (str.equals("Could not connect"))
    		{
    			//No Connectivity or server side issue 
    			ab = 0;
    		}
    		else
    		{	
	    		ab = Integer.parseInt(str);
    		}
			Button bt1 = (Button) findViewById(R.id.button1);
			Button bt2 = (Button) findViewById(R.id.button2);
			obj.updateTotalCount("MainActivity", "button1", ab);
			obj.updateTotalCount("MainActivity", "button2", ab);
	    	if(ab == 0)
			{
				bt1.setText("Download");
				bt2.setText("Play");
			}
			else
			{
				bt1.setText("Save");
				bt2.setText("Start");
			}
    	}
    	
		@Override
		protected String doInBackground(String...uris) {

			if(uris[0].equals("0"))
			{
				if(tObj.sendJSONdata(uris[1]).equals("Data Sent"))
					obj.delete_all_rows();
				return "DO NOTHING";
			}
			else
				return tObj.getAorB();
		}

	}
}
