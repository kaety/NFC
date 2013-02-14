package kandidat.nfc.nfcapp;

import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.*;


public class MainActivity extends Activity {
	
	
	//Objekt som representerar NFC adaptern
	private NfcAdapter nfcAdapter;

    @Override
    /**
     * This function is lanched when the app is started.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
         
    /*    //Får tag i ett objekt som representerar NFC-adaptern
        NfcManager manager = (NfcManager) getBaseContext().getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();
        
        if(!nfcAdapter.isEnabled()){
        	//Öppnar menyn så att användaren kan aktivera NFC
        	Intent nfcOptionsIntent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
        	startActivity(nfcOptionsIntent);    
        }*/
    }
    
    /** Called when the user clicks the Send button */
    public void sendNFCMessage(View view) {
        // Do something in response to NFC send button
    	
    	//START LOADING SCREEN FIRST????
    	//SEND AND RECIEVE NFC MESSAGE HERE IF WE GET WHAT WE WANT ->>
    	//START ACCESSACTIVITY ELSE START DENIEDACTIVITY
    	
    	//NFC ACCESS
    	if(1==1){
    	Intent intent = new Intent(this, AccessActivity.class);
    	startActivity(intent);
    	}
    	else{
    		//NOT NFC ACCESS
    	
    	    	Intent intent = new Intent(this, DeniedActivity.class);
    	    	startActivity(intent);
    	    	
    	}
    	
    }
   
    
    /** Called when the user clicks the Settings button */
    public void settingsAct(View view) {
    	
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onPause() {

        super.onPause();

        //mAdapter.disableForegroundDispatch(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        //mAdapter.enableForegroundDispatch(this, pendingIntent, 
         //   intentFiltersArray, techListsArray);
    }

    @Override
    public void onNewIntent(Intent intent) {
    	 //Parcelable message = intent.getParcelableExtra(NfcAdapter.ACTION_NDEF_DISCOVERED);
    	//do something with message
    }
}

