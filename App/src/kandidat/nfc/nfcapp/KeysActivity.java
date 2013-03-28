package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class KeysActivity extends Activity {

	private DAO dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keys);
		// Show the Up button in the action bar.
		dao = new DAO(this);
		dao.open();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		dao.close();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keys, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/////////////////////Buttons///////////////////////////////////////////////////////
	/**
	 * Creates or changes key for given door and key
	 * @param v
	 */
	public void createOrChange(View v){
		
		String lockID = getLockId();
	    String unlockID = getUnlockId();
	    //dao.insert creates or changes if already created
		dao.insert(lockID,unlockID);
		
	}
	/**
	 * Searches and displays the key for chosen door
	 * @param v
	 */
	public void search(View v){
		
		TextView tv =(TextView) findViewById(R.id.textView1);
		tv.setText("The key is:\n" + searchForUnlockId());
		
	}
	/**
	 * Just cancels...
	 * @param v
	 */
	public void finish(View v){
		
		finish();
		
	}
	/**
	 * puts doorName and key in an intent and send to a new activity for sharing via Android Beam
	 * @param v
	 */
	public void share(View v){
		
		Intent intent = new Intent(this ,ShareActivity.class);
		intent.putExtra("doorId",getLockId());
		intent.putExtra("key",searchForUnlockId());
		startActivity(intent);
		
	}
	////////////////////////////////////////////////////////////////////////////////////////
	public String searchForUnlockId(){
		
		EditText e1 = (EditText) findViewById(R.id.editText1);
		String lockID = e1.getText().toString();
		return dao.get(lockID);
	
	}
	
	public String getUnlockId(){
		EditText e1 = (EditText) findViewById(R.id.editText2);
		return e1.getText().toString();
	}
	
	public String getLockId(){
	
		EditText e1 = (EditText) findViewById(R.id.editText1);
		return e1.getText().toString();
	
	}
}
