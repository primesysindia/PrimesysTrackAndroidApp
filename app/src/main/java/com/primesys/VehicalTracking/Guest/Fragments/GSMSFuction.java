package com.primesys.VehicalTracking.Guest.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.SpeedalertDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.Guest.Adpter.GStudentListAdpter;
import com.primesys.VehicalTracking.Guest.Adpter.GVhehicalSMSlistAdpter;
import com.primesys.VehicalTracking.Guest.GDatabaseHelper;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 7/10/16.
 */
public class GSMSFuction extends Fragment {
    private View rootView;
    private  RequestQueue reuestQueue;
    private  StringRequest stringRequest;
    public static  boolean Exit = false;
    Button cal_dist,speedalrt,stopcar,startcar;
    RecyclerView smslist;
    public static Context context;
    SweetAlertDialog pDialog;
    public GStudentListAdpter padapter;
    public static GDatabaseHelper helper;
    private ArrayList<DeviceDataDTO> childlist=new ArrayList<>();
    public static String StudentId="0";
    public static SpeedalertDTO speed=new SpeedalertDTO();
    ArrayList<VehicalTrackingSMSCmdDTO> smslistdata=new ArrayList<VehicalTrackingSMSCmdDTO>();
    public GVhehicalSMSlistAdpter mAdpter;
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
        helper = GDatabaseHelper.getInstance(context);
        findById();
        if (helper.Show_SMSFunction().size()>0)
            SetSmsListData();

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    public void CheckStudent(Context context1) {
        helper = GDatabaseHelper.getInstance(context);

            childlist=helper.Show_Device_list();

            if (childlist.size()>1)
                ShowListofStudent(context);
            else {
                StudentId = childlist.get(0).getId();
                ShowDeviceNoConfirmationDialog();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermission())
                        requestPermission();


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
    private void ShowListofStudent(Context context1) {
        // custom dialog

         final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_studentlist);
        dialog.setCancelable(false);
        dialog.closeOptionsMenu();
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.show();
        listStudent=(ListView)dialog.findViewById(R.id.student_list);

//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
        padapter=new GStudentListAdpter(context, R.layout.fragment_mapsidebar, childlist);
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
                ShowDeviceNoConfirmationDialog();
                if (!checkPermission())
                    requestPermission();

                dialog.dismiss();
            }
        });


        dialog.show();
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

    private void SetSmsListData() {

        ArrayList<VehicalTrackingSMSCmdDTO> smslistdata=new ArrayList<>();
        smslistdata=helper.Show_SMSFunction();
        mAdpter = new GVhehicalSMSlistAdpter(smslistdata, R.layout.row_smslist, context);
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


        tv_msg.setText("Please confirm 9405443050 is device SIM Number ? ");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowListofStudent(context);
                dialog.dismiss();

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.ShowSweetAlert(context, context.getResources().getString(R.string.str_guest_error_msg));
            }
        });


        dialog.show();
    }





}

//[{"CommnadType":"Over Speed","ActualCommand":"#speed#123456#080#","Title":"Over Speed","AnsFromDevice":"speedok!"},{"CommnadType":"stop electric","ActualCommand":"#Stopelec#123456#","Title":"stop electric","AnsFromDevice":"Stop Electicity ok!"},{"CommnadType":"Supply electric","ActualCommand":"#supplyelec#123456#","Title":"Supply electric","AnsFromDevice":"supply electcity ok!\r"},{"CommnadType":"stop Oil","ActualCommand":"#Stopoil#123456#","Title":"stop Oil","AnsFromDevice":"stop oil ok!"},{"CommnadType":"supply Oil","ActualCommand":"#supplyoil#123456#","Title":"supply Oil","AnsFromDevice":"supply oil ok!"},{"CommnadType":"Location","ActualCommand":"#smslink#123456#","Title":"Location","AnsFromDevice":"http:/maps.google.com/maps?q\u003dN0.000000,E0.000\r\n000 speed:000.0km/h Time:19:08:12\r\nDate:16/10/04 IMEI:355488020878745"},{"CommnadType":"ACC on","ActualCommand":"#ACC#ON#","Title":"ACC on","AnsFromDevice":"ACC ON OK"},{"CommnadType":"ACC off","ActualCommand":"#ACC#OFF#","Title":"ACC off","AnsFromDevice":"ACC OFF OK"},{"CommnadType":"Bike Start","ActualCommand":"","Title":"Bike Start","AnsFromDevice":"ACC !!!IMEI:355488020878745N0.000000,E0.000000"},{"CommnadType":"Speed Alarm","ActualCommand":"","Title":"Speed Alarm","AnsFromDevice":"Speed Alarm !IMEI:355488020878745\r\n0.000000,N,0.000000,E"}]