package com.primesys.VehicalTracking.Gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try{
			ComponentName comp = new ComponentName(context.getPackageName(),
					GCMNotificationIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);

		}catch (Exception e){
			e.printStackTrace();
		}


		/*	try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				context.startForegroundService(new Intent(context, GCMNotificationIntentService.class));
			} else {
				context.startService(new Intent(context, GCMNotificationIntentService.class));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		*/
	}
}
