package com.primesys.VehicalTracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Utility.Common;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Forget_password extends AppCompatActivity {

    EditText Email;
    Button submit,clear;
    Toolbar toolbar;
    private String TAG="Rquest";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    Context context=Forget_password.this;
    public SharedPreferences sharedPreferences;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public  String key_Roll_id="Roll_id";

    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        findviewid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sendreqtoforgetpassword();
            }
        });

    }

/*
    private void Sendreqtoforgetpassword() {
        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"UserServiceAPI/SendForgrtPassword",new Response.Listener<String>()

        {

            @Override
            public void onResponse(String response) {
                System.out.println( response);

                ParseSave(response);

                pDialog.hide();

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            ParseSave(new String(error.networkResponse.data));
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email",Email.getText().toString());

                System.out.println("REq---ForgetPass WOrd us  -------"+params);
                return params;
            }

        };

        stringRequest.setTag(TAG);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }
*/


    private void Sendreqtoforgetpassword()
    {


        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"UserServiceAPI/SendForgrtPassword",new Response.Listener<String>()
        {
        @Override
            public void onResponse(String response) {
            System.out.println("----------------------"+response);

            ParseSave(response);

            pDialog.hide();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //    pDialog.hide();

                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            ParseSave(new String(error.networkResponse.data));
                        }
                    }
                })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email",Email.getText().toString());

                System.out.println("REq---ForgetPass WOrd us  -------" + params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);




    }

    private void ParseSave(String response) {

        try {

            JSONObject jo=new JSONObject(response);
            if( jo.getString("error").equals("false")){
             //   finish();
                //Initialize data
                sharedPreferences = context.getSharedPreferences("User_data", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();

                editor.putString(key_id, "");
                editor.putString(key_PASS, "");
                editor.putString(key_Roll_id, "");
                editor.remove(key_IS);

                editor.commit();
                //Common.ShowSweetSucess(context, jo.getString("message"));
                 SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText( jo.getString("message"));
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        finish();
                    }
                });
                pDialog.setCancelable(true);
                pDialog.show();
            }else{
                Common.ShowSweetAlert(context, jo.getString("message"));

            }
/*
            if(response.contains("Password sended to your email id.")){
                Common.ShowSweetAlert(context, "Password sended to your email id. Please check your mail");
                finish();
            }else{
                Common.ShowSweetAlert(context,"Email Id not register.Please enter valid email Id.");

            }
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findviewid() {
        Email=(EditText)findViewById(R.id.input_email);
        submit= (Button) findViewById(R.id.btn_submit);
        clear= (Button) findViewById(R.id.btn_clear);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
