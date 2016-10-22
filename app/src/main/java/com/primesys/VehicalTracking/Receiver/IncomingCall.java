package com.primesys.VehicalTracking.Receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCall extends BroadcastReceiver {
    private Context pcontext;

    public void onReceive(Context context, Intent intent) {
        
    try {
    	this.pcontext=context;
               // TELEPHONY MANAGER class object to register one listner
                TelephonyManager tmgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                        
                //Create Listner
                MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
                
                // Register listener for LISTEN_CALL_STATE
                tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {


		public void onCallStateChanged(int state, String incomingNumber) {
        
            Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);

            if (state == 1) {

             /*   String msg = "New Phone Call Event. Incomming Number : "+incomingNumber;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(pcontext, msg, duration);
                toast.show();*/
            	/*if (incomingNumber.contains(Voice_Servilence.devicedata.getDeviceSimNumber())) {
            		if(Voice_Servilence.pDialogmain.isShowing())
            			Voice_Servilence.pDialogmain.dismiss();
            			Voice_Servilence.closeActivity();

            		
					
				}
            	*/
            }
        }
    }

	
}
 