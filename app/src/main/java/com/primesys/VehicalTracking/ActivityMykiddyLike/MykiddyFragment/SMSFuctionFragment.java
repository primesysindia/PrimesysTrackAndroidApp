package com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.SMSlistAdpter;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.SpeedalertDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
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

import static com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNew.Bottom_navigation;


/**
 * Created by root on 7/10/16.
 */
public class SMSFuctionFragment extends Fragment {
    private View rootView;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    public static  boolean Exit = false;
    Button cal_dist,speedalrt,stopcar,startcar;
    RecyclerView smslist;
    public static Context context;
    SweetAlertDialog pDialog;
    public StudentListAdpter padapter;
    DBHelper helper;
    private ArrayList<DeviceDataDTO> childlist=new ArrayList<>();
    public static String StudentId="0";
    public static SpeedalertDTO speed=new SpeedalertDTO();
    ArrayList<VehicalTrackingSMSCmdDTO> smslistdata=new ArrayList<VehicalTrackingSMSCmdDTO>();
    public SMSlistAdpter mAdpter;
    private ListView listStudent;
    public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
    public SharedPreferences sharedPreferences;
    private String key_DeviceSimNo="DeviceSimNo";
    private SharedPreferences.Editor editor;
    public String DeviceSimNo="";
    private TextView tv_msg;
    private Dialog EditVsNumberDialog;
    private String TAG="GSMSFuction";
    private String updated_simno="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.sms_function, container, false);
        context = container.getContext();
        findById();

        if (PrimesysTrack.mDbHelper.Show_SMSFunction().size()>0)
            SetSmsListData();
        else
            GetSMSItemlist();
        CheckStudent(context);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public void CheckStudent(Context context1) {

        if (Common.getConnectivityStatus(context1)&& PrimesysTrack.mDbHelper.Show_Device_list().size()==0) {
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
            childlist=PrimesysTrack.mDbHelper.Show_Device_list();

            if (childlist.size()>1)
                ShowListofStudent();
            else {
                StudentId = childlist.get(0).getId();
                GetDeviceMobileNo();
                //GetCarEnginePin();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermission())
                        requestPermission();


                }
            }
        }


    }



    private void findById() {
        smslist = (RecyclerView) rootView.findViewById(R.id.smslistfunction);
        smslist.setLayoutManager(new LinearLayoutManager(context));
        smslist.setItemAnimator(new DefaultItemAnimator());
        smslist.setHasFixedSize(true);


        sharedPreferences=context.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    private void ShowListofStudent() {
        // custom dialog

        final Dialog dialog = new Dialog(context);
        dialog.setTitle(context.getResources().getString(R.string.select_device)+" For SMS Function");

        dialog.setContentView(R.layout.dialog_studentlist);
        dialog.setCancelable(false);
        dialog.closeOptionsMenu();
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.show();
        listStudent=(ListView)dialog.findViewById(R.id.student_list);
        Button cancel = (Button) dialog.findViewById(R.id.d_cancel);
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Home.viewPager.setCurrentItem(0);
                dialog.dismiss();
//                getActivity().finish();
                Bottom_navigation.setSelectedItemId(R.id.navigation_track);



            }
        });
//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
        padapter=new StudentListAdpter(context, R.layout.fragment_mapsidebar, childlist);
        listStudent.setAdapter(padapter);

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
              /*  for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                // change the background color of the selected element
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
*/
                StudentId=childlist.get(position).getId();
                GetDeviceMobileNo();
              //  GetCarEnginePin();

                if (!checkPermission())
                    requestPermission();

                dialog.dismiss();
            }
        });


        dialog.show();
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
                HttpPost   httpost=new HttpPost(Common.URL+"ParentAPI.asmx/GetTrackInfo");
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
                PrimesysTrack.mDbHelper.Insert_Device_list(childlist);
                if (childlist.size()>1)
                    ShowListofStudent();
                else
                    StudentId=childlist.get(0).getId();
                try {
                    GetDeviceMobileNo();

                }catch (Exception e){
                    e.printStackTrace();
                }
