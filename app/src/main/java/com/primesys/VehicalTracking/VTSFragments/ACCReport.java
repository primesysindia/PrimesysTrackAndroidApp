package com.primesys.VehicalTracking.VTSFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.Home;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.Dto.SpeedalertDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.MyAdpter.VhehicalReportlistAdpter;
import com.primesys.VehicalTracking.R;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 7/10/16.
 */
public class ACCReport extends Fragment {
    private View rootView;
    private  RequestQueue reuestQueue;
    private  StringRequest stringRequest;
    public static  boolean Exit = false;
    Button cal_dist,speedalrt,stopcar,startcar;
    RecyclerView acclist;
    public static Context context;
    SweetAlertDialog pDialog;
    public StudentListAdpter padapter;
    DBHelper helper;
    private ArrayList<GmapDetais> childlist=new ArrayList<>();
    public static String StudentId="0";
    public static String StudentName="";

    public static SpeedalertDTO speed=new SpeedalertDTO();
    ArrayList<VehicalTrackingSMSCmdDTO> smslistdata=new ArrayList<VehicalTrackingSMSCmdDTO>();
    public VhehicalReportlistAdpter mAdpter;
    private ListView listStudent;
    public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
    public SharedPreferences sharedPreferences;
    private String key_DeviceSimNo="DeviceSimNo";
    private SharedPreferences.Editor editor;
    public String DeviceSimNo="";
    private TextView tv_msg;
    private Dialog EditVsNumberDialog;
    private String TAG="SMSFuction";
    private String updated_simno="";
    public static String DeviceImieNo="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.acc_function, container, false);
        context = container.getContext();
        helper = DBHelper.getInstance(context);
       acclist = (RecyclerView) rootView.findViewById(R.id.acclistfunction);
        acclist.setLayoutManager(new LinearLayoutManager(context));
        acclist.setItemAnimator(new DefaultItemAnimator());
        acclist.setHasFixedSize(true);
        findById();
        if (helper.Show_AccReport().size()>0)
            SetreportListData();
        else
            GetVehReportlist();

        try {
            ShowMapFragment.CDT.cancel();
            Home.tabLayout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*

        acclist = (RecyclerView) view.findViewById(R.id.acclist);
        acclist.setLayoutManager(new LinearLayoutManager(context));
        acclist.setItemAnimator(new DefaultItemAnimator());

*/



       /* mAdpter = new VhehicalSMSlistAdpter(acclistdata, R.layout.row_acclist, context);
        acclist.setAdapter(mAdpter);*/

    }



    public void CheckStudent(Context context1) {
        helper = DBHelper.getInstance(context1);

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
            else {
                StudentId = childlist.get(0).getId();
                StudentName= childlist.get(0).getName();
                DeviceImieNo = childlist.get(0).getImei_no();
                Log.e("DeviceImieNo=========",DeviceImieNo);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermission())
                        requestPermission();


                }
            }
        }


    }



    private void findById() {



        sharedPreferences=context.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    private void ShowListofStudent() {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(this.context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.dialog_studentlist);
            dialog.setCancelable(false);
            dialog.closeOptionsMenu();
            dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            listStudent = (ListView) dialog.findViewById(R.id.student_list);
            ImageView close = (ImageView) dialog.findViewById(R.id.imageView_close);

//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
            padapter = new StudentListAdpter(context, R.layout.fragment_mapsidebar, childlist);
            listStudent.setAdapter(padapter);

            listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    StudentId = childlist.get(position).getId();
                    StudentName= childlist.get(position).getName();

                    DeviceImieNo=childlist.get(position).getImei_no();

                    Log.e("=================================================",""+StudentId+"   "+DeviceImieNo);
                    if (!checkPermission())
                        requestPermission();

                    dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Home.viewPager.setCurrentItem(0);
                    dialog.dismiss();

                }

            });

            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Track Informatiion
    class TrackInfrmation extends AsyncTask<Void, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog =Common.ShowSweetProgress(context, "Progress wait.......");
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
                GmapDetais dmDetails=new GmapDetais();
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
                {
                    StudentId=childlist.get(0).getId();
                    DeviceImieNo=childlist.get(0).getImei_no();
                    StudentName= childlist.get(0).getName();

                }


            }else{
                Common.ShowSweetAlert(context,"No User Information" );
            }

        }
    }



    public  void parsespeedalert(String comMsg) {
        //Parse reponcse of exiting speed alert
        VTSFunction.Exit=true;
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
   /* private void GetVehReportlist() {

        reuestQueue=Volley.newRequestQueue(context); //getting Request object from it
        final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(context, "Progress wait.......");

        //JSon object request for reading the json data
       // stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetVehReportlist",new Response.Listener<String>() {
        stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/UserServiceAPI/GetVehReportlistCheck",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  d----"+response);
                pDialog1.dismiss();

                Parsesmslist(response);
            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());

                        pDialog1.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID",StudentId);

                System.out.println("REq---GetVehReportlist------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }*/

    private void GetVehReportlist() {


        reuestQueue = Volley.newRequestQueue(context);

        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "UserServiceAPI/GetVehReportlistCheck", new Response.Listener<String>() {
          //  stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/UserServiceAPI/GetVehReportlistCheck",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("Responce---GetVehReportlist********************************************------"+response);

                Parsesmslist(response);


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
                            Parsesmslist(new String(error.networkResponse.data));
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID",StudentId);

                System.out.println("REq---GetVehReportlist------"+params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }
    private void Parsesmslist(String response) {

        try {

            JSONArray jarray=new JSONArray(response);

            if (jarray.length()>0) {
                for (int i = 0; i < jarray.length(); i++) {

                    JSONObject jo = jarray.getJSONObject(i);
                    VehicalTrackingSMSCmdDTO msgObj = new VehicalTrackingSMSCmdDTO();

                    msgObj.setCommnadType(jo.getString("CommnadType") + "");

                    msgObj.setActualCommand(jo.getString("ActualCommand") + "");
                    msgObj.setTitle(jo.getString("Title"));
                    msgObj.setAnsFromDevice(jo.getString("AnsFromDevice"));

                    smslistdata.add(msgObj);

                }

                helper.insertAccReport(smslistdata);


            }else Common.ShowSweetAlert(context,"Error in fetching data.Please try again.");




        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            SetreportListData();
        }

    }

    private void SetreportListData() {

        ArrayList<VehicalTrackingSMSCmdDTO> smslistdata=new ArrayList<>();
        smslistdata=helper.Show_AccReport();
        mAdpter = new VhehicalReportlistAdpter(smslistdata, R.layout.row_smslist, context);
        acclist.setAdapter(mAdpter);
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions((Activity) context,new String[]{android.Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SMS);

    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    //  Sendsms(devicedata.getDeviceSimNumber());
                } else {
                    // permission denied
                    Common.ShowSweetAlert(context,"You denied permission of sending SMS. If you really want to use Voice surveillance allow that one. ");

                }
                return;
            }
        }
    }












}

