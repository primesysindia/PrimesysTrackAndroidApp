package com.primesys.VehicalTracking.VTSFragments;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primesys.VehicalTracking.Activity.DateTimeActivity;
import com.primesys.VehicalTracking.Activity.Home;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.MyAdpter.HistoryMapAdapter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 19/4/16.
 */
public class historyFragment extends Fragment implements OnMapReadyCallback,RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener
{

    private static double HistoryDistance=0;

    private static final String TAG ="Request" ;
    private static final int TAG_CODE_PERMISSION_LOCATION = 200;
    public static int StudentId=0;
    private static Handler handler=new Handler();
    static Context historyContext;
    static GoogleMap googleMap ;
    static ArrayList<LocationData> LocationData_list;
    private static double caldist;
    private static Runnable RunnAnim;
    ArrayList<LatLng> arrayPoints=new ArrayList<LatLng>();
    public static Marker selectedMarker;
    private static PolylineOptions polylineOptions = new PolylineOptions();
    Random random = new Random();
    FragmentManager manager;
    private static final int REQ_DATETIME=1;
    private static List<Marker> markers = new ArrayList<Marker>();
    static int totalCount;
    String defaultImage;
    public static Boolean Updatestatus=false;
    ArrayList<DeviceDataDTO> arr=new ArrayList<DeviceDataDTO>();
    int cnt=0;
    ImageLoader imageLoader;
    static RequestQueue RecordSyncQueue;
    HistoryMapAdapter myAdapter;
    private MapView mapViewhistory;
    private static View rootView;
    public  static List<LatLng> directionPoint=new ArrayList<>();
    // @AIView(R.id.label_list_sample_rfal)
    private RapidFloatingActionLayout rfaLayout;
    //   @AIView(R.id.label_list_sample_rfab)
    private RapidFloatingActionButton rfaButton;

    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionContentLabelList rfaContent;
    private ListView personlist;
    private ArrayList<DeviceDataDTO> tracklist=new ArrayList<DeviceDataDTO>();
    private RequestQueue reuestQueue;
    StringRequest stringRequest;
    private FloatingActionButton fab_date;
    static int currentPt;
    private static float bearing;
    static Marker mark;
    private KeyguardManager keyguardManager;
    private  static KeyguardManager.KeyguardLock lock;
    static Boolean ClearFlag=false;
    static Button Pause,Resume;
    public static TextView datetime,speed,distance;
    public static int polycount=0;
    private SupportMapFragment mapFragment;
    private ToggleButton togglelist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
         rootView = inflater.inflate(R.layout.activity_history, container, false);
        historyContext=this.getActivity();
        if (!isGooglePlayServicesAvailable()) {
            Common.showToast("Google Play services not available !", historyContext);
        }
        
        findviewbyid();

        try {
            if (ShowMapFragment.CDT!=null)
                ShowMapFragment.CDT.cancel();
            Home.tabLayout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }


