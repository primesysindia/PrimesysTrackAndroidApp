package com.primesys.VehicalTracking.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.primesys.VehicalTracking.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.LOCATION_SERVICE;


public class Common {

	//Live socket
	public static String SERVERIP = "www.mykidtracker.in";//"www.mykiddytracker.com mykidtracker
	public static int PORT = 4545;

	//Live Url
	public static String URL = "http://mykidtracker.in:81/API/";
	public static String TrackURL = "http://"+SERVERIP+":8080/TrackingAppDB/TrackingAPP/";
	static public String Relative_URL = "http://www.mykidtracker.in:81";


	/*public static String SERVERIP = "192.168.1.6";//"www.mykiddytracker.com mykidtracker
	public static int PORT = 4545;*/
	// Payment Key for live
	public static final String key = "a6doMg";
	public static final String salt = "XpRAicke";
	public static final String txnid = "TXN_1";
	public static final String PaymentURL = "https://secure.payu.in/_payment";
	public static final String PayU_surl = "https://www.payumoney.com/mobileapp/payumoney/success.php";
	public static final String PayU_furl = "https://www.payumoney.com/mobileapp/payumoney/failure.php";

	/*public static final String  key  = "6vrdXc";
	public static final String  salt  = "L0j6ojPH";
	public static final String  txnid = "TXN_1";
	public static final String  PaymentURL="https://test.payu.in/_payment";
	public static final String PayU_surl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
	public static final String PayU_furl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";
*/

	//  Paytm Live Data
	public static String MID = "PRTECH60810536349319";
	public static String MercahntKey = "T!qQMu4Y#WqEylSF";
	public static String INDUSTRY_TYPE_ID = "Retail109";
	public static String CHANNLE_ID = "WAP";
	public static String WEBSITE = "PRTECHWEB";
	public static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";


	//Local URL
	//public static String URL="http://mykidtracker.in:81/API/";
  /*	public static String URL="http://192.168.1.23:90/API/";
	public static String SQLURL="http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/";
	public static String TrackURL="http://192.168.1.102:8080/TrackingAppDB/TrackingAPP/";
	//public static  String Relative_URL="http://192.168.1.16:86/API/";
	static public String Relative_URL="http://www.mykidtracker.in:81";
*/


	//LOcalsocket
	/*public static String SERVERIP ="123.252.246.214";//"www.mykiddytracker.com mykidtracker
	public static int PORT = 5555;*/

// Payment Key for test
	/*public static final String  key  = "6vrdXc";
	public static final String  salt  = "L0j6ojPH";
	public static final String  txnid = "TXN_1";
	public static final String  PaymentURL="https://test.payu.in/_payment";
	public static final String PayU_surl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
	public static final String PayU_furl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";*/

   /* //  Paytm Test Data
    public static String MID = "Primes84973633105435";
    public static String MercahntKey = "wJ!YZIYs3DRvMOvh";
    public static String INDUSTRY_TYPE_ID = "Retail";
    public static String CHANNLE_ID = "WAP";
    public static String WEBSITE = "APP_STAGING";
    public static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";*/


	public static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	public static String userid = "";
	public static String schoolId = "0";

	public static String mesurment_unit = "km";

	public static String Groupname;
	public static boolean conectionStatus = true;
	public static String roleid;
	public static String fontType1 = "Roboto-Bold.ttf";
	public static String username;
	public static int Student_Count;
	public static int TrackDay = 0;
	public static String email;
	public static String markerImg;
	public static String user_regType;
	public static boolean Location_getting;