/*
                if (!checkPermission())
                    requestPermission();*/

            }else{
                Common.ShowSweetAlert(context,"No User Information" );
            }

        }
    }



    public  void parsespeedalert(String comMsg) {
        //Parse reponcse of exiting speed alert
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
    private void GetSMSItemlist() {

        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
         final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(context, "Progress wait.......");

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetMyKiddySMSItemlist",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.e("-***---*************-","Responce of GetSMSItemlist ---"+response);
                pDialog1.dismiss();

               Parsesmslist(response);
            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.hide();
                        if (error!=null)
                            Log.d("Error", error.toString());


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID",StudentId);

                System.out.println("REq---GetSMSItemlist------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
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

                PrimesysTrack.mDbHelper.insertSMSFunction(smslistdata);


            }else Common.ShowSweetAlert(context,"Error in fetching data.Please try again.");




        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            SetSmsListData();
        }

    }
    private void SetSmsListData() {

        ArrayList<VehicalTrackingSMSCmdDTO> smslistdata=new ArrayList<>();
        smslistdata=PrimesysTrack.mDbHelper.Show_SMSFunction();
        mAdpter = new SMSlistAdpter(smslistdata, R.layout.row_smslist, context);
        smslist.setAdapter(mAdpter);
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



    private void GetDeviceMobileNo()
    {


        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
         pDialog = Common.ShowSweetProgress(context, "Getting Device Sim No.......");

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetSMSDeviceSimNo",new Response.Listener<String>() {
            //stringRequest = new StringRequest(Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/SQLQuiz/Postscore",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of Get sim no----"+response);

                parseMObJSON(response);
                pDialog.dismiss();


            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if (error!=null)
                            Log.e("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID", SMSFuctionFragment.StudentId);
                params.put("ComandType","monitor");

                System.out.println("REq----get mob------"+params);
                return params;
            }

        };


        stringRequest.setTag("Phome no");
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    protected void parseMObJSON(String result)
    {

        try {
            JSONObject jo=new JSONObject(result);
            if (jo.getString("error").equals("false"))
            {     DeviceSimNo = jo.getString("DeviceSimNumber");

                if (DeviceSimNo.equalsIgnoreCase("null")||DeviceSimNo.equalsIgnoreCase(""))
                    ShowEditVsNumberDialog("");
                else
                     ShowDeviceNoConfirmationDialog();


            }else Common.ShowSweetAlert(context,"We are not getting device sim no.Please contact to contact@mykiddytracker.com ");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //finish();
        }


    }





    private void ShowDeviceNoConfirmationDialog() {

        Button yes,edit,no;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.deviceno_confirmation_dialog);
        dialog.setTitle("Confirm Device Sim No");
        dialog.setCancelable(false);
        tv_msg=(TextView) dialog.findViewById(R.id.tv_msg);
        yes=(Button) dialog.findViewById(R.id.vs_yes);
        no=(Button) dialog.findViewById(R.id.vs_no);
        edit=(Button)dialog.findViewById(R.id.vs_edit);


        tv_msg.setText("Please confirm "+DeviceSimNo+" is device SIM Number ? ");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDeviceSimNO();

                dialog.dismiss();


            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowListofStudent();
                dialog.dismiss();

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditVsNumberDialog(DeviceSimNo);



            }
        });


        dialog.show();
    }





    public void ShowEditVsNumberDialog(String deviceSimNo) {

        // custom dialog

        final EditText simno;
        final Button submit;
        EditVsNumberDialog = new Dialog(context);
        EditVsNumberDialog.setContentView(R.layout.vs_editno_dialog);
        EditVsNumberDialog.setTitle("Update Device SIM No ");


        simno=(EditText)EditVsNumberDialog.findViewById(R.id.edit_no);
        submit=(Button) EditVsNumberDialog.findViewById(R.id.vsnosubmit);

        if (deviceSimNo.equals("")||deviceSimNo.equals("NUll"))
            simno.setText(deviceSimNo);




        // if button is clicked, close the custom dialog
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updated_simno=simno.getText().toString();

                if (!updated_simno.equals("")&&updated_simno.length()>=10)
                {
                    PostDeviceSimno(updated_simno);

                }
                else
                    Common.ShowSweetAlert(context,"Please enter valid device sim number.");

            }
        });

        EditVsNumberDialog.show();

    }

    protected void PostDeviceSimno(final String simno) {


        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
        final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(context, "Progress wait.......");

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/PostDeviceSimno",new Response.Listener<String>() {
            //stringRequest = new StringRequest(Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/SQLQuiz/Postscore",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog1.hide();

                System.out.println("Responce of mobileno---"+response);

                parseSimupdateJSON(response);



            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.hide();
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID", SMSFuctionFragment.StudentId);
                params.put("Simno",simno);

                System.out.println("REq----post mobileno------"+params);
                return params;
            }

        };


        stringRequest.setTag(TAG);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    protected void parseSimupdateJSON(String response) {

        try {
            JSONObject jo=new JSONObject(response);

            if (jo.getString("error").equals("false")) {

                DeviceSimNo=updated_simno;
              //  tv_msg.setText("Please confirm "+DeviceSimNo+" is device SIM Number ? ");
                ShowDeviceNoConfirmationDialog();
                Common.ShowSweetSucess(context,jo.getString("message"));


            }else {
                Common.ShowSweetAlert(context,jo.getString("message"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            EditVsNumberDialog.dismiss();
        }
    }

   void SaveDeviceSimNO(){

       sharedPreferences=context.getSharedPreferences("User_data",Context.MODE_PRIVATE);
       editor = sharedPreferences.edit();

       editor.putString(key_DeviceSimNo, DeviceSimNo);
       editor.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.back) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }



    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    Bottom_navigation.setSelectedItemId(R.id.navigation_track);
                    return true;
                }
                return false;
            }
        });
    }
}

