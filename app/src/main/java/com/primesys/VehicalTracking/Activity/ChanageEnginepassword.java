package com.primesys.VehicalTracking.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChanageEnginepassword extends AppCompatActivity {

    EditText textCurrent,textNew,textConfirm;
    ImageView backimage;
    TextView textheader;
    String curretPassword,newPassword,confirmPasssword;
    ProgressDialog dialog;
    Button btnSave,btnCancel;
    String strCurrent,strNew,strConfirm,oldPin,studentId;
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    String TAG="ChangePassword";
    Context changeContext=ChanageEnginepassword.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanage_enginepassword);
        findViewByID();
        oldPin=getIntent().getStringExtra("OldPin");
        studentId=getIntent().getStringExtra("StudentId");
        System.err.println("OLD PIn============="+oldPin);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    if(Validation()){
                        textConfirm.setError(null);
                        textCurrent.setError(null);
                        if(Common.connectionStatus)
                        {
                            PostEnginePin(strConfirm);
                            setClear();
                        }
                    }
                }catch(Exception e)
                {
                    Log.e("Exception", ""+e);
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  setClear();
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
        if (strCurrent.length()==0) {
            textCurrent.setError("Enter current Engine Pin");
            textCurrent.requestFocus();
            valid=false;
        }else if (!strCurrent.equals(oldPin)) {
            textCurrent.setError("Current Engine Pin is wrong");
            textCurrent.requestFocus();
            valid=false;
        }
        else if (strNew.length()==0) {
            textNew.setError("Enter new passoword");
            textNew.requestFocus();
            valid=false;
        }else if (strNew.length()<4) {
            textNew.setError("Passoword shouled 4 digit");
            textNew.requestFocus();
            valid=false;
        }else if (strConfirm.length()==0) {
            textConfirm.setError("Enter confirm Engine Pin");
            textConfirm.requestFocus();
            valid=false;
        }else if (!strNew.equalsIgnoreCase(strConfirm)) {
            textConfirm.setError("Engine Pin not matched");
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

    protected void PostEnginePin(final String Pin) {
        reuestQueue= Volley.newRequestQueue(changeContext); //getting Request object from it
        final ProgressDialog pDialog1 = new ProgressDialog(changeContext);
        pDialog1.setTitle("Progress wait.......");
        pDialog1.setCancelable(false);
        pDialog1.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Method.POST, Common.TrackURL+"UserServiceAPI/PostEnginePin",new Response.Listener<String>() {


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
                        pDialog1.hide();
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

                params.put("StudentID",studentId);
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
                SweetAlertDialog pDialog = new SweetAlertDialog(changeContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Sucess");
                pDialog.setContentText(jo.getString("message"));
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                });
                pDialog.show();

            }else {
                Common.ShowSweetAlert(changeContext,jo.getString("message"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
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
