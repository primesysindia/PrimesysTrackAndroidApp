package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.Gcm.GCMNotificationIntentService;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GeofenceShowSTudent extends AppCompatActivity implements OnMapReadyCallback {
	private static final int TAG_CODE_PERMISSION_LOCATION = 100;
	private static Circle mCircle;
	private Marker mMarker;
	private static GoogleMap mGoogleMap;
	private ImageView Geopin;
	double radiusInMeters = 100.0;
	static int strokeColor = Color.parseColor("#CC6698"); //red outline #80000000
	static int shadeColor = Color.parseColor("#66f0d0e0");//Color.parseColor("#1Af0d0e0"); //opaque red fill
	private SeekBar seekBar_radius;
	private static CircleOptions circleOptions;
	private static Context context;
	static GeofenceDTO geoObj = new GeofenceDTO();
	StringRequest stringRequest;
	RequestQueue reuestQueue;
	private Object TAG;
	private FragmentManager myFragmentManager;
	private SupportMapFragment myMapFragment;
	private static MarkerOptions markerOptions;
	private VerticalSeekBar verticalSeebar;
	private String Meterselect;
	private boolean Update = false;
	static ArrayList<GeofenceDTO> listgeo = new ArrayList<GeofenceDTO>();
	private static GeofencinglistAdpter userAdapter;
	private ArrayList<DeviceDataDTO> arr;
	private ListView liststudent;
	private static ListView fencelist;
	public static int StudentId;
	Toolbar toolbar;
	private View mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geofence_show_student);
		context = GeofenceShowSTudent.this;
		System.err.println("=====show====Geo current-----------------------------");

		FindById();

	}


	private void FindById()
	{
		// TODO Auto-generated method stub



		myMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapfragment);
		myMapFragment.getMapAsync(this);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	}





	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mapView = myMapFragment.getView();
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

			if (GCMNotificationIntentService.geofenceObj!=null) {
				geoObj= GCMNotificationIntentService.geofenceObj;
				setgeolocation(geoObj);
			}


		}
		else {
			ActivityCompat.requestPermissions(this, new String[] {
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION },
					TAG_CODE_PERMISSION_LOCATION);
		}



	}


		private static void setgeolocation(GeofenceDTO geoObjexist) {
            String Geomsg="";

			mGoogleMap.clear();
			geoObj=geoObjexist;
		    LatLng latLng = new LatLng(Double.parseDouble(geoObjexist.getLat()),Double.parseDouble( geoObjexist.getLang()));

		    circleOptions = new CircleOptions().center(latLng).radius(Double.parseDouble(geoObjexist.getDistance())).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
		    mCircle = mGoogleMap.addCircle(circleOptions);
		    mCircle.setRadius(Double.parseDouble(geoObjexist.getDistance()));
		    System.out.println("---Update lat long-----"+latLng+" radius=="+geoObjexist.getDistance());

		    markerOptions = new MarkerOptions()
		             .position(latLng);
		    
					if (geoObjexist.getGeofencestatus().equalsIgnoreCase("in"))
         		Geomsg="Child went Inside " + geoObjexist.getGeoName()+" geofence";
  				else      
  					Geomsg="Child went Outside " +geoObjexist.getGeoName()+" geofence";

             markerOptions.snippet(Geomsg+"\n"+"Latitude : "+String.format("%.6f",latLng.latitude)+"\t"+"Longitude : "+String.format("%.6f",latLng.longitude));
             markerOptions.title("Speed : "+geoObjexist.getSpeed()+" km/h"+"\t"+"Date : "+ Common.getDateCurrentTimeZone(geoObjexist.getTimestamp())+"\n");

		   // markerOptions.title(mCircle.getRadius()+" meter");
		    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinmarker));
		    mGoogleMap.addMarker(markerOptions);
		    
		/*    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		    mGoogleMap.animateCamera(zoom);

		    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/
		    System.err.println("=====show====Geo current"+geoObjexist.getCurrlat()+","+geoObjexist.getCurrlang());
		    System.err.println("=====show====Geo fence"+geoObjexist.getLat()+","+geoObjexist.getLang());

		    LatLng boylatlang = new LatLng(Double.parseDouble(geoObjexist.getCurrlat()),Double.parseDouble( geoObjexist.getCurrlang()));
		    MarkerOptions boymarkerOptions = new MarkerOptions()
            .position(boylatlang)
            .title(boylatlang.toString());
		    // markerOptions.title(mCircle.getRadius()+" meter");
		    boymarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerboy));
		    mGoogleMap.addMarker(boymarkerOptions);
		    
		    
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
			

		@Override
		public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent login = new Intent(context, LoginActivity.class);
		// set the new task and clear flags
		login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(login);
		}
	
		
		
	
		
		
		}
