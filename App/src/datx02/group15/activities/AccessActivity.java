package datx02.group15.activities;


import kandidat.nfc.nfcapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This activity is called from main if a successful unlock has been done.
 * Only shown in a while.
 * @author Fredrik
 *
 */
public class AccessActivity extends Activity {
	
	 //stopping splash screen starting home activity.  
    private final int NOSPLASH = 0;  
    //time duration in millisecond for which your splash screen should visible to  
      //user.  
    private final int SPLASHTIME = 2500;  
  
    //handler for splash screen. Should NOT be static
    private Handler splashHandler = new Handler() {  
         @Override  
         public void handleMessage(Message msg) {  
              if(msg.what == NOSPLASH){ 
                    AccessActivity.this.finish();
              }
              super.handleMessage(msg);  
         }  
    };  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_access);
		//Generating message and sending it to splash handle   
        Message msg = new Message();  
        msg.what = NOSPLASH;  
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
	}
}
