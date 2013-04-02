package kandidat.nfc.nfcapp;

import java.nio.charset.Charset;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.nfc.security.Krypto;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
/**
 * This activity is started from KeysActivity and it is intended to share a specific key.
 * This can be done either secure or not.
 * If it is supposed to be done securely to sending phone must be in controll of the other phones public RSA-key
 * @author Fredrik
 */
public class ShareActivity extends Activity implements CreateNdefMessageCallback {

	//The four bytes unique identifier for a door
	private String doorId;
	//The four bytes key for the door
	private String key;
	//Communication object
	private NfcAdapter nfcAdapter;
	//The receiving phones public key
	private String publicKey;
	
	@Override
	/**
	 * Gets the information from calling intent such as doorId, key, 
	 * also gets reference for communication and finds the public key if there is any
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		// Getting information
		Intent intent = getIntent();
		doorId = intent.getStringExtra("doorId");
		key = intent.getStringExtra("key");
		
		//Gets the NFCAdapter and prepares class for beaming NDEF
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.setNdefPushMessageCallback(this, this);
		
		//Getting public key from shared prefs if it exist else null
		SharedPreferences pref = this.getSharedPreferences("publicKey", 1);
		publicKey = pref.getString("publicKey", null);
	}

	/**
	 * Automatic call.
	 * Creates an NDEF and encrypts the unlockId if there is a public key found
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {
		//Using our protocol
		NFCPMessage sendMsg = new NFCPMessage(doorId.substring(0,2), doorId.substring(2),
				NFCPMessage.STATUS_OK, NFCPMessage.MESSAGE_TYPE_SHARE, NFCPMessage.ERROR_NONE, key);
		
		//if public key is found encrypt unlockId and put it back
		if (publicKey != null){
			String unlockId = sendMsg.getUnlockId();
			Krypto krypto = new Krypto(publicKey);
			unlockId = krypto.encryptMessage(unlockId);
			sendMsg.setUnlockId(unlockId);
		}else{
			sendMsg.setErrorCode(NFCPMessage.ERROR_NO_SECURITY);
		}
		//Create record
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(Charset.forName("US-ASCII")),
				new byte[0], sendMsg.toString().getBytes(
						Charset.forName("US-ASCII")));
		return new NdefMessage(new NdefRecord[] { record });
	}
}
