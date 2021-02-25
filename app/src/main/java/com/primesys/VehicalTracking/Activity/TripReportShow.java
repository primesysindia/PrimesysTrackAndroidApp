package com.primesys.VehicalTracking.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Dto.TripInfoDto;
import com.primesys.VehicalTracking.MyAdpter.TripReportAdpter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TripReportShow extends AppCompatActivity {

    RecyclerView reportlistl;
    LinearLayoutManager laymanager;
    Context context;
    public SharedPreferences sharedPreferences;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private TripReportAdpter mAdpter;
    public static ArrayList<TripInfoDto>TripList=new ArrayList<>();
    private TextView trip_heading;

    Calendar cal = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String now =dateFormat.format(cal.getTime());
    private TextView totalkm;
    private Long EndTime,StartTime,DeviceImei;
    private SharedPreferences.Editor editor;
    String TripFechtime="TripFechtime";
    String TripFechCount="TripFechCount";
    Long CurruntTime=Calendar.getInstance().getTimeInMillis()/1000;
    private Toolbar toolbar;
    private RadioGroup distance_unit;
    RadioButton rb_mile,rb_km;
    public static boolean isKmEnable=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_report_show);
        context= TripReportShow.this;
        findViewById();
        DeviceImei= Long.parseLong(getIntent().getStringExtra("DeviceImieNo"));
        StartTime=Long.parseLong(getIntent().getStringExtra("StartDateTime"));
        EndTime=Long.parseLong(getIntent().getStringExtra("EndDateTime"));



        //trip_heading.setText("Trip Report for\n" + "Vehicle Name:"+ TripList.get(0).getDevicename()+" , IMEI No: "+  TripList.get(0).getDevice() +", Date: "+now);
        totalkm.setText("Total Km : "+String.format("%.2f",Double.parseDouble(getIntent().getStringExtra("TripTotal")))+" Km");
        trip_heading.setText("Trip Report \n" + "Date :-"+  Common.getDateWithoutTimeCurrentTimeZone(Long.parseLong(TripList.get(0).getSrctimestamp()))+" , Generated on :-"+ now+ " for "+ TripList.get(0).getDevicename());


        mAdpter = new TripReportAdpter(TripList, R.layout.row_trip_report, context);
        reportlistl.setAdapter(mAdpter);
        distance_unit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_km){

                    isKmEnable=true;
                    totalkm.setText("Total Km : "+String.format("%.2f",Double.parseDouble(getIntent().getStringExtra("TripTotal")))+" Km");
                    if (mAdpter!=null)
                        mAdpter.notifyDataSetChanged();
                }else {
                    isKmEnable=false;
                    totalkm.setText("Total Mile : "+String.format("%.2f",Double.parseDouble(getIntent().getStringExtra("TripTotal"))*Common.miles_multiplyer)+" Mile");
                    if (mAdpter!=null)
                        mAdpter.notifyDataSetChanged();
                }

            }
        });

    }

    private void findViewById() {
        reportlistl= (RecyclerView) findViewById(R.id.tripreport);
        reportlistl.setLayoutManager(new LinearLayoutManager(context));
        reportlistl.setItemAnimator(new DefaultItemAnimator());
        reportlistl.setHasFixedSize(true);
        trip_heading=(TextView)findViewById(R.id.trip_heading);
        totalkm=(TextView)findViewById(R.id.totalkm);
        sharedPreferences=this.getSharedPreferences("ApplicationMetaData",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        distance_unit = (RadioGroup) findViewById(R.id.distance_unit);
         rb_km = (RadioButton) findViewById(R.id.rb_km);
         rb_mile = (RadioButton) findViewById(R.id.rb_mile);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mail:
                ShowTripMaildialog(StartTime,EndTime);

               /* if (sharedPreferences.contains(TripFechtime)&&sharedPreferences.contains(TripFechCount)){
                    Long Diff= (sharedPreferences.getLong(TripFechtime,CurruntTime+86400))-CurruntTime;

                    //Log.e("TRip Show data-------1",Diff+"");

                    if (sharedPreferences.getInt(TripFechCount,1)<=3&&Diff<=86400){
                        ShowTripMaildialog(StartTime,EndTime);
                        editor.putInt(TripFechCount, sharedPreferences.getInt(TripFechCount,1)+1);
                        editor.commit();
                     //   Log.e("TRip Show data-------2",Diff+"------"+sharedPreferences.getInt(TripFechCount,1));

                    }else if(Diff>86400){
                        ShowTripMaildialog(StartTime,EndTime);

                        editor.putLong(TripFechtime, CurruntTime+86400);
                        editor.putInt(TripFechCount, 1);
                        editor.commit();
                       // Log.e("TRip Show data-------3",Diff+"------"+sharedPreferences.getInt(TripFechCount,1));

                    }else {
                       // Log.e("TRip Show data-------4",Diff+"------"+sharedPreferences.getInt(TripFechCount,1));

                       Common.ShowSweetAlert(context,"Report download is exceeded for today,You can view report three times per day. ");
                    }

                }else {
                   // Log.e("TRip Show data-------0","------"+sharedPreferences.getInt(TripFechCount,1));

                    ShowTripMaildialog(StartTime,EndTime);

                    editor.putLong(TripFechtime, CurruntTime+86400);
                    editor.putInt(TripFechCount, 1);
                    editor.commit();
                }
*/

                return true;

        }

        return false;
    }



    private void ShowTripMaildialog(final long gmtTimeStampFromDate, final long gmtTimeStampFromDate1) {

        // custom dialog

        final EditText txt_pin;
        final Dialog dialog = new Dialog( context);
        dialog.setContentView(R.layout.dialog_mailid);
        TextView d_title = (TextView) dialog.findViewById(R.id.d_title);
        d_title.setText("EMail-Id");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);


        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MailId=txt_pin.getText().toString();

                System.out.println("************************************"+MailId);
                if(Validemail(MailId))
                {
                    GetTripReportOnMail(gmtTimeStampFromDate,gmtTimeStampFromDate1,MailId);
                    Common.ShowSweetSucess(context,"Trip report is sent to "+MailId+ ".");
                    dialog.dismiss();
                }



            }
        });

        dialog.show();

    }

    private void GetTripReportOnMail(final long gmtTimeStampFromDate, final long gmtTimeStampToDate, final String mailId) {

        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
      //  pDialog1 = Common.ShowSweetProgress(context, " Getting Trip Report wait.......");

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetTripReportOnMail",new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  Trip report Km ----"+response);

                ParseTripReponce(response);

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {

                     //   pDialog1.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("DeviceImieNo", DeviceImei+"");
                params.put("StartDateTime",gmtTimeStampFromDate+"");
                params.put("EndDateTime", gmtTimeStampToDate+"");
                params.put("Email", mailId+"");
                System.out.println("REq---GetTripReport------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    private void ParseTripReponce(String response) {


    }

    public  boolean Validemail(String mail){
        Boolean valid=true;

        if(mail.length()==0)
        {

            Common.ShowSweetAlert(context, "Enter E-Mail Address !");

            valid=false;
        }
        else if(!mail.matches(Common.EMAIL_REGEX))
        {

            Common.ShowSweetAlert(context, "Enter Valid E-Mail Address!");

            valid=false;
        }
        return valid;
    }

}
