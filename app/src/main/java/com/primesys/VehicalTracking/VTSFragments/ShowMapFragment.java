package com.primesys.VehicalTracking.VTSFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.Dto.LocationDTO;
import com.primesys.VehicalTracking.Dto.persondetail;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.GPSEnableSetting;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 19/4/16.
 */
public class ShowMapFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,
        OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int TAG_CODE_PERMISSION_LOCATION = 100;
    public static int StudentId;
    public static String Photo;
    public static GoogleMap mMap;
    private static float bearing;
    private static PolylineOptions polylineOptions;
    MapView mapView;

    public static Bitmap bmp1;
    private static LatLng prev;
    public static int flag = 0;
    static String InvitedId;
    String path;
    private static int speed;
    private static String date;
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
    private String photo;
    private String LocatonDate;
    private SweetAlertDialog pDialog;
    static Context trackContext;
    private static DBHelper helper;
    private Toolbar toolbar;
    ListView personlist;
    final String TAG = "REquest";
    public static ArrayList<GmapDetais> tracklist = new ArrayList<GmapDetais>();
    ArrayList<persondetail> tracklistcopy = new ArrayList<persondetail>();
    public SharedPreferences sharedPreferences;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public String key_Roll_id = "Roll_id";
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    SharedPreferences.Editor editor;
    private boolean isfirst;

    private NavigationView navigationView;
    private TextView txt_name, txt_email;
    private CircularNetworkImageView profile_pic;
    private ImageView cust_icon;
    private int cnt = 0;
    private ShowMapAdapter myAdapter;
    public static Boolean trackInfo = false;
    private DrawerLayout drawer;
    private ImageLoader imageLoader;
    private String defaultImage;
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

    public ShowMapFragment() {
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
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        rfaLayout = (RapidFloatingActionLayout) rootView.findViewById(R.id.label_list_sample_rfal);
        rfaButton = (RapidFloatingActionButton) rootView.findViewById(R.id.label_list_sample_rfab);
        rfaContent = new RapidFloatingActionContentLabelList(trackContext);
        //Fab Button listner
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        Findviewbyid();

        EnableLocationProvider();



        if (Common.getConnectivityStatus(trackContext)&& helper.Show_Device_list().size()==0) {
            // Call Api to get track information
            try {
                    GetAllTrackperson();

                } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {

            if (tracklist.size()>0)
            {
                tracklist.clear();
                if(myAdapter!=null)
                    myAdapter.clear();
            }
            tracklist=helper.Show_Device_list();


            if (tracklist.size() > 0) {
                myAdapter = new ShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);

                personlist.requestFocusFromTouch();
                personlist.setSelection(0);
                personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                cnt++;
            } else {
                Common.showToast("No User Information", trackContext);
            }



        }


        personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowMapFragment.flag = 0;
                GmapDetais user = tracklist.get(position);
                ShowMapFragment.StudentId = Integer.parseInt(user.getId());
                ShowMapFragment.Photo = user.getPath().replaceAll(" ", "%20");

                trackInfo = false;


                try {

                 /*   URL url = new URL(Common.Relative_URL + user.getPath().replaceAll(" ", "%20"));
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    ShowMap.bmp1 = Common.getRoundedShape(myBitmap);*/


                    LoginActivity.mClient.sendMessage(StopTracKEvent());
                    LoginActivity.mClient.sendMessage(makeJSON(user.getId()));


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this.getActivity()).addApi(AppIndex.API).build();


        return rootView;


    }

    private void Findviewbyid() {
/*
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);*/

        helper = DBHelper.getInstance(trackContext);
        if (!isGooglePlayServicesAvailable()) {
            new SweetAlertDialog(trackContext, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                    .setContentText("Google Play services not available !")
                    .show();
            //      this.finish();
        }
        personlist = (ListView) rootView.findViewById(R.id.User_list);


        // Gets to GoogleMap from the MapView and does initialization stuff
      /*  mMap = mapView.getMap();*/
        if (ContextCompat.checkSelfPermission(trackContext, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(trackContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            if (mMap == null) {
                mMap = ((MapView) rootView.findViewById(R.id.mapview)).getMap();
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            mapView.getMapAsync(this);
            Log.e("NAP VIE***********************", mMap.toString() + "----------------------");
        }
     else {
        ActivityCompat.requestPermissions(this.getActivity(), new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                TAG_CODE_PERMISSION_LOCATION);
    }

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
        mapView.onResume();
        super.onResume();
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


        reuestQueue = Volley.newRequestQueue(trackContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(trackContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL + "ParentAPI.asmx/GetTrackInfo", new Response.Listener<String>() {

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    protected void parseData(String result) {

        Log.e("HOmE Responce----", result);
        // TODO Auto-generated method stub
        if (tracklist.size()>0)
        {
            tracklist.clear();
            if(myAdapter!=null)
            myAdapter.clear();
        }

            try {
            JSONArray joArray = new JSONArray(result);
            for (int i = 0; i < joArray.length(); i++) {
                JSONObject joObject = joArray.getJSONObject(i);
                GmapDetais dmDetails = new GmapDetais();
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
                myAdapter = new ShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);

                personlist.requestFocusFromTouch();
                personlist.setSelection(0);
                personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                cnt++;
            } else {
                Common.showToast("No User Information", trackContext);
            }

        }

    }


    protected String makeJSON(String Id) {
        String trackSTring = "{}";
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

        Log.e("Send req For track chiled---", trackSTring + " " + Id);
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
            String lat = jData.getString("lat");
            String lan = jData.getString("lan");
            speed = jData.getInt("speed");
            date = Common.getDateCurrentTimeZone(jData.getLong("timestamp"));
           ShowMapFragment.updateGoogleMapLocation(lat, lan);
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




    static void updateGoogleMapLocation(String lat, String lan) {
        try {

            Bitmap bmpDefaulr = BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.default_marker);

            LatLng current = new LatLng(Double.parseDouble(lat), Double.parseDouble(lan));
           if(flag==0)  //when the first update comes, we have no previous points,hence this
            {

                try {
                    	mMap.clear();
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


                    }catch(Exception ex)
                    {
                        System.out.println("Error in post loc");
                        ex.printStackTrace();

                    }

             //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan)),18 ));


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
                            polylineOptions = new PolylineOptions();
                            polylineOptions.width(5);
                            polylineOptions.geodesic(true);
                            polylineOptions.color(trackContext.getResources().getColor(R.color.colorPrimaryDark));
                            polylineOptions.add(prev, current);

                            mMap.addPolyline(polylineOptions);
                        }
                        Log.e("Bearing-------------------------", "------------" + bearing);

                        prev = current;
                       mark.remove();
                        mp = new MarkerOptions();
                        mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
                        mp.rotation(bearing);
                        mp.snippet("Latitude : " + String.format("%.6f", current.latitude) + "\t" + "Longitude : " + String.format("%.6f", current.longitude));
                        mp.title("Speed : " + speed + " km/h" + "\t" + "Date : " + date + "\n" + "");
                        Log.e("Map Frgment in matrker----------------", mMap + "***");
                        if (mMap == null) {
                            mMap = ((MapView) rootView.findViewById(R.id.mapview)).getMap();
                        }

                        mark = mMap.addMarker(mp);
                     //   mark.showInfoWindow();
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


            Float zoomlevel=mMap.getCameraPosition().zoom;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan)), (float) 16.5));

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
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ShowMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.primesys.VehicalTracking/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
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
                new SweetAlertDialog(trackContext, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                        .setContentText("Insufficient Memory in Loading Map !")
                        .show();

            } else {

                InvitedId =StudentId+"";
                photo = Photo;
                // setContentView(R.layout.activity_currentlocation);

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());



                if (Common.getConnectivityStatus(trackContext)&& helper.Show_Device_list().size()==0) {
                    // Call Api to get track information
                    try {
                        GetAllTrackperson();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else {

                    if (tracklist.size()>0)
                    {
                        tracklist.clear();
                        if(myAdapter!=null)
                            myAdapter.clear();
                    }
                    tracklist=helper.Show_Device_list();


                    if (tracklist.size() > 0) {
                        myAdapter = new ShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                        personlist.setAdapter(myAdapter);

                        personlist.requestFocusFromTouch();
                        personlist.setSelection(0);
                        personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                        cnt++;
                    } else {
                        Common.showToast("No User Information", trackContext);
                    }



                }

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
                }

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


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
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ShowMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.primesys.VehicalTracking/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        flag = 0;
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "stop_track");
            trackSTring = jo.toString();
        } catch (Exception e) {

        }
        LoginActivity.mClient.sendMessage(trackSTring);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
		*//*LoginActivity.mClient.sendMessage(makeJSON());*//*
        LoginActivity.mClient.sendMessage(restartTrackEvent());
    }
*/


    public void onTabSelected(ShowMapFragment tab) {
        Fragment fragment = new ShowMapFragment();
        replaceRootFragment(fragment);
    }

    public void replaceRootFragment(Fragment fragment) {

        LoginActivity.mClient.sendMessage(restartTrackEvent());

     /*   if (this.getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
            int id = this.getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getId();
            try {
                this.getActivity().getSupportFragmentManager().popBackStackImmediate(id, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException e) {
                return;
            }
        }xx
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id., fragment)
                .commit();*/


    }



    public ArrayList<String> getAddress(double lat, double lng) {
        String add = null;
        String postal = null;
        ArrayList<String> addlist = new ArrayList<String>();

        Geocoder geocoder = new Geocoder(trackContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            String currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            double latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity = obj.getSubAdminArea();
            String currentState = obj.getAdminArea();
            add = add + "\t" + obj.getLocality() + ",";
            add = add + "\t" + obj.getSubAdminArea();

            addlist.add(add.replace("null", ""));
            postal = "\t" + obj.getAdminArea() + ",";
            postal = postal + "\t" + obj.getCountryName() + ",";
            postal = postal + "\t" + obj.getPostalCode() + "";
            addlist.add(postal.replace("null", ""));


            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return addlist;
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
    static View customMarker() {
        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
       /* ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
        childImage.setImageBitmap(bmp1);*/
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

}
