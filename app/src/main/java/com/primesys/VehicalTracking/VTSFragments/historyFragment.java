package com.primesys.VehicalTracking.VTSFragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primesys.VehicalTracking.DateTimeActivity;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.LoginActivity;
import com.primesys.VehicalTracking.MyAdpter.HistoryMapAdapter;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
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
public class historyFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener
{


    private static final String TAG ="Request" ;
    public static int StudentId;
    android.support.v4.app.FragmentManager myFragmentManager;
    SupportMapFragment mySupportMapFragment;

    static Context historyContext;
    static GoogleMap googleMap ;
    static ArrayList<LocationData> list;
    ArrayList<LatLng> arrayPoints=new ArrayList<LatLng>();
    public static Marker selectedMarker;
    private PolylineOptions polylineOptions = new PolylineOptions();
    Handler handler = new Handler();
    Random random = new Random();
    FragmentManager manager;
    private static final int REQ_DATETIME=1;
    private static List<Marker> markers = new ArrayList<Marker>();
    static int totalCount;
    ListView gmapList;
    String defaultImage;
    public static Boolean Updatestatus=false;
    ArrayList<GmapDetais> arr=new ArrayList<GmapDetais>();
    int cnt=0;
    ImageLoader imageLoader;
    static RequestQueue RecordSyncQueue;
    HistoryMapAdapter myAdapter;
    Handler handle =new Handler();
    private static DBHelper helper=DBHelper.getInstance(historyContext);
    private MapView mapViewhistory;
    private View rootView;

    // @AIView(R.id.label_list_sample_rfal)
    private RapidFloatingActionLayout rfaLayout;
    //   @AIView(R.id.label_list_sample_rfab)
    private RapidFloatingActionButton rfaButton;

    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionContentLabelList rfaContent;
    private ListView personlist;
    private ArrayList<GmapDetais> tracklist=new ArrayList<GmapDetais>();
    private RequestQueue reuestQueue;
    StringRequest stringRequest;
    private FloatingActionButton fab_date;


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
        mapViewhistory = (MapView) rootView.findViewById(R.id.gmaphistoty);
        mapViewhistory.onCreate(savedInstanceState);

        findviewbyid();

        if (Common.getConnectivityStatus(historyContext)) {

            GetAllTrackperson();
        }

        //Fab Button listner
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        try {
            // Loading map
            initilizeMap();
           if (Common.Location_getting)
            addDefaultLocations();
        } catch (Exception e) {
            e.printStackTrace();
        }

                personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //	ShowMapFragment gmap=new ShowMapFragment();
                            //	gmap.flag=0;
                            DateTimeActivity.selStatus = true;
                            GmapDetais user1 = tracklist.get(position);
                            historyFragment.StudentId = Integer.parseInt(user1.getId());
                            Log.e("History studid---------", historyFragment.StudentId + "----"+position);

                            ShowMapFragment.Updatestatus = true;

