package com.primesys.VehicalTracking.VTSFragments;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primesys.VehicalTracking.Activity.APIController;
import com.primesys.VehicalTracking.Activity.DateTimeActivity;
import com.primesys.VehicalTracking.Activity.DeviceLevelRenewService;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Activity.RenewServiceActivity;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.MyAdpter.HistoryMapAdapter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 19/4/16.
 */
public class history_API_Fragment extends Fragment implements OnMapReadyCallback,RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener
{

    private static double HistoryDistance=0;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String TAG ="Request" ;
    private static final int TAG_CODE_PERMISSION_LOCATION = 200;
    public static int StudentId;
    private static Handler handler=new Handler();
    static Context historyContext;
    static GoogleMap googleMap ;
    static ArrayList<LocationData> LocationData_list;
    private static double caldist;
    private static Runnable RunnAnim;
    private static long Student_IMEI_no=0;
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
    private static DBHelper helper;
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
    static Button Pause;
    static Button Resume;
    public static TextView datetime,speed,distance;
   // private static ToggleButton togglelist;
    public  static DeviceDataDTO user1;
    private static DeviceDataDTO currenthistoryObj;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public  static long HistoryStartTime= Common.getStartOfDay(new Date());
    public  static long HistoryEndTime=Common.getEndOfDay(new Date());
    public static MyCustomProgressDialog TrackProgressDialog;
    private static CountDownTimer LOCATIONCDT;
    private SupportMapFragment mapFragment;
    private View mapView;
    static ToggleButton togglelist;
    // private SwitchDateTimeDialogFragment dateTimeFragment;
    List<RFACLabelItem> mapTypeItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static String KEY_MAP_DEFAULT="Map_defult";
    private SharedPreferences.Editor editor;
    private SearchView edit_search_device;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
         rootView = inflater.inflate(R.layout.activity_historytab, container, false);
        historyContext=this.getActivity();
        if (!isGooglePlayServicesAvailable()) {
            Common.ShowSweetAlert(historyContext, "Google Play services not available !");
        }