	public static String currTrack;
	public static boolean connectionStatus = true;
	public static String app_id = "54c3627b3595081f8adbe2c8", classId ;
	public static String timstamp;
	public static String EV_MSG_RECEIVED = "msg_received";
	public static String EV_CURR_LOC = "current_location";
	public static String EV_TOD_LOC = "todays_loc";
	public static String EV_DEVICE_STATUS = "device_status";
	public static String EV_GRP_CREAT = "group_created";
	public static String EV_ERROR = "error";
	public static String TrackUser;
	public static String EV_GRPS_LIST = "groups_listing";
	public static String EV_MEM_LIST = "group_members_listing";
	public static String EV_FRND_SUCC = "friend_added_successfully";
	public static String EV_FRND_LIST = "friend_list";
	public static String EV_GRP_ADD = "group_member_added";
	public static String EV_FRND_Alredy = "already_a_friend";
	public static String EV_MSG_ACK = "msg_ack";
	public static String EV_GEOFENCE_REG_KEY="gcm_reg_key";
	public static  String EV_REG_SUCC = "reg_success";

	public static String EV_GEOFENCE_ERROR = "device_is_not_connected";
	public static String EV_GETLOCATION_DISTANCE = "get_distance";
	public static String EV_LOCATION_DISTANCE = "location_for_distance";
	public static String EV_START_CAR = "start_car";
	public static String EV_STOP_CAR = "stop_car";
	public static String EV_STARTSTOP_CAR_RESPONCE = "car_status";
	public static String EV_GETCAR_SPEEDALERT = "get_speed_alert";
	public static String EV_CAR_SPEEDALERTDATA = "speed_alert_data";
	public static String EV_UPDATECAR_SPEEDALERT = "update_speed_alert";
	public static String EV_CAR_SPEEDALERTNOTFOUND = "alert_data_not_found";
	public static String EV_GEOFENCE_NOTFOUND = "geo_fences_not_found";
	public static String EV_UPDATECAR_SPEEDALERTSUCCESSFULL = "over_speed_alert_updated_successfully";

	public static String EV_GEOFENCE="geo_fence_alert";
	public static String EV_GEOFENCE_SUCCESS="geofence_added_successfully";
	public static String EV_GEOFENCE_ADD="geofence_add";
	public static String EV_GEOFENCE_UPDATE="geofence_update";
	public static String EV_GEOFENCE_DEVICENOTCONN="device_is_not_connected";

	public static String EV_GEOFENCE_DELETE="geofence_delete";

	public static String EV_GEOFENCE_GETLIST="get_geofencelist";
	public static String EV_GEOFENCE_LIST="geofence_list";
	public static String EV_GEOFENCEUPDATE_SUCCESS="geofence_updated_successfully";
	public static String EV_GCMADDED_SUCCESS="gcm_key_added_successfully";
	public static String EV_GCMDELETED_SUCCESS="geofence_deleted_successfully";

	public static String studID = "", stuName = "", teacher_name = "";
	public static String user_regCity = "";

	public static final Boolean VtsEnable = false;
	public static int DistanceInterval = 1;
	public static String Url_Type = "pt";
	public static Boolean VtsFuncAllow = false;
	public static Boolean VtsSmsAllow = false;

	public static Boolean AccReportAllow = false;
	public static Boolean ACCSqliteEnable = true;
	public static int AccsmscentralSyncronoise = 20;
	public static int ACCSmsDeleteCheckCount = 500;
	public static int ACCSMSDeleteNo = 300;
	public static int MarkerTimeDiff = 300;
	public static int PolylineDistLimit = 200;
	public static int TrackReqTimeout = 20;
	public static double WrongWay_tolerance = 15.0;
	public static int DeviceStatusReq_Time = 60;
	public static String UpdateAppURL = "http://mykidtracker.in:81/apk/PrimesysTrack.apk";
	public static boolean PlatformRenewalStatus = false;
	public static String WebUserId = "0";
	// Google Project Number
	public static  String GOOGLE_PROJECT_ID = "576180947590";
	public static  String MESSAGE_KEY = "message";
	public static  String MESSAGE_EMP_TASK_KEY = "emp_task";
    public static double miles_multiplyer=0.621371;

