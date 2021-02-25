package com.primesys.VehicalTracking.Guest.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.Distance_calulate;
import com.primesys.VehicalTracking.Activity.Home;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.SpeedalertDTO;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Receiver.IncomingSms;
import com.primesys.VehicalTracking.Utility.Common;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 3/10/16.
 */
public class GVTSFunction extends Fragment {
    private View rootView;
    private  RequestQueue reuestQueue;
    private  StringRequest stringRequest;
    public static  boolean Exit = false;
    Button cal_dist,speedalrt,stopcar,startcar;
    private static Context context;
    ProgressDialog pDialog;
    public StudentListAdpter padapter;
    DBHelper helper;
    private ListView listStudent;
    private ArrayList<DeviceDataDTO> childlist=new ArrayList<>();
    public static String StudentId="";
    public static SpeedalertDTO speed=new SpeedalertDTO();
    private Boolean pinexist=false;
    private String Enginepin="";
    public Dialog createpindialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.activity_vts_report_home, container, false);
        context = this.getActivity();
        findById();
        cal_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vts=new Intent(context,Distance_calulate.class);
                vts.putExtra("StudentId",StudentId);
                startActivity(vts);
            }
        });
        stopcar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pinexist)
                    ShowPassword_dialog("stop");
                else
                    ShowPasswordCreate_dialog();
            }
        });
        startcar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pinexist)
                    ShowPassword_dialog("start");
                else
                    ShowPasswordCreate_dialog();
            }
        });
        speedalrt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //LoginActivity.mClient.sendMessage(makeJsonto_getspeedalert());
                //  Show_speedalert_dilog();
                Log.e(PrimesysTrack.TAG,"*---------------------------*---------------------*************");

               try
               {
                   String txt="Speed  Alarm!IMEI:355488020113737  18.562317,N,73.834386,E";
                   new IncomingSms().MatchSpeedAlarm(context.getApplicationContext(), txt);

               }catch (Exception e){

               }

            }
        });
        try {
            GShowMapFragment.CDT.cancel();
            Home.tabLayout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }


    protected void ShowPasswordCreate_dialog() {
        // custom dialog

        final EditText txt_pin;
        createpindialog = new Dialog(this.getActivity());
        createpindialog.setContentView(R.layout.dialog_submitpassword);
        createpindialog.setTitle("Create Engine Pin");
        txt_pin = (EditText) createpindialog.findViewById(R.id.txt_pin);


        Button Submitpin = (Button) createpindialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_pin.getText().length()!=0&&txt_pin.getText().length()<=4){

                    PostEnginePin(txt_pin.getText().toString());

                }else{
                    txt_pin.setError("Please enter valid four digit engine pin .");
                }

            }
        });

        createpindialog.show();
    }





    protected void ShowPassword_dialog(final String event) {
        // custom dialog

        final EditText txt_pin;
        final Dialog dialog = new Dialog( this.getActivity());
        dialog.setContentView(R.layout.dialog_submitpassword);
        dialog.setTitle("Verify engine Pin");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_pin);


        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_pin.getText().length()!=0){

                    if (txt_pin.getText().toString().equals(Enginepin)&&event.equals("start")){
                        LoginActivity.mClient.sendMessage(makeJsonstartcar());
                        dialog.dismiss();
                    }
                    else if (txt_pin.getText().toString().equals(Enginepin)&& event.equals("stop")){
                        LoginActivity.mClient.sendMessage(makeJsonstopcar());
                        dialog.dismiss();
                    }
                    else
                        Common.ShowSweetAlert(context,getResources().getString(R.string.passwordnotmatch));

                }else{
                    txt_pin.setError("Please enter valid four digit engine pin .");
                }

            }
        });

        dialog.show();
    }
    protected String makeJsonto_getspeedalert() {

        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event", Common.EV_GETCAR_SPEEDALERT);

            jo.put("student_id",Integer.parseInt(StudentId));
            trackSTring=jo.toString();
            System.out.println(trackSTring);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return trackSTring;

    }
    public void CheckStudent(Context context1) {

        if (Common.getConnectivityStatus(context1)&& helper.Show_Device_list().size()==0) {
            // Call Api to get track information
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new TrackInfrmation().executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR,
                            (Void) null);
                } else {
                    new TrackInfrmation().execute();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {
            childlist=helper.Show_Device_list();

            if (childlist.size()>1)
                ShowListofStudent();
            else
                StudentId=childlist.get(0).getId();
         //   GetstudentVTSStatus();

        }


    }


    private FragmentManager.OnBackStackChangedListener getListener()
    {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener()
        {
            public void onBackStackChanged()
            {
                FragmentManager manager = getFragmentManager();

                if (manager != null)
                {
                    if(manager.getBackStackEntryCount() >= 1){
                        String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount()-1).getName();
                        Log.i("TOP ON BACK STACK",topOnStack);
                    }
                }
            }
        };

        return result;
    }
    private void GetstudentVTSStatus() {
        reuestQueue=Volley.newRequestQueue(context); //getting Request object from it
        final ProgressDialog pDialog1 = new ProgressDialog(context);
        pDialog1.setTitle("Progress wait.......");
        pDialog1.setCancelable(false);
        pDialog1.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetstudentVTSStatus",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  GetstudentVTSStatus----"+response);
                pDialog1.dismiss();

                parseEnableJSON(response);


            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.dismiss();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            parseEnableJSON(new String(error.networkResponse.data));
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID",StudentId);

                System.out.println("REq---GetstudentVTSStatus------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }


    protected void parseEnableJSON(String response) {
        JSONObject jo=null;
        //---{"VtsEnable":"0","DistanceInterval":"5","StudentId":"5286","Error":"false","Message":"Sucessfull"}

        try {
            jo = new JSONObject(response);
            if (jo.getString("Error").equalsIgnoreCase("false")) {
                if (jo.getString("VtsEnable").equals("0"))
                {
                    Common.DistanceInterval=Integer.parseInt(jo.getString("DistanceInterval"));
                    if (!jo.getString("Pin").equals("0"))
                    {
                        pinexist=true;
                        Enginepin=jo.getString("Pin");

                    }

                }
                else{
                    Common.ShowSweetAlert(context,getResources().getString(R.string.vtsdisablemsg));

                 //   finish();
                }

            }else {
                Common.ShowSweetAlert(context,getResources().getString(R.string.vtsdisablemsg));
             //   finish();

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void findById() {
        cal_dist=(Button)rootView.findViewById(R.id.image_distance);
        speedalrt=(Button)rootView.findViewById(R.id.image_speed);

        stopcar=(Button)rootView.findViewById(R.id.image_stop);
        startcar=(Button)rootView.findViewById(R.id.image_start);


    }
    private void ShowListofStudent() {
        // custom dialog

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_studentlist);
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);


        dialog.setCancelable(false);
        listStudent=(ListView)dialog.findViewById(R.id.student_list);

//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
        padapter=new StudentListAdpter(context, R.layout.fragment_mapsidebar, childlist);
        listStudent.setAdapter(padapter);

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                // change the background color of the selected element
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));*/

                StudentId=childlist.get(position).getId();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //Track Informatiion
    class TrackInfrmation extends AsyncTask<Void, String, String>{

        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Login  wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httpost=new HttpPost(Common.URL+"ParentAPI.asmx/GetTrackInfo");
                List<NameValuePair> param=new ArrayList<NameValuePair>(1);
                param.add(new BasicNameValuePair("ParentId", Common.userid));
                httpost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse response = httpclient.execute(httpost);
                result= EntityUtils.toString(response.getEntity());
                Log.e("response List Map", ""+result);
            }catch(Exception e){
                result=e.getMessage();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            parsingTrackInfo(result);

        }
    }
    public void parsingTrackInfo(String result) {
        try{
            Log.e("Track Info list",result);
            JSONArray joArray=new JSONArray(result);
            for (int i = 0; i < joArray.length(); i++) {
                JSONObject joObject =joArray.getJSONObject(i);
                DeviceDataDTO dmDetails=new DeviceDataDTO();
                dmDetails.setId(joObject.getString("StudentID"));
                dmDetails.setName(joObject.getString("Name"));

                dmDetails.setPath(joObject.getString("Photo").replaceAll("~", "").trim());
                dmDetails.setType(joObject.getString("Type"));
                dmDetails.setImei_no(joObject.getString("DeviceID"));

                childlist.add(dmDetails);
            }


        }catch(Exception e){
            Log.e("Exception", ""+e);
        }finally{
            //it work Better but take time to Load
            if (childlist.size()>0) {

                //Insert Offeline data
                helper.Insert_Device_list(childlist);
                if (childlist.size()>1)
                    ShowListofStudent();
                else
                    StudentId=childlist.get(0).getId();

                //	GetstudentVTSStatus();

            }else{
                Common.ShowSweetAlert(context,"No User Information" );
            }

        }
    }



    public  void parsespeedalert(String comMsg) {
        //Parse reponcse of exiting speed alert
        GVTSFunction.Exit=true;
	/*	{"data":{
			"alert_id":"328483059",
			"alert_type":
			{"alert":
			{"email":"True","push_to_app":"True","sms":"True"}}
		,"enable":"True","name":"demo","speed":"052"},
		"event":"speed_alert_data"}*/

        try {

            JSONObject jomain=new JSONObject(comMsg);


            JSONObject jodata=jomain.getJSONObject("data");
            JSONObject joalert_type=jodata.getJSONObject("alert_type");

            JSONObject joalert=joalert_type.getJSONObject("alert");

            speed.setAlertID(jodata.getString("alert_id"));
            speed.setEnable(jodata.getString("enable"));
            speed.setAlertName(jodata.getString("name"));
            speed.setSpeed(jodata.getString("speed"));
            speed.setAlertapp(joalert.getString("push_to_app"));
            speed.setAlertemail(joalert.getString("email"));
            speed.setSms(joalert.getString("sms"));

            Show_speedalert_dilog();

        } catch (JSONException e) {
            e.printStackTrace();		}



    }



    public void Show_speedalert_dilog() {
        // custom dialog
        final CheckBox alert_app,alertsms;
        final CheckBox alert_email;
        ToggleButton activemode;
        final EditText alertname;
        final EditText speedlimit;
        final Dialog dialog = new Dialog(this.getActivity().getWindow().getContext());
        dialog.setContentView(R.layout.dialog_speedlalert);
        dialog.setTitle("Set Alert Deatil");


        activemode = (ToggleButton) dialog.findViewById(R.id.toggleenable);
        alertname = (EditText) dialog.findViewById(R.id.alertname);
        speedlimit=(EditText)dialog.findViewById(R.id.speedlimit);

        alert_app = (CheckBox) dialog.findViewById(R.id.alertapp);
        alert_email = (CheckBox) dialog.findViewById(R.id.alertemail);
        alertsms = (CheckBox) dialog.findViewById(R.id.alertsms);


        Button Submitdetil = (Button) dialog.findViewById(R.id.alertsubmit);

        if (Exit) {
            alertname.setText(speed.getAlertName());
            speedlimit.setText(speed.getSpeed());
            alert_app.setChecked(Boolean.parseBoolean(speed.getAlertapp()));
            alert_email.setChecked(Boolean.parseBoolean(speed.getAlertemail()));
            alertsms.setChecked(Boolean.parseBoolean(speed.getSms()));
            activemode.setChecked(Boolean.parseBoolean(speed.getEnable()));
        }else {
            // for by default Active mode
            speed.setEnable("True");
            speed.setAlertID("0");
        }


        activemode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    speed.setEnable("True");
                else
                    speed.setEnable("False");
            }
        });

        // if button is clicked, close the custom dialog
        Submitdetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alertname.getText().length()!=0){
                    if(alert_app.isChecked()||alert_email.isChecked())
                    {
                        if (alert_app.isChecked())
                            speed.setAlertapp("True");
                        else speed.setAlertapp("False");

                        if (alert_email.isChecked())
                            speed.setAlertemail("True");
                        else speed.setAlertemail("False");

                        if (alertsms.isChecked())
                            speed.setSms("True");
                        else speed.setSms("False");

                        speed.setAlertName(alertname.getText().toString());
                        speed.setSpeed(speedlimit.getText().toString());

                        LoginActivity.mClient.sendMessage(make_alertUpdate_JSON());

                        dialog.dismiss();

                    }else {
                        Common.ShowSweetAlert(context,"Please checked atleast one from each group ");
                    }
                }else{
                    alertname.setError("Please enter valid alert name.");
                }

            }
        });

        dialog.show();
    }


    protected static String make_alertUpdate_JSON() {

        System.out.println("make_alertUpdate_JSON****"+new Gson().toJson(speed));
        String update_string="{}";
        try{
            JSONObject jo = new JSONObject();

            jo.put("event", Common.EV_UPDATECAR_SPEEDALERT);
            jo.put("student_id",Integer.parseInt(StudentId));
            jo.put("alert_id",speed.getAlertID());

            JSONObject jodata = new JSONObject();

            jodata.put("name",speed.getAlertName());
            jodata.put("speed",String.format("%03d", Integer.parseInt(speed.getSpeed())));
            jodata.put("enable",speed.getEnable());
            //	jodata.put("status","N");

            JSONObject joalert_type = new JSONObject();

            JSONObject joalert = new JSONObject();

            joalert.put("push_to_app",speed.getAlertapp());
            joalert.put("email",speed.getAlertemail());
            joalert.put("sms",speed.getSms());


            joalert_type.put("alert",joalert);


            jodata.put("alert_type",joalert_type);

            jo.put("data",jodata);

            //	fencenew_string=gson.toJson(jo).replace("\\", "");

            update_string=jo.toString();

            System.err.println("Request  Update update_string======== "+update_string);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return update_string;

    }





    protected String makeJsonstopcar() {
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event", Common.EV_STOP_CAR);

            jo.put("student_id",StudentId);
            trackSTring=jo.toString();
            System.out.println(trackSTring);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return trackSTring;
    }

    protected String makeJsonstartcar() {
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event", Common.EV_START_CAR);

            jo.put("student_id",StudentId);
            trackSTring=jo.toString();
            System.out.println(trackSTring);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return trackSTring;
    }

    protected void PostEnginePin(final String Pin) {
        reuestQueue=Volley.newRequestQueue(context); //getting Request object from it
        final ProgressDialog pDialog1 = new ProgressDialog(context);
        pDialog1.setTitle("Progress wait.......");
        pDialog1.setCancelable(false);
        pDialog1.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/PostEnginePin",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  PostEnginePin----"+response);
                pDialog1.dismiss();

                parseupdateJSON(response,Pin);


            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.dismiss();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            parseupdateJSON(new String(error.networkResponse.data),Pin);
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID",StudentId);
                params.put("Pin",Pin);

                System.out.println("REq---PostEnginePin------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    protected void parseupdateJSON(String response, String pin) {

        try {
            JSONObject jo=new JSONObject(response);

            if (jo.getString("error").equals("false")) {
                Enginepin=pin;
                pinexist=true;
            }
            Common.ShowSweetAlert(context,jo.getString("message"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            createpindialog.dismiss();
        }
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vts_report_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chng_pass:
                Intent intent=new Intent(context,ChanageEnginepassword.class);
                intent.putExtra("OldPin", Enginepin);
                intent.putExtra("StudentId",StudentId);
                startActivity(intent);
			*//*Common.showToast(context,getResources()().getString(R.string.welcome)+Common.username);*//*
                return true;
            case R.id.forget_pass:
                GetForgetPassword();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

*/
    private void GetForgetPassword() {

        reuestQueue=Volley.newRequestQueue(context); //getting Request object from it
        final ProgressDialog pDialog1 = new ProgressDialog(context);
        pDialog1.setTitle("Progress wait.......");
        pDialog1.setCancelable(false);
        pDialog1.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"ParentAPI.asmx/SendEnginePin",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  GetForgetPassword----"+response);
                pDialog1.dismiss();

                if (response.contains("false"))
                    Common.ShowSweetSucess(context," Engine pin send sucessfully on your register Mail-Id. ");
                else
                    Common.ShowSweetAlert(context,"Error on sending mail.Please contact with contact@mykiddytracker.com ");

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.dismiss();
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

                params.put("StudentId",StudentId);
                params.put("ParentId", Common.userid);

                System.out.println("REq---GetForgetPassword------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }





}
