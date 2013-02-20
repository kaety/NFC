package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
	
	public void changePassword(View view){
		EditText e1 = (EditText) findViewById(R.id.passwordCreate);
		EditText e2 = (EditText) findViewById(R.id.passwordConfirm);
		int password = Integer.parseInt(e1.getText().toString());
		if(password == Integer.parseInt(e2.getText().toString()) ){
			Editor editor = getSharedPreferences("password", 0).edit();
			editor.putString("password", password + "");
			editor.commit();
		}
	
	}

}
