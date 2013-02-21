package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	/**
	 * Called when the user press the "Create new Password" Button
	 * @param view
	 */
	public void changePassword(View view) {
		EditText e1 = (EditText) findViewById(R.id.passwordCreate);
		EditText e2 = (EditText) findViewById(R.id.passwordConfirm);

		String stringpw1 = e1.getText().toString();
		String stringpw2 = e2.getText().toString();

		// Check for empty fields
		if (stringpw1.equals("") || stringpw2.equals("")) {
			Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setText("Empty field(s)");
			toast.show();

		} else {

			int password = Integer.parseInt(stringpw1);
			//Correct new password
			if (password == Integer.parseInt(stringpw2)) {
				
				//Save new password
				Editor editor = getSharedPreferences("password", 0).edit();
				editor.putString("password", password + "");
				editor.commit();
				
				// Go back to MainActivity
				this.finish();
				
			//Passwords didn't match	
			} else {
				Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setText("Passwords didn't match");
				toast.show();

			}

		}
		//Clear textfields
		e1.setText("");
		e2.setText("");
		//TODO get cursor to first textfield
	}	
		
		
		
	
	

}
