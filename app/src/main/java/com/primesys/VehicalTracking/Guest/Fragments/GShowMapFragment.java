package com.primesys.VehicalTracking.Guest.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.Dto.snapdto;
import com.primesys.VehicalTracking.GoogleDirection.DirectionsJSONParser;
import com.primesys.VehicalTracking.Guest.Adpter.GShowMapAdapter;
import com.primesys.VehicalTracking.Guest.GDatabaseHelper;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Double.parseDouble;

/**
 * Created by root on 19/4/16.
 */
public class GShowMapFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,
        OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener, TextToSpeech.OnInitListener {

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
    public static Bitmap bmp1;
    private static LatLng prev;
    public static int flag = 0;
    static String InvitedId;
    String path;
    private static int speed;
    Date dt = new Date();

    private static String date;
    ;
    static MarkerOptions mp;
    String latval = null, lanval;
    static Marker mark;
    static RequestQueue RecordSyncQueue;
    Context contextMap;
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
    private static GDatabaseHelper helper;
    private Toolbar toolbar;
    static ListView personlist;
    final String TAG = "REquest";
    public static ArrayList<DeviceDataDTO> tracklist = new ArrayList<DeviceDataDTO>();
    public SharedPreferences sharedPreferences;
    static StringRequest stringRequest;
    static RequestQueue reuestQueue;
    SharedPreferences.Editor editor;
    private boolean isfirst;

    private NavigationView navigationView;
    private TextView txt_name, txt_email;
    private CircularNetworkImageView profile_pic;
    private ImageView cust_icon;
    private int cnt = 0;
    private GShowMapAdapter myAdapter;
    public static Boolean trackInfo = false;
    private DrawerLayout drawer;
    private ImageLoader imageLoader;
    private String defaultImage;
    private RecyclerView menu_list;
    private static View rootView;
    private android.support.v4.app.FragmentManager mSupportMapFragment;
    private GoogleApiClient client;

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

    public static List<LatLng> directionPositionList=new ArrayList<>();
    public static MyCustomProgressDialog TrackProgressDialog;
    private static TextToSpeech tts;
    public ArrayList<LocationData> CurrentLoacationlist=new ArrayList<>();
    public static CountDownTimer CDT10sec;
    private static int j=0;
    private static long CurrentTimestamp=Long.parseLong((System.currentTimeMillis()+"").substring(0, Math.min((System.currentTimeMillis()+"").length(),10)));


    public GShowMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.activity_show_map, container, false);
        trackContext = container.getContext();


        rfaLayout = (RapidFloatingActionLayout) rootView.findViewById(R.id.label_list_sample_rfal);
        rfaButton = (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        rfaContent = new RapidFloatingActionContentLabelList(trackContext);
        //Fab Button listner
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        setHasOptionsMenu(true);
        tts = new TextToSpeech(trackContext, this);
        TrackProgressDialog=(MyCustomProgressDialog) MyCustomProgressDialog.ctor(trackContext);
        TrackProgressDialog.setCancelable(false);

        Findviewbyid();

        EnableLocationProvider();


      //  mMap.setInfoWindowAdapter(new Current_MarkerInfiWindow());

            tracklist=helper.Show_Device_list();


            if (tracklist.size() > 0) {
                tracklist.get(0).setColor("#007300");
                tracklist.get(1).setColor("#0000ff");
                tracklist.get(2).setColor("#e50000");

                myAdapter = new GShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);
                personlist.requestFocusFromTouch();


                cnt++;
            } else {
                Common.showToast("No User Information", trackContext);
            }



        CountDownTimer CDTOthclick = new CountDownTimer(2 * 1000, 1000) {
            int i = 0;
            public void onTick(long millisUntilFinished) {
                i--;
            }

            public void onFinish() {
                personlist.performItemClick(
                        personlist.getAdapter().getView(0, null, null),
                        0,
                        personlist.getAdapter().getItemId(0));


            }
        }.start();





        personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GShowMapFragment.flag = 0;
                GShowMapFragment.prev = null;
                trackInfo = false;
                directionPositionList.clear();
                PolylineDirection=null;
                DeviceDataDTO user = tracklist.get(position);
                GShowMapFragment.StudentId = Integer.parseInt(user.getId());
                GShowMapFragment.StudentName=user.getName();
                GShowMapFragment.Photo = user.getPath().replaceAll(" ", "%20");
                if(mMap!=null)
                mMap.clear();

                mMap.setInfoWindowAdapter(new Current_MarkerInfoWindow());

                 j=0;
                if (CurrentLoacationlist.size()>0)
                    CurrentLoacationlist.clear();

                if (CDT10sec!=null)
                    CDT10sec.cancel();


                CurrentLoacationlist=helper.get_CurrentLocationlist(GShowMapFragment.StudentId);

                if(position==0){
                    flag=0;
                    updateGoogleMapLocation(CurrentLoacationlist.get(j),CurrentLoacationlist.get(0).getLat()+"", CurrentLoacationlist.get(0).getLan()+"");
                    Log.e("Current LOcation Size--",CurrentLoacationlist.size()+"");


                    CDT10sec = new CountDownTimer(5*1000, 1000)
                    {

                        int i=0;
                        public void onTick(long millisUntilFinished)
                        {
                            i--;
                        }

                        public void onFinish()
                        {
                            j++;
                            flag=1;

                            if (j<CurrentLoacationlist.size())
                           {
                               updateGoogleMapLocation(CurrentLoacationlist.get(j), CurrentLoacationlist.get(j).getLat()+"", CurrentLoacationlist.get(j).getLan()+"");

                               CDT10sec.start();
                           }
                        }
                    }.start();

                }else if (position==1) {


                    updateGoogleMapBlueLocation(CurrentLoacationlist.get(0),CurrentLoacationlist.get(0).getLat()+"", CurrentLoacationlist.get(0).getLan()+"");

                    CDT10sec = new CountDownTimer(5 * 1000, 1000) {
                        int i = 0;
                        public void onTick(long millisUntilFinished) {
                            i--;
                        }

                        public void onFinish() {
                            j++;
                            updateGoogleMapBlueLocation(CurrentLoacationlist.get(0),CurrentLoacationlist.get(0).getLat()+"", CurrentLoacationlist.get(0).getLan()+"");

                            CDT10sec.start();
                        }

                    }.start();


                }else if (position==2){

                    if (LOCATIONCDT!=null)
                        LOCATIONCDT.cancel();
                    if (TrackProgressDialog!=null&&TrackProgressDialog.isShowing())
                        TrackProgressDialog.dismiss();

                    date = Common.getDateCurrentTimeZone(CurrentTimestamp-190080);

                    speed = 0;


                    current = new LatLng(CurrentLoacationlist.get(10).getLat(), CurrentLoacationlist.get(10).getLan());
                    addresslist = Common.getAddress(trackContext, current.latitude, current.longitude);

                    SpeedMeter.speedTo(0, 4000);
                    SpeedMeter.setWithTremble(false);


                    try {
                        mMap.clear();
                        flag = 1;
                        cntMap = 1;
                        mp = new MarkerOptions();
                        mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customredMarker())));
                        mp.flat(true);
                        prev = current;
                        mark = mMap.addMarker(mp);
                        mark.showInfoWindow();


                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, (float) 13));


                    } catch (Exception ex) {
                        System.out.println("Error in post loc");
                        ex.printStackTrace();

                    }




                }

            }
        });



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this.getActivity()).addApi(AppIndex.API).build();
        return rootView;


    }



    private View customblueMarker() {
        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
        childImage.setImageResource(R.drawable.car_blue);

        return marker;

    }
    private View customredMarker() {
        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
        childImage.setImageResource(R.drawable.red_car);

        return marker;

    }

    private void Findviewbyid() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
        SpeedMeter=(SpeedView)rootView.findViewById(R.id.speedView);

        helper = GDatabaseHelper.getInstance(trackContext);
        if (!isGooglePlayServicesAvailable()) {
            new SweetAlertDialog(trackContext, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                    .setContentText("Google Play services not available !")
                    .show();
            //      this.finish();
        }
        personlist = (ListView) rootView.findViewById(R.id.User_list);


        //////FOr Fab Button

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

    }
    @Override
    public void onResume() {
      //  mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        //mapView.onDestroy();

        super.onDestroy();
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



    protected String makeJSON(String Id) {
        String trackSTring = "{}";

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
                    Common.ShowSweetAlert(trackContext, "We cant get location of this device.Please try again.");
                }
            }
        }.start();

        mMap.setInfoWindowAdapter(new Current_MarkerInfoWindow());

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

        }
        return trackSTring;
        // TODO Auto-generated method stub

    }



    public static void changeLocation(String message) {

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
/*            date = Common.getDateCurrentTimeZone(jData.getLong("timestamp"));
            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            datediff=  Common.getGMTTimeStampFromDate((sdf.format(timestamp)))-(jData.getLong("timestamp"));*/
         //   GShowMapFragment.updateGoogleMapLocation(CurrentLoacationlist.get(j), lat, lan);



            //	System.err.print("changeLocation"+  message);
            Common.Location_getting = false;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
		/*	//GetJson History
			String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());

			LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));
			*/

        }
    }


     void updateGoogleMapBlueLocation(LocationData locationData, String lat, String lan) {

        if (LOCATIONCDT!=null)
            LOCATIONCDT.cancel();
        if (TrackProgressDialog!=null&&TrackProgressDialog.isShowing())
            TrackProgressDialog.dismiss();

        if(j==0)
            date = Common.getDateCurrentTimeZone(CurrentTimestamp);
        else
            date = Common.getDateCurrentTimeZone(CurrentTimestamp+(j*5));

        speed = 0;


        current = new LatLng(locationData.getLat(), locationData.getLan());
        addresslist = Common.getAddress(trackContext, current.latitude, current.longitude);
        SpeedMeter.speedTo(0, 4000);
        SpeedMeter.setWithTremble(false);


        try {
            mMap.clear();
            flag = 1;
            cntMap = 1;
            mp = new MarkerOptions();
            mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customblueMarker())));
            mp.flat(true);
            prev = current;
            mark = mMap.addMarker(mp);
            mark.showInfoWindow();


             mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan)), (float) 13));


        } catch (Exception ex) {
            System.out.println("Error in post loc");
            ex.printStackTrace();

        }


    }



     void updateGoogleMapLocation(LocationData locationData, String lat, String lan) {

        if (LOCATIONCDT!=null)
            LOCATIONCDT.cancel();
        if (TrackProgressDialog!=null&&TrackProgressDialog.isShowing())
            TrackProgressDialog.dismiss();

        speed = locationData.getSpeed();


        if(j==0)
            date = Common.getDateCurrentTimeZone(CurrentTimestamp);
        else
            date = Common.getDateCurrentTimeZone(CurrentTimestamp+(j*5));
         try {

            current = new LatLng(parseDouble(lat), parseDouble(lan));
             addresslist=Common.getAddress(trackContext,current.latitude,current.longitude);
                SpeedMeter.speedTo(speed,4000);
                SpeedMeter.setWithTremble(true);


            if(flag==0)  //when the first update comes, we have no previous points,hence this
            {
                try {
                    mMap.clear();
                    flag=1;
                    cntMap=1;
                    mp = new MarkerOptions();
                    mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customgreenMarker())));
                    mp.flat(true);
                    prev=current;
                    mark=mMap.addMarker(mp);
                    mark.showInfoWindow();

                }catch(Exception ex)
                    {
                        System.out.println("Error in post loc");
                        ex.printStackTrace();

                    }
            }
            else
            {
            try {
                    try {

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
                            GetRoadsnap(prev,current);
                               prev = current;
                        }
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
        }



           // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan)), (float) 13));
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(parseDouble(lat), parseDouble(lan)))      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the zoom
                    .bearing(bearing)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception ex) {

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
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {

            getUsedMemorySize();

            if (usedSize > totalSize - 1000) {
             /*   new SweetAlertDialog(trackContext, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                        .setContentText("Insufficient Memory in Loading Map !")
                        .show();*/

            } else {

                InvitedId =StudentId+"";
                photo = Photo;
                // setContentView(R.layout.activity_currentlocation);

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());

                if (mMap == null) {

                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    //   showCurrentLocation();
                    // check if map is created successfully or not
                    if (mMap == null) {
                        new SweetAlertDialog(trackContext, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                                .setContentText("Sorry! unable to create maps !")
                                .show();
                    }
                }else {

                    if (ContextCompat.checkSelfPermission(trackContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(trackContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    }
                    else {
                        ActivityCompat.requestPermissions(this.getActivity(), new String[] {
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION },
                                TAG_CODE_PERMISSION_LOCATION);
                    }

                }



            }

        } catch (Exception e) {
            Log.e("ShowGMap", "" + e);
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







    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        flag = 0;
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "stop_track");
            trackSTring = jo.toString();
        } catch (Exception e) {

        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
		*//*LoginActivity.mClient.sendMessage(makeJSON());*//*
        LoginActivity.mClient.sendMessage(restartTrackEvent());
    }
*/


    public void onTabSelected(GShowMapFragment tab) {
        Fragment fragment = new GShowMapFragment();
        replaceRootFragment(fragment);
    }

    public void replaceRootFragment(Fragment fragment) {
        LoginActivity.mClient.sendMessage(restartTrackEvent());
    }






/*
    //creating custom marker
    @SuppressLint("InflateParams")
    View customMarker()
    {			CircularNetworkImageView netCircle=new CircularNetworkImageView(contextMap);

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
    static View customgreenMarker() {
        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
            childImage.setImageResource(R.drawable.car_green);

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


    public class Current_MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        TextView veh_name,alarm_type,alarm_time;

       Current_MarkerInfoWindow(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.custom_info_live_track, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView veh_name = ((TextView)myContentsView.findViewById(R.id.name));
            TextView alarm_time = ((TextView)myContentsView.findViewById(R.id.alarmtime));
            TextView address = ((TextView)myContentsView.findViewById(R.id.address));
            TextView speedtext = ((TextView)myContentsView.findViewById(R.id.speed));
            veh_name.setText(GShowMapFragment.StudentName);


            if (date!=null&&!date.equals(""))
            alarm_time.setText(date);
            else alarm_time.setText(Common.getDateCurrentTimeZone(CurrentTimestamp));

            speedtext.setText(speed+" Km/h");

            if (addresslist.size()>0){
                address.setText(addresslist.get(0)+addresslist.get(1).replaceAll(" ", ""));

            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
       // Toast.makeText(trackContext, "clicked label: " + position, Toast.LENGTH_SHORT).show();
       switch (position) {
           case 0:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
               break;
           case 1:
               mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
               break;
           case 2:
               mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
               break;
           case 3:
               mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
               break;

           default:
               mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

               break;
       }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {

        switch (position) {
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
        rfabHelper.toggleContent();
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




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.getdir:
            Common.ShowSweetAlert(trackContext,trackContext.getResources().getString(R.string.str_guest_error_msg));
                return true;

        }

        return false;
    }

    private void ShowDirectionAddDialog() {

        final EditText txt_pin;
        final Dialog dialog = new Dialog( trackContext);
        dialog.setContentView(R.layout.dialog_mailid);
        dialog.setTitle("Enter Destination Address ");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);
        txt_pin.setHint("Enter Destination Address.");

        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dest_address=txt_pin.getText().toString();

                System.out.println("***********dest_address*************************"+dest_address);
                if(dest_address.length()!=0)
                {
                  /*  GetVehMonthlyTotalKm(startTimeStampFromDate,endStampFromDate,MailId);
                    Common.ShowSweetSucess(mContext,"Monthly mileage report is sent to "+MailId+ ". Please check in mail inbox. ");*/

                    final LatLng origin = mp.getPosition();
                    final LatLng destination = getLocationFromAddress(trackContext,dest_address);

                  /*  // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);*/

                    GoogleDirection.withServerKey(serverKey)
                            .from( origin)
                            .to( destination).transportMode(TransportMode.DRIVING)

                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            // Do something here

                            String status = direction.getStatus();

                            Gson gson=new Gson();

                            System.out.println("***********onDirectionSuccess***********"+status);
                            if (direction.isOK()) {
                                    try{
                                        if (PolylineDirection!=null&&PolylineDirection.isVisible())
                                            PolylineDirection.remove();
                                        if (Dir_destination_Marker!=null&&Dir_origin_Marker!=null)
                                        {
                                            Dir_origin_Marker.remove();
                                            Dir_destination_Marker.remove();

                                        }

                                     JSONObject   jObject = new JSONObject(gson.toJson(direction));
                                        DirectionsJSONParser parser = new DirectionsJSONParser();

                                        // Starts parsing data
                                        List<List<HashMap<String, String>>> routes = parser.parse(jObject);

                                        Log.e("Dtata parse ----",routes.toString());
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        ShowTotalDistToast();
                                    }

                              
                               // Dir_origin_Marker= mMap.addMarker(new MarkerOptions().position(origin));
                          /*      polylineOptions = new PolylineOptions();
                                polylineOptions.width(5);
                                polylineOptions.geodesic(true);
                                polylineOptions.color(trackContext.getResources().getColor(R.color.colorPrimaryDark));
                                polylineOptions.add(prev, current);

                                mMap.addPolyline(polylineOptions);*/
                                Dir_destination_Marker= mMap.addMarker(new MarkerOptions().position(destination));


/*
                                for (int i = 0; i < direction.getRouteList().size(); i++) {
                                    Route route = direction.getRouteList().get(i);
                                    String color = colors[i % colors.length];
                                    ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                    mMap.addPolyline(DirectionConverter.createPolyline(trackContext, directionPositionList, 5, Color.parseColor(color)));
                                }*/

                                directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();

                                mMap.addPolyline(new PolylineOptions().add(origin,directionPositionList.get(0)));
                                 PolylineDirection =mMap.addPolyline(DirectionConverter.createPolyline
                                         (trackContext, (ArrayList<LatLng>) directionPositionList,
                                                 5,
                                                 Color.RED));
                              //  direction.getGeocodedWaypointList()



                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            System.out.println("***********onDirectionFailure*************************"+t);
                        }
                    });


                    dialog.dismiss();
                }else
                    Common.ShowSweetAlert(trackContext,"Please enter Address.");




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

                    System.out.println("Responce---GetRoadsnap******************************------"+response);

                    Parseroadsnap(response,current);


                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.toString());

                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                VolleyError er = new VolleyError(new String(error.networkResponse.data));
                                error = er;
                                Parseroadsnap(new String(error.networkResponse.data), current);
                            }
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
                mp.position(RoadSnapLatLngList.get(RoadSnapLatLngList.size()-1)).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customgreenMarker())));
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
                polylineOptions.add(prev, GShowMapFragment.current);
                mMap.addPolyline(polylineOptions);

                prev = GShowMapFragment.current;
                mark.remove();
                mp = new MarkerOptions();
                mp.position(GShowMapFragment.current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customgreenMarker())));
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



}
