package com.primesys.VehicalTracking.Activity;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;


public class InternetConnectionReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) &&
				!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
		{
			return;
		}

		ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

		if (cm == null) {
			return;
		}

	}
}
