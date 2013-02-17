package kandidat.nfc.nfcapp;

import android.os.*;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class AccessActivity extends Activity {

	
	//modified copypaste from the interwebs
	
	 //stopping splash screen starting home activity.  
    private static final int STOPSPLASH = 0;  
    //time duration in millisecond for which your splash screen should visible to  
      //user.  
    private static final long SPLASHTIME = 2500;  
  
    //handler for splash screen  
    private Handler splashHandler = new Handler() {  
         @Override  
         public void handleMessage(Message msg) {  
              switch (msg.what) {  
                case STOPSPLASH:  
                    
                    AccessActivity.this.finish();
                    break;  
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
        msg.what = STOPSPLASH;  
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_access, menu);
		return true;
	}

}
