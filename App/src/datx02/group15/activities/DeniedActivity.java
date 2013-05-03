package datx02.group15.activities;

import datx02.group15.communication.NFCPMessage;
import kandidat.nfc.nfcapp.R;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

/**
 * Called when you have no access or something goes wrong.
 * @author Fredrik
 *
 */
public class DeniedActivity extends Activity {
	
	/**
	 * Need to add more cases here if we add more Error codes
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_denied);
		
		Bundle extras = getIntent().getExtras();
		TextView view = (TextView) findViewById(R.id.errorLabel);
		int code = extras.getInt("ErrorCode");
		 
		
		switch (code) {
			case NFCPMessage.INT_ERROR_NONE:
				//All is okay
				break;
         	case NFCPMessage.INT_ERROR_NO_SECURITY:  
         		view.setText("No security exists.");
         		break;
         	default:
         		;
		 }
         		
	}

}
