package kandidat.nfc.nfcapp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

	private static final int RADIO_GROUP_ID = 999;

	/**
	 * Getting a new DAO and prepares it.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keys);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getting a new DAO and prepares it
		dao = new DAO(this);
		dao.open();

		//Getting the loggingView
		loggerTextView =(TextView) findViewById(R.id.textView1);

		//Populate the linear layout in the scrollView
		LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearlayout1);
		Map<String,String> map = dao.getAll();
		RadioGroup rg = new RadioGroup(this);
		rg.setId(RADIO_GROUP_ID);

		RadioButton rd;
		for (Map.Entry<String, String> entry : map.entrySet()){
			rd = new RadioButton(this);
			rd.setText(entry.getKey() + "     /    " + entry.getValue());
			rg.addView(rd);

		}
		linearlayout.addView(rg);


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
		//Restart to redraw
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	/**
	 * Called from button.
	 * Searches and displays the key for chosen door.
	 * @param v
	 */
	public void search(View v){

		String unlockId = dao.get(getLockId());
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
//		new dialogDeleteFragment();
		delete();
	}
	
	private void delete(){
		RadioGroup rg = (RadioGroup) findViewById(RADIO_GROUP_ID);
		RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
		String lockId = rb.getText().subSequence(0, 4).toString();
		rg.removeView(rb);
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
		RadioGroup rg = (RadioGroup) findViewById(RADIO_GROUP_ID);
		RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
		String lockId = rb.getText().subSequence(0, 4).toString();

		if(lockId.length() == 4){

			Intent intent = new Intent(this ,ShareActivity.class);
			intent.putExtra("doorId",lockId);
			String unlockId = dao.get(lockId);
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.keys_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.keys_create:
			return true;
		case R.id.keys_delete:
			delete();
			return true;
		case R.id.keys_search:
			return true;
		case R.id.keys_share:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