                            final String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());

                            LoginActivity.mClient.sendMessage(makeJSONHistoryChild(formattedDate,historyFragment.StudentId+""));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });



        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(historyContext,DateTimeActivity.class);
                //	Log.e("History studid  menu---------", ShowGmapClient.StudentId+"  "+HistoryMapAdapter.user1.getType());

                i.putExtra("StudentId",historyFragment.StudentId);
                if (HistoryMapAdapter.user1!=null)
                    i.putExtra("Type",HistoryMapAdapter.user1.getType());
                else
                    i.putExtra("Type","demo_student");

                startActivityForResult(i,REQ_DATETIME);
            }
        });


                        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null&&requestCode==REQ_DATETIME&&resultCode== Activity.RESULT_OK)
        {

        }
    }

    //History Child
    String makeJSONHistoryChild(String date,String Id)
    {
        DBHelper helper=DBHelper.getInstance(historyContext);
        helper.truncateTables("db_history");
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event","get_tracking_history");
            if(Common.roleid.equals("5"))
                jo.put("student_id","demo_student");
            else
                jo.put("student_id",Integer.parseInt(Id));
            jo.put("timestamp",Common.convertToLong(date));
            trackSTring=jo.toString();
        }
        catch(Exception e)
        {

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

    private void initilizeMap() {

        try
        {
            if (googleMap == null) {
                // Gets to GoogleMap from the MapView and does initialization stuff
                googleMap = ((MapView) rootView.findViewById(R.id.gmaphistoty)).getMap();
                }
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);
             //  mapView.getMapAsync(this);

                if (googleMap == null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }




        } catch (Exception e) { ;
            // TODO: handle exception
            e.printStackTrace();
        }
    }





    //get Set

    /**
     * function to load map If map is not created it will create it for you
     * */
    /*private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Common.showToast("Sorry! unable to create maps", historyContext);
            }
        }
    }*/
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(historyContext);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 0).show();
            return false;
        }
    }


    @Override
    public void onResume() {
        mapViewhistory.onResume();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
        LoginActivity.mClient.sendMessage(makeJSONHistory(formattedDate));
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
    //add Default Location
    public static void addDefaultLocations() {
       clearMarkers();
        list=new ArrayList<LocationData>();
        DBHelper dbH=DBHelper.getInstance(historyContext);
        list=dbH.showDetails();
        totalCount=list.size();
        Log.e("-----addDefaultLocations-------","addDefaultLocations"+"--------"+list.size());

	/*	if(totalCount==0)
			Common.showToast(getResources().getString(R.string.history_msg), historyContext);*/
        for (int i = 0; i < totalCount; i++) {
         //   Log.e("-----addMarkerToMap-------","addMarkerToMap"+"--------"+i );

            addMarkerToMap(list.get(i),i);

        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    //    Log.e("-----markers-------","markers"+"--------"+markers.size());

        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            if(markers.size()!=0)
                ShowMapOnLolipop();
        } else{
            // do something for phones running an SDK before lollipop
            if(markers.size()!=0)
                startAnimation();
        }

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
    private static void startAnimation() {
        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 15),
                5000,
                MyCancelableCallback);

      /*  googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 18));*/
        currentPt = 0-1;
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

    static int currentPt;
    static GoogleMap.CancelableCallback MyCancelableCallback =
            new GoogleMap.CancelableCallback(){

                @Override
                public void onCancel() {
                }

                @Override
                public void onFinish() {

                    if(++currentPt < markers.size()){

                        float targetBearing = bearingBetweenLatLngs( googleMap.getCameraPosition().target, markers.get(currentPt).getPosition());
                        LatLng targetLatLng = markers.get(currentPt).getPosition();
                        CameraPosition cameraPosition =
                                new CameraPosition.Builder()
                                        .target(targetLatLng)
                                        .tilt(currentPt<markers.size()-1 ? 90 : 0)
                                        .bearing(targetBearing)
                                        .zoom(googleMap.getCameraPosition().zoom)
                                        .build();
                        googleMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(cameraPosition),
                                3000,
                                currentPt==markers.size()-1 ? FinalCancelableCallback : MyCancelableCallback);
                        highLightMarker(currentPt);
                    }
                }
            };
    static GoogleMap.CancelableCallback FinalCancelableCallback = new GoogleMap.CancelableCallback() {

        @Override
        public void onFinish() {
            googleMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        }

        @Override
        public void onCancel() {

        }
    };



    private void GetAllTrackperson() {


        reuestQueue = Volley.newRequestQueue(historyContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(historyContext, SweetAlertDialog.PROGRESS_TYPE);
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
                tracklist.add(dmDetails);
            }


        } catch (Exception e) {
            Log.e("Exception", "" + e);
        } finally {
            //it work Better but take time to Load
            if (tracklist.size() > 0) {
                myAdapter = new HistoryMapAdapter(historyContext, R.layout.fragment_mapsidebar, tracklist, imageLoader);
                personlist.setAdapter(myAdapter);

                personlist.requestFocusFromTouch();
                personlist.setSelection(0);
                personlist.performItemClick(personlist.getAdapter().getView(0, null, null), 0, 0);
                cnt++;
            } else {
                Common.showToast("No User Information", historyContext);
            }

        }

    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        googleMap = ((MapView) rootView.findViewById(R.id.gmaphistoty)).getMap();
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
        googleMap = ((MapView) rootView.findViewById(R.id.gmaphistoty)).getMap();
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



}
