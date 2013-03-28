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

public class ShareActivity extends Activity implements CreateNdefMessageCallback {

	private String doorId;
	private String key;
	private NfcAdapter nfcAdapter;
	private String publicKey = null;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		// Getting information
		Intent intent = getIntent();
		doorId = intent.getStringExtra("doorId");
		key = intent.getStringExtra("key");
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.setNdefPushMessageCallback(this, this);
		
		//Getting public key from shared prefs
		SharedPreferences pref = this.getSharedPreferences("publicKey", 1);
		publicKey = pref.getString("publicKey", "");
	}


	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {

		
		NFCPMessage sendMsg = new NFCPMessage(doorId.substring(0,2), doorId.substring(2),
				NFCPMessage.STATUS_OK, NFCPMessage.MESSAGE_TYPE_SHARE,NFCPMessage.ERROR_NONE, key);
		
		//if public key is found encrypt message
		if (publicKey != null){
			String unlockId = sendMsg.getUnlockId();
			Krypto krypto = new Krypto(publicKey);
			unlockId = krypto.encryptMessage(unlockId);
			sendMsg.setUnlockId(unlockId);
		}
		
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(Charset.forName("US-ASCII")),
				new byte[0], sendMsg.toString().getBytes(
						Charset.forName("US-ASCII")));
		return new NdefMessage(new NdefRecord[] { record });
	}

}
