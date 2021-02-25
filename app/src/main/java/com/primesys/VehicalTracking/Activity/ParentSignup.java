package com.primesys.VehicalTracking.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ParentSignup extends AppCompatActivity {
    EditText text_name,text_email,text_mobileno;
    EditText text_address;
    Button btn_registration,btn_clear;
    private CheckBox check_agree;
    Context context = ParentSignup.this;
    String name,email,mobileno,username,password,user_type,msg,Group;
    public static final String TAG="Registration";
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    private final String key_id = "User_Id";
    private Toolbar toolbar;
    private boolean isExist=false;
    private SweetAlertDialog pDialog;
    private String phpurl="http://www.mykiddytracker.com/send_mail";
    private RadioGroup radioSexGroup;
    String txt_gender="M";
    Boolean isNewEmail=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup);
        finViewBYID();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        text_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    if (text_email.getText().length() > 0 || email.matches(Common.EMAIL_REGEX)) {
                        CheckValiduser();
                    } else Common.ShowSweetAlert(context, "Enter Valid E-Mail Address!");
                }
            }
        });


        btn_registration.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int selected=radioSexGroup.getCheckedRadioButtonId();
                RadioButton gender=(RadioButton) findViewById(selected);
                txt_gender=  gender.getText().toString().substring(0,1);

                if (valid())
                    if (Common.getConnectivityStatus(context))
                        if (isNewEmail)
                        postRegistrationRequest();
                        else
                            Common.showToast("You are already registered user.For latest password Click on Forgot password!", context);

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

        text_address=(EditText)findViewById(R.id.input_address);

        btn_registration=(Button)findViewById(R.id.btn_signup);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        //	check_agree=(CheckBox)findViewById(R.id.check_agree);
    }



    boolean valid()
    {
        boolean valid=true;
        String MOB_NO="\\d{10}";

        name=text_name.getText().toString().trim();
        email=text_email.getText().toString().trim();
        mobileno=text_mobileno.getText().toString().trim();
        msg=text_address.getText().toString().trim();

        //	pincode=text_zipcode.getText().toString().trim();
        if(name.length()==0)
        {
            Common.ShowSweetAlert(context, "Enter name!");
            valid=false;
        } else if(name.length()<4)
        {
            Common.ShowSweetAlert(context, "Enter name at least 4 character !");
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
        text_address.setText("");

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
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"ParentAPI/SaveTrackUserNew",new Response.Listener<String>() {

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
                    params.put("mob_no",mobileno);
                    params.put("address",msg);
                    params.put("gender",txt_gender);


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
        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);

        reuestQueue.add(stringRequest);
    }




    //parse the result
    void parseJSON(String result)
    {
        System.err.println(result);


      //  {"error":"false","msg":"User register succesfully.You can use it.","usename":"jxjxkk@hdjdj.com","password":"jmd4493"}

            try {
                JSONObject jo = new JSONObject(result);
                if (jo.getString("error").equalsIgnoreCase("false")) {
                    username = jo.getString("username");
                    password = jo.getString("password");
                    Common.ShowSweetSucess(context,"");
                    senEmailRequest();

                    SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("User register successfully.For credential check your Mail-Id.");
                    pDialog.setCancelable(true);
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    });
                    pDialog.show();

                } else {
                    Common.showToast("You are registered user. For latest password Click on Forgot password!", context);

                }
            }
            catch(Exception e)
            {

                e.printStackTrace();
            }

    }




    private void CheckValiduser() {
        reuestQueue= Volley.newRequestQueue(context);
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Validating  email");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"ParentAPI/CheckMailExist",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response!=null)
                    parseEmailJSON(response);
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
                    params.put("email",text_email.getText().toString());
                    Log.e("CheckValiduser--","CheckValiduser Req --------"+params);
                }catch(Exception e)
                {
                    e.printStackTrace();
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
    void parseEmailJSON(String result)
    {
        Log.e("parseEmailJSON--","parseEmailJSON REspo --------"+result);

        try
        {
            JSONObject jo=new JSONObject(result);

            if(jo.getString("error").equalsIgnoreCase("true")){
                Common.showToast("You are already registered user. For latest password Click on Forgot password!", context);
                text_email.setError("You are already  registered user. For latest password Click on Forgot password!");
                isNewEmail=false;

            }else {
                text_email.setError(null);
                isNewEmail=true;
            }
        }
        catch(Exception e)
        {

            e.printStackTrace();
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
        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);

        reuestQueue.add(stringRequest);
    }
    void parseEmail(String result)
    {
        Common.showToast("Check E-mail for Username and Password", context);
        Intent loginIntent = new Intent(context, LoginActivity.class);
        startActivity(loginIntent);
        finish();


    }
}
