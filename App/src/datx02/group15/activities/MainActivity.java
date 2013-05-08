package datx02.group15.activities;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import kandidat.nfc.nfcapp.R;

import datx02.group15.communication.NFCPMessage;
import datx02.group15.database.DataAccessObject;
import datx02.group15.security.RSAHandler;


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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
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
	//Timer constant and the time user logged in
	private final long TIMEOUT = 300 *1000;//GONE IN 60seconds
	private Long loginTime;
	// Object representing the NFC adapter
	private NfcAdapter nfcAdapter;
	//DAO used to access database
	private DataAccessObject dataAccessObject;
	private RSAHandler receivedRSAHandler;
	

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
		dataAccessObject = new DataAccessObject(this);
		dataAccessObject.open();
		
		//Insert values to use with test
		dataAccessObject.insertOrUpdate(NFCPMessage.TEST_UNIQUEID, NFCPMessage.TEST_UNLOCKID);;

	    try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
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
		SharedPreferences pre = getSharedPreferences("login", 1);
		loginTime = pre.getLong("login", 0L);
		//The following is because we don't want to be able to receive messages
		//if we are not logged in
		if((System.currentTimeMillis()-loginTime) > TIMEOUT){
			startActivity(new Intent(this,LoginActivity.class));
			Toast.makeText(this, "TIMEOUT: PLEASE LOG IN AGAIN", Toast.LENGTH_LONG).show();
			finish();
		}else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
				processIntent(getIntent());
		}
	}
	/**
	 * Don't delete this. This i actually used even if it not looks like that
	 */
	@Override
	public void onNewIntent(Intent intent){
		setIntent(intent);
	}

	/**
	 * Receives NFC-intents and displays them in view. Called from above.
	 * @param intent
	 */
	void processIntent(Intent intent) {
			latestRecievedMsg = getLastestNFCMessage(intent);
			Logger.getAnonymousLogger().log(Level.SEVERE,latestRecievedMsg);
			latestReceivedNFCPMessage = new NFCPMessage(latestRecievedMsg);
			showMessageInTextView(latestReceivedNFCPMessage);
			
			String status = latestReceivedNFCPMessage.getStatus();
			String type = latestReceivedNFCPMessage.getType();
			
			if(type.equals(NFCPMessage.MESSAGE_TYPE_RESULT)){
				SharedPreferences pref = getSharedPreferences(SettingsActivity.newUnlockId, Context.MODE_PRIVATE);
				String change = pref.getString(SettingsActivity.newUnlockId, "");
				
				if (status.equals(NFCPMessage.STATUS_OK)) {
					Editor editor = pref.edit();
					editor.putString(SettingsActivity.newUnlockId, "");
					editor.commit();
					dataAccessObject.insertOrUpdate(latestReceivedNFCPMessage.getName()+latestReceivedNFCPMessage.getId(), change);
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
				    	dataAccessObject.insertOrUpdate(latestReceivedNFCPMessage.getUniqueId(), latestReceivedNFCPMessage.getUnlockId());
				    	}
				    }
				)
				 .setNegativeButton(android.R.string.no, null).show();
				//insert into Database
				
			}else if(type.equals(NFCPMessage.MESSAGE_TYPE_BEACON)){
				if (!latestReceivedNFCPMessage.getPublicKey().equals("")){
					receivedRSAHandler = new RSAHandler(latestReceivedNFCPMessage.getPublicKey());
					//Save latest received publicKey to encrypt with
					Editor editor = getSharedPreferences("publicKey", 0).edit();
					editor.putString("publicKey", latestReceivedNFCPMessage.getPublicKey());
					editor.commit();
				}
		
			}
			getIntent().setAction("");
	}

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * NDEF message handler
	 * much debug here. In future add cases and delete cases.
	 */
	public NdefMessage createNdefMessage(NfcEvent event) {
		NFCPMessage sendMsg = null;

		//If no message has been received.
		if (latestReceivedNFCPMessage == null) { 
			return null;
		}else if (latestReceivedNFCPMessage.getType().equals(NFCPMessage.MESSAGE_TYPE_BEACON)) {
			
			//Check if key is in database
			String unlockId = dataAccessObject.get(latestReceivedNFCPMessage.getUniqueId());
			if(unlockId != null){ //If a key exists in the Database
				SharedPreferences pref = getSharedPreferences(SettingsActivity.newUnlockId, Context.MODE_PRIVATE);
				String change = pref.getString(SettingsActivity.newUnlockId, "");
				if(change == ""){
					if(!latestReceivedNFCPMessage.getPublicKey().equals("")){
						String encryptedMessage = receivedRSAHandler.encryptMessage(latestReceivedNFCPMessage.getRandomMsg()+unlockId);
	
						sendMsg = new NFCPMessage(latestReceivedNFCPMessage.getName(), latestReceivedNFCPMessage.getId(),
								NFCPMessage.STATUS_OK,NFCPMessage.MESSAGE_TYPE_UNLOCK,NFCPMessage.ERROR_NONE,
								encryptedMessage);
	
					}else{
						runOnUiThread(new Runnable() {
				            @Override
				            public void run() {
				            	Toast.makeText(MainActivity.this,"Error. No encryption.",Toast.LENGTH_LONG).show();
				            }
				        });
						return null;
					}
				}else{
					if(!latestReceivedNFCPMessage.getPublicKey().equals("")){
						String encryptedMessage = receivedRSAHandler.encryptMessage(latestReceivedNFCPMessage.getRandomMsg()+unlockId+change);
	
						sendMsg = new NFCPMessage(latestReceivedNFCPMessage.getName(), latestReceivedNFCPMessage.getId(),
								NFCPMessage.STATUS_OK,NFCPMessage.MESSAGE_TYPE_CONFIG,NFCPMessage.ERROR_NONE,
								encryptedMessage);
					}else{
						runOnUiThread(new Runnable() {
				            @Override
				            public void run() {
				            	Toast.makeText(MainActivity.this,"Error. No encryption.",Toast.LENGTH_LONG).show();
				            }
				        });
						return null;
					}
				}
			}else{
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
		} else if (latestReceivedNFCPMessage.getType().equals(NFCPMessage.MESSAGE_TYPE_UNLOCK)) {
			//This should never happen.
			return null;
		} else if (latestReceivedNFCPMessage.getType().equals(NFCPMessage.MESSAGE_TYPE_RESULT)) { 
			//Nothing should be sent
			return null;
		} else {
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
		return new String(msg.getRecords()[0].getPayload());
		// record 0 contains the MIME type, record 1 is the AAR, if present
	}

	/**
	 * Sets string s into debugging messagefield on the first screen.
	 * 
	 * @param s
	 */
	public void showMessageInTextView(NFCPMessage message) {
		
		TextView view = (TextView) findViewById(R.id.message);
		view.setText(message.getRelevantUserOutput());
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's state
		if (receivedRSAHandler != null){
			savedInstanceState.putSerializable("KRYPTO", receivedRSAHandler);
		}
	
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    receivedRSAHandler = (RSAHandler) savedInstanceState.getSerializable("KRYPTO");
	}
	@Override
	public void onDestroy(){
		dataAccessObject.close();
		super.onDestroy();
	}
}