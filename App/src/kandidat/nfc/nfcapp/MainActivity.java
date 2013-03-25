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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements CreateNdefMessageCallback {

	private String latestRecievedMsg;
	private NFCPMessage nfcpMessage;
	private final long TIMEOUT = 60 *1000;//GONE IN 60seconds
	private Long loginTime;
	// Objekt som representerar NFC adaptern
	private NfcAdapter nfcAdapter;

	@SuppressLint("NewApi")
	@Override
	/**
	 * Force user to start NFC and Android Beam. Enables callback.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
		
		
		// Får tag i ett objekt som representerar NFC-adaptern
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		// Kolla om användarens NFC är påslagen
		if (!nfcAdapter.isEnabled()) {
			// Öppnar menyn så att användaren kan aktivera NFC
			startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
		}
		// Följande eftersom metoden endast finns för API 16 och högre medan
		// t.ex. LG L5 har version 15
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			if (!nfcAdapter.isNdefPushEnabled()) {
				startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
			}
		}

		nfcAdapter.setNdefPushMessageCallback(this, this);
	}

	/**
	 *  Called when the user clicks the Settings button 
	 *  
	 */
	public void settingsAct(View view) {
		startActivity(new Intent(this, SettingsActivity.class));
	}

	/**
	 * Because we don't want anything to happen when we click optionsbutton.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		////////////////////////////////DEBUG//////////////////////////////
		Toast.makeText(this, "onPause", 1).show();
		///////////////////////////////////////////////////////////////////
	}
	/**
	 * Get LoginTime. Check if you have to login again.
	 */
	@Override
	public void onResume() {
		super.onResume();
		////////////////////////////DEBUG////////////////////////////////////////////
		Toast.makeText(this, "onResume", 1).show();
		/////////////////////////////////////////////////////////////////////////////

		// Get the between instance stored values
	//	SharedPreferences pre = getSharedPreferences("login", 1);
		//logintime blir login eller 0 om inget värde finns!
	//	loginTime = pre.getLong("login", 0L);
		
	//	if((System.currentTimeMillis()-loginTime) > TIMEOUT){
	//		startActivity(new Intent(this,LoginActivity.class));
	//		Toast.makeText(this, "TIMEOUT: PLEASE LOG IN AGAIN", 1).show();
	//		finish();
	//	}else
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
				processIntent(getIntent());
		}
	}

	/**
	 * Receives NFC-intents and displays them in view. Called from above.
	 * 
	 * @param intent
	 */
	void processIntent(Intent intent) {
			setMessage(getLastestNFCMessage(intent));
			nfcpMessage = new NFCPMessage(latestRecievedMsg);
			//status 0 inga fel!!!!
			if (nfcpMessage.getStatus().equals("0") && nfcpMessage.getType().equals("3")) {
				
				startActivity(new Intent(this,AccessActivity.class));
				
			} else if (nfcpMessage.getStatus().equals("1") && nfcpMessage.getType().equals("3")) {
				// NOT NFC ACCESS
				Intent deniedIntent = new Intent(this,
						DeniedActivity.class);
				deniedIntent.putExtra("ErrorCode", nfcpMessage.getErrorCode());
				startActivity(deniedIntent);
				nfcpMessage.clear();
			}
			getIntent().setAction("");
	}

	@Override
	/**
	 * NDEF message handler
	 * much debug here. In futher add cases and delete cases.
	 */
	public NdefMessage createNdefMessage(NfcEvent event) {
		NFCPMessage sendMsg = null;

		if (nfcpMessage == null) {
			// return null;

			// testing
			sendMsg = new NFCPMessage("TE","01","0", "1","0", "Anna");

		} else if (nfcpMessage.getType().equals("1")) {

			// skicka typ 2 meddelande

			sendMsg = new NFCPMessage("TE", "01", "0","2","0", "Anna");

		} else if (nfcpMessage.getType().equals("2")) {
			// testkod för att ta emot meddelande typ 2

			// skicka meddelande typ 3
			sendMsg = new NFCPMessage("TE", "01", "0", "3","0", "Anna");

		} else if (nfcpMessage.getType().equals("3")) {
			// att göra när vi tar emot meddelande typ 3
			return null;

		} else {
			// allt går fel
		}

		
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(Charset.forName("US-ASCII")),
				new byte[0], sendMsg.toString().getBytes(
						Charset.forName("US-ASCII")));
		return new NdefMessage(new NdefRecord[] { record });
	}

	/**
	 * Gets the latest NFC message and return it as a string
	 * 
	 * @param intent
	 * @return
	 */
	public String getLastestNFCMessage(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		return latestRecievedMsg = new String(msg.getRecords()[0].getPayload());
		// record 0 contains the MIME type, record 1 is the AAR, if present
	}

	/**
	 * Sets string s into debugging messagefield on the first screen.
	 * 
	 * @param s
	 */
	public void setMessage(String s) {
		// Toast.makeText(this, latestRecievedMsg, Toast.LENGTH_LONG).show();
		TextView view = (TextView) findViewById(R.id.message);
		view.setText(s + "\n");
	}
}