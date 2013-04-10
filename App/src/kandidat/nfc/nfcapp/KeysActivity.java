package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//TODO Delete needs to be added

/**
 * Handles the Database of keys.
 * Sharing, Creating, Changing
 * @author Fredrik
 *
 */
public class KeysActivity extends Activity {

	//Database Access Object
	private DAO dao;
	//An instance of the loggingwindows, under the buttons
	private TextView loggerTextView;
	
	/**
	 * Getting a new DAO and prepares it.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keys);
		
		//getting a new DAO and prepares it
		dao = new DAO(this);
		dao.open();
		
		//Getting the loggingView
		loggerTextView =(TextView) findViewById(R.id.textView1);
		
	}
	
	/**
	 * Closes connection to database.
	 */
	@Override
	protected void onDestroy(){
		
		super.onDestroy();

		dao.close();
		
	}

	

	/**
	 * Called from Button.
	 * Creates or changes key for given door and key.
	 * @param v
	 */
	public void createOrChange(View v){
		
		String lockID = getLockId();
	    String unlockID = getUnlockId();
	    
	    //Check if all fields have proper length
	    if(lockID.length() != 4){
	    	
	    	loggerTextView.setText("lockId should be a four character String");
	    	
	    }else if(unlockID.length() != 4){
	    	
	    	loggerTextView.setText("unlockId should be a four character String");
	    	
	    }else{
	    	

	    	dao.insertOrUpdate(lockID,unlockID);
	    	loggerTextView.setText("Keypair stored");
	    	
	    }
	}
	/**
	 * Called from button.
	 * Searches and displays the key for chosen door.
	 * @param v
	 */
	public void search(View v){
		
		String unlockId = searchForUnlockId();
		if (unlockId != null){
			
			loggerTextView.setText("The key is:\n" + unlockId);
			
		}else{
			
			loggerTextView.setText("The key is not found");
			
		}
		
	}
	
	/**
	 * Called from button.
	 * Just cancels...
	 * @param v
	 */
	public void delete(View v){
		String lockId = getLockId();
		if (lockId.length() == 4){
			dao.delete(lockId);
		}else{
			Toast.makeText(this, "UnlockId has to be for characters", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * Called from button.
	 * Puts doorName and key in an intent and send to a new activity for sharing via Android Beam.
	 * @param v
	 */
	public void share(View v){
		
		String lockId = getLockId();
		if(lockId.length() == 4){
			
			Intent intent = new Intent(this ,ShareActivity.class);
			intent.putExtra("doorId",getLockId());
			String unlockId = searchForUnlockId();
			if(unlockId != null){
				intent.putExtra("key",unlockId);
				startActivity(intent);
			}else{
				loggerTextView.setText("Key not found. You can't share a key that does not exist!");
			}
			
		}else{
			loggerTextView.setText("You must supply a four character long doorId");
		}
		
	}
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Private method for extracting info from field and then serch for its unlockId in the Database.
	 * @return
	 */
	private String searchForUnlockId(){
		
		EditText e1 = (EditText) findViewById(R.id.editText1);
		String lockID = e1.getText().toString();
		return dao.get(lockID);
	
	}
	/**
	 * Extracting info from field to
	 * @return
	 */
	private String getUnlockId(){
		EditText e1 = (EditText) findViewById(R.id.editText2);
		return e1.getText().toString();
	}
	/**
	 * Extracting info from field 1
	 * @return
	 */
	private String getLockId(){
	
		EditText e1 = (EditText) findViewById(R.id.editText1);
		return e1.getText().toString();
	
	}
}
