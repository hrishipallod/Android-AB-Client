
package com.example.followthecode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDb  extends SQLiteOpenHelper{
	
	
	public static final int dev_id = 200;
	public static final int app_id = 1;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ldb";
	private static final String TABLE_NAME = "event_stats";
	private static final String JSTRING = "jstring";

	public EventDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create Table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + JSTRING + " TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	void delete_all_rows()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM "+ TABLE_NAME);	
		db.close();
	}
	void addToDB(String jString)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(JSTRING, jString);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	String createJSONArray() throws JSONException
	{
		//Read data
		//Create JSON object
		//Send data
		//Delete table contents
		//JSONObject jObj = new JSONObject();
		JSONArray jArray = new JSONArray();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT JSTRING FROM " + TABLE_NAME, null);
		cursor.moveToFirst();
		if(cursor.moveToFirst())
		{
			do
			{
				JSONObject temp = new JSONObject(cursor.getString(0));
				jArray.put(temp);
			}while(cursor.moveToNext());
		}
		//jObj.put("events", jArray);
		return jArray.toString();
	}
	
}
