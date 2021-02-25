package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment.ShowfenceStudentAdpter;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.RecyclerItemClickListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class GeoFencingShowInOut extends AppCompatActivity implements OnMapReadyCallback {
	private static final int TAG_CODE_PERMISSION_LOCATION = 100;
	private Circle mCircle;
	private Marker mMarker;
	private GoogleMap mGoogleMap;
	private ImageView Geopin;
	double radiusInMeters = 100.0;
	int strokeColor = Color.parseColor("#CC6698"); //red outline
	int shadeColor = Color.parseColor("#f0d0e0"); //opaque red fill
	private SeekBar seekBar_radius;
	private CircleOptions circleOptions;
	private static Context context;
	GeofenceDTO geoObj = new GeofenceDTO();
	StringRequest stringRequest;
	RequestQueue reuestQueue;
	private Object TAG;
	private FragmentManager myFragmentManager;
	private MapFragment myMapFragment;
	private MarkerOptions markerOptions;
	private VerticalSeekBar verticalSeebar;
	private String Meterselect;
	private boolean Update = false;
	static ArrayList<GeofenceDTO> listgeo = new ArrayList<GeofenceDTO>();
	private static GeofencinglistAdpter userAdapter;
	private ArrayList<DeviceDataDTO> arr;
	private RecyclerView liststudent;
	private static RecyclerView fencelist;
	public static int StudentId;
	private Toolbar toolbar;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_fencing_show_in_out);
		context = GeoFencingShowInOut.this;
		FindById();
		ShowStudentlistDialog();

	}


	private void ShowStudentlistDialog() {


		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.student_listdialog);
		dialog.setTitle("Slelect Student");

		// set the custom dialog components - text, image and button
		liststudent = (RecyclerView) dialog.findViewById(R.id.studentlist);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
		liststudent.setLayoutManager(mLayoutManager);
		// TODO Auto-generated method stub
		if (Common.getConnectivityStatus(context) && PrimesysTrack.mDbHelper.Show_Device_list().size() == 0) {
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
		} else {

			arr = PrimesysTrack.mDbHelper.Show_Device_list();
			ShowfenceStudentAdpter myAdapter = new ShowfenceStudentAdpter(context, R.layout.fragment_mapsidebar, arr);
			liststudent.setAdapter(myAdapter);
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);

		}
		liststudent.addOnItemTouchListener(
				new RecyclerItemClickListener(context, new   RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						// TODO Handle item click
						Log.e("@@@@@",""+position);
						GeoFencingShowInOut.StudentId = Integer.parseInt(arr.get(position).getId());
						LoginActivity.mClient.sendMessage(makejsontoGetGeofencelist());

						dialog.dismiss();
					}
				})
		);
		dialog.show();
	}


	protected String makejsontoGetGeofencelist() {
		// make JSON for track

		String fenceSTring = "{}";
		try {
			JSONObject jo = new JSONObject();
			jo.put("event", "getfencelist");
			if (Common.roleid.equals("5"))
				jo.put("student_id", "demo_student");
			else
				jo.put("student_id", StudentId);
			fenceSTring = jo.toString();
			System.err.print("start_track Event Fired");
		} catch (Exception e) {

		}
		return fenceSTring;


	}


	private void FindById() {
		// TODO Auto-generated method stub
		myFragmentManager = getFragmentManager();
		myFragmentManager = getFragmentManager();
		myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.mapFragment);
		//  Geopin=(ImageView) findViewById(R.id.geopin);
		mapView = (MapView) myMapFragment.getView();

		myMapFragment.getMapAsync((OnMapReadyCallback) this);


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			mGoogleMap.setMyLocationEnabled(true);

			return;
		}
		fencelist=(RecyclerView) findViewById(R.id.fencelist);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
		fencelist.setLayoutManager(mLayoutManager);

				    
			}



	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
						PackageManager.PERMISSION_GRANTED) {
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

			if (mapView != null &&
					mapView.findViewById(Integer.parseInt("1")) != null) {
				// Get the button view
				View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
				// and next place it, on bottom right (as Google Maps app)
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
						locationButton.getLayoutParams();
				// position on right bottom
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
				layoutParams.setMargins(0, 0, 10, 80);



			}

		}
		else {
			ActivityCompat.requestPermissions(this, new String[] {
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION },
					TAG_CODE_PERMISSION_LOCATION);
		}



	}





	private void setgeolocation(GeofenceDTO geoObjexist) {
			mGoogleMap.clear();
			geoObj=geoObjexist;
		    LatLng latLng = new LatLng(Double.parseDouble(geoObjexist.getLat()),Double.parseDouble( geoObjexist.getLang()));

		    circleOptions = new CircleOptions().center(latLng).radius(Double.parseDouble(geoObjexist.getDistance())).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
		    mCircle = mGoogleMap.addCircle(circleOptions);
		    mCircle.setRadius(Double.parseDouble(geoObjexist.getDistance()));
		    System.out.println("---Update lat long-----"+latLng+" radius=="+geoObjexist.getDistance());

		    markerOptions = new MarkerOptions()
		             .position(latLng)
		             .title(latLng.toString()+" Radius: "+geoObjexist.getDistance()+"m");
		   // markerOptions.title(mCircle.getRadius()+" meter");
		    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinmarker));
		    mGoogleMap.addMarker(markerOptions);
		    
		/*    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		    mGoogleMap.animateCamera(zoom);

		    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/
		    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(geoObjexist.getLat()),Double.parseDouble( geoObjexist.getLang())), 15.5f), 4000, null);

				
			}

		/*	
			 public BitmapDrawable writeOnDrawable(int drawableId, String text){

				    Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

				    Paint paint = new Paint(); 
				    paint.setStyle(Style.FILL);  
				    paint.setColor(Color.BLACK); //Change this if you want other color of text
				    paint.setTextSize(30);
				    //Change this if you want bigger/smaller font

				    Canvas canvas = new Canvas(bm);
				    canvas.drawText(text, 0, bm.getHeight()/2, paint); //Change the position of the text here

				    return new BitmapDrawable(bm);
				}
		*/
			private void updateMarkerWithCircle(LatLng position) {
			    mCircle.setCenter(position);
			    mMarker.setPosition(position);
			    
			}

			private void drawMarkerWithCircle(LatLng position){
				verticalSeebar.setVisibility(View.VISIBLE);
			    circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
			    mCircle = mGoogleMap.addCircle(circleOptions);

			    

		        markerOptions = new MarkerOptions()
		                 .position(position)
		                 .title(position.toString());
		       // markerOptions.title(mCircle.getRadius()+" meter");
		        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinmarker));
		        mGoogleMap.addMarker(markerOptions);
		        geoObj.setLang(position.longitude+"");
		        geoObj.setLat(position.latitude+"");
		        geoObj.setDistance(mCircle.getRadius()+"");
		        geoObj.setStudentId(GeoFencingHomeNew.GeoStudentId+"");
		        verticalSeebar.setProgress(1);
		        Common.ShowSweetAlert(context, "Selected Area : "+mCircle.getRadius()+" meter");

		        
			}

			/* @Override  
			    public void onProgressChanged(SeekBar seekBar, int progress,  
			            boolean fromUser) {  
			    }  
			    @Override  
			    public void onStartTrackingTouch(SeekBar seekBar) {  
			     //   Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();  
			    }  
			    @Override  
			    public void onStopTrackingTouch(SeekBar seekBar) {  
			 //       updateMarkerWithCircleradius(seekBar_radius.getProgress());

			    }*/
			   
				private void updateMarkerWithCircleradius(int radius) {
					// TODO Auto-generated method stub
				    mCircle.setRadius(radiusInMeters*radius);
			        geoObj.setDistance(mCircle.getRadius()+"");
			        Meterselect=radiusInMeters*radius+" meters";
				    markerOptions.title(radiusInMeters*radius+" meters");
			        Common.ShowSweetAlert(context, "Selected Area : "+Meterselect);

				    //this is the string that will be put above the slider
				 //   verticalSeebar.setThumb(writeOnDrawable(R.drawable.seekthumb, Meterselect)); 
				}
				
				
				@Override
				public boolean onCreateOptionsMenu(Menu menu) {
					// Inflate the menu; this adds items to the action bar if it is present.
				//	getMenuInflater().inflate(R.menu.geofencingdraw_circle, menu);
					return true;
				}

				@Override
				public boolean onOptionsItemSelected(MenuItem item) {
					// Handle action bar item clicks here. The action bar will
					// automatically handle clicks on the Home/Up button, so long
					// as you specify a parent activity in AndroidManifest.xml.
					int id = item.getItemId();
				
					return super.onOptionsItemSelected(item);
				}



			protected void parseJSON(String response) {
				// TODO Auto-generated method stub
				System.err.println("Reponce   GetGeofencinglistRequest "+response);
			//cinglistRequest {"msg":"Information Saved Successfully","error":"false"}

				try {
					JSONObject jo=new JSONObject(response);
					
					if(jo.getString("error").equals("false"))
					{
						Common.ShowSweetAlert(context, jo.getString("msg"));
						this.finish();

					}

					else {
						Common.ShowSweetAlert(context, jo.getString("msg"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

		
		
		//Track Informatiion
		class TrackInfrmation extends AsyncTask<Void, String, String>{
			 ProgressDialog pDialog;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			  pDialog = new ProgressDialog(context);
				pDialog.setTitle("Progress wait.......");
				pDialog.setCancelable(false);
				pDialog.show();
			
			}
			@Override
			protected String doInBackground(Void... params) {
				String result="";
				try{
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost   httpost=new HttpPost(Common.URL+"ParentAPI.asmx/GetTrackInfo");

					List<NameValuePair> param=new ArrayList<NameValuePair>(1);
					param.add(new BasicNameValuePair("ParentId", Common.userid));
					httpost.setEntity(new UrlEncodedFormEntity(param));
					HttpResponse response = httpclient.execute(httpost);
					result= EntityUtils.toString(response.getEntity());
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
				pDialog.hide();


			}
		}
		public void parsingTrackInfo(String result) {
			try{
				Log.e("Track Info list",result);
				JSONArray joArray=new JSONArray(result);
				for (int i = 0; i < joArray.length(); i++) {
					JSONObject joObject =joArray.getJSONObject(i);
					DeviceDataDTO dmDetails=new DeviceDataDTO();
				
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
					
					PrimesysTrack.mDbHelper.Insert_Device_list(arr);
					ShowfenceStudentAdpter myAdapter = new ShowfenceStudentAdpter(context, R.layout.fragment_mapsidebar, arr);
					liststudent.setAdapter(myAdapter);
				}else{
					Common.ShowSweetAlert(context, "No User Information");
				}

			}
		}
		
		
		
		//Set List of Fence
		
		public static void parsefencelistJSON(String response) {
			// TODO Auto-generated method stub
			System.err.println("Reponce   getgeolist "+response);
		/*	if(listgeo.size()>0)
			{
				listgeo.clear();
				userAdapter.clear();
			}*/
			
				try {
					JSONArray array=new JSONArray(response);
					for (int i = 0; i < array.length(); i++) {
						JSONObject jo=array.getJSONObject(i);
						GeofenceDTO geoObj=new GeofenceDTO();
						geoObj.setStudentId(jo.getString("StudentID")); 
					//	geoObj.setStudentname(jo.getString("StudentName"));
				    	geoObj.setEnable(jo.getString("Activemode"));
				    	  geoObj.setLang(jo.getString("Lang"));
				          geoObj.setLat(jo.getString("Lat"));
				          geoObj.setDistance(jo.getString("Distance"));
							geoObj.setStatusin(jo.getString("Status_in"));
							geoObj.setStatusout(jo.getString("Status_out"));
							geoObj.setAlertapp(jo.getString("Alert_app"));
							geoObj.setAlertemail(jo.getString("Alert_email"));
							geoObj.setSms(jo.getString("Alert_sms"));
							geoObj.setGeoName(jo.getString("Name"));
							geoObj.setDiscripation(jo.getString("Description"));

							geoObj.setGeoID(jo.getString("GeoID"));
					
						listgeo.add(geoObj);


					}
					userAdapter = new GeofencinglistAdpter(context, R.layout.geofencinglist_row, listgeo);
					fencelist.setAdapter(userAdapter);

				//	setgeolocation(listgeo.get(0));
				} catch (JSONException e) {
					Log.d("ParsonJSON:", ""+e);
				}
			
			
		}
		
		
		}
