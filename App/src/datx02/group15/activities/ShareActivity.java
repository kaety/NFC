package datx02.group15.activities;

import java.nio.charset.Charset;

import datx02.group15.communication.NFCPMessage;

import kandidat.nfc.nfcapp.R;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;


/**
 * This activity is started from KeysActivity and it is intended to share a
 * specific key. This can be done either secure or not. If it is supposed to be
 * done securely to sending phone must be in controll of the other phones public
 * RSA-key
 * 
 * @author Fredrik
 */
public class ShareActivity extends Activity implements
		CreateNdefMessageCallback {

	// The four bytes unique identifier for a door
	private String doorId;
	// The four bytes key for the door
	private String unlockId;
	// Communication object
	private NfcAdapter nfcAdapter;
	// The receiving phones public key

	private NFCPMessage sendMsg;

	@Override
	/**
	 * Gets the information from calling intent such as doorId, key, 
	 * also gets reference for communication and finds the public key if there is any
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Gets the NFCAdapter and prepares class for beaming NDEF
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.setNdefPushMessageCallback(this, this);

		if (sendMsg == null) {
			prepareNdefMessage();
		}
	}

	/**
	 * Called from oncreate the prepare the message when the activity is created
	 * instead of when it is needed
	 */
	public void prepareNdefMessage() {
		// Getting information
		Intent intent = getIntent();
		doorId = intent.getStringExtra("doorId");
		unlockId = intent.getStringExtra("key");

		// Using our protocol
		sendMsg = new NFCPMessage(doorId.substring(0, 2), doorId.substring(2),
				NFCPMessage.STATUS_OK, NFCPMessage.MESSAGE_TYPE_SHARE,
				NFCPMessage.ERROR_NONE, unlockId);
	}

	/**
	 * Automatic call. Creates an NDEF and encrypts the unlockId if there is a
	 * public key found
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {
		// Create record
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(Charset.forName("US-ASCII")),
				new byte[0], sendMsg.toString().getBytes(
						Charset.forName("US-ASCII")));
		return new NdefMessage(new NdefRecord[] { record });
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's state
		if (doorId != null && unlockId != null) {
			savedInstanceState.putString("doorId", doorId);
			savedInstanceState.putString("unlockId", unlockId);
		}
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);
		if (doorId == null && unlockId == null) {
			// Restore state members from saved instance
			doorId = savedInstanceState.getString("doorId");
			unlockId = savedInstanceState.getString("unlockId");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			Intent parentActivityIntent = new Intent(this, KeysActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
