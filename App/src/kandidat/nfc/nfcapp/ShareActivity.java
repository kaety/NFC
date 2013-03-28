package kandidat.nfc.nfcapp;

import java.nio.charset.Charset;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ShareActivity extends Activity implements CreateNdefMessageCallback {

	private String doorId;
	private String key;
	NfcAdapter nfcAdapter;
	
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
	}


	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {

		NFCPMessage sendMsg = new NFCPMessage(doorId.substring(0,2), doorId.substring(2),
				NFCPMessage.STATUS_OK, NFCPMessage.MESSAGE_TYPE_SHARE,NFCPMessage.ERROR_NONE, key);
		
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(Charset.forName("US-ASCII")),
				new byte[0], sendMsg.toString().getBytes(
						Charset.forName("US-ASCII")));
		return new NdefMessage(new NdefRecord[] { record });
	}

}
