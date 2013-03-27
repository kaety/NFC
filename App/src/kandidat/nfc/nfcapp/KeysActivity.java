package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
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
	public void createOrChange(View v){
		EditText e1 = (EditText) findViewById(R.id.editText1);
		EditText e2 = (EditText) findViewById(R.id.editText2);
		String lockID = e1.getText().toString();
	    String unlockID = e2.getText().toString();
		dao.insert(lockID,unlockID);
	}
	public void search(View v){
		EditText e1 = (EditText) findViewById(R.id.editText1);
		String lockID = e1.getText().toString();
		String s = dao.get(lockID);
		TextView tv =(TextView) findViewById(R.id.textView1);
		tv.setText("The key is:\n" + s);
	}
	public void finish(View v){
		finish();
	}
}
