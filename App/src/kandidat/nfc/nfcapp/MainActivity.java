package kandidat.nfc.nfcapp;

import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	
	
	//Objekt som representerar NFC adaptern
	private NfcAdapter mAdapter;

    @Override
    /**
     * This function is lanched when the app is started.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Detta är en kontroll att appen har minst den SDK-version som krävs för
        //att använda NFC
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1) {
        	// do whatever necessary to hide NFC features
        }
        mAdapter = NfcAdapter.getDefaultAdapter(getBaseContext());
        if(mAdapter == null){
        	//Telefonen saknar NFC-adapter
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

