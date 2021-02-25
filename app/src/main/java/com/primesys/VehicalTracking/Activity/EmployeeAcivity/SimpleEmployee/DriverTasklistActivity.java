package com.primesys.VehicalTracking.Activity.EmployeeAcivity.SimpleEmployee;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Dto.DriverEmpTaskSheduledDTO;
import com.primesys.VehicalTracking.MyAdpter.TaskListReportAdpter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DriverTasklistActivity extends AppCompatActivity {

    private Context mContext;
    RecyclerView TaskList;
    public SharedPreferences sharedPreferences;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private TaskListReportAdpter mAdpter;
    private TextView trip_heading;
    Calendar cal = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String now =dateFormat.format(cal.getTime());
    private TextView totalkm;
    private Long EndTime,StartTime,DeviceImei;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    String day="0",month="0",year="0";
    private String user_id="0";
    ArrayList<DriverEmpTaskSheduledDTO> TaskDataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_tasklist);
        mContext= DriverTasklistActivity.this;

        findViewById();
        day=getIntent().getStringExtra("day");
        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");
        user_id=getIntent().getStringExtra("user_id");
        GetDriverTaskList();


    }


    private void findViewById() {
        TaskList= (RecyclerView) findViewById(R.id.driver_tasklist);
        TaskList.setLayoutManager(new LinearLayoutManager(mContext));
        TaskList.setItemAnimator(new DefaultItemAnimator());
        TaskList.setHasFixedSize(true);
        trip_heading=(TextView)findViewById(R.id.trip_heading);
        totalkm=(TextView)findViewById(R.id.totalkm);
        sharedPreferences=this.getSharedPreferences("ApplicationMetaData",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void GetDriverTaskList() {

        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"ParentAPI/GetDriverEmpTaskList",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseTaskJSON(response);
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

                params.put("emp_user_id", user_id);
                params.put("day", day+"");
                params.put("month", month+"");
                params.put("year", year+"");

                Log.e("---------------","REq---GetDriverTaskList-------"+params);
                return params;
            }

        };

        stringRequest.setTag("");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    private void parseTaskJSON(String response) {
        Log.e("---------------","Response---GetDriverTaskList-------"+response);

        try{

            JSONArray jArray=new JSONArray(response);
            for (int i=0;i<jArray.length();i++)
            {
                JSONObject jo= jArray.getJSONObject(i);
                DriverEmpTaskSheduledDTO obj=new DriverEmpTaskSheduledDTO();

                obj.setAddress(jo.getString("address"));
                obj.setCar_name(jo.getString("car_name"));
                obj.setStartTime(jo.getString("time"));

                obj.setVehicleID(jo.getString("emp_car_id"));
                obj.setTask_id(jo.getString("task_id"));
                TaskDataList.add(obj);




            }
            if (TaskDataList.size()>0){

                mAdpter = new TaskListReportAdpter(TaskDataList, R.layout.row_task_list, mContext);
                TaskList.setAdapter(mAdpter);
                trip_heading.setText(Common.username+" you have following  pickup point on \n"
                        +" Date: "+day+"-"+month+"-"+year+ " and allocated Vehicle Name:"+ TaskDataList.get(0).getCar_name());
                totalkm.setText("Total pickup point : "+TaskDataList.size()+"");

            }else {
                Common.ShowSweetAlert(mContext,"You don't have a pickup today.");
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }
}


