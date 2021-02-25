package com.primesys.VehicalTracking.VTSFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.Speedometer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.DeviceLevelRenewService;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Activity.RenewServiceActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeoFencingHomeNew;
import com.primesys.VehicalTracking.ActivityMykiddyLike.Voice_Servilence;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.snapdto;
import com.primesys.VehicalTracking.GoogleDirection.DirectionsJSONParser;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.GPSEnableSetting;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;
import com.primesys.VehicalTracking.Utility.PolyUtil;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Double.parseDouble;

/**
 * Created by root on 19/4/16.
 */
public class ShowMapFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,
        OnMapReadyCallback, TextToSpeech.OnInitListener,GoogleMap.OnMarkerClickListener {

    private static final int TAG_CODE_PERMISSION_LOCATION = 100;
    public static int StudentId;
    public static String Photo;
    public static GoogleMap mMap;
    private static float bearing;
    private static PolylineOptions polylineOptions;
    private static LatLng current;
    private static ArrayList<String> addresslist=new ArrayList<>();
    private static String StudentName="";
    private static long datediff;
    private static ArrayList<LatLng> RoadSnapLatLngList=new ArrayList<>();
    private static CountDownTimer LOCATIONCDT;
    private static int StudentPosition;
    private static String KEY_LOCATION="Location";
    private static String KEY_MAP_DEFAULT="Map_defult";

    public static Bitmap bmp1;
    private static LatLng prev;
    public static int flag = 0;
    static String InvitedId;
    String path;
    private static int speed;
    private static String date;
    static MarkerOptions mp;
    static Marker mark;
    long freeSize = 0L;
    long totalSize = 0L;
    long usedSize = -1L;
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;
    public static Boolean Updatestatus = false, menuSelct;
    static int cntMap = 0;
    public Bitmap bitmap = null;
    static Context trackContext;
    private static DBHelper helper;
    private Toolbar toolbar;
    static ListView personlist;
    final String TAG = "REquest";
    public static ArrayList<DeviceDataDTO> tracklist = new ArrayList<DeviceDataDTO>();
    public static SharedPreferences sharedPreferences;
    static StringRequest stringRequest;
    static RequestQueue reuestQueue;
    public static SharedPreferences.Editor editor;
    private boolean isfirst;

    private NavigationView navigationView;
    private TextView txt_name, txt_email;
    private CircularNetworkImageView profile_pic;
    private ImageView cust_icon;
    private int cnt = 0;
    private static ShowMapAdapter myAdapter;
    public static Boolean trackInfo = false;
    private DrawerLayout drawer;
    private static ImageLoader imageLoader;
    private static String defaultImage;
    private RecyclerView menu_list;
    private static View rootView;
    private android.support.v4.app.FragmentManager mSupportMapFragment;
    private GoogleApiClient client;

    // @AIView(R.id.label_list_sample_rfal)
    private RapidFloatingActionLayout rfaLayout;
    //   @AIView(R.id.label_list_sample_rfab)
    private RapidFloatingActionButton rfaButton;

    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionContentLabelList rfaContent;
    private String photo;
    private static SpeedView SpeedMeter;
    public static CountDownTimer CDT;
    private String serverKey="AIzaSyA-RHgjK-VmzKaPvH_56cY2tpQXgzF291Y";
    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};
    private static Polyline PolylineDirection;
    private Marker Dir_origin_Marker,Dir_destination_Marker;

    static ArrayList<String> DeviceAllList=new ArrayList<>();
    public static List<LatLng> directionPositionList=new ArrayList<>();
    public static MyCustomProgressDialog TrackProgressDialog;
    private static TextToSpeech tts;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static DeviceDataDTO currentGmapObj=new DeviceDataDTO();
    public static  ToggleButton togglelist;
    private SupportMapFragment mapFragment;
    private View mapView;
    // private static ToggleButton togglelist;
    private static FloatingActionButton fab_all_device;
    SimpleDateFormat simpleyearDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  List<RFACLabelItem>  mapTypeItems=new ArrayList<>();
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private Marker showAllMarker;
    private SearchView edit_search_device;
    private int device_onCount=0;
    private int device_IdleCount=0;

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
    private TextView txt_on_off_device;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_show_map, container, false);
        trackContext = container.getContext();

        rfaContent = new RapidFloatingActionContentLabelList(trackContext);
        //Fab Button listner
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        setHasOptionsMenu(true);
        tts = new TextToSpeech(trackContext, this);
        TrackProgressDialog=(MyCustomProgressDialog) MyCustomProgressDialog.ctor(trackContext);
        TrackProgressDialog.setCancelable(false);
        TrackProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        powerManager = (PowerManager)trackContext.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();
        Findviewbyid();

        EnableLocationProvider();


        //  mMap.setInfoWindowAdapter(new Current_MarkerInfiWindow());

   /*     tracklist= PrimesysTrack.mDbHelper.Show_Device_list();
        if (Common.getConnectivityStatus(trackContext)&& tracklist.size()==0) {
            // Call Api to get track information
            try {
                GetAllTrackperson();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {

            if (tracklist.size() > 0) {
                myAdapter = new ShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);
                cnt++;
            } else {
                Common.ShowSweetAlert(trackContext, "No User Information");
            }


        }
*/

        if (Common.getConnectivityStatus(trackContext)) {
            try {
                if (!Common.userid.equalsIgnoreCase(""))
                    GetAllTrackperson();
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        Intent startMain = new Intent(trackContext, LoginActivity.class);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        trackContext.startActivity(startMain);

                    } else {
                        Intent startMain = new Intent(trackContext, LoginActivity.class);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        trackContext.startActivity(startMain);

                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowMapFragment.flag = 0;
                ShowMapFragment.prev = null;
                ShowMapFragment.current=null;

                txt_on_off_device.setVisibility(View.GONE);

                trackInfo = false;
                directionPositionList.clear();
                PolylineDirection=null;
                RoadSnapLatLngList.clear();
                ShowMapFragment.StudentPosition=position;
                currentGmapObj=tracklist.get(position);
                ShowMapFragment.StudentId = Integer.parseInt(currentGmapObj.getId());
                ShowMapFragment.StudentName=currentGmapObj.getName();
                ShowMapFragment.Photo = currentGmapObj.getPath().replaceAll(" ", "%20");
                if(mMap!=null)
                    mMap.clear();
                getActivity().invalidateOptionsMenu();
                Log.e("Expiary===Track====","------------ "+currentGmapObj.getRemaining_days_to_expire()+"--"+currentGmapObj.getType());
                try {

                    if (Common.PlatformRenewalStatus) {
                        if (Integer.parseInt(currentGmapObj.getRemaining_days_to_expire())>= 0) {
                            if (LoginActivity.mClient!=null) {
                                LoginActivity.mClient.sendMessage(StopTracKEvent());
                                LoginActivity.mClient.sendMessage(makeJSON(currentGmapObj.getId()));
                            }

                        } else {
                            ShowPaymentDialog();
                        }
                    }else {
                        if (LoginActivity.mClient!=null)
                        {

                            LoginActivity.mClient.sendMessage(StopTracKEvent());
                            LoginActivity.mClient.sendMessage(makeJSON(currentGmapObj.getId()));

                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //You could set ListView Adapter here.
        togglelist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    personlist.setVisibility(View.VISIBLE);
                    // edit_search_device.setIconified(false);
                    edit_search_device.setVisibility(View.VISIBLE);
                }
                else
                {
                    personlist.setVisibility(View.GONE);
                    edit_search_device.setIconified(true);
                    edit_search_device.setVisibility(View.GONE);
                }
            }

        });

        fab_all_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.mClient!=null)
                    LoginActivity.mClient.sendMessage(StopTracKEvent());
                device_onCount=0;
                device_IdleCount=0;
                GetAllDeviceLocation();
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

        //client = new GoogleApiClient.Builder(this.getActivity()).addApi(AppIndex.API).build();
        return rootView;


    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* try {
            LoginActivity.mClient.sendMessage(StopTracKEvent());

        }catch (Exception e){e.printStackTrace();}
