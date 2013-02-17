package kandidat.nfc.nfcapp;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity  implements CreateNdefMessageCallback {
	

	//Objekt som representerar NFC adaptern
	private NfcAdapter nfcAdapter;

    @Override
    /**
     * This function is lanched when the app is started.
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
         
        //Får tag i ett objekt som representerar NFC-adaptern
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        
        if(!nfcAdapter.isEnabled()){
        	//Öppnar menyn så att användaren kan aktivera NFC
        	startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else if (!nfcAdapter.isNdefPushEnabled()) {
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        
        nfcAdapter.setNdefPushMessageCallback(this, this);


    }
    
    /** Called when the user clicks the Send button */
    public void sendNFCMessage(View view) {
        // Do something in response to NFC send button
    	//START LOADING SCREEN FIRST????
    	//SEND AND RECIEVE NFC MESSAGE HERE IF WE GET WHAT WE WANT ->>
    	//START ACCESSACTIVITY ELSE START DENIEDACTIVITY
    	
    	//NFC ACCESS
    	if(true){
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
        
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

    }

   
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        Toast.makeText(this, new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { 
                		NdefRecord.createMime(
                        "application/vnd.com.example.android.beam", text.getBytes())
        });
        return msg;
    } 
}

