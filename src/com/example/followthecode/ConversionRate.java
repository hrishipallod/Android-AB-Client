package com.example.followthecode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConversionRate extends SQLiteOpenHelper{
	//Static Variables - Table, DB details
			private static final int DATABASE_VERSION = 1;
			private static final String DATABASE_NAME = "local_db";
			private static final String TABLE_NAME = "conversion_stats";
			private static final String ACTIVITY = "activity";
			private static final String ITEM = "item";
			private static final String A = "a";   
			private static final String B = "b";
			private static final String TOTAL_A = "total_a";
			private static final String TOTAL_B = "total_b";

			public ConversionRate(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			// Create Table
			@Override
			public void onCreate(SQLiteDatabase db) {
				String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ACTIVITY + " TEXT," + ITEM + " TEXT," + A + " INTEGER DEFAULT 0,"+ B + " INTEGER DEFAULT 0,"+ TOTAL_A + " INTEGER DEFAULT 0,"+ TOTAL_B + " INTEGER DEFAULT 0 )";
				db.execSQL(CREATE_TABLE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
				onCreate(db);
			}

			int check(String activity, String item)
			{
				int count;
				SQLiteDatabase db = this.getWritableDatabase();
				Cursor cursor = db.rawQuery("SELECT count(*) FROM " + TABLE_NAME + " WHERE " + ACTIVITY + " = \"" + activity + "\" AND " + ITEM + " = \"" + item + "\"", null);
				cursor.moveToFirst();
				count = cursor.getInt(0);
				if (count>0) 
				{
					db.close();
					return 0;
				}
				else
				{
					db.close();
					return 1;
					
				}	
			}
			void delete_all_rows()
			{
				SQLiteDatabase db = this.getWritableDatabase();
				db.execSQL("DELETE FROM "+ TABLE_NAME);	
				db.close();
			}
			void insert(String activity, String item)
			{
				if(check(activity, item) == 1)
				{
					SQLiteDatabase db = this.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put(ACTIVITY, activity);
					values.put(ITEM, item);
					db.insert(TABLE_NAME, null, values);
					db.close();
				
				}
			}
			//Add new
			void updateTotalCount(String activity, String item, int mode) {

				//increment total count for 'mode'
				SQLiteDatabase db = this.getWritableDatabase();
				
				//String selectQuery = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE activity = '" + activity + "' AND item = '" + item + "'";
				//String selectQuery = "SELECT COUNT(*) from conversion_stats where activity = 'MainActivity' and item = 'Button1'";
				
				String update;
				if(mode == 0)
				{
					update = "UPDATE " + TABLE_NAME + " SET " + TOTAL_A + " = " + TOTAL_A + " + 1 WHERE activity = \"" + activity + "\" AND item = \"" + item + "\"";
				}
				else
				{
					update = "UPDATE " + TABLE_NAME + " SET " + TOTAL_B + " = " + TOTAL_B + " + 1 WHERE activity = \"" + activity + "\" AND item = \"" + item + "\"";
				}
				db.execSQL(update);
			
				db.close(); 
			}
			void updateCount(String activity, String item, int mode) {

				//increment conversion count for 'mode'
				String update;
				SQLiteDatabase db = this.getWritableDatabase();
				
				if(mode == 0)
				{
					update = "UPDATE " + TABLE_NAME + " SET " + A + " = " + A + " + 1 WHERE activity = \"" + activity + "\" AND item = \"" + item + "\"";
				}
				else
				{	
					update = "UPDATE " + TABLE_NAME + " SET " + B + " = " + B + " + 1 WHERE activity = \"" + activity + "\" AND item = \"" + item + "\"";
				}
				db.execSQL(update);
				
				db.close(); 
			}
			
			String createJSONObject(int dev_id, int app_id) throws JSONException
			{
				//Read data
				//Create JSON object
				//Send data
				//Delete table contents
				JSONObject jObj = new JSONObject();
				jObj.put("developer_id", dev_id);
				jObj.put("app_id", app_id);
				JSONArray jArray = new JSONArray();
				SQLiteDatabase db = this.getWritableDatabase();
				Cursor cursor = db.rawQuery("SELECT activity, item, a, b, total_a, total_b FROM " + TABLE_NAME, null);
				cursor.moveToFirst();
				if(cursor.moveToFirst())
				{
					do
					{
						JSONObject temp = new JSONObject();
						temp.put("activity_name", cursor.getString(0));
						temp.put("item_name", cursor.getString(1));
						temp.put("a", cursor.getInt(2));
						temp.put("b", cursor.getInt(3));
						temp.put("total_a", cursor.getInt(4));
						temp.put("total_b", cursor.getInt(5));
						jArray.put(temp);
					}while(cursor.moveToNext());
				}
				jObj.put("activities", jArray);
				return jObj.toString();
			}
}