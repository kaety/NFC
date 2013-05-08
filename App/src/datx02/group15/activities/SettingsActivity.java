package datx02.group15.activities;

import datx02.group15.dialogs.AdminDialog;
import datx02.group15.dialogs.AdminDialog.DialogAdminInterface;
import kandidat.nfc.nfcapp.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends Activity implements DialogAdminInterface {
	
	public static final String newUnlockId = "1234";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}

	
	/*
	 * Android put this here. Whatta fuck is it!?
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void toPassword(View view){
		startActivity(new Intent(this,PasswordActivity.class));
	}
	public void toKeys(View view){
		startActivity(new Intent(this,KeysActivity.class));
	}
	public void toAdmin(View view){
		DialogFragment newFragment = new AdminDialog();
	    newFragment.show(getFragmentManager(), "");
	}


	@Override
	public void onDialogAdminPositiveClick(DialogFragment dialog, String unlockId) {
		if(unlockId.length() == 4){
			Editor editor = getSharedPreferences(newUnlockId, 0).edit();
			editor.putString(newUnlockId, unlockId);
			editor.commit();
			Toast.makeText(this, unlockId, Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "It need to be exactly 4 characters long", Toast.LENGTH_LONG).show();
		}
		
	}


	@Override
	public void onDialogAdminNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}

}
