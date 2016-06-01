package com.primesys.VehicalTracking;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract.Helpers;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.LruBitmapCache;


//public class ShowGmapClient  extends Activity implements ConnectionCallbacks,LocationListener, OnConnectionFailedListener{
	/*public   GoogleMap mMap;
	public static  Bitmap bmp1 ;
	private  LatLng prev ;
	public   static int flag=0;
	public static int  StudentId;
	String path;
	ImageLoader imageLoader;
	static Context trackContext;
	private static int speed;
	private static String date;
	MarkerOptions mp;
	String latval = null,lanval;
	Marker mark;
	static RequestQueue RecordSyncQueue;
	ListView gmapList;
	String defaultImage;
	ShowMapAdapter myAdapter;
	static String TAG="ShowGMap";
	ShowGmapClient contextMap=ShowGmapClient.this;
	long freeSize = 0L;
	long totalSize = 0L;
	long usedSize = -1L;
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;
	private static RequestQueue reuestQueue;
	private static StringRequest stringRequest;
	public static Boolean Updatestatus=false,menuSelct;
	ArrayList<GmapDetais> arr=new ArrayList<GmapDetais>();
	int cnt=0;
	int cntMap=0;
	public static Boolean PostLocationflag=true;
	private static DBHelper helper=DBHelper.getInstance(trackContext);
;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trackContext = ShowGmapClient.this;

		if (!isGooglePlayServicesAvailable()) {
			Common.showToast("Google Play services not available !", trackContext);
			finish();
		}

		try{

			getUsedMemorySize();

			if (usedSize>totalSize-1000) {

				Common.showToast("Insufficient Memory in Loading Map.", contextMap);

			}else{
				StudentId=getIntent().getIntExtra("StudentId",0);
				//Make REq To socket of child boy

				try {
					LoginActivity.mClient.sendMessage(makeJSON());
					setContentView(R.layout.activity_currentlocation);

					RecordSyncQueue = Volley.newRequestQueue(contextMap);

					ImageLoader.ImageCache imageCache = new LruBitmapCache();
					imageLoader = new ImageLoader(
							RecordSyncQueue, imageCache);
					imageLoader = new ImageLoader(Volley.newRequestQueue(contextMap), imageCache);
					ShowMapAdapter.trackInfo=false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());

				//get Set
				final int abTitleId = getResources().getIdentifier("action_bar_title", "id", "android");
				findViewById(abTitleId).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try{

							if (cnt<=0) {
								gmapList.setVisibility(View.VISIBLE);
								myAdapter=new ShowMapAdapter(ShowGmapClient.this, R.layout.fragment_mapsidebar, arr,imageLoader);
								gmapList.setAdapter(myAdapter);
								overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
								gmapList.setSelection(0);
								cnt++;
							}else{
								gmapList.setVisibility(View.GONE);
								cnt=0;
							}

						}catch(Exception ex){
							Log.e("Exception", ""+ex);
						}

					}
				});

				if (Common.getConnectivityStatus(trackContext)&& helper.Show_Device_list().size()==0) {
					// Call Api to get track information
					try {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							new TrackInfrmation().executeOnExecutor(
											AsyncTask.THREAD_POOL_EXECUTOR,
											(Void) null);
						} else {
							new TrackInfrmation().execute();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}else {
					
					//SEt OFfline List
					gmapList=(ListView)findViewById(R.id.list);
					arr=helper.Show_Device_list();
					myAdapter=new ShowMapAdapter(ShowGmapClient.this, R.layout.fragment_mapsidebar, arr,imageLoader);
					gmapList.setAdapter(myAdapter);
					overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
					gmapList.setSelection(0);
					cnt++;
				//	Common.showToast("Offline data set", trackContext);
				}
				


			*//*	String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
				LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));   *//*
				
				
				if(Common.roleid.equalsIgnoreCase("7")){
					//Get Image from Service And Add it

					bmp1=null;
					if (getIntent().getByteArrayExtra("G_image")!=null) {
						try{
							byte[] byteArray = getIntent().getByteArrayExtra("G_image");
							bmp1 = (Bitmap)BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
						}catch(Exception ex){
							bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
						}

					}


				}
				else{
					if( getIntent().getByteArrayExtra("G_image")!=null){
						byte[] byteArray = getIntent().getByteArrayExtra("G_image");
						bmp1 = (Bitmap)BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
					}
					else

						bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.student);
				}
				if (mMap == null) {

					mMap = ((MapFragment) getFragmentManager().findFragmentById(
							R.id.gmap)).getMap();
					mMap.getUiSettings().setZoomControlsEnabled(true);
					mMap.getUiSettings().setMyLocationButtonEnabled(true);
					showCurrentLocation();
					// check if map is created successfully or not
					if (mMap == null) {
						Common.showToast("Sorry! unable to create maps",trackContext);
					}
				}

				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


				RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);
				
				rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				    @Override
				    public void onCheckedChanged(RadioGroup group, int checkedId) {
				        if(checkedId == R.id.rb_normal){
				        	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				        }else if(checkedId == R.id.rb_satellite){
				        	mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				        }else if(checkedId == R.id.rb_terrain){
				        	mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				        }
				    }
				});

		}

		}catch(Exception e)
		{
			Log.e("ShowGMap", ""+e);
		}



	}

	@Override
	protected void onStop() {
		super.onStop();
		flag=0;
		String trackSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event","stop_track");
			trackSTring=jo.toString();
		}
		catch(Exception e)
		{

		}
		LoginActivity.mClient.sendMessage(trackSTring);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		*//*LoginActivity.mClient.sendMessage(makeJSON());*//*
		LoginActivity.mClient.sendMessage(restartTrackEvent());
	}
	public static void changeLocation(String message)
	{

		System.out.print("Event Received");
		try
		{


			JSONObject jo = new JSONObject(message);
			JSONObject jData = jo.getJSONObject("data");
			String lat=jData.getString("lat");
			String lan=jData.getString("lan");
			speed=jData.getInt("speed");
			date=Common.getDateCurrentTimeZone(jData.getLong("timestamp"));
			((ShowGmapClient) trackContext).updateGoogleMapLocation(lat,lan);
		//	System.err.print("changeLocation"+  message);
			Common.Location_getting=false;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}finally{
		*//*	//GetJson History
			String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());

			LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));   
			*//*
	
		}
	}
	//show currentLocation
	void showCurrentLocation()
	{
		try{
			*//*
			 * This Is One Is Default 
			 *//*
			mMap.setMyLocationEnabled(true);
			LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
			// Getting LocationManager object from System Service LOCATION_SERVICE

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = lm.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = lm.getLastKnownLocation(provider);

			if(location!=null){
				//HIde  CUrrent Laocation update on sir's demand
				//onLocationChanged(location);			}
			lm.requestLocationUpdates(provider, 20000, 0, this);

				}
		}catch(Exception e){

		}
	}
	@Override
	public void onLocationChanged(Location location) {

		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	void updateGoogleMapLocation(String lat,String lan)
	{
		try{

			Bitmap bmpDefaulr=BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.default_marker1);

			LatLng current = new LatLng(Double.parseDouble(lat), Double.parseDouble(lan));
			if(flag==0)  //when the first update comes, we have no previous points,hence this 
			{ 

				try {
				//	mMap.clear();
					prev=current;
					flag=1;
					cntMap=1;
					mp = new MarkerOptions();
					Bitmap bmp=BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.custom_marker);
					mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
					mp.snippet("Latitude : "+String.format("%.6f",current.latitude)+"\t"+"Longitude : "+String.format("%.6f",current.longitude));
					mp.title("Speed : "+speed+" km/h"+"\t"+"Date : "+date+"\n"+"");
					mark=mMap.addMarker(mp);
					mark.showInfoWindow();
					
					// Call Api to get track information
					try{
							if(PostLocationflag)
							PostLocation_Report(lat,lan);

						}catch(Exception ex)
						{
							System.out.println("Error in post loc");
							ex.printStackTrace();
							
						}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			else
			{
				try {
					if (cntMap==1) {
						mark=mMap.addMarker(mp);
						cntMap++;
					}else{
						try {
							mark.setIcon(BitmapDescriptorFactory.fromBitmap(bmpDefaulr));
							mMap.addPolyline((new PolylineOptions())
									.add(prev, current).width(6).color(Color.CYAN)
									.visible(true));
							prev=current;
							mp = new MarkerOptions();
							Bitmap bmp=BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.custom_marker);
							mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
							
							mp.snippet("Latitude : "+String.format("%.6f",current.latitude)+"\t"+"Longitude : "+String.format("%.6f",current.longitude));
							mp.title("Speed : "+speed+" km/h"+"\t"+"Date : "+date+"\n"+"");
							mark=mMap.addMarker(mp);
							
							mark.showInfoWindow();
							current = null;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



			}
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat),Double.parseDouble(lan)), 15));

		}catch(Exception ex){

		}

	}
	//creating custom marker
	View customMarker()
	{
		View marker = ((LayoutInflater)trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
		ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
		childImage.setImageBitmap(bmp1);
		return marker;
	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_edit, menu);
		return super.onCreateOptionsMenu(menu);

	}
	// onclick menu item 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			onBackPressed();
			finish();
			return super.onOptionsItemSelected(item);
		case R.id.history:
			Log.e("Stid Id showgMap", "---"+StudentId);
			String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
			LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));
		//	setResult(RESULT_OK, getIntent().putExtra("dateTime", formattedDate.format(date)));
//			GoToHistoty();
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public static void GoToHistoty() {
		// TODO Auto-generated method stub
		System.gc();
	    System.runFinalization(); // added in https://github.com/android/platform_frameworks_base/commit/6f3a38f3afd79ed6dddcef5c83cb442d6749e2ff
	    System.gc();
	    
	    if (Common.Location_getting) {
			Intent i = new Intent(trackContext, HistoryActivity.class);
				i.putExtra("StudentId", StudentId);
				trackContext.startActivity(i);
			}else 
				Common.showToast("Please Wait Getting connection", trackContext);

		
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//menu.findItem(R.id.history).setIcon(new IconDrawable(this, IconValue.fa_history).colorRes(R.color.colorPrimary).actionBarSize());
		return super.onPrepareOptionsMenu(menu);
	}
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}
	// make JSON for track
	String makeJSON()
	{
		String trackSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event","start_track");
			if(Common.roleid.equals("5"))
				jo.put("student_id","demo_student");
			else
				jo.put("student_id",StudentId);
			trackSTring=jo.toString();
			System.err.print("Event Fired");
		}
		catch(Exception e)
		{

		}
		return trackSTring;
	}
	// JSON request to get the history
	static String makeJSONHistory(String date)
	{
		helper.truncateTables("db_history");
		String trackSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event","get_tracking_history");
			if(Common.roleid.equals("5"))
				jo.put("student_id","demo_student");
			else
				jo.put("student_id",StudentId);
			jo.put("timestamp",Common.convertToLong(date));
			trackSTring=jo.toString();
		}
		catch(Exception e)
		{

		}
		return trackSTring;
	}
	//Track Informatiion
	class TrackInfrmation extends AsyncTask<Void, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			String result="";
			try{
				HttpClient httpclient=new DefaultHttpClient();
				HttpPost httpost=new HttpPost(Common.URL+"ParentAPI.asmx/GetTrackInfo");
				List<NameValuePair> param=new ArrayList<NameValuePair>(1);
				param.add(new BasicNameValuePair("ParentId", Common.userid));
				httpost.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse response = httpclient.execute(httpost);
				result=EntityUtils.toString(response.getEntity());
				Log.e("response List Map", ""+result);
			}catch(Exception e){
				result=e.getMessage();
			}

			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			parsingTrackInfo(result);

		}
	}
	public void parsingTrackInfo(String result) {
		try{
			Log.e("Track Info list",result);
			JSONArray joArray=new JSONArray(result);
			for (int i = 0; i < joArray.length(); i++) {
				JSONObject joObject =joArray.getJSONObject(i);
				GmapDetais dmDetails=new GmapDetais();
				if (i<=0) {
					defaultImage=joObject.getString("Photo").replaceAll("~", "").trim();
					if (Common.roleid.contains("5")) {
						Common.currTrack="demo_student";

					}else{
						Common.currTrack=joObject.getString("StudentID");
					}
				}
				dmDetails.setId(joObject.getString("StudentID"));
				dmDetails.setName(joObject.getString("Name"));

				dmDetails.setPath(joObject.getString("Photo").replaceAll("~", "").trim());
				dmDetails.setType(joObject.getString("Type"));
				arr.add(dmDetails);
			}


		}catch(Exception e){
			Log.e("Exception", ""+e);
		}finally{
			//it work Better but take time to Load
			if (arr.size()>0) {
				
				//Insert Offeline data
				
				helper.Insert_Device_list(arr);
				gmapList=(ListView)findViewById(R.id.list);
				myAdapter=new ShowMapAdapter(ShowGmapClient.this, R.layout.fragment_mapsidebar, arr,imageLoader);
				gmapList.setAdapter(myAdapter);
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
				gmapList.setSelection(0);
				cnt++;
			}else{
				Common.showToast("No User Information",contextMap );
			}

		}
	}
	public static void startMethod(){
		*//*Intent startIntent=new Intent(trackContext,HistoryActivity.class);
		startIntent.putExtra("StudentId",StudentId);
		trackContext.startActivity(startIntent);*//*
	}
	private Bitmap getRoundedShape(Bitmap bitmap) {
		int targetWidth = 100;
		int targetHeight = 100;
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

	public  long getUsedMemorySize() {
		try {
			Runtime info = Runtime.getRuntime();
			freeSize = info.freeMemory();
			totalSize = info.totalMemory();
			usedSize = totalSize - freeSize;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usedSize;

	}
	public  String convertToStringRepresentation(final long value){
		final long[] dividers = new long[] { T, G, M, K, 1 };
		final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
		if(value < 1)
			throw new IllegalArgumentException("Invalid file size: " + value);
		String result = null;
		for(int i = 0; i < dividers.length; i++){
			final long divider = dividers[i];
			if(value >= divider){
				result = format(value, divider, units[i]);
				break;
			}
		}
		return result;
	}
	private static String format(final long value,
			final long divider,
			final String unit){
		final double result =
				divider > 1 ? (double) value / (double) divider : (double) value;
				return new DecimalFormat("#,##0.#").format(result) + " " + unit;
	}



	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			mMap.clear();
			LoginActivity.mClient.sendMessage(restartTrackEvent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected String restartTrackEvent() {
		String trackSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event","start_track");
			if (Common.currTrack.matches(".*\\d.*")) {
				jo.put("student_id",Integer.parseInt(Common.currTrack));
			}else{
				jo.put("student_id", Common.currTrack);
			}
			trackSTring=jo.toString();
		}
		catch(Exception e)
		{

		}
		return trackSTring;
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

//Post Location Report
	
	public static void PostLocation_Report(final String lat, final String lan) {
		
		reuestQueue=Volley.newRequestQueue(trackContext); //getting Request object from it
		//JSon object request for reading the json data
		stringRequest = new StringRequest(Method.POST,Common.URL+"UserServiceAPI/PostLocation_Report",new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

			}

		},
		new Response.ErrorListener() {


			@Override
			public void onErrorResponse(VolleyError error) {
				if(error.networkResponse != null && error.networkResponse.data != null){
					VolleyError er = new VolleyError(new String(error.networkResponse.data));
					error = er;
					System.out.println(error.toString());
				}
			}

		
		}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> param = new HashMap<String, String>();
				try {
					int Track_id=0;
					if ( ShowMapAdapter.gp==null)
						 Track_id=StudentId;
					else {
						 Track_id=Integer.parseInt(ShowMapAdapter.gp.getId());

					}

						
			
					
					param.put("TrackpersonId",Track_id+"");
					param.put("UserId", Common.userid);
					param.put("Lat", lat);
					param.put("Lang", lan);
					
					Log.e("PostLocation report on server", param.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return param;
			}

		};


		stringRequest.setTag(TAG);
		// Adding request to request queue
		reuestQueue.add(stringRequest);
	}*/


//}