        findviewbyid();

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
                Common.ShowSweetAlert(historyContext, "No User Information");
            }

        }
            //Fab Button listner
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);



                personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            if (tracklist.get(position).getExpiary_date()==null){
                                tracklist.clear();
                                tracklist.addAll(PrimesysTrack.mDbHelper.Show_Device_list());

                            }
                            LoginActivity.mClient.sendMessage(StopTracKEvent());
                            currenthistoryObj = (DeviceDataDTO) tracklist.get(position);

                            Log.e(TAG,"currenthistoryObj--------"+currenthistoryObj.getExpiary_date());
                            if (Common.PlatformRenewalStatus) {
                                if (Integer.parseInt(currenthistoryObj.getRemaining_days_to_expire())>= 0) {
                                    try {
                                        //	ShowMapFragment gmap=new ShowMapFragment();
                                        //	gmap.flag=0;

                                         HistoryStartTime=Common.getStartOfDay(new Date());
                                        HistoryEndTime=Common.getEndOfDay(new Date());
                                        DateTimeActivity.selStatus = true;
                                        history_API_Fragment.user1 = tracklist.get(position);
                                        history_API_Fragment.StudentId = Integer.parseInt(user1.getId());
                                        history_API_Fragment.Student_IMEI_no = Long.parseLong(user1.getImei_no());

                                        Log.e("History studid---------", history_API_Fragment.StudentId + "----" + position);

                                        ShowMapFragment.Updatestatus = true;

                                        final String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
                                        clearHistoryData();

                                        //LoginActivity.mClient.sendMessage(makeJSONHistoryChild(formattedDate, history_API_Fragment.StudentId + ""));

                                        ShowDateTimeDialog();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    ShowPaymentDialog();
                                }
                            } else {
                                try {
                                    //	ShowMapFragment gmap=new ShowMapFragment();
                                    //	gmap.flag=0;
                                    DateTimeActivity.selStatus = true;
                                    history_API_Fragment.user1 = tracklist.get(position);
                                    history_API_Fragment.StudentId = Integer.parseInt(user1.getId());
                                    history_API_Fragment.Student_IMEI_no = Long.parseLong(user1.getImei_no());
                                    HistoryStartTime=Common.getStartOfDay(new Date());
                                    HistoryEndTime=Common.getEndOfDay(new Date());
                                    ShowMapFragment.Updatestatus = true;

                                    final String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
                                    clearHistoryData();

                                  //  LoginActivity.mClient.sendMessage(makeJSONHistoryChild(formattedDate, history_API_Fragment.StudentId + ""));

                                    ShowDateTimeDialog();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

        //You could set ListView Adapter here.

        togglelist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    personlist.setVisibility(View.VISIBLE);
                else
                    personlist.setVisibility(View.GONE);
            }

        });



        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    if (history_API_Fragment.StudentId!=0){

                                if (Common.PlatformRenewalStatus) {
                                    if (Integer.parseInt(currenthistoryObj.getRemaining_days_to_expire())>= 0) {
                                            clearHistoryData();
                                            Intent i=new Intent(historyContext,DateTimeActivity.class);
                                            //	Log.e("History studid  menu---------", ShowGmapClient.StudentId+"  "+HistoryMapAdapter.user1.getType());

                                            i.putExtra("StudentId", history_API_Fragment.StudentId);
                                            if (history_API_Fragment.user1!=null)
                                                i.putExtra("Type", history_API_Fragment.user1.getType());
                                            else
                                                i.putExtra("Type","demo_student");

                                            startActivityForResult(i,REQ_DATETIME);



                                    } else {
                                        ShowPaymentDialog();
                                    }
                                }else {

                                        clearHistoryData();
                                        Intent i=new Intent(historyContext,DateTimeActivity.class);
                                        //	Log.e("History studid  menu---------", ShowGmapClient.StudentId+"  "+HistoryMapAdapter.user1.getType());

                                        i.putExtra("StudentId", history_API_Fragment.StudentId);
                                        if (history_API_Fragment.user1!=null)
                                            i.putExtra("Type", history_API_Fragment.user1.getType());
                                        else
                                            i.putExtra("Type","demo_student");

                                        //  startActivityForResult(i,REQ_DATETIME);

                                        startActivityForResult(i,REQ_DATETIME);



                                }
                    }else {
                        Common.ShowSweetAlert(historyContext,"Please select your device.");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });





        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(RunnAnim);
                Pause.setVisibility(View.GONE);
                Resume.setVisibility(View.VISIBLE);
               /* IncomingSms sms=new IncomingSms();
                sms.ParseACCONMsg(historyContext, "ACC  !!!IMEI:355488020822828");*/
            }
        });

        Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pause.setVisibility(View.VISIBLE);
                Resume.setVisibility(View.GONE);
                handler.postDelayed(RunnAnim,100);

            }
        });

        //You could set ListView Adapter here.
        togglelist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    personlist.setVisibility(View.VISIBLE);
                    edit_search_device.setIconified(false);
                }
                else
                {
                    personlist.setVisibility(View.GONE);
                    edit_search_device.setIconified(true);

                }
            }

        });


        edit_search_device.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.filter(newText.toLowerCase());
                Log.e(PrimesysTrack.TAG, " setOnQueryTextListener----call ");
                return false;
            }
        });






        return rootView;
    }



    @Override
    public void onMapReady(GoogleMap Map) {
        this.googleMap = Map;

        try {
            initilizeMap();
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

            TrackProgressDialog.show();
            LOCATIONCDT = new CountDownTimer(Common.TrackReqTimeout*1000, 1000)
            {
                int i=0;
                public void onTick(long millisUntilFinished)
                {
                    i--;
                }

                public void onFinish()
                {
                    if (TrackProgressDialog.isShowing()) {
                        TrackProgressDialog.dismiss();
                        // Common.ShowSweetAlert(trackContext, "Looks like some N/W issues.Please try again.");
                    }
                }
            }.start();

        }
        catch(Exception e)
        {
                e.printStackTrace();
        }
        return trackSTring;
    }


    private void findviewbyid() {
        // Gets the MapView from the XML layout and creates it
        sharedPreferences=historyContext.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        edit_search_device=rootView.findViewById(R.id.edit_search_device);

        edit_search_device.setQueryHint(historyContext.getResources().getString(R.string.hint_seach_device));

        editor = sharedPreferences.edit();
        rfaLayout= (RapidFloatingActionLayout) rootView.findViewById(R.id.label_list_sample_rfal);
        rfaButton= (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        rfaContent = new RapidFloatingActionContentLabelList(historyContext);
       // googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        personlist = (ListView)rootView.findViewById(R.id.User_list);
        //togglelist = (ToggleButton) rootView.findViewById(R.id.tooglelist);
      //  togglelist.setChecked(true);
        fab_date= (FloatingActionButton)rootView.findViewById(R.id.fab_date);
        fab_date.setVisibility(View.GONE);
        datetime = (TextView) rootView.findViewById(R.id.datetime);
        speed = (TextView) rootView.findViewById(R.id.historyspeed);
        distance = (TextView) rootView.findViewById(R.id.distance);

        Pause=(Button)rootView.findViewById(R.id.btn_pause);
        Resume=(Button)rootView.findViewById(R.id.btn_resume);

        togglelist = (ToggleButton) rootView.findViewById(R.id.tooglelist);
        togglelist.setChecked(true);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.gmaphistoty);
        mapView = mapFragment.getView();

        mapFragment.getMapAsync((OnMapReadyCallback) this);
       
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Normal View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(0)
        );
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Satelite View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(1)
        );
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Terrain View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(2)
        );
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Hybrid View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(3)
        );
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Rail Track View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(4)
        );


        rfaContent
                .setItems(mapTypeItems)
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

        TrackProgressDialog=(MyCustomProgressDialog) MyCustomProgressDialog.ctor(historyContext);
        TrackProgressDialog.setCancelable(false);
        TrackProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



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

                if (sharedPreferences.contains(KEY_MAP_DEFAULT)){
                    int type=sharedPreferences.getInt(KEY_MAP_DEFAULT,0);
                    onRFACItemIconClick(type,mapTypeItems.get(type));
                }else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setMapStyle(null);
                }

          /*      if (mapView != null &&
                        mapView.findViewById(Integer.parseInt("1")) != null) {
                    // Get the button view
                    View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                            locationButton.getLayoutParams();
                    // position on right bottom
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    layoutParams.setMargins(0, 0, 20, 20);
                }

*/
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


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(historyContext);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 0).show();
            return false;
        }
    }

    public void onPause() {
        super.onPause();
        clearHistoryData();
    }


    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

     /*   getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    Home.Bottom_navigation.setSelectedItemId(R.id.navigation_track);
                    return true;
                }
                return false;
            }
        });*/
    }

      @Override
      public void onDestroy() {
          super.onDestroy();
      }

      @Override
      public void onLowMemory() {
          super.onLowMemory();
      }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
          /*  String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
            LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();

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
            e.printStackTrace();

        }
        return trackSTring;
    }
    //add Default Location
    public static void addDefaultLocations(ArrayList<LocationData> locationData) {

        if (LOCATIONCDT!=null)
            LOCATIONCDT.cancel();
        if (TrackProgressDialog!=null&&TrackProgressDialog.isShowing())
            TrackProgressDialog.dismiss();
      //  togglelist.setChecked(false);

        clearMarkers();
        LocationData_list=new ArrayList<LocationData>();
        LocationData_list.addAll(locationData);
        totalCount=LocationData_list.size();
		if(totalCount>0)
        {
            togglelist.setChecked(false);

            for (int i = 0; i < totalCount; i++) {
                addMarkerToMap(LocationData_list.get(i),i);
                directionPoint.add(new LatLng(LocationData_list.get(i).getLat(),LocationData_list.get(i).getLan()));
            }
            currentPt = 0;
            if(markers.size()!=0)
            {               // startAnimation();
                setAnimation(directionPoint);
            }

        }
        else {
            Common.ShowSweetAlert(historyContext, historyContext.getResources().getString(R.string.history_msg)+" "+currenthistoryObj.getName());

        }


    }

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



    /**
     * Adds a marker to the map.
     */
    public static void addMarkerToMap(LocationData data, int pos) {
        LatLng lat=new LatLng(data.getLat(),data.getLan());
        DateTimeActivity.selStatus=false;
        if(pos==0||pos==totalCount-1)
        {
            Marker marker = googleMap.addMarker(new MarkerOptions().position(lat)
                    .title("Speed is "+data.getSpeed()+Common.mesurment_unit)

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
                    .title("Speed is "+data.getSpeed()+Common.mesurment_unit)
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


        try{

            if(googleMap!=null)
                googleMap.clear();
            if (markers!=null)
                markers.clear();

        }catch (Exception e){
            e.printStackTrace();
        }



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


             Log.e("setAnimation-------","111111111111111111111111---------------------"+currentPt+"   directionPoint  --"+directionPoint.size());
             current = directionPoint.get(currentPt);

             MarkerOptions mp = new MarkerOptions();

             if (currenthistoryObj.getType().equalsIgnoreCase("Child"))
                 mp.position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerboy));
             else if (currenthistoryObj.getType().equalsIgnoreCase("Car"))
                 mp.position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_orange));
             else if (currenthistoryObj.getType().equalsIgnoreCase("Pet"))
             mp.position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker));

             mp.rotation(bearing);

             mp.snippet("Latitude : " + String.format("%.6f", current.latitude) + "\t" + "Longitude : " + String.format("%.6f", current.longitude));
             //   mp.title("Speed : " + speed + " km/h" + "\t" + "Date : " + date + "\n" + "");
             final Marker marker = googleMap.addMarker(mp);

            // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 18));


             googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 14), new GoogleMap.CancelableCallback() {

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
        try {
                Pause.setVisibility(View.VISIBLE);
                Resume.setVisibility(View.GONE);


            final boolean hideMarker = true;
            final long start = SystemClock.uptimeMillis();
            Projection proj = myMap.getProjection();
            final Random rand = new Random();
            final float minX = 0.0f;
            final float maxX = 360.0f;
            final long duration = 1100 * directionPoint.size();
            //   final long duration = 30000;
            ClearFlag = true;
            googleMap.getUiSettings().setAllGesturesEnabled(false);

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(RunnAnim = new Runnable() {
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
                        handler.postDelayed(this, 800);
                        Location Prevloc = null;
                        Location Currloc = null;
                        try {
                            if ((i > 0) && i < directionPoint.size() - 1) {

                                Prevloc = new Location(LocationManager.GPS_PROVIDER);
                                Prevloc.setLatitude((directionPoint.get(i).latitude));
                                Prevloc.setLongitude((directionPoint.get(i).longitude));
                                Currloc = new Location(LocationManager.GPS_PROVIDER);
                                Currloc.setLatitude((directionPoint.get(i + 1).latitude));
                                Currloc.setLongitude((directionPoint.get(i + 1).longitude));
                                if (bearing != 0.0)
                                    marker.setRotation(bearing);
                                else {
                                  //  marker.setRotation(rand.nextFloat() * (maxX - minX) + minX);
                                }
                                Float zoomlevel=myMap.getCameraPosition().zoom;



                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(directionPoint.get(i))      // Sets the center of the map to Mountain View
                                        .zoom(zoomlevel)                   // Sets the zoom
                                        .build();                   // Creates a CameraPosition from the builder
                                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                //   Log.e("animateMarker--------------", "----bearing-----------------" + bearing + "");

                                if (Currloc != null && Prevloc != null) {
                                    // mMap.clear();
                                    bearing = Prevloc.bearingTo(Currloc);
                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.width(5);
                                    polylineOptions.geodesic(true);
                                    polylineOptions.color(historyContext.getResources().getColor(R.color.colorPrimaryDark));
                                    polylineOptions.add(directionPoint.get(i - 1), directionPoint.get(i));

                                    googleMap.addPolyline(polylineOptions);
                                }
                                caldist = distance(directionPoint.get(i).latitude, directionPoint.get(i).longitude, directionPoint.get(i + 1).latitude, directionPoint.get(i + 1).longitude, "K");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setVisible(true);
                        }
                    }
                    HistoryDistance = HistoryDistance + caldist;
                    if (LocationData_list.size()>0&& LocationData_list.size()>i){
                        datetime.setText(Common.getDateCurrentTimeZone(LocationData_list.get(i).getTimestamp()) + "");
                        speed.setText("Speed:" + LocationData_list.get(i).getSpeed() + Common.mesurment_unit);
                        if (Common.mesurment_unit.contains("miles")){
                            distance.setText("Distance:" + String.format("%.2f", HistoryDistance) + " miles");

                        }else {
                            distance.setText("Distance:" + String.format("%.2f", HistoryDistance) + " km");

                        }
                    }

                    i++;
                    if (i == directionPoint.size()) {
                        handler.removeMessages(0);

                        Pause.setVisibility(View.INVISIBLE);
                        Resume.setVisibility(View.INVISIBLE);

                    }

                }
            });
            googleMap.getUiSettings().setAllGesturesEnabled(true);

        }catch (Exception e){
            e.printStackTrace();
        }
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
        pDialog.setCancelable(true);
        pDialog.show();

        //JSon object request for reading the json dat
        stringRequest = new StringRequest(Request.Method.POST,Common.URL+"ParentAPI.asmx/GetTrackInfo" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
                pDialog.hide();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error!=null)
                            Log.d("Error", error.toString());

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                Common.ShowSweetAlert(historyContext, "No User Information");
            }

        }

    }



    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {

        // Toast.makeText(trackContext, "clicked label: " + position, Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,0);
                    editor.commit();


                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 1:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,1);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 2:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,2);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 3:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,3);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                break;
            case 4:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setTrafficEnabled(true);
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    historyContext, R.raw.mapstyle_retro));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }

                    editor.putInt(KEY_MAP_DEFAULT,4);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                break;
            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
        rfabHelper.toggleContent();
        rfaLayout.collapseContent();

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {

        switch (position) {
            case 0:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,0);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 1:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,1);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 2:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,2);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 3:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    googleMap.setTrafficEnabled(true);
                    googleMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,3);
                    editor.commit();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                break;
            case 4:
                try {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setTrafficEnabled(true);
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    historyContext, R.raw.mapstyle_retro));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }

                    editor.putInt(KEY_MAP_DEFAULT,4);
                    editor.commit();
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                break;
            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
        rfabHelper.toggleContent();
        rfaLayout.collapseContent();

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


    public long GetDateDifference(Date endDate){

        //milliseconds
        long different = endDate.getTime() - System.currentTimeMillis();//Current_date.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
       /* different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
*/
        return elapsedDays;

    }


    private void ShowPaymentDialog() {

        new SweetAlertDialog(historyContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(String.valueOf(historyContext.getResources().getText(R.string.str_make_reachage_title)))
                .setContentText((String.valueOf(historyContext.getResources().getText(R.string.str_make_reachage))))
                .setCancelText("No,Exit !")
                .setConfirmText("Make Payment!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (Common.PlatformRenewalStatus){
                            Intent startMain = new Intent(historyContext,DeviceLevelRenewService.class);
                            startActivity(startMain);

                        }else {
                            Intent startMain = new Intent(historyContext,RenewServiceActivity.class);
                            startActivity(startMain);
                        }


                        sDialog.cancel();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.cancel();
                    }
                })
                .show();
    }

    //Stop track
    String StopTracKEvent() {
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "stop_track");
            trackSTring = jo.toString();

            Log.e(APIController.TAG,"------------************//Stoptrack calll");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackSTring;
        // TODO Auto-generated method stub

    }

    public void ShowDateTimeDialog(){

        try {
              new SingleDateAndTimePickerDialog.Builder(getActivity())

                        .maxDateRange(new Date(System.currentTimeMillis()))
                        .curved().title("Select Date and Time")

                        .defaultDate(new Date(System.currentTimeMillis()- TimeUnit.MINUTES.toMillis(15) ))
                        .minDateRange(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)))
                        .backgroundColor(getResources().getColor(R.color.white))
                        .mainColor(getResources().getColor(R.color.colorAccent))
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                GetHistoryData(date.getTime(), history_API_Fragment.StudentId, history_API_Fragment.Student_IMEI_no);

                            }
                        })

                        .display();





        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void GetHistoryData(final long time, final int studentId, final long student_IMEI_no) {
        reuestQueue = Volley.newRequestQueue(historyContext);
        final SweetAlertDialog pDialog = new SweetAlertDialog(historyContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Getting history data");
        pDialog.setCancelable(true);
        pDialog.show();

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "UserServiceAPI/GetHistorydata", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try{
                    Log.e(PrimesysTrack.TAG,"Track personHistory respo--- " + response);
                    pDialog.dismiss();

                    addDefaultLocations(parseHistoryData(response));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("StudentId", student_IMEI_no+"");
                params.put("StartDateTime", (String) (time+"").subSequence(0, 10));
                Log.e(PrimesysTrack.TAG,"Track personHistory Req--- " + params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }


    private ArrayList<LocationData>  parseHistoryData(String response) {
        ArrayList<LocationData> list=new ArrayList<>();
        try {
            JSONArray array=new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                LocationData l = new LocationData();

                if (jo.getString("lat_direction").equalsIgnoreCase("N")&&jo.getString("lan_direction").equalsIgnoreCase("E")) {

                    l.setLat(jo.getDouble("lat"));
                    l.setLan(jo.getDouble("lan"));

                }else if (jo.getString("lat_direction").equalsIgnoreCase("N")&&jo.getString("lan_direction").equalsIgnoreCase("W")) {

                    l.setLat(jo.getDouble("lat"));
                    l.setLan(-jo.getDouble("lan"));

                }
                else if (jo.getString("lat_direction").equalsIgnoreCase("S")&&jo.getString("lan_direction").equalsIgnoreCase("E")) {

                    l.setLat(-jo.getDouble("lat"));
                    l.setLan(jo.getDouble("lan"));

                }else if (jo.getString("lat_direction").equalsIgnoreCase("S")&&jo.getString("lan_direction").equalsIgnoreCase("W")) {

                    l.setLat(-jo.getDouble("lat"));
                    l.setLan(-jo.getDouble("lan"));

                }
                l.setSpeed(jo.getInt("speed"));
                l.setTimestamp(jo.getLong("timestamp"));
                list.add(l);
                //	Toast.makeText(login_context, "dATAC ---->  "+msg,Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }




    ///If the device is not connected to our portal and user try to Add / Update Geo Fence, error will be returned.

}
