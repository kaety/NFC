package kandidat.nfc.nfcapp;

import java.nio.charset.Charset;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  implements CreateNdefMessageCallback {

	//ndefmessage strings
	private String latestRecievedMsg;
	private NFCPMessage nfcpMessage;

	//test
	private Boolean flag = true;

	//Objekt som representerar NFC adaptern
	private NfcAdapter nfcAdapter;

	@SuppressLint("NewApi")
	@Override
	/**
	 * This function is lanched when the app is started.
	 */
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		//Får tag i ett objekt som representerar NFC-adaptern
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		//Kolla om användarens NFC är påslagen
		if(!nfcAdapter.isEnabled()){
			//Öppnar menyn så att användaren kan aktivera NFC
			startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
		}
		//Följande eftersom metoden endast finns för API 16 och högre medan t.ex. LG L5 har version 15
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			if (!nfcAdapter.isNdefPushEnabled()) {
				startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
			}
		}


		nfcAdapter.setNdefPushMessageCallback(this, this);
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

		//nfcAdapter.disableForegroundDispatch(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		//nfcAdapter.enableForegroundDispatch(this, pendingIntent, 
		//   intentFiltersArray, techListsArray);

		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}

	}

	/**
	 * Receives  ALL INTENTS 
	 * @param intent
	 */
	void processIntent(Intent intent) {
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			
			setMessage(getLastestNFCMessage(intent));
			nfcpMessage = new NFCPMessage(latestRecievedMsg);

			if(nfcpMessage.getStatus() && nfcpMessage.getType()==3){
				startActivity(new Intent(MainActivity.this, AccessActivity.class));
			}
			else if(!nfcpMessage.getStatus() && nfcpMessage.getType()==3){
				//NOT NFC ACCESS
				Intent deniedIntent =new Intent(MainActivity.this, DeniedActivity.class);
				deniedIntent.putExtra("ErrorCode", nfcpMessage.getErrorCode());
				startActivity(deniedIntent);
				nfcpMessage.clearpenis();
			}

			getIntent().setAction("");
		}
		else{
			setMessage("FUNKAR SOM DET SKA");
		}
	}
	

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	@SuppressLint("NewApi")
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		NFCPMessage sendMsg = null;

		if (nfcpMessage == null) {
			// return null;

			// testing
			sendMsg = new NFCPMessage("NFC", 1, true, 1, 0);

		} else if (nfcpMessage.getType() == 1) {

			// skicka typ 2 meddelande

			sendMsg = new NFCPMessage("NFC", 1, 2, 1234);

		} else if (nfcpMessage.getType() == 2) {
			// testkod för att ta emot meddelande typ 2

			// skicka meddelande typ 3
			sendMsg = new NFCPMessage("NFC", 1, true, 3, 0);

		} else if (nfcpMessage.getType() == 3) {
			// att göra när vi tar emot meddelande typ 3
			return null;


		} else {
			// allt går fel
		}

		NdefMessage msg;

		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(Charset.forName("US-ASCII")),
				new byte[0], sendMsg.toString().getBytes(
						Charset.forName("US-ASCII")));
		msg = new NdefMessage(new NdefRecord[] { record });
		return msg;
	}

	/**
	 * Gets the latest NFC message and return it as a string
	 * @param intent
	 * @return
	 */
	public String getLastestNFCMessage(Intent intent){
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		return latestRecievedMsg = new String(msg.getRecords()[0].getPayload());
		// record 0 contains the MIME type, record 1 is the AAR, if present

	}

	/**
	 * Sets string s into debugging messagefield on the first screen.
	 * @param s
	 */
	public void setMessage(String s){
		// Toast.makeText(this, latestRecievedMsg, Toast.LENGTH_LONG).show();
		TextView view = (TextView) findViewById(R.id.message);
		view.setText(s + "\n");
	}

}