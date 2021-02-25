package com.primesys.VehicalTracking.Gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeofenceShowSTudent;
import com.primesys.VehicalTracking.Dto.EmpTaskDTO;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;




public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static GeofenceDTO geofenceObj ;
	public SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	int playSound=0;


	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@SuppressLint("LongLogTag")
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		/*GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);
		try {
			sharedPreferences=getApplicationContext().getSharedPreferences("User_data",Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
			editor.commit();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				//sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				//sendNotification("Deleted messages on server: "+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				for (int i = 0; i < 3; i++) {
					Log.i(TAG,
							"Working... " + (i + 1) + "/5 @ "
									+ SystemClock.elapsedRealtime());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}

				}
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				Log.e(TAG, "Received======: " + extras.get(Common.MESSAGE_KEY).toString());

				if (extras.get(Common.MESSAGE_KEY).toString().contains(Common.MESSAGE_EMP_TASK_KEY)){
					*//* Received: Bundle[{message={"data":{"emp_car_id":"9664",
					"emp_user_id":"509559","address":"fdssssssssssssssssss"
					,"month":"2","user_id":"509558","year":"2018",
					"message_type":"emp_task","time":"05-Feb-2018 03:45 PM"
					,"day":"5"}}, google.sent_time=1517829704588,
					android.support.content.wakelockid=2,
					from=576180947590,
					 google.message_id=0:1517829704605866%f07b0c94f9fd7ecd}]*//*

					ParseGCM_Emp_Task_Message(extras.get(Common.MESSAGE_KEY).toString());

				}else {
					ParseGCM_Message(extras.get(Common.MESSAGE_KEY).toString());

				}
			*//*	sendNotification("Message Received from Google GCM Server: "
						+ extras.get(Common.MESSAGE_KEY));*//*
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);*/
	}

	private void ParseGCM_Emp_Task_Message(String data) {
		EmpTaskDTO taskObj =new EmpTaskDTO();
		System.err.println(" ParseGCM_Emp_Task_Message ----"+data);
		try {
			JSONObject jo=new JSONObject(data);

			//{"data":{"emp_car_id":"9664","emp_user_id":"509558",
			// "address":"wakad rupesh","month":"2","user_id":"509559",
			// "year":"2018","message_type":"emp_task","time":"12:12 AM","day":"6"}}
			JSONObject jodata=jo.getJSONObject("data");

			taskObj.setEmp_user_id(jodata.getString("emp_user_id"));
			taskObj.setUser_id(jodata.getString("user_id"));
			taskObj.setTime(jodata.getString("time"));
			taskObj.setAddress(jodata.getString("address"));
			taskObj.setEmp_car_id(jodata.getString("emp_car_id"));
			taskObj.setDay(jodata.getString("day"));
			taskObj.setMonth(jodata.getString("month"));
			taskObj.setYear(jodata.getString("year"));

			if (sharedPreferences.getString("User_Id","0").equals(taskObj.getEmp_user_id()))
			sendNotification(taskObj);

			//	setgeolocation(geo);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		}

	}

	@SuppressLint("LongLogTag")
	private void sendNotification(EmpTaskDTO taskObj)
		{
			Log.d(TAG, "Preparing to send notification...: " + taskObj.getAddress());
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Intent task_intent=new Intent(this, LoginActivity.class);
			/*task_intent.putExtra("day",taskObj.getDay()+"");
			task_intent.putExtra("month",taskObj.getMonth()+"");
			task_intent.putExtra("year",taskObj.getYear()+"");
			task_intent.putExtra("user_id",taskObj.getEmp_user_id()+"");
*/
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					task_intent, 0);

			Common.playDefaultNotificationSound(getApplicationContext());


			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.mipmap.ic_icon)
					.setContentTitle("Task allocated to you")
					.setContentText("You have picked up  from "+taskObj.getAddress()+"" +
							" at "+taskObj.getDay()+"-"+taskObj.getMonth()+"-"+taskObj.getYear()+" "+taskObj.getTime());
					//.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			Log.d(TAG, "Notification sent successfully.");
		}

	
	
	
	/*{"fence_det":{"center":{"lan":73.771835,"lat":18.590255},
		"fence_no":1,"radius":100,"type":"circle","fence_id":1,
		"fence_name":"First Fence"},
		"lan":73.771835,"geo_fence_status":"in",
		"lat":18.590255,"speed":5,"timestamp":1469346453}*/
/*{"data":{"fence_det":{"center":{"lan":73.771835,"lat":18.590255},"fence_no":1,"radius":100,"type":"circle","fence_id":1,
	"fence_name":[70,105,114,115,116,32,70,101,110,99,101]},"lan":73.77065166666667,"geo_fence_status":"in","lat":18.590235,"speed":8,"timestamp":1470654885},"event":"geo_fence_alert"}
*/

	
	
