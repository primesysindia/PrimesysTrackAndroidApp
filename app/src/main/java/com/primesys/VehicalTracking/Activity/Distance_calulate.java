package com.primesys.VehicalTracking.Activity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Distance_calulate extends FragmentActivity implements OnMapReadyCallback {

	EditText text_startdate,text_enddate;
	Button submit;
	static GoogleMap googleMap;
	static Context context;
	static TextView text_total;
	String startdate="",enddate="";
	private DatePickerDialog delivaryDatePickerDialog;
	private SimpleDateFormat mFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
	public static  ArrayList<LocationData> Location_list=new ArrayList<LocationData>();
	private static int totalCount=0;
	View fragmentdismap;
	private static List<Marker> markers = new ArrayList<Marker>();
	int StudentId=0;
	private static DBHelper helper;
	static Double HistoryDistance=0.0;
	private static  RequestQueue reuestQueue;
	private static  StringRequest stringRequest;
	private static int j=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distance_calulate);
		findbyid();
		context=Distance_calulate.this;
		StudentId=Integer.parseInt(getIntent().getStringExtra("StudentId"));
	
		text_startdate.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				showStartDateTimePicker();

			}
		});
		text_enddate.setOnFocusChangeListener(new OnFocusChangeListener() {	   
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				showEndDateTimePicker();
			}
	    });
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	LoginActivity.mClient.sendMessage(makeJSONDistancePoint());
				j=0;
				HistoryDistance=0.0;
				LoginActivity.mClient.sendMessage(makeJSONDistancePoint());
				clearMarkers();

			}
		});

		if (googleMap == null) {
			// Gets to GoogleMap from the MapView and does initialization stuff
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
			mapFrag.getMapAsync(this);
		}

	}

	
	//add Default Location
		public static  void addDefaultLocations() {
		//	DBHelper dbH=DBHelper.getInstance(historyContext);
		//	list=dbH.showDetails();
			 totalCount = Location_list.size();
			Log.e("-----addDefaultLocations-------","addDefaultLocations"+"--------"+Location_list.size());

		/*	if(totalCount==0)
				Common.showToast(getResources().getString(R.string.history_msg), historyContext);*/
			for (int i = 0; i < totalCount; i++) {
				addMarkerToMap(Location_list.get(i),i);
				if (i==totalCount-1) {
					googleMap.animateCamera(
							CameraUpdateFactory.newLatLngZoom(markers.get(totalCount-1).getPosition(),10));
				}

				if (i>0&&i<totalCount-1) {
					
					Getdistance(Location_list.get(i).getLat(),Location_list.get(i).getLan(),Location_list.get(i+1).getLat(), Location_list.get(i+1).getLan());
					
				}
			}

		}
		
		
		public void clearMarkers() {
			googleMap.clear();
			markers.clear();		
		}
		/**
		 * Adds a marker to the map.
		 */
		public static void addMarkerToMap(LocationData data, int pos) {
			LatLng lat=new LatLng(data.getLat(),data.getLan());
			DateTimeActivity.selStatus=false;
			if(pos==0||pos==totalCount-1)
			{
				Marker marker = googleMap.addMarker(new MarkerOptions().position(lat)
						.title("Speed is "+data.getSpeed()+" km/h")

						.snippet(Common.getDateCurrentTimeZone(data.getTimestamp())));
	/*			polylineOptions = new PolylineOptions();
				polylineOptions.width(5); 
				polylineOptions.geodesic(true);
				arrayPoints.add(lat); polylineOptions.addAll(arrayPoints); 
				googleMap.addPolyline(polylineOptions);*/
				markers.add(marker);
			}
			else
			{
				Marker marker = googleMap.addMarker(new MarkerOptions().position(lat)
						.title("Speed is "+data.getSpeed()+" km/h")
						.snippet(Common.getDateCurrentTimeZone(data.getTimestamp()))
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_overlay))

						);
				marker.setFlat(true);
				/*polylineOptions = new PolylineOptions();
				polylineOptions.color(Color.CYAN); 
				polylineOptions.width(5); 
				polylineOptions.geodesic(true);
				arrayPoints.add(lat); polylineOptions.addAll(arrayPoints); 
				googleMap.addPolyline(polylineOptions);*/
				markers.add(marker);
			}
			
		
		}
		
		
	public void showStartDateTimePicker() {
		   final Calendar currentDate = Calendar.getInstance();
		   final Calendar date = Calendar.getInstance();
		   new DatePickerDialog(context, new OnDateSetListener() {
		       @Override
		       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            date.set(year, monthOfYear, dayOfMonth);
		            new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
		                @Override
		                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
		                    date.set(Calendar.MINUTE, minute);
		                    
		                    Log.v("date==", "The choosen one date- " + date.getTime()+"----"+date+"***"+date.getTimeInMillis());
		                	text_startdate.setText(date.getTime().toString());
		    				//startdate=date.getTimeInMillis()+"";
		                	startdate= Common.getGMTTimeStampFromDate(date.getTime().toString())+"";

		                }
		               
						
		            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
		       }
		   }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
		}

	
	
	public void showEndDateTimePicker() {
		   final Calendar currentDate = Calendar.getInstance();
		   final Calendar date = Calendar.getInstance();
		   new DatePickerDialog(context, new OnDateSetListener() {
		       @Override
		       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            date.set(year, monthOfYear, dayOfMonth);
		            new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
		                @Override
		                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
		                    date.set(Calendar.MINUTE, minute);
		                    
		                    Log.v("date==", "The choosen one date- " + date.getTime()+"----"+date+"***"+date.getTimeInMillis());
		                    text_enddate.setText(date.getTime().toString());
		                	//enddate=date.getTimeInMillis()+"";
		                	enddate= Common.getGMTTimeStampFromDate(date.getTime().toString())+"";

		                }
		               
						
		            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
		       }
		   }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
		}
	private void findbyid() {
		// TODO Auto-generated method stub
		text_enddate=(EditText) findViewById(R.id.text_enddate);
		text_startdate=(EditText) findViewById(R.id.text_startdate);
		submit=(Button) findViewById(R.id.btn_submit);
		text_total=(TextView) findViewById(R.id.text_total);
		initilizeMap();
	}


		private void initilizeMap() {

			}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;

		googleMap.getUiSettings().setZoomControlsEnabled(true);

		// check if map is created successfully or not
				if (googleMap == null) {
					Common.showToast("Sorry! unable to create maps", context);
				}
	}
		

		protected String makeJSONDistancePoint() {

			String trackSTring="{}";
			try{
				JSONObject jo=new JSONObject();
				jo.put("event", Common.EV_GETLOCATION_DISTANCE);
				jo.put("student_id",StudentId);
				jo.put("from_timestamp",Long.parseLong(startdate));
				jo.put("to_timestamp",Long.parseLong(enddate));

				trackSTring=jo.toString();
				System.err.println("Request...makeJSONDistancePoint.."+trackSTring);
			}
			catch(Exception e)
			{
				Toast.makeText(context, "->"+e, Toast.LENGTH_LONG).show();
			}
			return trackSTring;
		}
		 
		 ///Calculate distance
		 private static void Getdistance(double src_lat,double src_long, double dest_lat, double dest_long) {
				reuestQueue=Volley.newRequestQueue(context); //getting Request object from
				String url="http://maps.googleapis.com/maps/api/directions/json?origin="+src_lat+","+src_long+"&destination="+dest_lat+","+dest_long+"&sensor=false";
				//JSon object request for reading the json data
				stringRequest = new StringRequest(Method.POST,url,new Response.Listener<String>() {
				//stringRequest = new StringRequest(Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/SQLQuiz/Postscore",new Response.Listener<String>() {


					@Override
					public void onResponse(String response) {

					//	System.out.println("Responce of GetSosList---"+response);

						parseJSON(response);


					}

				},
				new Response.ErrorListener() {


					@Override
					public void onErrorResponse(VolleyError error) {
						if(error.networkResponse != null && error.networkResponse.data != null){
							VolleyError er = new VolleyError(new String(error.networkResponse.data));
							error = er;
							System.out.println(error.toString());
							parseJSON(new String(error.networkResponse.data));
						}
					}
				}) {


					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						//params.put("UserId",Common.userid);

						//System.out.println("REq---GetSosList------"+params);
						return params;
					}

				};


				stringRequest.setTag("shj");
				// Adding request to request queue
				reuestQueue.add(stringRequest);
					
			}

			protected static void parseJSON(String result) {
				// TODO Auto-generated method stub
				JSONObject distOb;
				JSONObject timeOb;
				j++;
				try {
					final JSONObject json = new JSONObject(result);
					JSONArray routeArray = json.getJSONArray("routes");
					JSONObject routes = routeArray.getJSONObject(0);

					JSONArray newTempARr = routes.getJSONArray("legs");
					JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

					distOb = newDisTimeOb.getJSONObject("distance");
					timeOb = newDisTimeOb.getJSONObject("duration");
					
					 Log.i("Diatance :", distOb.getString("text"));
					    Log.i("Time :", timeOb.getString("text"));
				HistoryDistance=HistoryDistance+Double.parseDouble(distOb.getString("text").replaceAll("[^0-9]", "").trim());

				//System.out.println("---------------//****************------"+HistoryDistance+"---"+j+"---------------------------");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if (j==totalCount-2) {
						//Common.showlongToast("DIstance Calulated by====="+HistoryDistance, context);
						System.out.println("DIstance Calulated by====="+HistoryDistance);
						text_total.setText("Total distance :: "+HistoryDistance+" Km");
					}
				}

			   
				
			}


}
