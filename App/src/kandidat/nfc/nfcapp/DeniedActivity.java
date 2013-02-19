package kandidat.nfc.nfcapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class DeniedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denied);
		
		Bundle extras = getIntent().getExtras();
		TextView view = (TextView) findViewById(R.id.errorLabel);
		int code = extras.getInt("ErrorCode");
		 
		
		switch (code) {  
         	case 1:  
         		view.setText("Fel 1 Description:blablabla osv");
         		break;
         	case 2:
         		view.setText("Fel 2 Description:blablabla osv");
         		break;
         	case 3:
         		view.setText("Fel 3 Description:blablabla osv");
         		break;
         	case 4:
         		view.setText("Fel 4 Description:blablabla osv");
         		break;
		 }
         		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_denied, menu);
		return true;
	}

	
}