//[{"CommnadType":"Over Speed","ActualCommand":"#speed#123456#080#","Title":"Over Speed","AnsFromDevice":"speedok!"},{"CommnadType":"stop electric","ActualCommand":"#Stopelec#123456#","Title":"stop electric","AnsFromDevice":"Stop Electicity ok!"},{"CommnadType":"Supply electric","ActualCommand":"#supplyelec#123456#","Title":"Supply electric","AnsFromDevice":"supply electcity ok!\r"},{"CommnadType":"stop Oil","ActualCommand":"#Stopoil#123456#","Title":"stop Oil","AnsFromDevice":"stop oil ok!"},{"CommnadType":"supply Oil","ActualCommand":"#supplyoil#123456#","Title":"supply Oil","AnsFromDevice":"supply oil ok!"},{"CommnadType":"Location","ActualCommand":"#smslink#123456#","Title":"Location","AnsFromDevice":"http:/maps.google.com/maps?q\u003dN0.000000,E0.000\r\n000 speed:000.0km/h Time:19:08:12\r\nDate:16/10/04 IMEI:355488020878745"},{"CommnadType":"ACC on","ActualCommand":"#ACC#ON#","Title":"ACC on","AnsFromDevice":"ACC ON OK"},{"CommnadType":"ACC off","ActualCommand":"#ACC#OFF#","Title":"ACC off","AnsFromDevice":"ACC OFF OK"},{"CommnadType":"Bike Start","ActualCommand":"","Title":"Bike Start","AnsFromDevice":"ACC !!!IMEI:355488020878745N0.000000,E0.000000"},{"CommnadType":"Speed Alarm","ActualCommand":"","Title":"Speed Alarm","AnsFromDevice":"Speed Alarm !IMEI:355488020878745\r\n0.000000,N,0.000000,E"}]