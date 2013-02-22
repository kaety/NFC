package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private final int NUMBER_OF_TRIES = 3;
	private int tries = NUMBER_OF_TRIES-1;

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
			Intent i = new Intent(this, PasswordActivity.class);
			i.putExtra("Key","Value");
			startActivity(i);
			finish();
		}

	}


	/**
	 * Called when the user clicks the Unlock button
	 * Checks if user type in correct or incorrect password
	 * Calls the right activity
	 */
	public void unlockPressed(View view) {
		Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

		EditText passwordInput = (EditText) findViewById(R.id.editText1);

		String input = passwordInput.getText().toString();

		if (input.equals("")) {
			toast.setText("Please type in a password...");
			toast.show();
		}else {
			// Get the stored password from SharedPreferences
			SharedPreferences pref = getSharedPreferences("password", 1);
			String storedpw = pref.getString("password", "");
			
			// compare stored password with "inputpassword"
			if (Integer.parseInt(input) == Integer.parseInt(storedpw)) {
				startActivity(new Intent(this, MainActivity.class));
				this.finish();
			} else {
				passwordInput.setText("");
				if (tries == 0) {
					toast.setText("NO MORE TRIES");
					//vibrationer för att indikera fel(dåligt)
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
				    vib.vibrate(1000);
					finish();
				} else {
					toast.setText("Wrong Password!\n"+tries + " tries left.");
					tries--;
				}

				toast.show();
			}
		}
	}

}
