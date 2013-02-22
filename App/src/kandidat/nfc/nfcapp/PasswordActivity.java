package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
	}

	
	
	/**
	 * Called when the user press the "Create new Password" Button
	 * @param view
	 */
	public void changePassword(View view) {
		Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		
		EditText e1 = (EditText) findViewById(R.id.passwordCreate);
		EditText e2 = (EditText) findViewById(R.id.passwordConfirm);

		String stringpw1 = e1.getText().toString();
		String stringpw2 = e2.getText().toString();

		// Check for empty fields
		if (stringpw1.equals("") || stringpw2.equals("")) {
			
			toast.setText("Empty field(s)");
			toast.show();

		} else {

			int password = Integer.parseInt(stringpw1);
			
			//passwords match
			if (password == Integer.parseInt(stringpw2)) {
				
				//password too short?
				if(stringpw1.length() < 4){
					toast.setText("Password to short, need to be atleast 4 digits");
					toast.show();
					
				}
				else{
				
				//Save new password
				Editor editor = getSharedPreferences("password", 0).edit();
				editor.putString("password", password + "");
				editor.commit();
				
				// Go back to SettingsActivity/Login
				Bundle extra = getIntent().getExtras();
				//if we came from LoginActivity extra will not be null  
				if(extra !=null){
					String s= extra.getString("Key");
					//if check not necessary but we keep it for "safety check"
					if(s.equals("Value")){
					startActivity(new Intent(this,LoginActivity.class));
					}
				}
				finish();
				
				
				}
			//Passwords didn't match	
			} else {
				toast.setText("Passwords didn't match");
				toast.show();

			}

		}
		//Clear textfields
		e1.setText("");
		e2.setText("");
		//get cursor to first textfield
		e1.requestFocus();
	}
	
}
