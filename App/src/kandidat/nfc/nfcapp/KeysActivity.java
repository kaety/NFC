package kandidat.nfc.nfcapp;

import java.util.Map;

import kandidat.nfc.nfcapp.KeysDialogCreate.DialogCreateInterface;
import kandidat.nfc.nfcapp.KeysDialogDelete.DialogDeleteInterface;
import kandidat.nfc.nfcapp.KeysDialogSearch.DialogSearchInterface;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
//import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Handles the Database of keys.
 * Sharing, Creating, Changing
 * @author Fredrik
 *
 */
public class KeysActivity extends Activity implements DialogDeleteInterface, DialogCreateInterface, DialogSearchInterface {

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

		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getting a new DAO and prepares it
		dao = new DAO(this);
		dao.open();

		//Getting the loggingView
		loggerTextView =(TextView) findViewById(R.id.textView1);

		//Populate the linear layout in the scrollView
//		LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearlayout1);
		Map<String,String> map = dao.getAll();
		RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

		RadioButton rd;
		for (Map.Entry<String, String> entry : map.entrySet()){
			rd = new RadioButton(this);
			rd.setText(entry.getKey() + " || " + entry.getValue());
			rg.addView(rd);

		}


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
	public void createOrChange(String lockID, String unlockID){

//		String lockID = getLockId();
//		String unlockID = getUnlockId();
//		Check if all fields have proper length
		if(lockID.length() != 4){
//			loggerTextView.setText("lockId should be a four character String");
			Toast.makeText(this, "Door should be four characters long", Toast.LENGTH_SHORT).show();
		}else if(unlockID.length() != 4){
//			loggerTextView.setText("unlockId should be a four character String");
			Toast.makeText(this, "Key should be four characters long", Toast.LENGTH_SHORT).show();
		}else{
			dao.insertOrUpdate(lockID,unlockID);
//			loggerTextView.setText("Keypair stored");
			Toast.makeText(this, "Keypair stored", Toast.LENGTH_SHORT).show();
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
	public void search(String unlockId){

		unlockId = dao.get(unlockId);
		if (unlockId != null){
//			loggerTextView.setText("The key is:\n" + unlockId);
			Toast.makeText(this, "The key is: " + unlockId, Toast.LENGTH_LONG).show();
		}else{
//			loggerTextView.setText("The key is not found");
			Toast.makeText(this, "The key is not found", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Called from button.
	 * Just cancels...
	 * @param v
	 */
	private void delete(){
		RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
		RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
		//Check if a radiobutton was checked
		if(rb != null){
			String lockId = rb.getText().subSequence(0, 4).toString();
			rg.removeView(rb);
			if (lockId.length() == 4){
				dao.delete(lockId);
			}else{
				Toast.makeText(this, "UnlockId has to be for characters", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Called from button.
	 * Puts doorName and key in an intent and send to a new activity for sharing via Android Beam.
	 * @param v
	 */
	public void share(){
		RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
		RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
		//Check if a RadioButton was checked
		if(rb != null){
			String lockId = rb.getText().subSequence(0, 4).toString();

			if(lockId.length() == 4){

				Intent intent = new Intent(this ,ShareActivity.class);
				intent.putExtra("doorId",lockId);
				String unlockId = dao.get(lockId);
				if(unlockId != null){
					intent.putExtra("key",unlockId);
					startActivity(intent);
				}else{
//					loggerTextView.setText("Key not found. You can't share a key that does not exist!");
					Toast.makeText(this,"Key not found. You can't share a key that does not exist!",Toast.LENGTH_SHORT).show();
				}

			}else{
				loggerTextView.setText("You must supply a four character long doorId");
				Toast.makeText(this, "You must supply a four character long doorId",Toast.LENGTH_SHORT ).show();
			}
		}else{
			Toast.makeText(this, "You have to choose a key",Toast.LENGTH_LONG ).show();
		}

	}
	/**
	 * Extracting info from field to
	 * @return
	 */
//	private String getUnlockId(){
//		EditText e1 = (EditText) findViewById(R.id.editText2);
//		return e1.getText().toString();
//	}
//	/**
//	 * Extracting info from field 1
//	 * @return
//	 */
//	private String getLockId(){
//		EditText e1 = (EditText) findViewById(R.id.editText1);
//		return e1.getText().toString();
//	}


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
			showCreateDialog();
			return true;
		case R.id.keys_delete:
			RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
			RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
			if(rb != null){
				showDeleteDialog();
			}else{
				Toast.makeText(this, "You have to choose a key to delete", Toast.LENGTH_LONG).show();
			}
			return true;
		case R.id.keys_search:
			showSearchDialog();
			return true;
		case R.id.keys_share:
			share();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void showCreateDialog() {
	    DialogFragment newFragment = new KeysDialogCreate();
	    newFragment.show(getFragmentManager(), "Create");
	}
	public void showDeleteDialog() {
	    DialogFragment newFragment = new KeysDialogDelete();
	    newFragment.show(getFragmentManager(), "Delete");
	}
	public void showSearchDialog() {
	    DialogFragment newFragment = new KeysDialogSearch();
	    newFragment.show(getFragmentManager(), "Search");
	}
	@Override
	public void onDialogCreatePositiveClick(DialogFragment dialog, String door, String key) {
		createOrChange(door,key);
		dialog.dismiss();
	}
	@Override
	public void onDialogCreateNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}
	@Override
	public void onDialogDeletePositiveClick(DialogFragment dialog) {
		delete();
		dialog.dismiss();
		Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onDialogDeleteNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
		
	}

	@Override
	public void onDialogSearchPositiveClick(DialogFragment dialog, String door) {
		search(door);
		dialog.dismiss();
	}

	@Override
	public void onDialogSearchNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}

}
