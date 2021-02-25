package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class GeofencingNewdrawCircle extends AppCompatActivity implements OnMapReadyCallback,SeekBar.OnSeekBarChangeListener {
	private static final int TAG_CODE_PERMISSION_LOCATION =102 ;
	private Circle mCircle;
    private Marker mMarker;
    private GoogleMap mGoogleMap;
    double radiusInMeters = 100.0;
    int strokeColor = Color.parseColor("#CC6698"); //red outline #80000000
    int shadeColor = Color.parseColor("#66f0d0e0");//Color.parseColor("#1Af0d0e0"); //opaque red fill
    private CircleOptions circleOptions;
    public static Context context;
    GeofenceDTO geoObj = new GeofenceDTO();
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    private FragmentManager myFragmentManager;
    private SupportMapFragment myMapFragment;
    private MarkerOptions markerOptions;
    private SeekBar radius_seekbar;
    private double KMeterselect;
    public static boolean Update = false;
    Boolean mapenable = true;
    private RadioGroup type;
    private LocationListener locationListenerObject;
	private Toolbar toolbar;
	EditText edt_place;
	private LatLng TargetLatLng=null;
	private TextView tv_seekbar;
	RadioButton radio_setmap;
	private ImageView img_search;
	private Button btn_plus,btn_minus;
	 CheckBox st_out,st_in,alert_app,alertsms;
	 CheckBox alert_email;
	 EditText geoname;
	private Button Submitdetil;
	private ToggleButton activemode;
	private View mapView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencingdraw_circle);
			FindById();





		img_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

					if (edt_place.getText().toString().length() != 0) {

						TargetLatLng = Common.getLocationFromAddress(context, edt_place.getText().toString());
						if (TargetLatLng != null && mGoogleMap != null) {
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TargetLatLng, 15.5f), 4000, null);

							//	Common.ShowSweetSucess(context, "Now, Tap on map to select geo-fence location.");
						} else {
							Common.ShowSweetAlert(context, getResources().getString(R.string.place_search_nulldetail));

						}

					} else {
						Common.ShowSweetAlert(context, "Please " + getResources().getString(R.string.place_search_hint));
					}

				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});

		radius_seekbar.setOnSeekBarChangeListener(this);
		/*search_place.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				}

		});*/
		btn_minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				radius_seekbar.setProgress(radius_seekbar.getProgress()-5);


			}
		});
		btn_plus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				radius_seekbar.setProgress(radius_seekbar.getProgress()+5);


			}
		});

		Submitdetil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(geoname.getText().length()!=0){
					if(st_in.isChecked()||st_out.isChecked()&& alert_app.isChecked()||alert_email.isChecked())
					{
						if (st_in.isChecked())
							geoObj.setStatusin("True");
						else geoObj.setStatusin("False");
						if (st_out.isChecked())
							geoObj.setStatusout("True");
						else geoObj.setStatusout("False");

						if (alert_app.isChecked())
							geoObj.setAlertapp("True");
						else geoObj.setAlertapp("False");

						if (alert_email.isChecked())
							geoObj.setAlertemail("True");
						else geoObj.setAlertemail("False");

						if (alertsms.isChecked())
							geoObj.setSms("True");
						else geoObj.setSms("False");
						geoObj.setEnable("True");

						geoObj.setGeoName(geoname.getText().toString());
						geoObj.setDiscripation("");

							if (KMeterselect>=0.5) {
							LoginActivity.mClient.sendMessage(make_fenceNew_JSON());
						}else
							Common.ShowSweetAlert(context, "Please select radius at least 0.5 KM ");



					}else {
						Common.ShowSweetAlert(context, "Please checked at least one from each group ");
					}
				}else{
					geoname.setError("Please enter valid geo-fencing name.");
				}

			}
		});

		activemode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					geoObj.setEnable("True");
				else
					geoObj.setEnable("False");
			}
		});

    }

	private void FindById() {
        // TODO Auto-generated method stub
        context = GeofencingNewdrawCircle.this;


		myMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapfragment);
		myMapFragment.getMapAsync(this);


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		radius_seekbar = (SeekBar) findViewById(R.id.radius_seekbar);
		btn_plus=(Button)findViewById(R.id.btn_plus);
		btn_minus=(Button)findViewById(R.id.btn_minus);

		edt_place= (EditText) findViewById(R.id.edt_tool_search);
		edt_place.setVisibility(View.VISIBLE);
		img_search=(ImageView)findViewById(R.id.img_search_bar);
		st_in = (CheckBox) findViewById(R.id.statusin);
		st_out = (CheckBox) findViewById(R.id.statusout);

		geoname = (EditText) findViewById(R.id.geoname);
		alert_app = (CheckBox) findViewById(R.id.alertapp);
		alert_email = (CheckBox) findViewById(R.id.alertemail);
		alertsms = (CheckBox) findViewById(R.id.alertsms);
		Submitdetil = (Button) findViewById(R.id.geosubmit);
		activemode = (ToggleButton) findViewById(R.id.toggleenable);

       Common.ShowSweetAlert(context, "Tap on map to select Geo-fencing location.");

	}



	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mapView = myMapFragment.getView();


		mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {

				//The code below demonstrate how to convert between LatLng and Location

				if (mapenable) {
					mGoogleMap.clear();
					//Convert LatLng to Location
					Location location = new Location("Test");
					location.setLatitude(point.latitude);
					location.setLongitude(point.longitude);
					location.setTime(new Date().getTime()); //Set time as current Date
					//Convert Location to LatLngll
					LatLng newLatLng = new LatLng(location.getLatitude(),
							location.getLongitude());

					MarkerOptions markerOptions = new MarkerOptions()
							.position(newLatLng)
							.title(newLatLng.toString());
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinmarker));
					mGoogleMap.addMarker(markerOptions);

					if (mCircle == null || mMarker == null) {
						drawMarkerWithCircle(newLatLng);
						Log.e("MAaaaaaaaaa","!11111111111111111111111111111111");
					} else {
						Log.e("MAaaaaaaaaa","!2222222222222222222222");

						updateMarkerWithCircle(newLatLng);
					}
				}

			}
		});
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




	
	/*private void setgeolocation(GeofenceDTO geoObjexist)
	{
					Update=true;
				    LatLng latLng = new LatLng(Double.parseDouble(geoObjexist.getLat()),Double.parseDouble( geoObjexist.getLang()));
				
				    circleOptions = new CircleOptions().center(latLng).radius(Double.parseDouble(geoObjexist.getDistance())).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
				    mCircle = mGoogleMap.addCircle(circleOptions);
				    mCircle.setRadius(Double.parseDouble(geoObjexist.getDistance()));
				    System.out.println("---Update lat long-----"+latLng+" radius=="+geoObjexist.getDistance());
				
				    markerOptions = new MarkerOptions()
				             .position(latLng)
				             .title(latLng.toString()+"/t Radius: "+geoObjexist.getDistance()+"M");
				   // markerOptions.title(mCircle.getRadius()+" meter");
				    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pinmarker));
				    mGoogleMap.addMarker(markerOptions);
				   CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
				    mGoogleMap.animateCamera(zoom);
				
				    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(geoObjexist.getLat()),Double.parseDouble( geoObjexist.getLang())), 15.5f), 4000, null);
				    radius_seekbar.setProgress((int) (Double.parseDouble(geoObj.getDistance())));

		geoname.setText(geoObj.getGeoName());
		st_in.setChecked(Boolean.parseBoolean(geoObj.getStatusin()));
		st_out.setChecked(Boolean.parseBoolean(geoObj.getStatusout()));
		alert_app.setChecked(Boolean.parseBoolean(geoObj.getAlertapp()));
		alert_email.setChecked(Boolean.parseBoolean(geoObj.getAlertemail()));
		alertsms.setChecked(Boolean.parseBoolean(geoObj.getSms()));
	}
*/

	private void updateMarkerWithCircle(LatLng position) {
	    mCircle.setCenter(position);
	    mMarker.setPosition(position);
		radius_seekbar.setProgress((int) (radiusInMeters*5));

		//radius_seekbar.setProgress((int) (Double.parseDouble(geoObj.getDistance())));

	}

	private void drawMarkerWithCircle(LatLng position){
		
	    circleOptions = new CircleOptions().center(position).radius(radiusInMeters*5).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
	    mCircle = mGoogleMap.addCircle(circleOptions);

        markerOptions = new MarkerOptions()
                 .position(position)
                 .title(position.toString());
       // markerOptions.title(mCircle.getRadius()+" meter");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinmarker));
        mGoogleMap.addMarker(markerOptions);
		geoObj.setDistance(radiusInMeters*5+"");
        geoObj.setLang(position.longitude+"");
        geoObj.setLat(position.latitude+"");
        geoObj.setDistance(mCircle.getRadius()+"");
        geoObj.setStudentId(GeoFencingHomeNew.GeoStudentId+"");

		radius_seekbar.setProgress((int) (Double.parseDouble(geoObj.getDistance())/100));
      //  Common.ShowSweetAlert("Selected Area : "+mCircle.getRadius()+" meter", context);

      //  tv_fence_radius.setText("Selected Radius : "+Double.parseDouble(geoObj.getDistance())/1000+"  KM");

	}

	 @Override  
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	 	{

			if (mCircle != null || mMarker != null) {
				updateMarkerWithCircleradius(seekBar.getProgress());
			}
			else{
				Common.ShowSweetAlert(context, "Please first select position on map. ");
				mapenable=true;

			}

	    }

	    @Override  
	    public void onStartTrackingTouch(SeekBar seekBar) {  
	     //   Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();  
	    }  
	    @Override  
	    public void onStopTrackingTouch(SeekBar seekBar) {  
	        Common.ShowSweetSucess(context, "Selected Radius : "+KMeterselect+"  KM");
	    }
	   
		private void updateMarkerWithCircleradius(int radius) {
		    mCircle.setRadius(radiusInMeters*radius);
	        geoObj.setDistance(mCircle.getRadius()+"");
	        KMeterselect=Double.parseDouble(geoObj.getDistance())/1000;
		    markerOptions.title(KMeterselect+" KM");
	    //    Common.ShowSweetAlert("Selected Radius : "+Meterselect, context);
	        //tv_fence_radius.setText("Selected Radius : "+KMeterselect+"  KM");
		 
		}
		
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.menu_back, menu);
			return true;
		}

	// onclick menu item
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.back:
			/*Intent friendlist=new Intent(addContext, DemoUserActivity.class);
			startActivity(friendlist);*/
				onBackPressed();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected String make_fenceNew_JSON() {

			String fencenew_string="{}";
			try{
				JSONObject jo = new JSONObject();

				jo.put("event",Common.EV_GEOFENCE_ADD);
				jo.put("student_id",GeoFencingHomeNew.GeoStudentId);
			
				JSONObject jodata = new JSONObject();

				jodata.put("name",geoObj.getGeoName());
				jodata.put("descr",geoObj.getDiscripation());
				jodata.put("enable",geoObj.getEnable());
				jodata.put("status","N");
				
				
				JSONObject joalert_type = new JSONObject();

				JSONObject in_out = new JSONObject();

				in_out.put("in",geoObj.getStatusin());
				in_out.put("out",geoObj.getStatusout());
				
				JSONObject joalert = new JSONObject();

				joalert.put("push_to_app",geoObj.getAlertapp());
				joalert.put("email",geoObj.getAlertemail());
				joalert.put("sms",geoObj.getSms());
				
				
				JSONObject jodetails = new JSONObject();

				jodetails.put("lat",geoObj.getLat());
				jodetails.put("lan",geoObj.getLang());
				jodetails.put("radius",geoObj.getDistance());
				jodetails.put("type","circle");
				
				joalert_type.put("in_out",in_out);
				joalert_type.put("alert",joalert);
			
				
				jodata.put("alert_type",joalert_type);
				jodata.put("details",jodetails);
				
				jo.put("data",jodata);

			//	fencenew_string=gson.toJson(jo).replace("\\", "");
				
				fencenew_string=jo.toString();

				System.err.println("Request  GetGeofencinglistRequest "+fencenew_string);

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return fencenew_string;
		
	}



	public static void parseNewFenceJSON(String result) {
		try
		{
			JSONObject jo=new JSONObject(result);
			if (jo.getString("event").equals("geofence_added_successfully")) {
				SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
				pDialog.setTitleText("Alert");
				pDialog.setContentText("Geo-fence added successfully.");
				pDialog.setCancelable(true);
				pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						Activity activity = (Activity) context;
						activity.finish();
					}
				});
				pDialog.show();





				}else {
					Common.ShowSweetAlert(context, "Error in  creating geo-fence ");

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public static void parseUpdateFenceJSON(String result) {
		try
		{
			JSONObject jo=new JSONObject(result);
			if (jo.getString("event").equals("geofence_updated_successfully")) {
				Common.ShowSweetAlert(context, "Geo-fence update successfully.");
				Activity activity = (Activity) context;
				//correct way to use finish()
				activity.finish();		
				}else {
					Common.ShowSweetAlert(context, "Error creating geo-fence. ");

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
}

    public static void parseDeviceNotConnectError(String comMsg) {

		Common.ShowSweetAlert(context,context.getString(R.string.device_not_connect));
    }
}
