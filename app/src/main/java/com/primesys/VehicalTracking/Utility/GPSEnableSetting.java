package com.primesys.VehicalTracking.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class GPSEnableSetting implements ConnectionCallbacks,LocationListener, OnConnectionFailedListener  {



	public void GPSDialog(Context context){
		final Context gpsContext=context;
		//to Alert 
		GoogleApiClient	 googleApiClient = null;
		if (googleApiClient == null) {    
			googleApiClient = new GoogleApiClient.Builder(gpsContext)
			.addApi(LocationServices.API)
			.addConnectionCallbacks(this) 
			.addOnConnectionFailedListener(this).build(); 
			googleApiClient.connect(); 
			LocationRequest locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); 
			locationRequest.setInterval(30 * 1000);    
			locationRequest.setFastestInterval(5 * 1000);
			LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
			.addLocationRequest(locationRequest);       
			builder.setAlwaysShow(true);  
			PendingResult<LocationSettingsResult> result =
					LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());



			result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
				@Override   
				public void onResult(LocationSettingsResult result) {    
					final Status status = result.getStatus(); 
					final LocationSettingsStates state = result.getLocationSettingsStates();
					switch (status.getStatusCode()) {   
					case LocationSettingsStatusCodes.SUCCESS:

						break;      
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						try {                            
							status.startResolutionForResult(
									(Activity) gpsContext, 1000);        
						} catch (IntentSender.SendIntentException e) {  


						} 
						break;  
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						break;                     }    
				}             });   
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

}
