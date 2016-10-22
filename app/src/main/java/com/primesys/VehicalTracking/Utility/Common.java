package com.primesys.VehicalTracking.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.primesys.VehicalTracking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Common {


	//Live Url
	public static String URL="http://mykidtracker.in:81/API/";
	public static String MONGOURL="http://192.168.1.103/TrackingApplication/API/";
	public static String SQLURL="http://114.143.99.170:8080/TrackingAppDB/TrackingAPP/";
	public static String TrackURL="http://114.143.99.170:8080/TrackingAppDB/TrackingAPP/";
	static public String Relative_URL="http://www.mykidtracker.in:81";

	//Local URL
	//public static String URL="http://mykidtracker.in:81/API/";
	/*public static String URL="http://192.168.1.23:84/API/";
	public static String SQLURL="http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/";
	public static String TrackURL="http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/";
	public static String MONGOURL="http://192.168.1.103/TrackingApplication/API/";
	//public static  String Relative_URL="http://192.168.1.23:84/API/";
	static public String Relative_URL="http://www.mykidtracker.in:81";*/

	//LIve socket
	public static String SERVERIP ="www.mykidtracker.in";//"www.mykiddytracker.com mykidtracker
	public static int PORT = 5555;

	//LOcalsocket
	/*public static String SERVERIP ="123.252.246.214";//"www.mykiddytracker.com mykidtracker
	public static int PORT = 5555;*/

	//	LIve Url
	public static final String PayU_surl = "https://www.payumoney.com/mobileapp/payumoney/success.php";
	public static final String PayU_furl = "https://www.payumoney.com/mobileapp/payumoney/failure.php";



	public static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	public static String userid="";
	public static String Groupname;
	public static boolean conectionStatus=true;
	public static String roleid;
	public static String fontType1 = "Roboto-Bold.ttf";
	public static String username;
	public static int Student_Count;
	public static int TrackDay;
	public static String email;
	public static String markerImg;
	public static String user_regType;
	public static boolean Location_getting;

	public static String currTrack;
	public static boolean connectionStatus=true;
	public static String app_id="54c3627b3595081f8adbe2c8",classId,schoolId;
	public static String timstamp;
	public static String EV_MSG_RECEIVED="msg_received";
	public static String EV_CURR_LOC="current_location";
	public static String EV_TOD_LOC="todays_loc";
	public static String EV_DEVICE_STATUS="device_status";
	public static String EV_GRP_CREAT="group_created";
	public static String EV_ERROR="error";
	public static String TrackUser;
	public static String EV_GRPS_LIST="groups_listing";
	public static String EV_MEM_LIST="group_members_listing";
	public static String EV_FRND_SUCC="friend_added_successfully";
	public static String EV_FRND_LIST="friend_list";
	public static String EV_GRP_ADD="group_member_added";
	public static String EV_FRND_Alredy="already_a_friend";
	public static String EV_MSG_ACK="msg_ack";
	public static String EV_GEOFENCE_ERROR="device_is_not_connected";
	public static String EV_GETLOCATION_DISTANCE="get_distance";
	public static String EV_LOCATION_DISTANCE="location_for_distance";
	public static String EV_START_CAR="start_car";
	public static String EV_STOP_CAR="stop_car";
	public static String EV_STARTSTOP_CAR_RESPONCE="car_status";
	public static String EV_GETCAR_SPEEDALERT="get_speed_alert";
	public static String EV_CAR_SPEEDALERTDATA="speed_alert_data";
	public static String EV_UPDATECAR_SPEEDALERT="update_speed_alert";
	public static String EV_CAR_SPEEDALERTNOTFOUND="alert_data_not_found";
	public static String EV_GEOFENCE_NOTFOUND="geo_fences_not_found";
	public static String EV_UPDATECAR_SPEEDALERTSUCCESSFULL="over_speed_alert_updated_successfully";


	public static String studID="",stuName="",teacher_name="";
	public static String  user_regCity="";

	public static final Boolean VtsEnable = false;
	public static int DistanceInterval = 1;
	public static String Url_Type="pt";
	public static Boolean VtsFuncAllow=false;
	public static Boolean VtsSmsAllow=false;


	//convert date to timestamp
	public static long convertToLong(String date)
	{
		long time=0;
		try
		{
			Calendar cal=Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("UTC"));
			time=(int)((cal.getTimeInMillis()+cal.getTimeZone().getOffset(cal.getTimeInMillis()))/1000L);
		}catch(Exception e)
		{
			e.printStackTrace();
			time=Long.parseLong(date);
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
	static String getALLUnicodes(String msg)
	{
		StringBuilder s=new StringBuilder();
		for (int i = 0; i <msg.length(); i++) {
			s.append(String.format ("\\u%04x", (int)msg.charAt(i)));
		}
		return s.toString();
	}

	public static void ShowSweetSucess(Context context,String message){
		final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
		pDialog.setTitleText("Sucess");
		pDialog.setContentText(message);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	public static void ShowSweetAlert(Context context,String message){
		final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
		pDialog.setTitleText("Error");
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


	public static Bitmap bytebitmapconversion(String bitmap){
		Bitmap bmp=null;

		byte[] imgbytes = Base64.decode(bitmap, Base64.DEFAULT);
		bmp = BitmapFactory.decodeByteArray(imgbytes, 0, imgbytes.length);
		return bmp;


	}

	//genrate the random number
	public static long getRefNo()
	{
		return (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
	}
	public static long getGMTTimeStampFromDate(String datetime) {
		long timeStamp = 0;
		Date localTime = new Date();

		String format = "dd-MM-yyyy hh:mm aa";
		SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
		sdfLocalFormat.setTimeZone(TimeZone.getDefault());

		try {

			localTime = (Date) sdfLocalFormat.parse(datetime);

			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"),
					Locale.getDefault());
			@SuppressWarnings("unused")
			TimeZone tz = cal.getTimeZone();

			cal.setTime(localTime);

			timeStamp = (localTime.getTime()/1000L);
			Log.d("GMT TimeStamp: ", " Date TimegmtTime: " + datetime
					+ ", GMT TimeStamp : " + localTime.getTime());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return timeStamp;

	}
	public static String getDateCurrentTimeZone(long timestamp) {
		long dv = Long.valueOf(timestamp)*1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("dd-MMM-yy hh:mma").format(df);
		return vv;
	}

	public static String getTimeCurrentTimeZone(long timestamp) {
		long dv = Long.valueOf(timestamp)*1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("hh:mma").format(df);
		return vv;
	}
	// common to the show the toast
	public static void showToast(String message,Context context)
	{
		LayoutInflater inflate=((Activity)context).getLayoutInflater();
		View layout=inflate.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.layout));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);
		Toast toast=Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}




	public static boolean getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
			{
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);


					/*WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					if (wifiInfo != null) {
					    Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
					}*/

					/*ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
Log.e("RUpesh Conn",ConnectionClassManager.getInstance().getDownloadKBitsPerSecond()+"");*/
				return true;

			}


			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			{


				return true;


			}
		}

		return false;
	}
	public static Bitmap getRoundedShape(Bitmap bitmap) {
		int targetWidth = 40;
		int targetHeight = 40;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
				targetHeight,Bitmap.Config.ARGB_8888);

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
}
