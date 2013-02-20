package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	int tries =3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Called when the user clicks the Unlock button
	 * 
	 * @throws InterruptedException
	 */
	public void unlockpressed(View view) throws InterruptedException {

		Toast toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

		EditText view1 = (EditText) findViewById(R.id.editText1);
		if (Integer.parseInt(view1.getText().toString()) == 1234) {
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
			
		} else {
			if (tries == 0) {
				toast.setText("NO MORE TRIES");
				finish();

			}else{
				toast.setText((tries + " tries left").toString());
				tries--;
				
			}
			toast.show();
		}
	}

}
