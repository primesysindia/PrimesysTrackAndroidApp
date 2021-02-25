package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.MyAdpter.ShowDevicelistAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowExistingDeviceActivity extends AppCompatActivity {

    private  android.support.v7.widget.RecyclerView student_list;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Context mContext=ShowExistingDeviceActivity.this;
    private ArrayList<DeviceDataDTO> tracklist;
    private ShowDevicelistAdpter myAdapter;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private String defaultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_existing_device);
        findViewById();
        tracklist= PrimesysTrack.mDbHelper.Show_Device_list();
        if (Common.getConnectivityStatus(mContext)&& tracklist.size()==0) {
            // Call Api to get track information
            try {
                GetAllTrackperson();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {

            if (tracklist.size() > 0) {
                myAdapter = new ShowDevicelistAdpter(mContext, R.layout.card_device_list, tracklist);
                student_list.setAdapter(myAdapter);
            } else {
                Common.ShowSweetAlert(mContext, "No User Information");
            }


        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent google = new Intent(mContext, AddStudentActivity.class);
                startActivity(google);
            }
        });

    }

    private void findViewById() {
        student_list= (RecyclerView) findViewById(R.id.recycler_student_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        student_list.setLayoutManager(layoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }


    private void GetAllTrackperson() {


        reuestQueue = Volley.newRequestQueue(mContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data

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

                System.err.println("Track all student person Req--- " + params);
                return params;
            }
        };
        stringRequest.setTag("");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    protected  void parseData(String result) {

        // TODO Auto-generated method stub
        if (tracklist.size()>0)
        {
            tracklist.clear();
            if(myAdapter!=null)
                myAdapter.notifyDataSetChanged();
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
                if (result.contains("ExpiryDate")){
                    dmDetails.setExpiary_date(joObject.getString("ExpiryDate"));
                }else dmDetails.setExpiary_date("00-00-0000");

                //   dmDetails.setExpiary_date("10-03-2017");

                tracklist.add(dmDetails);
            }

            if (tracklist.size() > 0) {
                    myAdapter = new ShowDevicelistAdpter(mContext, R.layout.card_device_list, tracklist);
                    student_list.setAdapter(myAdapter);
                    PrimesysTrack.mDbHelper.Insert_Device_list(tracklist);
            } else {
                Common.ShowSweetAlert(mContext, "No User Information");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

}
