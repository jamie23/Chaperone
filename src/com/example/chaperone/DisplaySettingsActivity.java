package com.example.chaperone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplaySettingsActivity extends Activity {
	
	//Variables for all saved strings
	EditText txtTime, txtPassphrase, txtNumber;
	
	//Button clicked when saving
	Button btnSave;
	
	//Session manager
	SessionManagement session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);
        
        //Setting up font for title
        Typeface newFont = Typeface.createFromAsset(getAssets(),"fonts/Walkway.ttf"); 
		TextView txtTitle = (TextView) findViewById(R.id.settingsTitle);

		txtTitle.setTypeface(newFont);
        
        session = new SessionManagement(getApplicationContext());
        
        //Get details from input text
        txtTime = (EditText) findViewById(R.id.time_value);
        txtPassphrase = (EditText) findViewById(R.id.passphrase_value);
        txtNumber = (EditText) findViewById(R.id.phoneNum_value);

        btnSave = (Button) findViewById(R.id.btnSave);
        
        
        //Listener for when save button is clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
        
        	@Override
			public void onClick(View arg0) {
        		//Get details from text box
        		String time = txtTime.getText().toString();
        		String passphrase = txtPassphrase.getText().toString();
        		String number = txtNumber.getText().toString();

        		
        		//Validation on each of the fields, give error message if not entered.
        		if(time.equals("")||passphrase.equals("")||number.equals("")){
        			Toast.makeText(getApplicationContext(), "Please fill out all the fields", Toast.LENGTH_LONG).show();

        		}else{
	        		session.overwriteSettings(time, passphrase, number);
	        		Intent rtnMainActivity = new Intent(getApplicationContext(), MainActivity.class);
	        		startActivity(rtnMainActivity);
	        		finish();
        		}
			}
		});
        
        Button btnDelete = (Button) findViewById(R.id.btnDeleteSet);
    	btnDelete.setOnClickListener(new View.OnClickListener() {
    		
    	@Override
		public void onClick(View arg0) {
    		session.logoutUser();
    		/*Intent rtnMainActivity = new Intent(getApplicationContext(), MainActivity.class);
    		startActivity(rtnMainActivity);
    		finish();*/
		}
        
    	});
	}
}
	