	public static String HomeAPI_UpdateTime="UpdateTime";
	public static long HomeApiIntervalTime=86400;
	public static int flag=0;
	public static Boolean freindflag=false;
	protected static String status;
	public static String to="";
	public static String photo="";
	public static int Sos_callcount=0;

	public static int SosMax_callAllow=3;
	public static long SosMaxPressInterval = 50L;
	public static int SosMaxcountPowerOff=3;
    public static int FenceAllowNo=1;
	public static String Emp_id="0";
    public static String adminId="0";
	public static boolean FeatureAddressEnable=false;

	/*public  static RetryPolicy vollyRetryPolicy = new DefaultRetryPolicy(
			30 * 1000,
			2,
			1
	);
*/
	public  static RetryPolicy vollyRetryPolicy = new RetryPolicy()

	{
		@Override
		public int getCurrentTimeout() {
			return 30000;
		}

		@Override
		public int getCurrentRetryCount() {
			return 30000;
		}

		@Override
		public void retry(VolleyError error) throws VolleyError {

		}
	};

	//convert date to timestamp
	public static long convertToLong(String date) {
		long time = 0;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("UTC"));
			time = (int) ((cal.getTimeInMillis() + cal.getTimeZone().getOffset(cal.getTimeInMillis())) / 1000L);
		} catch (Exception e) {
			e.printStackTrace();
			time = Long.parseLong(date);
		}
		return time;
	}




	/*// java.lang.Stringcommon to the show the toast
	public static void showToast(Context context,String message)
	{
		LayoutInflater inflate=((Activity)context).getLayoutInflater();
		View layout=inflate.inflate(R.layout.custom_toast, (ViewGroup)((Activity)context).findViewById(R.id.layout));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);
		Toast toast=Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}*/


	//convert to unicode
	static String getALLUnicodes(String msg) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < msg.length(); i++) {
			s.append(String.format("\\u%04x", (int) msg.charAt(i)));
		}
		return s.toString();
	}

	public static void ShowSweetSucess(Context context, String message) {
		final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
		pDialog.setTitleText("Success");
		pDialog.setContentText(message);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	public static void ShowSweetAlert(Context context, String message) {
		final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
		pDialog.setTitleText("Alert");
		pDialog.setContentText(message);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	public static SweetAlertDialog ShowSweetProgress(Context context, String message) {
		SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText(message);
		pDialog.setCancelable(false);
		pDialog.show();
		return pDialog;
	}


	public static Bitmap bytebitmapconversion(String bitmap) {
		Bitmap bmp = null;

		byte[] imgbytes = Base64.decode(bitmap, Base64.DEFAULT);
		bmp = BitmapFactory.decodeByteArray(imgbytes, 0, imgbytes.length);
		return bmp;


	}

	//genrate the random number
	public static long getRefNo() {
		return (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
	}

	public static long getGMTTimeStampFromDate(String datetime) {
		long timeStamp = 0;
		Date localTime = new Date();

		String format = "dd-MM-yyyy hh:mm aa";
		SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format,Locale.getDefault());
		/*TimeZone IndiaTimezone = TimeZone.getTimeZone("Asia/Calcutta");
		sdfLocalFormat.setTimeZone(IndiaTimezone);*/

		try {

			localTime = sdfLocalFormat.parse(datetime);

			Calendar cal = Calendar.getInstance(Locale.getDefault());
			@SuppressWarnings("unused")
			TimeZone tz = cal.getTimeZone();
			cal.setTime(localTime);
			timeStamp = (localTime.getTime() / 1000L);
			Log.d("GMT TimeStamp: ", " Date TimegmtTime-----: " + datetime
					+ ", GMT TimeStamp : " + localTime.getTime());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return timeStamp;

	}

	public static String getDateCurrentTimeZone(long timestamp) {
		long dv = Long.valueOf(timestamp) * 1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("dd-MMM-yyyy hh:mma").format(df);
		return vv;
	}

	public static String getDateWithoutTimeCurrentTimeZone(long timestamp) {
		long dv = Long.valueOf(timestamp) * 1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("dd-MMM-yyyy").format(df);
		return vv;
	}

	public static String getTimeCurrentTimeZone(long timestamp) {
		long dv = Long.valueOf(timestamp) * 1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("hh:mma").format(df);
		return vv;
	}

	// common to the show the toast
	public static void showToast(String message, Context context) {
		LayoutInflater inflate = ((Activity) context).getLayoutInflater();
		View layout = inflate.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.layout));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}


	public static boolean getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);


					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					if (wifiInfo != null) {
					    Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
					}


				return true;

			}


			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}

		return false;
	}

	public static Bitmap getRoundedShape(Bitmap bitmap) {
		int targetWidth = 40;
		int targetHeight = 40;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
				targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = bitmap;
		canvas.drawBitmap(sourceBitmap,
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;

	}

	public static void playDefaultNotificationSound(Context context) {
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();
	}


	public static ArrayList<String> getAddress(Context context, double lat, double lng) {
		String add = null;
		String postal = null;
		ArrayList<String> addlist = new ArrayList<String>();
		//Log.e("getAddress==== ", "Address------" + lat + " =" + lng);
		Location Loc = new Location(LocationManager.GPS_PROVIDER);
		Loc.setLatitude(lat);
		Loc.setLongitude(lng);
		//getLocalityFrmGeoCoder(context,Loc);
		try {
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());


			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			if (addresses.size()>0){
				Address obj = addresses.get(0);
				add = obj.getAddressLine(0);
				String currentAddress = obj.getSubAdminArea() + "," + obj.getAdminArea();
				double latitude = obj.getLatitude();
				double longitude = obj.getLongitude();
				String currentCity = obj.getSubAdminArea();
				String currentState = obj.getAdminArea();
				add = add + "\t" + obj.getLocality() + ",";
				add = add + "\t" + obj.getSubAdminArea();

				addlist.add(add.replace("null", ""));
				postal = "\t" + obj.getAdminArea() + ",";
				postal = postal + "\t" + obj.getCountryName() + ",";
				postal = postal + "\t" + obj.getPostalCode() + "";
				addlist.add(postal.replace("null", ""));

			}


			Log.e("Commom ", "Address------" + add);
			// Toast.makeText(this, "Address=>" + add,
			// Toast.LENGTH_SHORT).show();

			// TennisAppActivity.showDialog(add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return addlist;
	}


	public static String getStringAddress(Context context, double lat, double lng) {
		String add = "";
		String postal = null;
		ArrayList<String> addlist = new ArrayList<String>();
		Log.e("getAddress== ", "Address------" + lat + " =" + lng);
		Location Loc = new Location(LocationManager.GPS_PROVIDER);
		Loc.setLatitude(lat);
		Loc.setLongitude(lng);
		//getLocalityFrmGeoCoder(context,Loc);
        if(lat!=0.0 &&lng!=0.0)
		try {
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			Address obj = null;

			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			obj = addresses.get(0);
			add = obj.getAddressLine(0);

			if (obj != null) {
				String currentAddress = obj.getSubAdminArea() + "," + obj.getAdminArea();
				double latitude = obj.getLatitude();
				double longitude = obj.getLongitude();
				String currentCity = obj.getSubAdminArea();
				String currentState = obj.getAdminArea();
				add = add + "\t" + obj.getLocality() + ",";
				add = add + "\t" + obj.getSubAdminArea();

				addlist.add(add.replace("null", ""));
				postal = "\t" + obj.getAdminArea() + ",";
				postal = postal + "\t" + obj.getCountryName() + ",";
				postal = postal + "\t" + obj.getPostalCode() + "";
				addlist.add(postal.replace("null", ""));


				//Log.e("Commom ", "Address------" + add);
				if (addlist.size()>0)
					add= addlist.get(0) + addlist.get(1).replaceAll(" ", "");

				// Toast.makeText(this, "Address=>" + add,
				// Toast.LENGTH_SHORT).show();

				// TennisAppActivity.showDialog(add);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (Exception ep) {
			ep.printStackTrace();
		}
		else
            add="Address not found";

		return add;
	}

	public static long getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		// return calendar.getTime();
		return calendar.getTimeInMillis();


	}

	public static long getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		// return calendar.getTime();
		return calendar.getTimeInMillis();

	}


	public static MyCustomProgressDialog ShowProgress(Context context) {
		MyCustomProgressDialog pDialog = (MyCustomProgressDialog) MyCustomProgressDialog.ctor(context);
		pDialog.setCancelable(false);

		pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pDialog.show();

		return pDialog;
	}


	public static Location getLocation(Context mContext) {
		Location location = null;
		try {
			LocationManager locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			Boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			Boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			Log.e("GPS------", "GPS Enabled" + isGPSEnabled + "---" + isNetworkEnabled);

			double latitude, longitude;
			if (isNetworkEnabled) {
				Log.e("GPS------", "1111111111111");

				if (locationManager != null) {
					Log.e("GPS------", "2222222222222");

					if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
							PackageManager.PERMISSION_GRANTED) {
						Log.e("GPS------", "33333333333333");

						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
						Log.e("location--11111111----", "" + location);

					}
				}
			}
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					Log.d("GPS", "GPS Enabled");
					if (locationManager != null) {
						if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
								PackageManager.PERMISSION_GRANTED) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();

							}
						}
					}

				}

			}
			Log.e("location-----", "" + location);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("location------", "" + location);

	/*	if (location==null)
			location=getloc(mContext)*/
		return location;
	}

	public static Location getloc(Context con) {
		LocationManager locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);

		final Location[] loc = {null};
// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				//makeUseOfNewLocation(location);


				loc[0] =location;
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

// Register the listener with the Location Manager to receive location updates
		if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

		} else
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		return loc[0];
	}


	public static Location loc(Context mcontext){

		AppLocationService appLocationService = new AppLocationService(
				mcontext);
		Location nwLocation = appLocationService
				.getLocation(LocationManager.NETWORK_PROVIDER);

		if (nwLocation != null) {
			double latitude = nwLocation.getLatitude();
			double longitude = nwLocation.getLongitude();
			/*Toast.makeText(
					getApplicationContext(),
					"Mobile Location (NW): \nLatitude: " + latitude
							+ "\nLongitude: " + longitude,
					Toast.LENGTH_LONG).show();*/
		} else {
			showSettingsAlert("NETWORK",mcontext);
		}

		return nwLocation;
	}


	/*public static Address getLocalityFrmGeoCoder(Context context, Location mLocation) {
		try {
			if(mLocation!=null){
				Geocoder gCoder = new Geocoder(context, Locale.getDefault());
				List<Address> address = gCoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
				if (address.size() > 0) {

					Log.e("getLocalityFrmGeoCoder","-------"+ address.toString());
					return address.get(0);
				}
			}

		} catch (Exception e) {
			Log.d("GEOCODER", "GEOCODER EXCEPTION");
			e.printStackTrace();

		}
		return null;
	}
*/


	public static void showSettingsAlert(String provider, final Context mContext) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				 mContext);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog
				.setMessage(provider + " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}
	public static LatLng getLocationFromAddress(Context context, String strAddress) {

		Geocoder coder = new Geocoder(context);
		List<Address> address;
		LatLng p1 = null;

		try {
			// May throw an IOException
			address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);
			location.getLatitude();
			location.getLongitude();

			p1 = new LatLng(location.getLatitude(), location.getLongitude() );

		} catch (IOException ex) {

			ex.printStackTrace();
		}

		return p1;
	}

	public static int getDisplayHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int height = display.getHeight();

		return height;
	}


}
