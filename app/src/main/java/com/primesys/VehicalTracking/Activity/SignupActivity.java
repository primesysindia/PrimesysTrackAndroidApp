package com.primesys.VehicalTracking.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SignupActivity extends AppCompatActivity {
    EditText text_name,text_email,text_mobileno;
    EditText text_msg;
    Button btn_registration,btn_clear;
    private CheckBox check_agree;
    Context context = SignupActivity.this;
    String name,email,mobileno,username,password,user_type,msg,Group;
    public static final String TAG="Registration";
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    Spinner spinner_userType,spinner_userGroup;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    private final String key_id = "User_Id";
    public ArrayList<String> grouplist= new ArrayList<String>();
    final static int REQUEST_CAMERA=100;
    final static int SELECT_FILE=200;
    private String phpurl="http://www.mykiddytracker.com/send_mail";

    //For Camera Image 7/9/2015
    final int CAMERA_CAPTURE = 1;
    // keep track of cropping intent
    final int PIC_CROP = 2;
    // captured picture uri
    private Uri picUri;
    private EditText text_lname;
    private Toolbar toolbar;
    private boolean isExist=false;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        finViewBYID();




        btn_registration.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (valid())
                    if (Common.getConnectivityStatus(context))
                        postRegistrationRequest();
                    else
                        Common.ShowSweetAlert(context, "Please enable internet connection!");


            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clear();
            }
        });



    }




    void finViewBYID()
    {

       text_name=(EditText)findViewById(R.id.input_name);
        text_email=(EditText)findViewById(R.id.input_email);
        text_mobileno=(EditText)findViewById(R.id.input_number);
        //	spinner_userType.setBackgroundResource(R.layout.spinner_textview);

        text_msg=(EditText)findViewById(R.id.input_msg);

        btn_registration=(Button)findViewById(R.id.btn_signup);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //	check_agree=(CheckBox)findViewById(R.id.check_agree);
    }




    boolean valid()
    {
        boolean valid=true;
        String MOB_NO="\\d{10}";

        name=text_name.getText().toString().trim();

        email=text_email.getText().toString().trim();
        mobileno=text_mobileno.getText().toString().trim();
        msg=text_msg.getText().toString().trim();

        //	pincode=text_zipcode.getText().toString().trim();
        if(name.length()==0)
        {

            Common.ShowSweetAlert(context, "Enter First Name!");

            valid=false;
        }

        else if(email.length()==0)
        {

            Common.ShowSweetAlert(context, "Enter E-Mail Address !");

            valid=false;
        }
        else if(!email.matches(Common.EMAIL_REGEX))
        {

            Common.ShowSweetAlert(context, "Enter Valid E-Mail Address!");

            valid=false;
        }
        else if(mobileno.length()==0)
        {

            Common.ShowSweetAlert(context, "Enter Mobile Number!");

            valid=false;
        }
        else if(!mobileno.matches(MOB_NO))
        {

            Common.ShowSweetAlert(context, "Mobile Number is 10 Digit !");


            valid=false;
        }


        return valid;
    }

    //reset or clear all the fields
    void clear()
    {
        text_name.setText("");
        text_email.setText("");
        text_mobileno.setText("");
        text_msg.setText("");

        //text_zipcode.setText("");
    }



    // volley string request
    void postRegistrationRequest()
    {
        reuestQueue= Volley.newRequestQueue(context);
         pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Creating Account");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"LoginServiceAPI.asmx/SaveDemoUser",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response!=null)
                    parseJSON(response);
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
                            parseJSON(new String(error.networkResponse.data));
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

                    params.put("name",name);
                    params.put("email",email);
                    params.put("contact",mobileno);
                    params.put("message",msg);
                    params.put("registrationType","App");
                    params.put("City", Common.user_regCity);


                    System.out.println("Login Req --------"+params);
                    //params.put("bdate",birthDate);
                }catch(Exception e)
                {

                }
                Log.e("params ",params+"");
                //	params.put("user", user+"");
                return params;
            }

        };
        stringRequest.setTag(TAG);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                20 * 1000,
                0,
                1
        );
        stringRequest.setRetryPolicy(retryPolicy);
        reuestQueue.add(stringRequest);
    }





    //parse the result
    void parseJSON(String result)
    {
        System.err.println(result);
        if(result.contains("error"))
        {
            Common.showToast("Check once again there is Error !", context);
        }
        else{
            try
            {
                JSONObject jo=new JSONObject(result);
                username=jo.getString("username");
                password=jo.getString("password");
                isExist=jo.getBoolean("user_exist");
                if(isExist)
                    Common.showToast("You are registered user. For latest password Click on Forgot password!", context);
                else
                    senEmailRequest();
            }
            catch(Exception e)
            {

            }
        }
    }
    //send email request
    public void senEmailRequest()
    {
        reuestQueue=Volley.newRequestQueue(context); //getting Request object from it
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setTitle("Progress wait.......");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,phpurl,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseEmail(response);
                pDialog.dismiss();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        try{
                            if(error.networkResponse != null && error.networkResponse.data != null){
                                VolleyError er = new VolleyError(new String(error.networkResponse.data));
                                error = er;
                                System.out.println(error.toString());
                                parseEmail(new String(error.networkResponse.data));
                            }
                        }catch(Exception e){
                            Log.e("Exception", ""+e);
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send_mail", "true");
                params.put("name",name);
                params.put("username", username);
                params.put("email",email);
                params.put("password",password);
                return params;
            }

        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }
    void parseEmail(String result)
    {
        Common.showToast("Check E-mail for Username and Password", context);
        Intent loginIntent = new Intent(context, LoginActivity.class);
        startActivity(loginIntent);
        finish();


    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the GHomeTab/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.back) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


}