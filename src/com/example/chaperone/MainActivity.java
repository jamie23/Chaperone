package com.example.chaperone;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	SessionManagement session;
	EditText txtStart, txtDestination, txtTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		//Setting up title
		Typeface newFont = Typeface.createFromAsset(getAssets(),"fonts/Walkway.ttf"); 
		TextView txtTitle = (TextView) findViewById(R.id.Title);

		txtTitle.setTypeface(newFont);
		
		session = new SessionManagement(getApplicationContext());
		session.checkLogin();
		HashMap<String, String> user = session.getUserDetails();
		
		String pass = user.get(SessionManagement.PASSPHRASE);
        
		Button btnStartHome = (Button) findViewById(R.id.btnStartHome);
       
		//Get details from input text
         txtStart = (EditText) findViewById(R.id.start_text);
         txtDestination = (EditText) findViewById(R.id.destination_text);
         txtTime = (EditText) findViewById(R.id.time_text);
		

        btnStartHome.setOnClickListener(new View.OnClickListener() {
            
        	@Override
			public void onClick(View arg0) {
        		//Get details from text box
        		String start = txtStart.getText().toString();
        		String destination = txtDestination.getText().toString();
        		String time = txtTime.getText().toString();
        		
        		
        		//Validation on each of the text fields
        		if(start.equals("")||destination.equals("")||time.equals("")){
        			Toast.makeText(getApplicationContext(), "Please fill out all the fields", Toast.LENGTH_LONG).show();

        		}else{
        			session.overwriteTravel(start, destination, time);
            		Intent rtnNavigatePage= new Intent(getApplicationContext(), DestinationEntry.class);
            		startActivity(rtnNavigatePage);
            		finish();
        		}
        		
			}
		});
				
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void openSettings(View view){
		//Open settings in response to the button being clicked.
		Intent intent = new Intent(this, DisplaySettingsActivity.class);
		startActivity(intent);
	}
	
	public void openDestination(View view){
		//Open settings in response to the button being clicked.
		Intent intent = new Intent(this, DestinationEntry.class);
		startActivity(intent);
	}

}
