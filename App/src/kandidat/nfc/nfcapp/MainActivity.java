package kandidat.nfc.nfcapp;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.nfc.security.Krypto;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Most complex class in the application and also the Main activity.
 * Handles next to all communication over NFC except sharing a key.
 * @author Fredrik
 *
 */
public class MainActivity extends Activity implements CreateNdefMessageCallback {

	//Every time a new Message is received it is put here
	private String latestRecievedMsg;
	
	private NFCPMessage latestReceivedNFCPMessage;
	private final long TIMEOUT = 60 *1000;//GONE IN 60seconds
	private Long loginTime;
	// Object representing the NFC adapter
	private NfcAdapter nfcAdapter;
	//DAO used to access database
	private DAO dao;
	private Krypto receivedKrypto;
	

	@SuppressLint("NewApi")
	@Override
	/**
	 * Force user to start NFC and Android Beam. Enables callback.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
		
		
		// Gets an instance of the adapter
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		// Check if the users NFC is on
		if (!nfcAdapter.isEnabled()) {
			//Open the settings to activate NFC
			startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
		}
		// The following is only allowed for API 16 or higher
		// LG L5 has version 15
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			if (!nfcAdapter.isNdefPushEnabled()) {
				//Activate Android Beam!
				startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
			}
		}
		//Activates callback for this method to send NFC message
		nfcAdapter.setNdefPushMessageCallback(this, this);
		
		//Create a new instance of DAO and open it 
		dao = new DAO(this);
		dao.open();
		
		//Insert values to use with test
		dao.insertOrUpdate(NFCPMessage.TEST_UNIQUEID, NFCPMessage.TEST_UNLOCKID);
		dao.insertOrUpdate("AAAA", "aaaa");
		dao.insertOrUpdate("BBBB", "bbbb");
		dao.insertOrUpdate("CCCC", "cccc");
		dao.insertOrUpdate("DDDD", "dddd");
		dao.insertOrUpdate("EEEE", "eeee");
		dao.insertOrUpdate("FFFF", "ffff");
		dao.insertOrUpdate("GGGG", "gggg");
		dao.insertOrUpdate("HHHH", "hhhh");
		dao.insertOrUpdate("IIII", "iiii");
		dao.insertOrUpdate("JJJJ", "jjjj");
		
		//Only create a instance of the Krypto class if there is none 

	    try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }

	}

	/**
	 *  Called when the user clicks the Settings button 
	 *  
	 */
	public void settingsAct(View view) {
		startActivity(new Intent(this, SettingsActivity.class));
	}

