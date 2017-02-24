package com.primesys.VehicalTracking.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.Dto.LocationDTO;
import com.primesys.VehicalTracking.Dto.persondetail;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
import com.primesys.VehicalTracking.MyAdpter.ShowMapmenuAdpter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.GPSEnableSetting;
import com.primesys.VehicalTracking.Utility.GraphicsUtil;
import com.primesys.VehicalTracking.Utility.LruBitmapCache;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public GoogleMap mMap;
    public static Bitmap bmp1;
    private LatLng prev;
    public static int flag = 0;
    static String InvitedId;
    String path;
    private static int speed;
    private static String date;
    MarkerOptions mp;
    String latval = null, lanval;
    Marker mark;
    static RequestQueue RecordSyncQueue;
    ShowMap contextMap = ShowMap.this;
    long freeSize = 0L;
    long totalSize = 0L;
    long usedSize = -1L;
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;
    public static Boolean Updatestatus = false, menuSelct;
    int cntMap = 0;
    public Bitmap bitmap = null;
    private String photo;
    private String LocatonDate;
    private SweetAlertDialog pDialog;
    static Context trackContext;
    private static DBHelper helper;
    private Toolbar toolbar;
    private RadioGroup rgViews;
    ListView personlist;
    final String TAG = "REquest";
    ArrayList<GmapDetais> tracklist = new ArrayList<GmapDetais>();
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.primesys.VehicalTracking.R.layout.activity_show_map);
        Findviewbyid();

        EnableLocationProvider();

        rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_normal) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (checkedId == R.id.rb_satellite) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (checkedId == R.id.rb_terrain) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }
        });
        if (Common.getConnectivityStatus(trackContext)) {
            GetAllTrackperson();
        }
        personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                GmapDetais user = tracklist.get(position);

                trackInfo = false;
            /*    BitmapDrawable bitmap_draw = (BitmapDrawable) imgchild.getDrawable();
                Bitmap bmp = bitmap_draw.getBitmap();
                ShowMap.bmp1 = bmp;
*/

                try {

                    URL url = new URL(Common.Relative_URL + user.getPath().replaceAll(" ", "%20"));
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    ShowMap.bmp1 = Common.getRoundedShape(myBitmap);


                    LoginActivity.mClient.sendMessage(StopTracKEvent());
                    LoginActivity.mClient.sendMessage(makeJSON(user.getId()));

                    Intent showmap = new Intent(trackContext, ShowMap.class);
                    showmap.putExtra("photo", user.getPath().replaceAll(" ", "%20"));
                    Log.e("photo from adpter", user.getPath().replaceAll(" ", "%20"));
                    trackContext.startActivity(showmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




    }

    private void Findviewbyid() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        trackContext = ShowMap.this;
        helper = DBHelper.getInstance(trackContext);
        if (!isGooglePlayServicesAvailable()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                    .setContentText("Google Play services not available !")
                    .show();
            finish();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            throw new Error("Can't find tool bar, did you forget to add it in Activity layout file?");
        }
        setSupportActionBar(toolbar);
        personlist = (ListView) findViewById(R.id.User_list);
        // Give it a horizontal LinearLayoutManager to make it a horizontal ListView
        menu_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(trackContext,
                LinearLayoutManager.HORIZONTAL, false);
        menu_list.setLayoutManager(layoutManager);


        rgViews = (RadioGroup) findViewById(R.id.rg_views);

        cust_icon = (ImageView) findViewById(R.id.homeicon);
        cust_icon.setVisibility(View.GONE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, trackContext.getTheme());
        toggle.setHomeAsUpIndicator(drawable);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        /*  mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                R.drawable.ic_icon, // Navigation menu toggle icon
                R.string.navigation_drawer_open, // Navigation drawer open description
                R.string.navigation_drawer_close // Navigation drawer close description
        )*/
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initialize data
        sharedPreferences = trackContext.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //setnavigation header


        View header = navigationView.inflateHeaderView(R.layout.nav_header_home);
        txt_name = (TextView) header.findViewById(R.id.nav_name);
        txt_email = (TextView) header.findViewById(R.id.nav_email);
        profile_pic = (CircularNetworkImageView) header.findViewById(R.id.Proimage);
        profile_pic.setImageResource(R.drawable.student);


        if (sharedPreferences.contains(key_id)) {
            Common.userid = sharedPreferences.getString(key_id, "");
            Common.roleid = sharedPreferences.getString(key_Roll_id, "");

        }

        GetImage();

        if (sharedPreferences.contains(key_USER)) {
            txt_email.setText(sharedPreferences.getString(key_USER, ""));
            txt_name.setText(sharedPreferences.getString(key_fname, ""));
            // Picasso.with(context).load(Common.Relative_URL+Common.userid+"_"+Common.userid+".jpg").transform(new CircleTransform()).into(profile_pic);

        } else {
            txt_email.setText("");
            txt_name.setText("");

        }
        Common.userid = sharedPreferences.getString(key_id, "");

        Typeface typeFace = Typeface.createFromAsset(getAssets(), Common.fontType1);
        txt_name.setTypeface(typeFace);
        txt_email.setTypeface(typeFace);




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

        // Add a marker in Sydney and move the camera
   /*     LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        try {

            getUsedMemorySize();

            if (usedSize > totalSize - 1000) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
                        .setContentText("Insufficient Memory in Loading Map !")
                        .show();

            } else {

                InvitedId = getIntent().getStringExtra("InvitedId");
                photo = getIntent().getStringExtra("photo");
                // setContentView(R.layout.activity_currentlocation);

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());


                // Call Api to get track information
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new TrackInfrmation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
                    } else {
                        new TrackInfrmation().execute();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (mMap == null) {

                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    //   showCurrentLocation();
                    // check if map is created successfully or not
                    if (mMap == null) {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error")
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


    //show currentLocation

    @TargetApi(Build.VERSION_CODES.M)
    void showCurrentLocation() {
        try {
            /*
             * This Is One Is Default
			 */
            //    mMap.setMyLocationEnabled(true);
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            // Getting LocationManager object from System Service LOCATION_SERVICE

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = lm.getBestProvider(criteria, true);

            // Getting Current Location
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            Location location = lm.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            lm.requestLocationUpdates(provider, 20000, 0, (LocationListener) this);

        } catch (Exception e) {

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

    @Override
    public void onClick(View v) {

    }


    //Track Informatiion
    class TrackInfrmation extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(ShowMap.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(Common.URL + "UserServiceAPI/GetTrackInfo");
                List<NameValuePair> param = new ArrayList<NameValuePair>(1);
                param.add(new BasicNameValuePair("InvitedId", InvitedId));
                Log.e("Loc Req ", "" + InvitedId);

                httpost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse response = httpclient.execute(httpost);
                result = EntityUtils.toString(response.getEntity());
                Log.e("response", "" + result);
            } catch (Exception e) {
                result = e.getMessage();
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
        try {

            JSONObject joObject = new JSONObject(result);
            LocationDTO dmDetails = new LocationDTO();
            if (joObject.has("Lat") && joObject.has("Lang") && joObject.has("Time")) {
                dmDetails.setLat(joObject.getString("Lat"));
                dmDetails.setLang(joObject.getString("Lang"));

                dmDetails.setTime(joObject.getString("Time"));
                // LocatonDate=Common.getDateCurrentTimeZone(1455962478);

                Date date = new Date(Long.parseLong(dmDetails.getTime())); // *1000 is to convert seconds to milliseconds
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                LocatonDate = sdf.format(date);
                System.out.println("dmfnj/////////////***********" + LocatonDate);

                SetSales_person_location(dmDetails);
            } else {
                SweetAlertDialog pDialog = new SweetAlertDialog(contextMap, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Location Data Not Found");
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        contextMap.finish();
                    }
                });
                pDialog.show();
            }


        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }

    }


    public static void startMethod() {
		/*Intent startIntent=new Intent(trackContext,HistoryActivity.class);
		startIntent.putExtra("StudentId",StudentId);
		trackContext.startActivity(startIntent);*/
    }

    @Override
    protected void onStop() {
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

    @Override
    protected void onRestart() {
        super.onRestart();
		/*LoginActivity.mClient.sendMessage(makeJSON());*/
        LoginActivity.mClient.sendMessage(restartTrackEvent());
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
            ((ShowMap) trackContext).updateGoogleMapLocation(lat, lan);
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

/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        */
/*menu.findItem(R.id.adduser).setIcon(new IconDrawable(this, IconValue.fa_user_plus).colorRes(R.color.primary).actionBarSize());
        menu.findItem(R.id.Invitation).setIcon(new IconDrawable(this, IconValue.fa_send_o).colorRes(R.color.primary).actionBarSize());*//*

        return super.onPrepareOptionsMenu(menu);
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
                String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
                LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));
                //	setResult(RESULT_OK, getIntent().putExtra("dateTime", formattedDate.format(date)));
//			GoToHistoty();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
*/


    void updateGoogleMapLocation(String lat, String lan) {
        try {

            Bitmap bmpDefaulr = BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.default_marker);

            LatLng current = new LatLng(Double.parseDouble(lat), Double.parseDouble(lan));
         /*   if(flag==0)  //when the first update comes, we have no previous points,hence this
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
                  *//*  try{
                        if(PostLocationflag)
                            PostLocation_Report(lat,lan);

                    }catch(Exception ex)
                    {
                        System.out.println("Error in post loc");
                        ex.printStackTrace();

                    }*//*
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {*/
            try {
                if (cntMap == 1) {
                    mark = mMap.addMarker(mp);
                    cntMap++;
                } else {
                    try {
                        //   mark.setIcon(BitmapDescriptorFactory.fromBitmap(bmpDefaulr));
                          /*  mMap.addPolyline((new PolylineOptions())
                                    .add(prev, current).width(6).color(Color.CYAN)
                                    .visible(true));*/
                        prev = current;
                        mp = new MarkerOptions();
                        mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));

                        mp.snippet("Latitude : " + String.format("%.6f", current.latitude) + "\t" + "Longitude : " + String.format("%.6f", current.longitude));
                        mp.title("Speed : " + speed + " km/h" + "\t" + "Date : " + date + "\n" + "");
                        mark = mMap.addMarker(mp);

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


            //}
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lan)), 15));

        } catch (Exception ex) {

        }

    }

    private void SetSales_person_location(LocationDTO dmDetails) {
        // TOD

        LatLng current = new LatLng(Double.parseDouble(dmDetails.getLat()), Double.parseDouble(dmDetails.getLang()));

        LatLng sydney = current;

      /*  ArrayList<String> Address=getAddress(current.latitude,current.longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(Address.get(0)+" "+Address.get(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_circle)));*/
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        try {

            Bitmap bmpDefaulr = BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.student);


            mp = new MarkerOptions();
            //Bitmap bmp=BitmapFactory.decodeResource(trackContext.getResources(), R.drawable.custom_marker);
            mp.position(current).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(trackContext, customMarker())));
            ArrayList<String> address = getAddress(current.latitude, current.longitude);
            mp.title(address.get(0));
            mp.snippet(address.get(1) + " " + LocatonDate);
            mMap.addMarker(mp);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    private Bitmap getRoundedShape(Bitmap bitmap) {
        int targetWidth = 100;
        int targetHeight = 100;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

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


    public ArrayList<String> getAddress(double lat, double lng) {
        String add = null;
        String postal = null;
        ArrayList<String> addlist = new ArrayList<String>();

        Geocoder geocoder = new Geocoder(ShowMap.this, Locale.getDefault());
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
    View customMarker() {
        View marker = ((LayoutInflater) trackContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
       /* ImageView childImage=(ImageView)marker.findViewById(R.id.child_icon);
        childImage.setImageBitmap(bmp1);*/
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


    public String convertToStringRepresentation(final long value) {
        final long[] dividers = new long[]{T, G, M, K, 1};
        final String[] units = new String[]{"TB", "GB", "MB", "KB", "B"};
        if (value < 1)
            throw new IllegalArgumentException("Invalid file size: " + value);
        String result = null;
        for (int i = 0; i < dividers.length; i++) {
            final long divider = dividers[i];
            if (value >= divider) {
                result = format(value, divider, units[i]);
                break;
            }
        }
        return result;
    }

    private static String format(final long value,
                                 final long divider,
                                 final String unit) {
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


    // JSON request to get the history
    static String makeJSONHistory(String date) {
        helper.truncateTables("db_history");
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


    private void GetImage() {


        reuestQueue = Volley.newRequestQueue(trackContext);


        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL + "UserServiceAPI/Getimage", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("-----------------", "Photo REspo--- " + response);
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    //    Picasso.with(context).load(Common.Relative_URL+jo.getString("photo")).transform(new CircleTransform()).into(profile_pic);

                    if (!"".equals(jo.getString("photo"))) {
                        Log.e("-----------------", "Photo REspo---111 " + response);

                        ImageLoader.ImageCache imageCache = new LruBitmapCache();
                        imageLoader = new ImageLoader(
                                Volley.newRequestQueue(trackContext), imageCache);


                        (profile_pic).setImageUrl(
                                Common.Relative_URL + jo.getString("photo"), imageLoader);

                    } else {
                        Bitmap bitmap = ((BitmapDrawable) profile_pic.getDrawable()).getBitmap();
                        GraphicsUtil graphicUtil = new GraphicsUtil();
                        profile_pic.setImageBitmap(graphicUtil.getCircleBitmap(bitmap, 16));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Bitmap bitmap = ((BitmapDrawable) profile_pic.getDrawable()).getBitmap();
                    GraphicsUtil graphicUtil = new GraphicsUtil();
                    profile_pic.setImageBitmap(graphicUtil.getCircleBitmap(bitmap, 16));
                }


                //  pDialog.hide();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //    pDialog.hide();
                        profile_pic.setImageResource(R.drawable.student);

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            // parseData(new String(error.networkResponse.data));
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", Common.userid);
                Log.e("-----------------", "Photo Req--- " + params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }


    private void GetAllTrackperson() {


        reuestQueue = Volley.newRequestQueue(trackContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
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
                tracklist.add(dmDetails);
            }


        } catch (Exception e) {
            Log.e("Exception", "" + e);
        } finally {
            //it work Better but take time to Load
            if (tracklist.size() > 0) {
                myAdapter = new ShowMapAdapter(trackContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);

                ShowMapmenuAdpter myAdapter1 = new ShowMapmenuAdpter(trackContext,tracklist);

                menu_list.setAdapter(myAdapter1);
                personlist.setSelection(0);
                cnt++;
            } else {
                Common.showToast("No User Information", trackContext);
            }

        }

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);

            this.finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent Add = new Intent(trackContext, UserProfileActivity.class);
            startActivity(Add);
        }/*else if (id == R.id.nav_addnewfriend) {

            Intent Add=new Intent(context,AddNewFriend.class);
            startActivity(Add);
        }
        else if (id == R.id.nav_newrequest) {
            Intent Add=new Intent(context,NewRequest.class);
            startActivity(Add);

        } else if (id == R.id.nav_block) {
            Intent Add=new Intent(context,BlockTracking.class);
            startActivity(Add);
        }*/ else if (id == R.id.nav_signout) {
            if (sharedPreferences.contains(key_IS))
                isfirst = sharedPreferences.getBoolean(key_IS, true);

            if (!isfirst) {
                editor = sharedPreferences.edit();
                editor.putString(key_id, "");
                editor.putString(key_PASS, "");
                editor.putString(key_Roll_id, "");
                editor.remove(key_IS);

                editor.commit();


                Common.ShowSweetSucess(trackContext, "You have Sucessfully Signout");
            } else {
                Common.ShowSweetAlert(trackContext, "Please Login First");

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else
                finish();


            try {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (id == R.id.nav_share) {
            shareIt();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "VTS");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sharemessaage));
        startActivity(sharingIntent);
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
}




