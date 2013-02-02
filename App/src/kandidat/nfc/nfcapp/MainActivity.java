package kandidat.nfc.nfcapp;

import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

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
        
        
        //Får tag i ett objekt som representerar NFC-adaptern
        NfcManager manager = (NfcManager) getBaseContext().getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();
        
        if(!nfcAdapter.isEnabled()){
        	//Öppnar menyn så att användaren kan aktivera NFC
        	Intent nfcOptionsIntent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
        	startActivity(nfcOptionsIntent);     
        }
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