	/**
	 * Because we don't want anything to happen when we click options button.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.main_config:
	    	startActivity(new Intent(this, SettingsActivity.class));
	      break;
	    default:
	      break;
	    }
	    return true;
	  } 


	/**
	 * Get LoginTime. Check if you have to login again.
	 */
	@Override
	public void onResume() {
		super.onResume();

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
	 * Don't delete this. This i actually used even if it not looks like that
	 */
	@Override
	public void onNewIntent(Intent intent){
		//This is not innocent if you would think so!
		setIntent(intent);
	}

	/**
	 * Receives NFC-intents and displays them in view. Called from above.
	 * 
	 * 
	 * @param intent
	 */
	void processIntent(Intent intent) {
			setMessage(getLastestNFCMessage(intent));
			latestReceivedNFCPMessage = new NFCPMessage(latestRecievedMsg);
			
			String status = latestReceivedNFCPMessage.getStatus();
			String type = latestReceivedNFCPMessage.getType();
			
			if(type.equals(NFCPMessage.MESSAGE_TYPE_RESULT)){
				
				if (status.equals(NFCPMessage.STATUS_OK)) {
				
					startActivity(new Intent(this,AccessActivity.class));
				
				} else if (status.equals(NFCPMessage.STATUS_ERROR)) {
					
					// NOT NFC ACCESS
					Intent deniedIntent = new Intent(this,DeniedActivity.class);
					deniedIntent.putExtra("ErrorCode", latestReceivedNFCPMessage.getErrorCode());
					startActivity(deniedIntent);
					latestReceivedNFCPMessage.clearAll();
					
				}
				
			}else if(type.equals(NFCPMessage.MESSAGE_TYPE_SHARE)){
				
				//getting errorCode to see if unlockId is encrypted or not
				String errorCode = latestReceivedNFCPMessage.getErrorCode();	
				
				//If there is encryption
				if(errorCode.equals(NFCPMessage.ERROR_NONE)){
					
					
				}
				
				
				//Ask before insert
				new AlertDialog.Builder(this)
				.setTitle("Confirm insert!")
				.setMessage("Do you really want to insert lockId: " +  latestReceivedNFCPMessage.getUniqueId() + " and unlockId: " + latestReceivedNFCPMessage.getUnlockId() + "?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	dao.insertOrUpdate(latestReceivedNFCPMessage.getUniqueId(), latestReceivedNFCPMessage.getUnlockId());
				    	}
				    }
				)
				 .setNegativeButton(android.R.string.no, null).show();
				//insert into Database
				
			}else if(type.equals(NFCPMessage.MESSAGE_TYPE_BEACON)){
				if (!latestReceivedNFCPMessage.getPublicKey().equals("")){
					receivedKrypto = new Krypto(latestReceivedNFCPMessage.getPublicKey());
					//Save latest received publicKey to encrypt with
					Editor editor = getSharedPreferences("publicKey", 0).edit();
					editor.putString("publicKey", latestReceivedNFCPMessage.getPublicKey());
					editor.commit();
				}
		
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

		//If no message has been received.
		if (latestReceivedNFCPMessage == null) { 
			
			
			sendMsg = new NFCPMessage(NFCPMessage.TEST_NAME,NFCPMessage.TEST_ID,NFCPMessage.STATUS_OK,
					NFCPMessage.MESSAGE_TYPE_BEACON,NFCPMessage.ERROR_NONE);

			
		} else if (latestReceivedNFCPMessage.getType().equals(NFCPMessage.MESSAGE_TYPE_BEACON)) {
			
			//Check if key is in database
			String unlockId = dao.get(latestReceivedNFCPMessage.getUniqueId());
			
			if(unlockId != null){ //If a key exists in the Database send unlock message(type 2)
				if(!latestReceivedNFCPMessage.getPublicKey().equals("")){
					
					String encryptedMessage = receivedKrypto.encryptMessage(unlockId+latestReceivedNFCPMessage.getRandomMsg());
					
					sendMsg = new NFCPMessage(latestReceivedNFCPMessage.getName(), latestReceivedNFCPMessage.getId(),
							NFCPMessage.STATUS_OK,NFCPMessage.MESSAGE_TYPE_UNLOCK,NFCPMessage.ERROR_NONE,
							encryptedMessage);
				}else{
					runOnUiThread(new Runnable() {
			            @Override
			            public void run() {
			            	Toast.makeText(MainActivity.this,"No encryption",
			            			Toast.LENGTH_LONG).show();
			            }
			        });
					//Kanske access denied
				}
				
			
			} else {
				//Can't toast in automatic handler so we have to run in UI-thread
				runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	Toast.makeText(MainActivity.this,"The Database contains no key for this door",
		            			Toast.LENGTH_LONG).show();
		            }
		        });
				return null;		
			
			}
			
		} else if (latestReceivedNFCPMessage.getType().equals(NFCPMessage.MESSAGE_TYPE_UNLOCK)) { //If message is of type 2(unlock command). Here only for debugging. Should be handled by controller.

			sendMsg = new NFCPMessage("TE", "01", NFCPMessage.STATUS_OK,
					NFCPMessage.MESSAGE_TYPE_RESULT,NFCPMessage.ERROR_NONE, "Anna");

		} else if (latestReceivedNFCPMessage.getType().equals(NFCPMessage.MESSAGE_TYPE_RESULT)) { //If latest received is of type 3 we should check the result
			
			return null;

		} else {			// Should never happen because there is only 3 types.
			
			//Can't toast in automatic handler so we have to run in UI-thread
			runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	Toast.makeText(MainActivity.this, "Illegal message type", Toast.LENGTH_SHORT).show();
	            }
	        });
			return null;
		
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

		TextView view = (TextView) findViewById(R.id.message);
		view.setText(s + "\n");
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's state
		if (receivedKrypto != null){
			savedInstanceState.putSerializable("KRYPTO", receivedKrypto);
		}
	
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    receivedKrypto = (Krypto) savedInstanceState.getSerializable("KRYPTO");
	}
	@Override
	public void onDestroy(){
		dao.close();
		super.onDestroy();
	}
}