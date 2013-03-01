package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);

		EditText oldPassword = (EditText) findViewById(R.id.oldpassword);

		SharedPreferences pref = getSharedPreferences("password", 1);
		String storedpw = pref.getString("password", "");

		// If we don't have a saved password we don't want to show
		// "typ in old pw"
		if (storedpw.equals("")) {
			oldPassword.setVisibility(View.GONE);
		}
	}

	/**
	 * Called when the user press the "Create new Password" Button
	 * 
	 * @param view
	 */
	public void changePassword(View view) {
		Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		EditText e1 = (EditText) findViewById(R.id.passwordCreate);
		EditText e2 = (EditText) findViewById(R.id.passwordConfirm);
		EditText e3 = (EditText) findViewById(R.id.oldpassword);

		String pw1 = e1.getText().toString();
		String pw2 = e2.getText().toString();

		if (e3.isShown()) {
			SharedPreferences pref = getSharedPreferences("password", 1);
			String storedpw = pref.getString("password", "");
			String stringpw3 = e3.getText().toString();
			if (stringpw3.equals("") || pw1.equals("") || pw2.equals("")) {
				toast.setText("Empty field(s)");
				toast.show();
			} else if (!stringpw3.equals(storedpw)) {
				toast.setText("Wrong old password");
				toast.show();
			} else if (pw1.equals(pw2)) {
				if (pw1.length() < 4) {
					toast.setText("Password to short, need to be atleast 4 digits");
					toast.show();
				} else {
					setNewPassword(pw1);
				}
			} else {
				toast.setText("Passwords didn't match");
				toast.show();
			}
		} else {

			// Check for empty fields
			if (pw1.equals("") || pw2.equals("")) {
				toast.setText("Empty field(s)");
				toast.show();
			} else {

				// passwords match
				if (pw1.equals(pw2)) {

					// password too short?
					if (pw1.length() < 4) {
						toast.setText("Password to short, need to be atleast 4 digits");
						toast.show();

					} else {
						// Save new password
						setNewPassword(pw1);
						// Go back to SettingsActivity/Login
						Bundle extra = getIntent().getExtras();
						// if we came from LoginActivity extra will not be null
						if (extra != null) {
							String s = extra.getString("Key");
							// if check not necessary but we keep it for
							// "safety check"
							if (s.equals("Value")) {
								startActivity(new Intent(this,
										LoginActivity.class));
							}
						}
						finish();
					}
					// Passwords didn't match
				} else {
					toast.setText("Passwords didn't match");
					toast.show();
				}

			}
		}
		// Clear textfields
		e1.setText("");
		e2.setText("");
		if (e3.isShown()) {
			e3.setText("");
		}
		// get cursor to first textfield
		e1.requestFocus();
	}

	void setNewPassword(String password) {
		Editor editor = getSharedPreferences("password", 0).edit();
		editor.putString("password", password);
		editor.commit();
	}

}
