package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddStudentActivity extends AppCompatActivity {

    private RadioGroup radioSexGroup;
    EditText text_fname,text_lname,text_mobileno,text_imei_no;
    private Button btn_submit,btn_clear;
    private Toolbar toolbar;
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    public SharedPreferences sharedPreferences;
    Context mContext=AddStudentActivity.this;
    private String txt_gender;
    String fname,lname,mobileno,imei_no,device_type;
    private Spinner device_type_spinner;
    int spinner_Slectedposition=0;
    private SweetAlertDialog pDialog;
    private boolean isNewIMEI=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        finViewBYID();
        btn_submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int selected=radioSexGroup.getCheckedRadioButtonId();
                RadioButton gender=(RadioButton) findViewById(selected);
                txt_gender=  gender.getText().toString().substring(0,1);

                if (valid())
                    if (Common.getConnectivityStatus(mContext)){
                        if (isNewIMEI)
                            addDeviceRequest();
                        else
                            Common.ShowSweetAlert(mContext, "This IMEI No already added device. For any query contact to contact@mykiddytracker.com ");

                    }

                    else
                        Common.ShowSweetAlert(mContext, "Please enable internet connection!");


            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clear();
            }
        });
        device_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                device_type=parent.getItemAtPosition(position).toString();
                spinner_Slectedposition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        text_imei_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (text_imei_no.getText().length() > 0 || text_imei_no.length()<15) {
                        CheckValidIMEI();
                    } else Common.ShowSweetAlert(mContext, "Enter Valid  Device IMEI Number!");
                }
            }
        });

    }


    void finViewBYID()
    {


        text_fname=(EditText)findViewById(R.id.input_fname);

        text_lname=(EditText)findViewById(R.id.input_lname);
        text_mobileno=(EditText)findViewById(R.id.input_number);
        //	spinner_userType.setBackgroundResource(R.layout.spinner_textview);

        text_imei_no=(EditText)findViewById(R.id.input_imei_no);

        btn_submit=(Button)findViewById(R.id.btn_signup);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        //	check_agree=(CheckBox)findViewById(R.id.check_agree);
        device_type_spinner=(Spinner)findViewById(R.id.spinner_devicetype);
    }

    boolean valid()
    {
        boolean valid=true;
        String MOB_NO="\\d{10}";

        fname=text_fname.getText().toString().trim();
        lname=text_lname.getText().toString().trim();
        mobileno=text_mobileno.getText().toString().trim();
        imei_no=text_imei_no.getText().toString().trim();

        //	pincode=text_zipcode.getText().toString().trim();
        if(spinner_Slectedposition==0)
        {

                Common.ShowSweetAlert(mContext, "Please select device type !");
                valid=false;
        }
        if(fname.length()==0)
        {
            Common.ShowSweetAlert(mContext, "Enter first name!");
            valid=false;
        } else if(fname.length()<4)
        {
            Common.ShowSweetAlert(mContext, "Enter first name at least 4 character !");
            valid=false;
        }

        else if(lname.length()==0)
        {
            Common.ShowSweetAlert(mContext, "Enter last name  !");
            valid=false;
        }
        else if(imei_no.length()==0)
        {
            Common.ShowSweetAlert(mContext, "Enter valid IMEI number !");
            valid=false;
        } else if(imei_no.length()<15)
        {
            Common.ShowSweetAlert(mContext, "Enter valid IMEI number it must be 15 digit !");
            valid=false;
        }

        else if(mobileno.length()==0)
        {
            Common.ShowSweetAlert(mContext, "Enter mobile number!");
            valid=false;
        }
        else if(!mobileno.matches(MOB_NO))
        {

            Common.ShowSweetAlert(mContext, "Mobile number is 10 digit !");
            valid=false;
        }

        return valid;
    }

    //reset or clear all the fields
    void clear()
    {
        text_fname.setText("");
        text_lname.setText("");
        text_mobileno.setText("");
        text_imei_no.setText("");

        //text_zipcode.setText("");
    }

    private void addDeviceRequest() {
        reuestQueue= Volley.newRequestQueue(mContext);
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Creating Account");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"ParentAPI/AddDevice",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response!=null)
                    parseAddDeviceJSON(response);
                pDialog.hide();
            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            Log.e("Response ",error.toString());
                            pDialog.hide();

                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try
                {

                    //	params.put("id",id);
                    params.put("device_type",device_type);

                    params.put("fname",fname);
                    params.put("lname",lname);
                    params.put("mob_no",mobileno);
                    params.put("imei_no",imei_no);
                    params.put("gender",txt_gender);
                    params.put("user_id", Common.userid);


                    System.out.println("postRegistrationRequest Req --------"+params);
                    //params.put("bdate",birthDate);
                }catch(Exception e)
                {

                }
                Log.e("params ",params+"");
                //	params.put("user", user+"");
                return params;
            }

        };
        stringRequest.setTag("");
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                20 * 1000,
                0,
                1
        );
        stringRequest.setRetryPolicy(retryPolicy);
        reuestQueue.add(stringRequest);
    }

    private void parseAddDeviceJSON(String response) {
        Log.e("parseAddDeviceJSON","parseAddDeviceJSON  REspo---- " + response);
    //  {"":"Device register successfully.You can use it.","error":"false","id":"9657"}
      try{
          JSONObject jo=new JSONObject(response);
          if (jo.getString("error").equalsIgnoreCase("false")){
              SendDeviceIdToErlangServer(jo.getString("id"));

          }else Common.ShowSweetSucess(mContext,jo.getString("message"));
          jo.getString("id");
          PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_DEVICE_LIST);

      }catch (Exception e){
          e.printStackTrace();
      }


    }

    private void SendDeviceIdToErlangServer(String student_id) {

        String baseurl = "http://mykidtracker.in:8181/set_device?student_id=" + student_id + "&parent_id=" + Common.userid + "&device_id=" + imei_no;
        reuestQueue= Volley.newRequestQueue(mContext);
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, baseurl,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response ","SendDeviceIdToErlangServer-Response----"+response);

//                if(response!=null)
//                    parseAddDeviceJSON(response);
                if (response.equalsIgnoreCase("ok")){
                    Common.ShowSweetSucess(mContext,"Device added successfully.");
                }else {

                    Common.ShowSweetAlert(mContext,response+ "Please contact at contact@mykiddytracker.com");
                }
                pDialog.hide();
            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            Log.e("Response ",error.toString());
                            pDialog.hide();

                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.e("params ","SendDeviceIdToErlangServer-----"+params);
                //	params.put("user", user+"");
                return params;
            }

        };
        stringRequest.setTag("");
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                20 * 1000,
                0,
                1
        );
        stringRequest.setRetryPolicy(retryPolicy);
        reuestQueue.add(stringRequest);


    }


    private void CheckValidIMEI() {
        reuestQueue= Volley.newRequestQueue(mContext);
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Validating  IMEI No");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"ParentAPI/CheckIMEIExist",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response!=null)
                    parseIMEIJSON(response);
                pDialog.hide();
            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            pDialog.hide();

                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try
                {
                    params.put("imei_no",text_imei_no.getText().toString());
                    Log.e("parseIMEIJSON--","parseIMEIJSON Req --------"+params);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                Log.e("params ",params+"");
                //	params.put("user", user+"");
                return params;
            }

        };
        stringRequest.setTag("");
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                20 * 1000,
                0,
                1
        );
        stringRequest.setRetryPolicy(retryPolicy);
        reuestQueue.add(stringRequest);

    }
    //parse the result
    void parseIMEIJSON(String result)
    {
        Log.e("parseIMEIJSON--","parseIMEIJSON REspo --------"+result);

        try
        {
            JSONObject jo=new JSONObject(result);

            if(jo.getString("error").equalsIgnoreCase("true")){
                Common.ShowSweetAlert(mContext, "This IMEI No already added device. For any query contact to contact@mykiddytracker.com ");
                text_imei_no.setError("This IMEI No already  added device.For any query contact to contact@mykiddytracker.com");
                isNewIMEI=false;

            }else {
                text_imei_no.setError(null);
                isNewIMEI=true;
            }
        }
        catch(Exception e)
        {

            e.printStackTrace();
        }
    }



}
