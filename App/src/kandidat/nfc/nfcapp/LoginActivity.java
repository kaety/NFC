package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private int tries = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		SharedPreferences pref = getSharedPreferences("password", 1);
		String password = pref.getString("password", "");
		
		//first time you use the app
		if (password.equals("")) {
			
			Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setText("Please choose a password");
			toast.show();
			
			startActivity(new Intent(this, PasswordActivity.class));
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Called when the user clicks the Unlock button
	 * Checks if user type in correct or incorrect password
	 * 
	 * @throws InterruptedException //do we need this??
	 */
	public void unlockpressed(View view) throws InterruptedException {

		Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

		EditText edit = (EditText) findViewById(R.id.editText1);

		String input = edit.getText().toString();

		if (input.equals("")) {
			toast.setText("Please type in a password...");
			toast.show();
		}

		else {
			// Get the stored password from SharedPreferences
			SharedPreferences pref = getSharedPreferences("password", 1);
			String storedpw = pref.getString("password", "");
			// compare stored password with "inputpassword"
			if (Integer.parseInt(input) == Integer.parseInt(storedpw)) {
				startActivity(new Intent(this, MainActivity.class));
				this.finish();

			} else {
				edit.setText("");
				if (tries == 0) {
					toast.setText("NO MORE TRIES");
					//bra vibrationer
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
				    vib.vibrate(1000);
					finish();
					finish();

				} else {
					toast.setText("Wrong Password: "+tries + " tries left");
					tries--;

				}

				toast.show();
				
			}
		}
	}

}
