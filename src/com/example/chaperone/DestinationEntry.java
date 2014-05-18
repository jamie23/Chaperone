package com.example.chaperone;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DestinationEntry extends Activity{
	LocationManager locationM;
		
	SessionManagement session;
	
	TextView currTime;
	long countTimer = 0;
	long passTimeInMilli = 0;
	boolean flagSent = false;
	
	Handler timeHandler = new Handler();
	
	//Based on a thread this runs every half a second to update. Updates every second.
	Runnable timerRunnable = new Runnable(){
		public void run(){

			//Initialise all user details which have been received from settings page.
			session = new SessionManagement(getApplicationContext());
			HashMap<String, String> userDetails = session.getUserDetails();
			String timeToAdd = userDetails.get(SessionManagement.TEXT_TIME);
			
			//Grabs interval and changes it to a long instead of string.
			long timeAdd = Long.parseLong(timeToAdd);
			
			long milliseconds = System.currentTimeMillis() - countTimer;
			int seconds = (int) (milliseconds/1000);
			int minutes = seconds/60;	
			seconds = seconds%60;
			
			//Displaying time correctly
			currTime.setText(String.format("%d:%02d", minutes, seconds));
			
			//When the timer runs out, start new timer to send SMS 
			if((minutes==passTimeInMilli)&&(flagSent==false)){
				sendSMS();
				flagSent = true;
				
				//Neat trick to add the interval after the first sms fire.
				passTimeInMilli+=timeAdd;
			}
			
			if(minutes!=passTimeInMilli){
				flagSent = false;
			}
			
			timeHandler.postDelayed(this, 500);
		}
	};
	
	
	protected void onCreate(Bundle savedInstanceState) {
		locationM = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_entry);
        
		//Setting up font for title
		Typeface newFont = Typeface.createFromAsset(getAssets(),"fonts/Walkway.ttf"); 
		TextView txtTitle = (TextView) findViewById(R.id.destTitle);
		txtTitle.setTypeface(newFont);
		
		session = new SessionManagement(getApplicationContext());

		//Grabbing user and journey details.
		HashMap<String, String> user = session.getTravelDetails();
		HashMap<String, String> userDetails = session.getUserDetails();

		String startPoint = user.get(SessionManagement.START_POINT);
		String endPoint = user.get(SessionManagement.DESTINATION);
		String estTime = user.get(SessionManagement.TIME_TO_DEST);
		
		final String userPassword = userDetails.get(SessionManagement.PASSPHRASE);
		
		//Set up text areas
		TextView  start_value = (TextView) findViewById(R.id.start_value);
        TextView dest_value = (TextView) findViewById(R.id.dest_value);
        TextView estimated_time = (TextView) findViewById(R.id.time_estimated);
        
        //Populate text areas
        start_value.setText(startPoint);
        dest_value.setText(endPoint);
        estimated_time.setText(estTime);
        
        /*
         * Timer code
         */
        currTime = (TextView) findViewById(R.id.time_remaining);

		//Get passed time as milliseconds
		passTimeInMilli = Long.parseLong(user.get(SessionManagement.TIME_TO_DEST));
		
        countTimer = System.currentTimeMillis();
        timeHandler.postDelayed(timerRunnable, 0);
        
        Button btnSubmitPass = (Button)findViewById(R.id.btn_destination_finished);
        final EditText passwordEntry = (EditText)findViewById(R.id.password_entry);

        btnSubmitPass.setOnClickListener(new View.OnClickListener() {

     	public void onClick(View arg0) {    
     		//If password correct, stop timer and return to main screen, otherwise do nothing
                String password = passwordEntry.getText().toString();

                //Check if user password is correct.
        		if(userPassword.equals(password)){
        			timeHandler.removeCallbacks(timerRunnable);
        			Intent rtnNavigatePage = new Intent(getApplicationContext(), MainActivity.class);
	        		startActivity(rtnNavigatePage);
	        		finish();
		        		
        		}else{
        			//Password not correct, alert user
        			Toast.makeText(getApplicationContext(), "Pasword incorrect", Toast.LENGTH_SHORT).show();
        		}
			}
		});
				
        
	}
	
	private void sendSMS()
    {   
		session = new SessionManagement(getApplicationContext());

		HashMap<String, String> user = session.getTravelDetails();
		HashMap<String, String> userDetails = session.getUserDetails();

		String startPoint = user.get(SessionManagement.START_POINT);
		String endPoint = user.get(SessionManagement.DESTINATION);		
		String numSend = userDetails.get(SessionManagement.NUM_TEXT);
	
		Location currLocation = locationM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location currLocation2 = locationM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double currLong = 0;
		double currLat = 0;
		boolean usedGPS = true;
		
		//If no gps available, use network provider
		if(currLocation==null){
			currLong = currLocation2.getLongitude();
			currLat = currLocation2.getLatitude();
			usedGPS = false;
		}else{
			currLong = currLocation.getLongitude();
			currLat = currLocation.getLatitude();
		}

		Geocoder geoCoderObj = new Geocoder(getApplicationContext());        
		try{
			List<Address> addressPerson = geoCoderObj.getFromLocation(currLat, currLong, 1);
			String firstLine = addressPerson.get(0).getAddressLine(0);
			String secondLine = addressPerson.get(0).getAddressLine(1);

			String textMessage = "CHAPERONE TEXT - Im Missing! Walking from " + startPoint + "-" + endPoint + "\nLast GPS Address: "+ firstLine + "\n" + secondLine ;
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(numSend, null, textMessage, null, null);   
			
		
		}catch(IOException e){
			e.printStackTrace();
		}
    }   
}