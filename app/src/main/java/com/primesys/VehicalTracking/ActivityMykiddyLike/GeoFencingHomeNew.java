package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GeoFencingHomeNew extends AppCompatActivity {

    public static int GeoStudentId;
    public static Context context;
    private Toolbar toolbar;
    private static RecyclerView geoFencelist;
    static ArrayList<GeofenceDTO> listgeo = new ArrayList<GeofenceDTO>();
    public static GeofencinglistNewAdpter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fencing_home_new);		
        GeoStudentId = Integer.parseInt(getIntent().getStringExtra("StudentId"));
        FindById();

    }


    private void FindById() {
        // TODO Auto-generated method stub
        context = GeoFencingHomeNew.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        geoFencelist= (RecyclerView) findViewById(R.id.fencelist);

        geoFencelist.setLayoutManager(new LinearLayoutManager(context));
        /*fenceedit=(ImageView)findViewById(R.id.fence_edit);
        fencenew=(ImageView)findViewById(R.id.fence_new);
        fencedelete=(ImageView)findViewById(R.id.fence_delete);
        fencesetting=(ImageView)findViewById(R.id.fence_setting);*/
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            if(Common.connectionStatus){
                LoginActivity.mClient.sendMessage(GetFencelist_JSON());

            }else {
                Common.ShowSweetAlert(context, getResources().getString(R.string.str_conn_error));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static String GetFencelist_JSON()
    {


        String fence_string="{}";
        try{
            JSONObject jo = new JSONObject();
            jo.put("event",Common.EV_GEOFENCE_GETLIST);
            jo.put("student_id",GeoFencingHomeNew.GeoStudentId);
            fence_string=jo.toString();

            System.err.println("Request   ======== "+fence_string);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return fence_string;

    }


    public static void parsefencelistJSON(String response) {
        System.err.println("Reponce   getgeolist "+response);

        if (response.contains("geo_fences_not_found")){
            Common.ShowSweetAlert(context, "You don't have any  Geo-fence.Please create first.");

        }else {


            if (listgeo.size() > 0) {
                listgeo.clear();
                userAdapter.notifyDataSetChanged();
            }

            try {
                JSONObject jomain = new JSONObject(response);

                JSONArray array = jomain.getJSONArray("fences");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jo = array.getJSONObject(i);
                    GeofenceDTO geoObj = new GeofenceDTO();

                    JSONObject joalert = jo.getJSONObject("alert_type");
                    JSONObject alert = joalert.getJSONObject("alert");
                    JSONObject in_out = joalert.getJSONObject("in_out");
                    JSONObject jodetail = jo.getJSONObject("details");

                    geoObj.setStudentId(GeoFencingHomeNew.GeoStudentId + "");
                    //	geoObj.setStudentname(jo.getString("StudentName"));

                    geoObj.setEnable(jo.getString("enable"));
                    geoObj.setLang(jodetail.getString("lan"));
                    geoObj.setLat(jodetail.getString("lat"));
                    geoObj.setDistance(jodetail.getString("radius"));
                    geoObj.setStatusin(in_out.getString("in"));
                    geoObj.setStatusout(in_out.getString("out"));
                    geoObj.setAlertapp(alert.getString("push_to_app"));
                    geoObj.setAlertemail(alert.getString("email"));
                    geoObj.setSms(alert.getString("sms"));
                    geoObj.setGeoName(jo.getString("name"));
                    geoObj.setDiscripation(jo.getString("descr"));

                    geoObj.setGeoID(jo.getString("id"));

                    listgeo.add(geoObj);


                }

                System.err.println("listgeo COunt_--------------------- " + listgeo.size());


                if (listgeo.size() > 0) {
                    userAdapter = new GeofencinglistNewAdpter(context,
                            R.layout.geofencingnew_list_row, listgeo);
                    geoFencelist.setAdapter(userAdapter);
                    //setgeolocation(listgeo.get(0));
                } else {
                    Common.ShowSweetAlert(context, "You don't have any  Geo-fence.Please create first.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseDeleteFenceJSON(String result) {
        try
        {
            JSONObject jo=new JSONObject(result);
            if (jo.getString("event").equals("geofence_deleted_successfully")) {
                Common.ShowSweetAlert(context, "Geo-fence deleted successfully.");
                try {
                   // PrimesysTrack.mDbHelper.DeleteGeofenceHistory(geoObj.getGeoID());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    LoginActivity.mClient.sendMessage(GetFencelist_JSON());
                }
            }

            else {
                Common.ShowSweetAlert(context, "Error in deleting geo-fence.Please try again. ");}

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_geofence_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.add_new_fence:
                try {
                    if (listgeo.size()<Common.FenceAllowNo) {


                        Intent intent = new Intent(context, GeofencingNewdrawCircle.class);
                        intent.putExtra("StudentId", GeoFencingHomeNew.GeoStudentId + "");
                        startActivity(intent);
                    }else{
                        Common.ShowSweetAlert(context, "Max " + Common.FenceAllowNo + "  geo-fence can be set.you can modify existing one.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            default:
                return true;
        }




    }



    public static void parseDeviceNotConnectError(String comMsg) {

        Common.ShowSweetAlert(context,context.getString(R.string.device_not_connect));
    }

    public static void parseUpdateFenceJSON(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo.getString("event").equals(Common.EV_GEOFENCEUPDATE_SUCCESS)) {
                Common.ShowSweetAlert(context, "Geo-fence update successfully.");
                Activity activity = (Activity) context;
                //correct way to use finish()
                activity.finish();
            } else {
                Common.ShowSweetAlert(context, "Error updating geo-fence. ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