*/
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (LoginActivity.mClient!=null)
            LoginActivity.mClient.sendMessage(StopTracKEvent());

        }catch (Exception e){e.printStackTrace();}
    }

    private void Findviewbyid() {
        sharedPreferences=trackContext.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        edit_search_device=rootView.findViewById(R.id.edit_search_device);

        edit_search_device.setQueryHint(trackContext.getResources().getString(R.string.hint_seach_device));
        txt_on_off_device=rootView.findViewById(R.id.txt_on_off_device);

        editor = sharedPreferences.edit();
        SpeedMeter=(SpeedView)rootView.findViewById(R.id.speedView);
        SpeedMeter.setUnit(Common.mesurment_unit);
        SpeedMeter.setPadding(10,0,10,0);
        SpeedMeter.setUnitUnderSpeedText(true);
        SpeedMeter.setSpeedTextPosition(Speedometer.Position.BOTTOM_CENTER);
        togglelist = (ToggleButton) rootView.findViewById(R.id.tooglelist);
        togglelist.setChecked(true);
        // SpeedMeter.setTextSize((float) (height*0.05));
        rfaLayout = (RapidFloatingActionLayout) rootView.findViewById(R.id.label_list_sample_rfal);
        rfaButton = (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapView = mapFragment.getView();

        mapFragment.getMapAsync((OnMapReadyCallback) this);
        personlist = (ListView) rootView.findViewById(R.id.User_list);
        //////FOr Fab Button

       mapTypeItems = new ArrayList<>();
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Normal View")
                .setResId(R.drawable.map_type)
                .setIconNormalColor(R.color.colorPrimary)
                .setIconPressedColor(R.color.colorPrimary)
                .setLabelColor(R.color.colorPrimary)
                .setWrapper(0)
        );
        mapTypeItems.add(new RFACLabelItem<Integer>()
                .setLabel("Satellite View")
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
                .setIconShadowRadius(ABTextUtil.dip2px(trackContext, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(trackContext, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                trackContext,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
        fab_all_device=rootView.findViewById(R.id.fab_all_device);

        rfaButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if (fab_all_device.getVisibility()==View.GONE){
                        fab_all_device.setVisibility(View.VISIBLE);

                    }else {
                        fab_all_device.setVisibility(View.GONE);

                    }
                }

                return false;
            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }


/*
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
*/

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 0).show();
            return false;
        }
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
        view.draw(canvas);

        return bitmap;
    }




    private void EnableLocationProvider() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) trackContext.getSystemService(trackContext.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {


        }
        if (gps_enabled != true && network_enabled != true) {
            GPSEnableSetting go = new GPSEnableSetting();
            go.GPSDialog(trackContext);
        }
    }



    private void GetAllTrackperson() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(trackContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
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
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ParentId", Common.userid);

                Log.e(PrimesysTrack.TAG,"GetAllTrackperson Child data req--------------"+params+Common.roleid);
                return params;
            }
        };
        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        PrimesysTrack.mRequestQueue.add(stringRequest);


    }

    protected static void parseData(String result) {
        Log.e(PrimesysTrack.TAG,"GetAllTrackperson Child data --------------"+result);

        // TODO Auto-generated method stub
        if (tracklist.size()>0)
        {
            tracklist.clear();
            if(myAdapter!=null)
                myAdapter.clear();
        }

        try {
            JSONArray joArray = new JSONArray(result);
            Log.e(PrimesysTrack.TAG,"GetAllTrackperson Child data --------size------"+joArray.length());

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
                if (result.contains("ExpiryDate")){
                    dmDetails.setExpiary_date(joObject.getString("ExpiryDate"));
                }else dmDetails.setExpiary_date("00-00-0000");
                if (result.contains("ActivationStatus")){
                    dmDetails.setRemaining_days_to_expire(joObject.getString("ActivationStatus"));
                }else dmDetails.setRemaining_days_to_expire("0");
                dmDetails.setShowGoogleAddress(joObject.getString("ShowGoogleAddress"));

                //   dmDetails.setExpiary_date("10-03-2017")
                tracklist.add(dmDetails);
            }


            if (tracklist.size() > 0) {

                if (tracklist.size()==1)
                    fab_all_device.setVisibility(View.GONE);
                else
                    fab_all_device.setVisibility(View.VISIBLE);

                myAdapter = new ShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);
                setOfflineLocation();


                // personlist.requestFocusFromTouch();
                // personlist.setSelection(0);
                //  currentGmapObj=tracklist.get(0);

                //  personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                // cnt++;

                PrimesysTrack.mDbHelper.Insert_Device_list(tracklist);
                //    CallDevicestatusAPI();

                // update_device_status();
            } else {
                Common.ShowSweetAlert(trackContext, "No User Information");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private static void setOfflineLocation() {

        if (sharedPreferences.contains(KEY_LOCATION))
        {
            //mMap.setInfoWindowAdapter(new Current_MarkerInfoWindow());
            String offline_lovcation=sharedPreferences.getString(KEY_LOCATION,"");
            try {
                mMap.setTrafficEnabled(true);
                currentGmapObj = tracklist.get(0);
                ShowMapFragment.StudentName = currentGmapObj.getName();
                if (!offline_lovcation.equalsIgnoreCase("")) {

                    JSONObject jo = new JSONObject(offline_lovcation);
                    JSONObject jData = jo.getJSONObject("data");
                    String lat = "0", lan = "0";

                    if (jData.getString("lat_direction").equalsIgnoreCase("N") && jData.getString("lan_direction").equalsIgnoreCase("E")) {
                        lat = jData.getString("lat");
                        lan = jData.getString("lan");
                    } else if (jData.getString("lat_direction").equalsIgnoreCase("N") && jData.getString("lan_direction").equalsIgnoreCase("W")) {

                        lat = jData.getString("lat");
                        lan = "-" + jData.getString("lan");
                    } else if (jData.getString("lat_direction").equalsIgnoreCase("S") && jData.getString("lan_direction").equalsIgnoreCase("E")) {

                        lat = "-" + jData.getString("lat");
                        lan = jData.getString("lan");

                    } else if (jData.getString("lat_direction").equalsIgnoreCase("S") && jData.getString("lan_direction").equalsIgnoreCase("W")) {
                        lat = "-" + jData.getString("lat");
                        lan = "-" + jData.getString("lan");
                    }


                    speed = jData.getInt("speed");
                    date = Common.getDateCurrentTimeZone(jData.getLong("timestamp"));
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    datediff = Common.getGMTTimeStampFromDate((sdf.format(timestamp))) - (jData.getLong("timestamp"));
                    ShowMapFragment.updateGoogleMapLocation(lat, lan);
                } else {

                    Common.ShowSweetAlert(trackContext, "Please select device to get location.");
                    setCurrentLocation();


                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            Toast.makeText(trackContext,"Tap on device to get real time location.",Toast.LENGTH_SHORT).show();
         //   Common.ShowSweetSucess(trackContext,"Tap on device to get real time location.");


        }else {


            setCurrentLocation();
            Toast.makeText(trackContext,"Tap on device to get real time location.",Toast.LENGTH_SHORT).show();

            // Common.ShowSweetSucess(trackContext,"Tap on device to get real time location.");
        }

    }

    public static void setCurrentLocation(){
        try{

            Location location = Common.getLocation(trackContext);
            if (location!=null){
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                LatLng loc = new LatLng(lat, lng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(loc)      // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setTrafficEnabled(true);

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected String makeJSON(String Id) {
        String trackSTring = "{}";
      //  mMap.setInfoWindowAdapter(new Current_MarkerInfoWindow());

        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "start_track");
            if (Common.roleid.equals("5")) {
                jo.put("student_id", "demo_student");
                Common.currTrack = "demo_student";
            } else {
                jo.put("student_id", Integer.parseInt(Id));
                Common.currTrack = Id;
            }
            trackSTring = jo.toString();
        } catch (Exception e) {

        }

        if (mMap != null) {
            //   showCurrentLocation();
            // check if map is created successfully or not
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.setInfoWindowAdapter(new Current_MarkerInfoWindow());

                }
            });

        }
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

        return trackSTring;
    }


    //Stop track
    String StopTracKEvent() {
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "stop_track");
            trackSTring = jo.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackSTring;
        // TODO Auto-generated method stub

    }



    public static void changeLocation(String message) {
        Log.e("Show map frhgament---",message);

        System.out.print("Event Received");
        try {


            JSONObject jo = new JSONObject(message);
            JSONObject jData = jo.getJSONObject("data");
            String lat="0",lan="0";

            if (jData.getString("lat_direction").equalsIgnoreCase("N")&&jData.getString("lan_direction").equalsIgnoreCase("E")) {
                lat=jData.getString("lat");
                lan=jData.getString("lan");
            }else if (jData.getString("lat_direction").equalsIgnoreCase("N")&&jData.getString("lan_direction").equalsIgnoreCase("W")) {

                lat=jData.getString("lat");
                lan="-"+jData.getString("lan");
            }
            else if (jData.getString("lat_direction").equalsIgnoreCase("S")&&jData.getString("lan_direction").equalsIgnoreCase("E")) {

                lat="-"+jData.getString("lat");
                lan=jData.getString("lan");

            }else if (jData.getString("lat_direction").equalsIgnoreCase("S")&&jData.getString("lan_direction").equalsIgnoreCase("W")) {
                lat="-"+jData.getString("lat");
                lan="-"+jData.getString("lan");
            }


            speed = jData.getInt("speed");
            date = Common.getDateCurrentTimeZone(jData.getLong("timestamp"));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            datediff=  Common.getGMTTimeStampFromDate((sdf.format(timestamp)))-(jData.getLong("timestamp"));
            ShowMapFragment.updateGoogleMapLocation(lat, lan);



            // System.err.print("changeLocation"+  message);
            Common.Location_getting = false;

            if(ShowMapFragment.StudentPosition==0){
                editor.putString(KEY_LOCATION,message);
                editor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
      /* //GetJson History
         String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());

         LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));
         */

        }
    }




    public static void updateGoogleMapLocation(String lat, String lan) {
        if (LOCATIONCDT!=null)
            LOCATIONCDT.cancel();


        if (TrackProgressDialog!=null&&TrackProgressDialog.isShowing())
        {

            TrackProgressDialog.dismiss();

        }

        if (currentGmapObj.getType().equalsIgnoreCase("Car")){
            SpeedMeter.setVisibility(View.VISIBLE);
        }else {
            SpeedMeter.setVisibility(View.GONE);
        }

        try {

            current = new LatLng(parseDouble(lat), parseDouble(lan));
            addresslist= Common.getAddress(trackContext,current.latitude,current.longitude);
            mMap.setTrafficEnabled(true);
            if (speed>0&&datediff< Common.MarkerTimeDiff){
                SpeedMeter.speedTo(speed,4000);
                SpeedMeter.setWithTremble(true);
            }
            else if (speed==0&&datediff< Common.MarkerTimeDiff)
            {
                SpeedMeter.speedTo(speed);
                SpeedMeter.setWithTremble(false);
            } else if (datediff> Common.MarkerTimeDiff)
            {
                SpeedMeter.speedTo(0);
                SpeedMeter.setWithTremble(false);
            }

            if(flag==0)  //when the first update comes, we have no previous points,hence this
            {

                try {
                    mMap.clear();
                    togglelist.setChecked(false);

                    flag=1;
                    cntMap=1;
                    mp = new MarkerOptions();
                    mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
                    mp.flat(true);
                    prev=current;
                    //CallDevicestatusAPI();
                    //   update_device_status();
                    mark=mMap.addMarker(mp);
                    mark.showInfoWindow();

                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(parseDouble(lat), parseDouble(lan)))      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the zoom
                        .bearing(bearing)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
            else
            {
                try {
                    /*if (cntMap == 1) {
                     *//* mark = mMap.addMarker(mp);
                        cntMap++;*//*
                    } else {*/
                    try {
                          /* mark.setIcon(BitmapDescriptorFactory.fromBitmap(bmpDefaulr));
                            mMap.addPolyline((new PolylineOptions())
                                    .add(prev, current).width(6).color(Color.CYAN)
                                    .visible(true));*/
                        Location Prevloc = null, Currloc = null;
                        if (prev != null && current != null) {
                            Prevloc = new Location(LocationManager.GPS_PROVIDER);
                            Prevloc.setLatitude(prev.latitude);
                            Prevloc.setLongitude(prev.longitude);
                            Currloc = new Location(LocationManager.GPS_PROVIDER);
                            Currloc.setLatitude(current.latitude);
                            Currloc.setLongitude(current.longitude);
                        }


                        if (Currloc != null && Prevloc != null) {
                            // mMap.clear();
                            bearing = Prevloc.bearingTo(Currloc);

                            if (speed>0&&datediff< Common.MarkerTimeDiff&&currentGmapObj.getType().equalsIgnoreCase("Car")){
                                GetRoadsnap(prev,current);
                                prev = current;

                            }
                            else {

                                Log.e("-------LocTrack---","FLag-----------11111 ELse");

                                if (currentGmapObj.getType().equalsIgnoreCase("Car{")){
                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.width(5);
                                    polylineOptions.geodesic(true);
                                    polylineOptions.color(trackContext.getResources().getColor(R.color.colorPrimaryDark));
                                    if (RoadSnapLatLngList.size()>0)
                                        polylineOptions.add(RoadSnapLatLngList.get(RoadSnapLatLngList.size()-1), current);
                                    else
                                        polylineOptions.add(prev, current);
                                    if (speed>0)
                                        mMap.addPolyline(polylineOptions);
                                }else {

                                    mMap.addMarker(new MarkerOptions().position(prev)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_overlay))

                                    ).setFlat(true);

                                }

                                prev = current;
                                mark.remove();
                                mp = new MarkerOptions();
                                mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
                                mp.rotation(bearing);
                                mp.flat(true);
                                if (speed!=0)
                                    SpeedMeter.speedTo(speed);
                                else {
                                    SpeedMeter.speedTo(0);
                                    SpeedMeter.setWithTremble(false);
                                }
                                mark = mMap.addMarker(mp);

                            }

                        }

                        //  mark.showInfoWindow();
                        current = null;
                        Currloc=null;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Float zoomlevel=mMap.getCameraPosition().zoom;
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan)), (float) 13));
                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(parseDouble(lat), parseDouble(lan)))      // Sets the center of the map to Mountain View
                        .zoom(zoomlevel)                   // Sets the zoom
                        .bearing(bearing)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }



        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapView = mapFragment.getView();

        Log.e(PrimesysTrack.TAG,"googleMap----"+mMap+"----"+mapView);
        if (ContextCompat.checkSelfPermission(trackContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(trackContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            if (sharedPreferences.contains(KEY_MAP_DEFAULT)){
                int type=sharedPreferences.getInt(KEY_MAP_DEFAULT,0);
                onRFACItemIconClick(type,mapTypeItems.get(type));
            }else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMapStyle(null);
            }
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                // position on right bottom

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    fab_all_device.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }

        try {

            InvitedId =StudentId+"";
            photo = Photo;
            if (mMap != null) {
                //   showCurrentLocation();
                // check if map is created successfully or not
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.setInfoWindowAdapter(new Current_MarkerInfoWindow());

                    }
                });

            }else {
                new SweetAlertDialog(trackContext, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                        .setContentText("Sorry! unable to create maps !")
                        .show();
            }

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);



            //    mMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public long getUsedMemorySize() {
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


    //Update device status
    public static void update_device_status(){
        if (CDT!=null)
            CDT.cancel();
        CDT = new CountDownTimer(Common.DeviceStatusReq_Time*1000, 1000)
        {
            int i=0;
            public void onTick(long millisUntilFinished)
            {
                i--;
            }

            public void onFinish()
            {
                CallDevicestatusAPI();
            }
        }.start();
    }

    private static void CallDevicestatusAPI()
    {

        reuestQueue = Volley.newRequestQueue(trackContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "UserServiceAPI/Get_DeviceStatus", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parse_DeviceStatus_Data(response);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                for ( DeviceDataDTO dto:tracklist) {

                    DeviceAllList.add(dto.getImei_no());
                }
                params.put("Devicelist", new Gson().toJson(DeviceAllList));

                System.err.println("Devicelist all Req--- " + params);
                return params;
            }
        };
        stringRequest.setTag("ShoWmap");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);



    }

    private static void parse_DeviceStatus_Data(String response) {

        try {
            JSONArray jarray=new JSONArray(response);
            for(int j=0;j<jarray.length();j++) {
                JSONObject jo=jarray.getJSONObject(j);

                for (int i = 0; i < tracklist.size(); i++) {
                    if (tracklist.get(i).getImei_no().equals(jo.getString("DeviceID"))){
                        tracklist.get(i).setStatus(jo.getString("Status"));
                        tracklist.get(i).setColor(jo.getString("Color"));
                    }

                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            myAdapter.notifyDataSetChanged();
            update_device_status();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        flag = 0;
        // LoginActivity.mClient.sendMessage(StopTracKEvent());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client.disconnect();
    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
      *//*LoginActivity.mClient.sendMessage(makeJSON());*//*
        LoginActivity.mClient.sendMessage(restartTrackEvent());
    }
*/


    public void onTabSelected(ShowMapFragment tab) {
        Fragment fragment = null;
        fragment = new ShowMapFragment();
        replaceRootFragment(fragment);
    }

    public void replaceRootFragment(Fragment fragment) {
        if (LoginActivity.mClient!=null)
            LoginActivity.mClient.sendMessage(restartTrackEvent());
    }






/*
    //creating custom marker
    @SuppressLint("InflateParams")
    View customMarker()
    {        CircularNetworkImageView netCircle=new CircularNetworkImageView(contextMap);

        View marker = ((LayoutInflater)trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);

        Log.e("---------Photo----------", photo + "");
        if(photo!=null && !photo.equals("null")){
            // Picasso.with(context).load(Common.Relative_URL+photo).into(proimg);
            Picasso.with(contextMap).load(Common.Relative_URL+getIntent().getStringExtra("photo")).transform(new CircleTransform()).into(childImage);

        }else{
            bitmap = ((BitmapDrawable)childImage.getDrawable()).getBitmap();
            childImage.setImageBitmap(netCircle.getRoundedShape(bitmap));
        }
        return marker;
    }
*/

    //creating custom marker
    static View customMarker() {
        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);


        if (currentGmapObj==null)
            currentGmapObj=tracklist.get(0);
        Log.e(PrimesysTrack.TAG,"customMarker----------"+currentGmapObj.getType());
        if (currentGmapObj.getType().equalsIgnoreCase("Child"))
            childImage.setImageResource(R.drawable.ic_markerboy);
        else if (currentGmapObj.getType().equalsIgnoreCase("Car")){
            if (speed>0&&datediff<Common.MarkerTimeDiff)
                childImage.setImageResource(R.drawable.car_green);
            else if (speed==0&&datediff<Common.MarkerTimeDiff)
                childImage.setImageResource(R.drawable.car_blue);
            else if (datediff>Common.MarkerTimeDiff)
                childImage.setImageResource(R.drawable.car_red);
        }
        else if (currentGmapObj.getType().equalsIgnoreCase("Pet"))
            childImage.setImageResource(R.mipmap.ic_icon);

        return marker;
    }


    protected String restartTrackEvent() {
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "start_track");
            if (Common.currTrack.matches(".*\\d.*")) {
                jo.put("student_id", Integer.parseInt(Common.currTrack));
            } else {
                jo.put("student_id", Common.currTrack);
            }
            trackSTring = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackSTring;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = 0;
            result = tts.setLanguage(Locale.ENGLISH);


            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                // speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    public  class Current_MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        Current_MarkerInfoWindow(){

            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.custom_info_live_track, null);
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            myContentsView.setLayoutParams(new FrameLayout.LayoutParams(width/2,ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView veh_name = ((TextView)myContentsView.findViewById(R.id.name));
            TextView alarm_time = ((TextView)myContentsView.findViewById(R.id.speed));
            TextView address = ((TextView)myContentsView.findViewById(R.id.address));
            TextView speedtext = ((TextView)myContentsView.findViewById(R.id.alarmtime));
            veh_name.setText(ShowMapFragment.StudentName);

            alarm_time.setText(date);
            speedtext.setText(speed+Common.mesurment_unit);

            if (addresslist.size()>0)
            {
                address.setText(addresslist.get(0)+addresslist.get(1).replaceAll(" ", ""));
                Log.e("Current_MarkerInfiW", "Address------" + addresslist.get(0)+addresslist.get(1).replaceAll(" ", ""));

            }


            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
/*
            TextView veh_name = ((TextView)myContentsView.findViewById(R.id.name));
            TextView alarm_time = ((TextView)myContentsView.findViewById(R.id.alarmtime));
            TextView address = ((TextView)myContentsView.findViewById(R.id.address));
            TextView speedtext = ((TextView)myContentsView.findViewById(R.id.speed));



            veh_name.setText(GShowMapFragment.StudentName);

            alarm_time.setText(date);
            speedtext.setText(speed+" Km/h");

            if (addresslist.size()>0)
                address.setText(addresslist.get(0)+addresslist.get(1).replaceAll(" ", ""));

            return myContentsView;*/
            return null;
        }

    }



    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {

        // Toast.makeText(trackContext, "clicked label: " + position, Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    try {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mMap.setTrafficEnabled(true);
                        mMap.setMapStyle(null);
                        editor.putInt(KEY_MAP_DEFAULT,0);
                        editor.commit();
                        fab_all_device.setVisibility(View.VISIBLE);


                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
                    }                        break;
                case 1:
                    try {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        mMap.setTrafficEnabled(true);
                        mMap.setMapStyle(null);
                        editor.putInt(KEY_MAP_DEFAULT,1);
                        editor.commit();
                        fab_all_device.setVisibility(View.VISIBLE);

                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
                    }                        break;
                case 2:
                    try {
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        mMap.setTrafficEnabled(true);
                        mMap.setMapStyle(null);
                        editor.putInt(KEY_MAP_DEFAULT,2);
                        editor.commit();
                        fab_all_device.setVisibility(View.VISIBLE);

                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
                    }                        break;
                case 3:
                    try {
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mMap.setTrafficEnabled(true);
                        mMap.setMapStyle(null);
                        editor.putInt(KEY_MAP_DEFAULT,3);
                        editor.commit();
                        fab_all_device.setVisibility(View.VISIBLE);

                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
                    }
                    break;
                case 4:
                    try {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mMap.setTrafficEnabled(true);
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        trackContext, R.raw.mapstyle_retro));

                        if (!success) {
                            Log.e(TAG, "Style parsing failed.");
                        }

                        editor.putInt(KEY_MAP_DEFAULT,4);
                        editor.commit();
                        fab_all_device.setVisibility(View.VISIBLE);

                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "Can't find style. Error: ", e);
                    }                break;
                default:
                    try{
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

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
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setTrafficEnabled(true);
                    mMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,0);
                    editor.commit();
                    fab_all_device.setVisibility(View.VISIBLE);

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 1:
                try {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mMap.setTrafficEnabled(true);
                    mMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,1);
                    editor.commit();
                    fab_all_device.setVisibility(View.VISIBLE);

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 2:
                try {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    mMap.setTrafficEnabled(true);
                    mMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,2);
                    editor.commit();
                    fab_all_device.setVisibility(View.VISIBLE);

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                        break;
            case 3:
                try {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    mMap.setTrafficEnabled(true);
                    mMap.setMapStyle(null);
                    editor.putInt(KEY_MAP_DEFAULT,3);
                    editor.commit();
                    fab_all_device.setVisibility(View.VISIBLE);

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                break;
            case 4:
                try {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setTrafficEnabled(true);
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    trackContext, R.raw.mapstyle_retro));
                    fab_all_device.setVisibility(View.VISIBLE);

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }

                    editor.putInt(KEY_MAP_DEFAULT,4);
                    editor.commit();
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
        rfabHelper.toggleContent();
        rfaLayout.collapseContent();
    }



    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }


    private void ShowDirectionAddDialog() {

        final EditText txt_pin;
        final Dialog dialog = new Dialog(trackContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mailid);
        final TextView tv_title=(TextView)dialog.findViewById(R.id.d_title);
        tv_title.setText("Destination Address");

        txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);
        txt_pin.setHint("Enter Destination Address.");
        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);
        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String dest_address = txt_pin.getText().toString();
                    if (dest_address.length() != 0) {
                  /*  GetVehMonthlyTotalKm(startTimeStampFromDate,endStampFromDate,MailId);
                    Common.ShowSweetSucess(mContext,"Monthly mileage report is sent to "+MailId+ ".");*/

                        final LatLng origin = mp.getPosition();
                        final LatLng destination = getLocationFromAddress(trackContext, dest_address);

                        GoogleDirection.withServerKey(serverKey)
                                .from(origin)
                                .to(destination).transportMode(TransportMode.DRIVING).execute(new DirectionCallback() {
                                    @Override
                                    public void onDirectionSuccess(Direction direction, String rawBody) {
                                        // Do something here

                                        String status = direction.getStatus();

                                        Gson gson = new Gson();

                                        System.out.println("***********onDirectionSuccess***********" + status);
                                        if (direction.isOK()) {
                                            try {
                                                if (PolylineDirection != null && PolylineDirection.isVisible())
                                                    PolylineDirection.remove();
                                                if (Dir_destination_Marker != null && Dir_origin_Marker != null) {
                                                    Dir_origin_Marker.remove();
                                                    Dir_destination_Marker.remove();

                                                }

                                                JSONObject jObject = new JSONObject(gson.toJson(direction));
                                                DirectionsJSONParser parser = new DirectionsJSONParser();

                                                // Starts parsing data
                                                List<List<HashMap<String, String>>> routes = parser.parse(jObject);

                                                Log.e("Dtata parse ----", routes.toString());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                ShowTotalDistToast();
                                            }


                                            Dir_destination_Marker = mMap.addMarker(new MarkerOptions().position(destination));


                                            directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();

                                            mMap.addPolyline(new PolylineOptions().add(origin, directionPositionList.get(0)));
                                            PolylineDirection = mMap.addPolyline(DirectionConverter.createPolyline
                                                    (trackContext, (ArrayList<LatLng>) directionPositionList,
                                                            5,
                                                            Color.RED));
                                            //  direction.getGeocodedWaypointList()


                                        }
                                    }

                                    @Override
                                    public void onDirectionFailure(Throwable t) {
                                        System.out.println("***********onDirectionFailure*************************" + t);
                                    }
                                });


                        dialog.dismiss();


                    } else
                        Common.ShowSweetAlert(trackContext, "Please enter address.");


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

        dialog.show();
    }

    private void ShowTotalDistToast() {

/*

        LayoutInflater inflate=((Activity)trackContext).getLayoutInflater();
        View layout=inflate.inflate(R.layout.custom_toast, (ViewGroup)((Activity)trackContext).findViewById(R.id.layout));
        TextView text = (TextView) layout.findViewById(R.id.text);

        text.setText(message);
        Toast toast=Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.show();
*/

    }


    //FOr gett road snap
    private static void GetRoadsnap(LatLng prev1, final LatLng current) {
        reuestQueue = Volley.newRequestQueue(trackContext);

        String URL="https://roads.googleapis.com/v1/snapToRoads?path="+prev1.latitude+","+prev1.longitude+"|"+current.latitude+","+current.longitude+"&interpolate=true&key="+trackContext.getResources().getString(R.string.road_snap_api_key);
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("Responce---Getsnap******************************------"+response);

                Parseroadsnap(response,current);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());

                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {

        };
        stringRequest.setTag("ShowmapSnap");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);



    }


    private static void Parseroadsnap(String response, LatLng current) {
        ArrayList<snapdto> RoadSnapList=new ArrayList<>();
        RoadSnapLatLngList=new ArrayList<>();
        try {
            JSONObject jomain=new JSONObject(response);
            JSONArray Jsanplist=jomain.getJSONArray("snappedPoints");

            if (Jsanplist.length()>0){
                RoadSnapLatLngList.clear();
                for (int i=0;i< Jsanplist.length();i++){
                    snapdto dto=new snapdto();
                    JSONObject jo= ((JSONObject) Jsanplist.get(i));
                    JSONObject jolocation= (JSONObject) jo.get("location");

                    dto.setLocation(new LatLng((Double)(jolocation.get("latitude")), (Double) (jolocation.get("longitude"))));
                    //  dto.setOriginalIndex(jo.get("originalIndex").toString());
                    RoadSnapLatLngList.add(dto.getLocation());
                    dto.setPlaceId(jo.get("placeId").toString());
                    RoadSnapList.add(dto);

                }

                mark.remove();

                mp = new MarkerOptions();
                mp.position(RoadSnapLatLngList.get(RoadSnapLatLngList.size()-1)).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
                mp.rotation(bearing);
                mp.flat(true);
                SpeedMeter.speedTo(speed);
                polylineOptions =DirectionConverter.createPolyline(trackContext, RoadSnapLatLngList,5,trackContext.getResources().getColor(R.color.colorPrimaryDark));

                mMap.addPolyline(polylineOptions);
                mark = mMap.addMarker(mp);
                ArrayList<LatLng> directionpoint=new ArrayList<>();
                directionpoint.add(RoadSnapLatLngList.get(RoadSnapLatLngList.size()-1));
                //   animateMarker(mMap,  mark,directionpoint , false);

            }
            else
            {
                polylineOptions = new PolylineOptions();
                polylineOptions.width(5);
                polylineOptions.geodesic(true);
                polylineOptions.color(trackContext.getResources().getColor(R.color.colorPrimaryDark));
                polylineOptions.add(prev, ShowMapFragment.current);
                mMap.addPolyline(polylineOptions);

                prev = ShowMapFragment.current;
                mark.remove();
                mp = new MarkerOptions();
                mp.position(ShowMapFragment.current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
                mp.rotation(bearing);
                mp.flat(true);
                SpeedMeter.speedTo(speed);
                mark = mMap.addMarker(mp);

            }


            if (directionPositionList.size()>0&& PolylineDirection.isVisible()&&PolylineDirection!=null)
                if (!PolyUtil.isLocationOnPath(current,(List)directionPositionList,true,Common.WrongWay_tolerance)){
                    //Common.ShowSweetAlert(trackContext,"Car out side the road");
                    speakOut();
                }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private static void speakOut() {

        tts.speak(String.valueOf(trackContext.getResources().getText(R.string.wrong_way)), TextToSpeech.QUEUE_FLUSH, null);
        sendNotification(trackContext,"Wrong Way",String.valueOf(trackContext.getResources().getText(R.string.wrong_way)));
    }



    private static void sendNotification(Context context,String title,String msg) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notificationId=1023;
/*

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, GeofenceShowSTudent.class), 0);
*/

        // notificationId = random.nextInt(9999 - 1000) + 1000;

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(title)
                .setTicker("Primesys Track")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        mBuilder.setOngoing(false);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mBuilder.setOnlyAlertOnce(true);
        //     mBuilder.setContentIntent(contentIntent);
        Common.playDefaultNotificationSound(context);

        mNotificationManager.notify(notificationId, mBuilder.build());
        Log.d("-----------------", "Notification sent successfully.");
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
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

        new SweetAlertDialog(trackContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(String.valueOf(trackContext.getResources().getText(R.string.str_make_reachage_title)))
                .setContentText((String.valueOf(trackContext.getResources().getText(R.string.str_make_reachage))))
                .setCancelText("No,Exit !")
                .setConfirmText("Make Payment!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (Common.PlatformRenewalStatus){
                            Intent startMain = new Intent(trackContext,DeviceLevelRenewService.class);
                            startActivity(startMain);

                        }else {
                            Intent startMain = new Intent(trackContext,RenewServiceActivity.class);
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




    private void GetAllDeviceLocation() {

        reuestQueue = Volley.newRequestQueue(trackContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(trackContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        System.out.println("Common.TrackURL******************------"+Common.TrackURL);


        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "UserServiceAPI/GetOptimizedAllDeviceLocation", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("Responce---GetOptimizedAllDeviceLocation********************************************------"+response);

                ParseLocationList(response);


                pDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());


                        pDialog.dismiss();
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            ParseLocationList(new String(error.networkResponse.data));
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ParentId",Common.userid);

                System.out.println("REq---GetOptimizedAllDeviceLocation------"+params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);

    }

    private void ParseLocationList(String response) {


        try {
            JSONArray jsonArray=new JSONArray(response);
            if (jsonArray.length()>0){
                mMap.clear();

                for (int i=0;i<jsonArray.length();i++){

                    JSONObject jo= (JSONObject) jsonArray.get(i);
                    SetAllLocation(jo);


                    if (Common.getGMTTimeStampFromDate((sdf.format(new Timestamp(System.currentTimeMillis())))) - (jo.getLong("timestamp"))<Common.MarkerTimeDiff){
                        if (jo.getInt("speed")>0)
                        device_onCount++;
                        else
                            device_IdleCount++;
                    }
                }


                Log.e(PrimesysTrack.TAG,"On--"+device_onCount+"  Idle--"+device_IdleCount);
                // txt_on_off_device.setText("On:"+device_onCount+" Off:"+(jsonArray.length()-device_onCount));

                String text = "<font color=#000000>Device Status: </font><font fonttype=bold color=#4CAF50>" +
                        "<b>On: </b></font> <font color=#000000>"+device_onCount+
                        "</font> <font color=#cc0029><b>Off: </b></font>" +
                        "<font color=#000000>"+(jsonArray.length()-(device_onCount+device_IdleCount))+"</font>" +
                        "</font> <font color=#cc0029><b>Idle: </b></font>" +

                        "<font color=#000000>"+(device_IdleCount)+"</font>";

                txt_on_off_device.setText(Html.fromHtml(text));
                txt_on_off_device.setVisibility(View.VISIBLE);

            }else {
                Common.ShowSweetAlert(trackContext,"Location not found for any device.");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SetAllLocation(JSONObject jo) {

       /* {
            "lat_direction": "n",
                "lan_direction": "w",
                "lat": "39.043306666666666",
                "lan": "-76.94399333333334",
                "speed": "33",
                "timestamp": "1524138468",
                "expiary_date": "2018-05-03 13:32:17.63",
                "address": "3598 Powder Mill Rd, Beltsville, MD 20705, USA",
                "student_id": "32807",
                "status": "14",
                "path": "~/Images/StudentPhoto/Truck_9999.png",
                "type": "Car",
                "name": "Trk 213293"
        },*/

        try {
            System.out.println("---Remain Days-----"+jo.getString("status"));

            if ((Integer.parseInt(jo.getString("status"))) >= 0) {
                SpeedMeter.setVisibility(View.GONE);
                mMap.setInfoWindowAdapter(null);

                speed = (( int) Double.parseDouble(jo.getString("speed")));
                String date = Common.getDateCurrentTimeZone(jo.getLong("timestamp"));
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                datediff = Common.getGMTTimeStampFromDate((sdf.format(timestamp))) - (jo.getLong("timestamp"));

                LatLng current = new LatLng(parseDouble(jo.getString("lat")), parseDouble(jo.getString("lan")));
                ShowMapFragment.StudentName = jo.getString("name");
                mMap.setTrafficEnabled(true);
                setAllInfoAdpter();
                try {
                    togglelist.setChecked(false);
                    MarkerOptions  mp = new MarkerOptions();
                    mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker(jo.getString("type")))));
                    mp.flat(true);
                    StringBuilder sb = new StringBuilder("");
                    sb.append("Speed:"+speed+Common.mesurment_unit+" Date:"+date);                   sb.append(" \n");
                    sb.append("\n");
                    mp.snippet(sb.toString());
                    mp.title("Name :"+ShowMapFragment.StudentName);
                    showAllMarker= mMap.addMarker(mp);
                    mMap.setOnMarkerClickListener(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(parseDouble(jo.getString("lat")), parseDouble(jo.getString("lan"))))      // Sets the center of the map to Mountain View
                        .zoom(8)                   // Sets the zoom
                        .bearing(bearing)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private View customMarker(String type) {


        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);

        Log.e(PrimesysTrack.TAG,"customMarker----------"+type);
        if (type.equalsIgnoreCase("Child"))
            childImage.setImageResource(R.drawable.ic_markerboy);
        else if (type.equalsIgnoreCase("Car")){
            if (speed>0&&datediff<Common.MarkerTimeDiff)
                childImage.setImageResource(R.drawable.car_green);
            else if (speed==0&&datediff<Common.MarkerTimeDiff)
                childImage.setImageResource(R.drawable.car_blue);
            else if (datediff>Common.MarkerTimeDiff)
                childImage.setImageResource(R.drawable.car_red);
        }
        else if (type.equalsIgnoreCase("Pet"))
            childImage.setImageResource(R.mipmap.ic_icon);

        return marker;

    }

    public void  setAllInfoAdpter(){

        try{

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(trackContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(trackContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(trackContext);
                snippet.setTextColor(Color.GRAY);
                ArrayList address=Common.getAddress(trackContext,marker.getPosition().latitude,marker.getPosition().longitude);

                Log.e("----","getInfoContents---"+address.size());
                if (address.size()>0)
                    snippet.setText(address.get(0).toString());
                else
                    snippet.setText(marker.getSnippet());

                Log.e("----","getInfoContents---"+address.size());

                info.removeAllViews();
                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void ShowDialogOfVoiceSevilence(final String Selected_studentId) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(trackContext);

        builder.setTitle("Voice Surveillance");
        builder.setMessage("Do you really want to do Voice Surveillance ?.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent voice=new Intent(trackContext, Voice_Servilence.class);
                voice.putExtra("StudentId", Selected_studentId+"");

                startActivity(voice);
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {


        if (currentGmapObj.getType()!=null)
        {
            if (currentGmapObj.getType().equalsIgnoreCase("Child"))
            {
                menu.findItem(R.id.voice_s).setVisible(true);
                menu.findItem(R.id.geofencing).setVisible(true);


            }

            else if (currentGmapObj.getType().equalsIgnoreCase("Car")){
                menu.findItem(R.id.geofencing).setVisible(true);
                menu.findItem(R.id.getdir).setVisible(true);


            }
            else if (currentGmapObj.getType().equalsIgnoreCase("Pet"))
                menu.findItem(R.id.voice_s).setVisible(true);
            menu.findItem(R.id.geofencing).setVisible(true);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.getdir:
                try
                {
                    if (mp!=null){
                        ShowDirectionAddDialog();

                    }else
                        Common.ShowSweetAlert(trackContext,"Please select device for source destination address..");

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case R.id.geofencing:
                if (currentGmapObj!=null&&currentGmapObj.getRemaining_days_to_expire()!=null) {
                    try{
                        if (Common.PlatformRenewalStatus) {
                            if (Integer.parseInt(currentGmapObj.getRemaining_days_to_expire())>= 0) {

                                if(Common.roleid.equals("3")|| Common.roleid.equals("7")|| Common.roleid.equals("10"))
                                {
                                    if (currentGmapObj.getType()!=null) {
                                        // Studentlist.StudentId=data.get(pos).getStudentId().toString();
                                        Intent intent = new Intent(trackContext, GeoFencingHomeNew.class);
                                        intent.putExtra("StudentId", currentGmapObj.getId() + "");

                                        startActivity(intent);
                                    }
                                    else {
                                        Common.ShowSweetAlert(trackContext, "Please select device first.");
                                    }
                                }else {
                                    Common.ShowSweetAlert(trackContext, "You are Demo user.Geo-fence is available only for registered user.");
                                }

                            } else {
                                ShowPaymentDialog();
                            }
                        }else {
                            if(Common.roleid.equals("3")|| Common.roleid.equals("7")|| Common.roleid.equals("10"))
                            {
                                if (currentGmapObj.getType()!=null) {
                                    // Studentlist.StudentId=data.get(pos).getStudentId().toString();
                                    Intent intent = new Intent(trackContext, GeoFencingHomeNew.class);
                                    intent.putExtra("StudentId", currentGmapObj.getId() + "");

                                    startActivity(intent);
                                }
                                else {
                                Common.ShowSweetAlert(trackContext,"Please select device first.");
                            }
                            }else {
                                Common.ShowSweetAlert(trackContext, "You are Demo user.Geo-fence is available only for registered user.");
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    Common.ShowSweetAlert(trackContext,"Please select device first.");
                }
                return super.onOptionsItemSelected(item);
            case R.id.voice_s:

                if (currentGmapObj!=null&&currentGmapObj.getRemaining_days_to_expire()!=null) {

                    try {
                        if (Common.PlatformRenewalStatus) {
                            if (Integer.parseInt(currentGmapObj.getRemaining_days_to_expire())>= 0) {
                                if (Common.roleid.equals("3") || Common.roleid.equals("7") || Common.roleid.equals("10")) {
                                    ShowDialogOfVoiceSevilence(currentGmapObj.getId());

                                } else {
                                    Common.ShowSweetAlert(trackContext, "You are Demo user.Voice surveillance is available only for registered user.");
                                }

                            } else {
                                ShowPaymentDialog();
                            }
                        } else {
                            if (Common.roleid.equals("3") || Common.roleid.equals("7") || Common.roleid.equals("10"))
                                ShowDialogOfVoiceSevilence(currentGmapObj.getId());
                            else
                                Common.ShowSweetAlert(trackContext, "You are Demo user.Voice surveillance is available only for registered user.");


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Common.ShowSweetAlert(trackContext,"Please select device first.");
                }
                return true;

        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wakeLock.release();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setSnippet(marker.getSnippet()+Common.getAddress(trackContext,marker.getPosition().latitude,marker.getPosition().longitude).get(0)+"");
        return false;
    }
}
