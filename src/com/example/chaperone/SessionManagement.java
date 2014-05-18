/*
 * Followed a tutorial to save details using android hive.
 */

package com.example.chaperone;

import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor; 



public class SessionManagement {
	//Class to share preferences
	SharedPreferences preferences;
	
	//Need editor for shared preferences
	Editor editor;
	
	Context currContext;
	
	//Shared preference mode
	int PRIVATE_MODE = 0;
	
	//shared preference file name
	private static final String PREF_NAME = "ChaperonePref";
	
	//Shared Preference keys
	public static final String IS_LOGIN = "IsLoggedIn";
	
	public static final String TEXT_TIME = "25";
	
	public static final String PASSPHRASE = "hash";

	public static final String NUM_TEXT = "code";
	
	public static final String START_POINT = "Bangor";
	
	public static final String DESTINATION = "Holywood";

	public static final String TIME_TO_DEST = "10";
		

	//constructor
	public SessionManagement(Context context){
		this.currContext = context;
		preferences = currContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = preferences.edit();
	}
	
	//Check login status
	public boolean isLoggedIn(){
		return preferences.getBoolean(IS_LOGIN, false);
	}
	
	public void logoutUser(){
		//clear all data from shared preferences
		editor.clear();
		editor.commit();
		
		//redirect to settings page after committing new cleared settings.
		Intent i = new Intent(currContext, DisplaySettingsActivity.class);
		
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		currContext.startActivity(i);
	}
	
	//Check if they have created settings, if not they should be redirected to that page
	public void checkLogin(){
		if(!this.isLoggedIn()){
			//user not logged in so redirect them
			Intent i = new Intent(currContext, DisplaySettingsActivity.class);
			
			//close all activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			//Add new flag to start new activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			//Starting login activity
			currContext.startActivity(i);
		}
	}
	
	//Storing the data
	public void overwriteSettings(String textTime, String passphrase, String numText){
		//Logged in here means settings have been setup.
		editor.putBoolean(IS_LOGIN, true);
		
		editor.putString(TEXT_TIME, textTime);
		
		editor.putString(PASSPHRASE, passphrase);
		
		editor.putString(NUM_TEXT, numText);
				
		editor.commit();
	}
	
	//Overwrite the travel settings
	public void overwriteTravel(String startPoint, String endPoint, String time){
		//Logged in here means settings have been setup.
		
		editor.putString(START_POINT, startPoint);
		
		editor.putString(DESTINATION, endPoint);
		
		editor.putString(TIME_TO_DEST, time);
				
		editor.commit();
	}
	
	//Retrieve travel details via a hashmap
		public HashMap<String, String> getTravelDetails(){
			HashMap<String,String> travelDetails = new HashMap<String,String>();
			
			//get details, the second parameter is what is returned if the value for the key doesn't exist
			travelDetails.put(START_POINT, preferences.getString(START_POINT,"Newcastle"));
			
			travelDetails.put(DESTINATION, preferences.getString(DESTINATION,"Belfast"));
			
			travelDetails.put(TIME_TO_DEST, preferences.getString(TIME_TO_DEST,"10"));
					
			return travelDetails;

		}
	
	
	//Retrieve user details via a hashmap
	public HashMap<String, String> getUserDetails(){
		HashMap<String,String> userDetails = new HashMap<String,String>();
		
		//get details, the second parameter is what is returned if the value for the key doesn't exist
		userDetails.put(TEXT_TIME, preferences.getString(TEXT_TIME,"20"));
		
		userDetails.put(PASSPHRASE, preferences.getString(PASSPHRASE,"hunter2"));
		
		userDetails.put(NUM_TEXT, preferences.getString(NUM_TEXT,"07906391110"));
				
		return userDetails;

	}
}