        if (Common.getConnectivityStatus(historyContext)&& PrimesysTrack.mDbHelper.Show_Device_list().size()==0) {
            // Call Api to get track information
            try {
                GetAllTrackperson();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {

            if (tracklist.size() > 0) {
                tracklist.clear();
                if (myAdapter != null)
                    myAdapter.clear();
            }
            tracklist = PrimesysTrack.mDbHelper.Show_Device_list();

            if (tracklist.size() > 0) {
                myAdapter = new HistoryMapAdapter(historyContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);

                personlist.requestFocusFromTouch();
              //  personlist.setSelection(0);
              //  personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                cnt++;
            } else {
                Common.showToast("No User Information", historyContext);
            }

        }
            //Fab Button listner
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //	GShowMapFragment gmap=new GShowMapFragment();
                            //	gmap.flag=0;
                            DateTimeActivity.selStatus = true;
                            DeviceDataDTO user1 = tracklist.get(position);
                            historyFragment.StudentId = Integer.parseInt(user1.getId());
                            Log.e("History studid---------", historyFragment.StudentId + "----"+position);

                            ShowMapFragment.Updatestatus = true;

                            final String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
                            clearHistoryData();

                            LoginActivity.mClient.sendMessage(makeJSONHistoryChild(formattedDate, historyFragment.StudentId+""));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });



        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (historyFragment.StudentId!=0){
                    clearHistoryData();
                    Intent i=new Intent(historyContext,DateTimeActivity.class);
                    //	Log.e("History studid  menu---------", ShowGmapClient.StudentId+"  "+GHistoryMapAdapter.user1.getType());

                    i.putExtra("StudentId", historyFragment.StudentId);
                    if (HistoryMapAdapter.user1!=null)
                        i.putExtra("Type", HistoryMapAdapter.user1.getType());
                    else
                        i.putExtra("Type","demo_student");

                    startActivityForResult(i,REQ_DATETIME);
                }else {
                    Common.ShowSweetAlert(historyContext,"Please select your vehicle");
                }

            }
        });





        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(RunnAnim);
               /* IncomingSms sms=new IncomingSms();
                sms.ParseACCONMsg(historyContext, "ACC  !!!IMEI:355488020822828");*/
            }
        });

        Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(RunnAnim,100);
            }
        });


        return rootView;
    }



    @Override
    public void onMapReady(GoogleMap Map) {
       this.googleMap = Map;

        try {
            // Loading map
            initilizeMap();
            if (Common.Location_getting)
                addDefaultLocations();
         /*   else
               Common.ShowSweetAlert(historyContext,"Can't get location.Please try again.");*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //History Child
    String makeJSONHistoryChild(String date,String Id)
    {
        PrimesysTrack.mDbHelper.truncateTables("db_history");
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event","get_tracking_history");
            if(Common.roleid.equals("5"))
                jo.put("student_id","demo_student");
            else
                jo.put("student_id",Integer.parseInt(Id));
            jo.put("timestamp", Common.convertToLong(date));
            trackSTring=jo.toString();
        }
        catch(Exception e)
        {

            e.printStackTrace();
        }
        return trackSTring;
    }


    private void findviewbyid() {
        // Gets the MapView from the XML layout and creates it

        rfaLayout= (RapidFloatingActionLayout) rootView.findViewById(R.id.label_list_sample_rfal);
        rfaButton= (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        rfaContent = new RapidFloatingActionContentLabelList(historyContext);
       // googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        personlist = (ListView)rootView.findViewById(R.id.User_list);

        fab_date= (FloatingActionButton)rootView.findViewById(R.id.fab_date);
        datetime = (TextView) rootView.findViewById(R.id.datetime);
        speed = (TextView) rootView.findViewById(R.id.historyspeed);
        distance = (TextView) rootView.findViewById(R.id.distance);

        Pause=(Button)rootView.findViewById(R.id.btn_pause);
        Resume=(Button)rootView.findViewById(R.id.btn_resume);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        togglelist = (ToggleButton) rootView.findViewById(R.id.tooglelist);
        togglelist.setChecked(true);

        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Normal View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Satelite View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Terrain View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Hybrid View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(3)
        );

        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(historyContext, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(historyContext, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                historyContext,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapViewhistory.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    Home.viewPager.setCurrentItem(0);
                    return true;
                }
                return false;
            }
        });
    }


    private void initilizeMap() {

        try
        {
            if (ContextCompat.checkSelfPermission(historyContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(historyContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);
                if (googleMap == null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }

            }
            else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        TAG_CODE_PERMISSION_LOCATION);
            }

        } catch (Exception e) { ;
            // TODO: handle exception
            e.printStackTrace();
        }
    }





    //get Set

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(historyContext);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 0).show();
            return false;
        }
    }




    /*  @Override
      public void onDestroy() {
          super.onDestroy();
          mapView.onDestroy();
      }

      @Override
      public void onLowMemory() {
          super.onLowMemory();
          mapView.onLowMemory();
      }
  */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (LoginActivity.mClient!=null){
            String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
            LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));
        }

    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    // JSON request to get the history
    static String makeJSONHistory(String date) {
        PrimesysTrack.mDbHelper.truncateTables("db_history");
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "get_tracking_history");
            if (Common.roleid.equals("5"))
                jo.put("student_id", "demo_student");
            else
                jo.put("student_id", Common.currTrack);
            jo.put("timestamp", Common.convertToLong(date));
            trackSTring = jo.toString();
        } catch (Exception e) {

        }
        return trackSTring;
    }
    //add Default Location
    public static void addDefaultLocations() {
        clearMarkers();
        LocationData_list=new ArrayList<LocationData>();
        LocationData_list= PrimesysTrack.mDbHelper.showDetails();
        totalCount=LocationData_list.size();

	/*	if(totalCount==0)
			Common.showToast(getResources().getString(R.string.history_msg), historyContext);*/
        for (int i = 0; i < totalCount; i++) {
         //   Log.e("-----addMarkerToMap-------","addMarkerToMap"+"--------"+i );

            addMarkerToMap(LocationData_list.get(i),i);
            directionPoint.add(new LatLng(LocationData_list.get(i).getLat(),LocationData_list.get(i).getLan()));

        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    //    Log.e("-----markers-------","markers"+"--------"+markers.size());

        /*if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            if(markers.size()!=0)
                ShowMapOnLolipop();
        } else{
            // do something for phones running an SDK before lollipop
            currentPt = 0;

            if(markers.size()!=0)
            {
               // startAnimation();
                setAnimation(directionPoint);

            }

        }*/
        ///Strat Animation for moving car
        setAnimation(directionPoint);


    }

    private static void ShowMapOnLolipop(){
        try {
//                Log.e("Inside ShowMapOnLolipop","////////////////////ShowMapOnLolipop///////////////////"+markers.get(0));

            googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(),15));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
  /*  private static void startAnimation() {
        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 15),
                5000,
                MyCancelableCallback);

      *//*  googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 18));*//*
        currentPt = 0-1;
    }*/

    private static Location convertLatLngToLocation(LatLng latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }

    private static float bearingBetweenLatLngs(LatLng begin, LatLng end) {
        Location beginL= convertLatLngToLocation(begin);
        Location endL= convertLatLngToLocation(end);
        return beginL.bearingTo(endL);
    }

    public void toggleStyle() {
        if (GoogleMap.MAP_TYPE_NORMAL == googleMap.getMapType()) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

   /* @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }*/
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
    /**
     * Clears all markers from the map.
     */
    public static void clearMarkers() {

        googleMap.clear();
        markers.clear();
    }
    /**
     * Remove the currently selected marker.
     */
    public void removeSelectedMarker() {
        this.markers.remove(this.selectedMarker);
        this.selectedMarker.remove();
    }

    /**
     * Highlight the marker by index.
     */
    private static void highLightMarker(int index) {
        highLightMarker(markers.get(index));
    }

    /**
     * Highlight the marker by marker.
     */
    private static void highLightMarker(Marker marker) {
        //marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        marker.showInfoWindow();
        selectedMarker=marker;
    }


   /* public void onDestroy() {
        super.onDestroy();
        finish();
    };
*/





    public static void setAnimation(final List<LatLng> directionPoint)
    {


        LatLng prev = null,current=null;
         if(currentPt++ < directionPoint.size() ) {
             current = directionPoint.get(currentPt);

             MarkerOptions mp = new MarkerOptions();
             mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(historyContext, customMarker())));
             mp.rotation(bearing);
             mp.snippet("Latitude : " + String.format("%.6f", current.latitude) + "\t" + "Longitude : " + String.format("%.6f", current.longitude));
             //mp.title("Speed : " + speed + " km/h" + "\t" + "Date : " + date + "\n" + "");

             final Marker marker = googleMap.addMarker(mp);
             googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 18), new GoogleMap.CancelableCallback() {

                 @Override
                 public void onFinish() {
                     animateMarker(googleMap, marker, directionPoint);
                 }
                 @Override
                 public void onCancel() {
                 }
             });




         }

    }


    private static void animateMarker(final GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint) {

        Pause.setVisibility(View.VISIBLE);
        Resume.setVisibility(View.VISIBLE);
        final boolean hideMarker = true;
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final Random rand=new Random();
        final float minX = 0.0f;
        final float maxX = 360.0f;
        final long duration = 1500*directionPoint.size();
     //   final long duration = 30000;
        ClearFlag=true;
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(RunnAnim=new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size()) {

                    marker.setPosition(directionPoint.get(i));


                }


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 1000);
                    Location Prevloc=null;
                    Location Currloc=null;
                    try
                    {
                        if ((i > 0) && i<directionPoint.size()) {


                            Prevloc = new Location(LocationManager.GPS_PROVIDER);
                            Prevloc.setLatitude((directionPoint.get(i).latitude));
                            Prevloc.setLongitude((directionPoint.get(i).longitude));
                            Currloc = new Location(LocationManager.GPS_PROVIDER);
                            Currloc.setLatitude((directionPoint.get(i+1) .latitude));
                            Currloc.setLongitude((directionPoint.get(i+1) .longitude));

                            if (bearing!=0.0)
                            marker.setRotation(bearing);
                            else
                            {
                                marker.setRotation(rand.nextFloat() * (maxX - minX) + minX);
                            }

                            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(i), 18));
                            if (Currloc != null && Prevloc != null) {
                                // mMap.clear();
                                bearing = Prevloc.bearingTo(Currloc);
                                double caldistdiff = distance(directionPoint.get(i).latitude, directionPoint.get(i).longitude, directionPoint.get(i + 1).latitude, directionPoint.get(i + 1).longitude, "K");
                                Log.e("polylineOptions========",""+caldistdiff*1000);

                                if (caldistdiff*1000<Common.PolylineDistLimit) {
                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.width(5);
                                    polylineOptions.geodesic(true);
                                    polylineOptions.color(historyContext.getResources().getColor(R.color.colorPrimaryDark));

                                    polylineOptions.add(directionPoint.get(i - 1), directionPoint.get(i));

                                    googleMap.addPolyline(polylineOptions);
                                }

                            }

                            caldist = distance(directionPoint.get(i).latitude,directionPoint.get(i).longitude,directionPoint.get(i+1).latitude,directionPoint.get(i+1).longitude, "K");

                        }




                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }


                HistoryDistance=HistoryDistance+caldist;

             //   Log.e("animateMarker--------------",LocationData_list.get(i).getTimestamp()+"  - "+LocationData_list.get(i).getSpeed()+"  - "+HistoryDistance);

                try{

                    datetime.setText(Common.getDateCurrentTimeZone(LocationData_list.get(i).getTimestamp())+"");
                    speed.setText("Speed:"+LocationData_list.get(i).getSpeed()+" km/h");

                    distance.setText("Distance:"+String.format("%.2f",HistoryDistance)+" km");
                }catch (Exception e){
                    e.printStackTrace();
                }

                i++;


                if (i==directionPoint.size())
                {
                    handler.removeMessages(0);
                    Pause.setVisibility(View.INVISIBLE);Resume.setVisibility(View.INVISIBLE);

                }
            }
        });
        googleMap.getUiSettings().setAllGesturesEnabled(true);

    }

    public static void clearHistoryData(){

        try {
            if (ClearFlag)
                handler.removeMessages(0);
            directionPoint.clear();
            datetime.setText("");
            speed.setText("");
            distance.setText("");
            HistoryDistance=0.0;
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void GetAllTrackperson() {


        reuestQueue = Volley.newRequestQueue(historyContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(historyContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST,Common.URL+"ParentAPI.asmx/GetTrackInfo" , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseData(response);


                pDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            parseData(new String(error.networkResponse.data));
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ParentId", Common.userid);

                System.err.println("Track person Req--- " + params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    protected void parseData(String result) {

        Log.e("HOmE Responce----", result);
        // TODO Auto-generated method stub
        if (tracklist.size()>0)
        {
            tracklist.clear();
            myAdapter.clear();
        }

        try {
            JSONArray joArray = new JSONArray(result);
            for (int i = 0; i < joArray.length(); i++) {
                JSONObject joObject = joArray.getJSONObject(i);
                DeviceDataDTO dmDetails = new DeviceDataDTO();
                if (i <= 0) {
                    defaultImage = joObject.getString("Photo").replaceAll("~", "").trim();
                    if (Common.roleid.contains("5")) {
                        Common.currTrack = "demo_student";

                    } else {
                        Common.currTrack = joObject.getString("StudentID");
                    }
                }
                dmDetails.setId(joObject.getString("StudentID"));
                dmDetails.setName(joObject.getString("Name"));

                dmDetails.setPath(joObject.getString("Photo").replaceAll("~", "").trim());
                dmDetails.setType(joObject.getString("Type"));
                dmDetails.setImei_no(joObject.getString("DeviceID"));

                tracklist.add(dmDetails);
            }


        } catch (Exception e) {
            Log.e("Exception", "" + e);
        } finally {
            //it work Better but take time to Load
            if (tracklist.size() > 0) {
                myAdapter = new HistoryMapAdapter(historyContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);

              //  personlist.requestFocusFromTouch();
                //personlist.setSelection(0);
             //   personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                cnt++;
            } else {
                Common.showToast("No User Information", historyContext);
            }

        }

    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        switch (position) {

            case 0:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 3:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        switch (position) {
            case 0:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 3:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
        rfabHelper.toggleContent();
    }


    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    //creating custom marker
    static View customMarker() {
        View marker = ((LayoutInflater) historyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
       /* ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
        childImage.setImageBitmap(bmp1);*/
        return marker;
    }




    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		/*::  This function converts decimal degrees to radians             :*/
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		/*::  This function converts radians to decimal degrees             :*/
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
