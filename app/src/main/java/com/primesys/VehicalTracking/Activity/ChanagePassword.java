package com.primesys.VehicalTracking.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChanagePassword extends Activity {

    EditText textCurrent,textNew,textConfirm;
    ImageView backimage;
    Context contextpasss=ChanagePassword.this;
    TextView textheader;
    String curretPassword,newPassword,confirmPasssword;
    ProgressDialog dialog;
    Button btnSave,btnCancel;
    String strCurrent,strNew,strConfirm;
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    String TAG="ChangePassword";
    Context changeContext=ChanagePassword.this;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";
    private final String key_IS = "IS_FIRST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanage_password);
        findViewByID();


        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    if(Validation()){
                        textConfirm.setError(null);
                        textCurrent.setError(null);
                        textNew.setError(null);
                        if(Common.connectionStatus)
                        {
                            postForgetPasswordREQ();
                            setClear();
                        }
                    }
                }catch(Exception e)
                {
                    Log.e("Exception", ""+e);
                }

            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
             finish();
            }
        });

    }
    //VALIDATION ON SEVICE
    boolean Validation() {
        Boolean valid=true;
        strConfirm=textConfirm.getText().toString().trim();
        strCurrent=textCurrent.getText().toString().trim();
        strNew=textNew.getText().toString().trim();
        String password = sharedPreferences.getString(key_PASS,"");

        if (strCurrent.length()==0) {
            textCurrent.setError("Enter Current Password");
            textCurrent.requestFocus();
            valid=false;
        }else if (strNew.length()==0) {
            textNew.setError("Enter New Passoword");
            textNew.requestFocus();
            valid=false;
        }/*else if (strNew.length()<6) {
            textNew.setError("Passoword shouled Min 6 digit");
            textNew.requestFocus();
            valid=false;
        }*/else if (strConfirm.length()==0) {
            textConfirm.setError("Enter Confirm Password");
            textConfirm.requestFocus();
            valid=false;
        }
        else if (!strCurrent.equalsIgnoreCase(password)) {
            textCurrent.setError("Your Current Password Is Wrong");
            textCurrent.requestFocus();
            valid=false;
        }
        else if (!strNew.equalsIgnoreCase(strConfirm)) {
            textConfirm.setError("Password Not Matched");
            textConfirm.requestFocus();
            valid=false;
        }
        return valid;
    }
    //findView by id
    void findViewByID()
    {
        textCurrent=(EditText)findViewById(R.id.text_current);
        textNew=(EditText)findViewById(R.id.text_new);
        textConfirm=(EditText)findViewById(R.id.text_confirm);
        btnSave=(Button)findViewById(R.id.btn_submit);
        btnCancel=(Button)findViewById(R.id.btn_Cancel);

        sharedPreferences=changeContext.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        return super.onCreateOptionsMenu(menu);

    }
    // onclick menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    void postForgetPasswordREQ()
    {
        reuestQueue=Volley.newRequestQueue(changeContext); //getting Request object from it
        final SweetAlertDialog pDialog = Common.ShowSweetProgress(changeContext, "Loding wait");
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Method.POST,Common.URL+"LoginServiceAPI.asmx/ChangePassword",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseJSON(response);
                pDialog.dismiss();
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
                            parseJSON(new String(error.networkResponse.data));
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", Common.userid);
                params.put("CurrentPassword", strCurrent);
                params.put("NewPassword", strNew);
                return params;
            }

        };
        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }
    //Parsing Response Coming From  server
    void parseJSON(String response) {
        try
        {

            JSONObject jo=new JSONObject(response);
            if (jo.getString("msg").contains("Password Updated Successfully!")) {

                editor = sharedPreferences.edit();
                editor.remove(key_IS);
                editor.commit();
                Common.showToast(jo.getString("msg"), changeContext);

                Intent i=new Intent(changeContext,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                finish();
            }else{

                Common.showToast(jo.getString("msg"), changeContext);
            }



        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    //clear text fields
    public void setClear() {
        textNew.setText("");
        textNew.setError(null);
        textCurrent.setText("");
        textCurrent.setError(null);
        textConfirm.setText("");
        textConfirm.setError(null);
        textCurrent.requestFocus();
    }

}