/*// GEO fence msg----{"data":{"fence_det":{"center":{"lan":"77.12520729750395",
	"lat":"28.649835131287656"},"radius":"100.0","type":"circle",
	"fence_id":"84474678","fence_name":"delhi"},"parent":505261,
	"student":5286,"lan":73.769235,"geo_fence_status":"out","lat_direction":"N","lan_direction":"E",
	"lat":18.590143333333334,"speed":19,"timestamp":1470978156},"event":"geo_fence_alert"}*/

	private void ParseGCM_Message(String msg) {
			geofenceObj=new GeofenceDTO();
			System.err.println("GEO fence msg----"+msg);
			try {
				JSONObject jo=new JSONObject(msg);
				
				
				JSONObject jodata=jo.getJSONObject("data");

				JSONObject josub=jodata.getJSONObject("fence_det");

				geofenceObj.setGeoID(josub.getString("fence_id"));
			//	geofenceObj.setFenceno(josub.getString(""));


				geofenceObj.setGeoName(josub.getString("fence_name"));
				geofenceObj.setType(josub.getString("type"));
				geofenceObj.setDistance(josub.getString("radius"));
				
				JSONObject josub_centre=josub.getJSONObject("center");
			    geofenceObj.setLat(josub_centre.getString("lat"));
				geofenceObj.setLang(josub_centre.getString("lan"));
				
	
					geofenceObj.setSpeed(jodata.getInt("speed"));
					geofenceObj.setTimestamp(jodata.getLong("timestamp"));
					geofenceObj.setGeofencestatus(jodata.getString("geo_fence_status"));
					geofenceObj.setStudentId(jodata.getString("student")+"");


					
					if (jodata.getString("lat_direction").equalsIgnoreCase("N")&&jodata.getString("lan_direction").equalsIgnoreCase("E")) {
						
						geofenceObj.setCurrlat(jodata.getString("lat"));
						geofenceObj.setCurrlang(jodata.getString("lan"));
						}else if (jodata.getString("lat_direction").equalsIgnoreCase("N")&&jodata.getString("lan_direction").equalsIgnoreCase("W")) {

						
						geofenceObj.setCurrlat(jodata.getString("lat"));
						geofenceObj.setCurrlang("-"+jodata.getString("lan"));
						}
						else if (jodata.getString("lat_direction").equalsIgnoreCase("S")&&jodata.getString("lan_direction").equalsIgnoreCase("E")) {

					
						geofenceObj.setCurrlat("-"+jodata.getString("lat"));
						geofenceObj.setCurrlang(jodata.getString("lan"));

						}else if (jodata.getString("lat_direction").equalsIgnoreCase("S")&&jodata.getString("lan_direction").equalsIgnoreCase("W")) {
					
						geofenceObj.setCurrlat("-"+jodata.getString("lat"));
						geofenceObj.setCurrlang("-"+jodata.getString("lan"));
						}
					
					Toast.makeText(getApplicationContext(), "Student id geofence ---->  "+geofenceObj.getStudentId(),Toast.LENGTH_LONG).show();
					System.err.println("-------------************-----------"+jodata.getString("student")+"---------------"+geofenceObj.getStudentId());
					PrimesysTrack.mDbHelper.insertGeofence(geofenceObj);
					sendNotification(geofenceObj);

				//	setgeolocation(geo);

			} catch (Exception e) {
				e.printStackTrace();
			}finally{

			}
				
	}


	@SuppressLint("LongLogTag")
	private void sendNotification(GeofenceDTO geofenceObj) {
		Log.d(TAG, "Preparing to send notification...: " + geofenceObj);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, GeofenceShowSTudent.class);
		//intent.putExtra("GcmInfo", geofenceObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent, 0);
    
       
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.mipmap.ic_icon)
				.setContentTitle(geofenceObj.getGeoName())
				.setTicker("Geofence Alert")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(geofenceObj.getGeoName()));
		
		
			Common.playDefaultNotificationSound(getApplicationContext());
		//	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS) ;
			mBuilder.setDefaults(Notification.DEFAULT_VIBRATE) ;
			
				if (geofenceObj.getGeofencestatus().equalsIgnoreCase("in"))
          			mBuilder.setContentText("Child went Inside " + geofenceObj.getGeoName()+" geofence");
      				else      
       				mBuilder.setContentText("Child went Outside " +geofenceObj.getGeoName()+" geofence");
				
				mBuilder.setOngoing(false);
				mBuilder.setAutoCancel(true);
				mBuilder.setPriority(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				mBuilder.setOnlyAlertOnce(true);
				mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		Log.d(TAG, "Notification sent successfully.");
	}
	
	@SuppressLint("LongLogTag")
	private void sendNotification(String msg) {
		Log.d(TAG, "Preparing to send notification...: " + msg);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, GeofenceShowSTudent.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.mipmap.ic_icon)
				.setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		Log.d(TAG, "Notification sent successfully.");
	}
	


	
}
