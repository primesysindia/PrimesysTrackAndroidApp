package com.primesys.VehicalTracking.MyAdpter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Activity.ChanageEnginepassword;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.SMSFuction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VhehicalSMSlistAdpter extends RecyclerView.Adapter<VhehicalSMSlistAdpter.ViewHolder>{

    private SharedPreferences.Editor editor;
    public List<VehicalTrackingSMSCmdDTO> smslist=new ArrayList<VehicalTrackingSMSCmdDTO>();
    private int rowLayout;
    private static Context mContext;
    public   int position=0;

    public SharedPreferences sharedPreferences;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    String DeviceSimNo="";
    private String TAG="ADPetr";
    public static VehicalTrackingSMSCmdDTO CurrentSMSObj=null;
    private String speed="";
    private int TAG_CODE_PERMISSION_SMS=400;


    private String key_DeviceSimNo="DeviceSimNo";
    public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
    public static Boolean pinexist=false;
    public static String Enginepin="";
    public Dialog createpindialog;
    public static SweetAlertDialog pDialogmain;
    public static  CountDownTimer countdowntimer;


    public VhehicalSMSlistAdpter(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata, int row_smslist, Context context) {
        super();
        this.smslist = smslistdata;
        this.rowLayout = row_smslist;
        this.mContext = context;
        sharedPreferences=mContext.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(rowLayout, viewGroup, false);

        System.out.println("  1  "+new ViewHolder(v)+"   2  "+viewGroup+"   3  "+rowLayout);


        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VehicalTrackingSMSCmdDTO item = smslist.get(position);
        //  holder.itemDesc.setText(item.get());
        holder.smstitle.setText(item.getTitle());
        Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
        holder.smstitle.setTypeface(typeFace);

        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return smslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView smstitle;
        
        public ViewHolder(View itemView)  {
            super(itemView);
            smstitle = (TextView)itemView.findViewById(R.id.titlesms);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CurrentSMSObj = smslist.get(getAdapterPosition());
                    if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Over Speed"))
                    Show_speedalert_dilog(CurrentSMSObj);
                    else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("stop electric"))
                    {
                        if (pinexist)
                            ShowPassword_dialog("Are you sure to stop vehicle?");
                        else
                            ShowPasswordCreate_dialog();
                    }

                  else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Supply electric"))
                    {
                        if (pinexist)
                            ShowPassword_dialog("Are you sure to start vehicle?");
                        else
                            ShowPasswordCreate_dialog();

                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Change Pin"))
                    {
                        Intent intent=new Intent(mContext,ChanageEnginepassword.class);
                        intent.putExtra("OldPin", Enginepin);
                        intent.putExtra("StudentId",SMSFuction.StudentId);
                        mContext.startActivity(intent);
                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Forget Pin"))
                    {
                        GetForgetPassword();

                    }
                    else
                    ShowConfirmDialog(CurrentSMSObj);
                }

            });


        }


    private void ShowConfirmDialog(final VehicalTrackingSMSCmdDTO currentSMSObj) {
         new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirmation?")
                .setContentText("Do you really want to do "+currentSMSObj.getTitle() +" ?.")
                .setConfirmText("Confirm!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.SEND_SMS) ==
                                    PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_SMS) ==
                                            PackageManager.PERMISSION_GRANTED) {


                            }
                            else {
                                ActivityCompat.requestPermissions((Activity) SMSFuction.context, new String[] {
                                                Manifest.permission.SEND_SMS,
                                                Manifest.permission.READ_SMS },
                                        TAG_CODE_PERMISSION_SMS);
                            }

                            /* if (!checkPermission())
                                Common.ShowSweetAlert(mContext,"You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                            else Sendsms(currentSMSObj.getActualCommand());*/

                        }else  Sendsms(currentSMSObj.getActualCommand());


                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }

    private void Show_speedalert_dilog(final VehicalTrackingSMSCmdDTO currentSMSObj) {

        final EditText speedlimit;
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.speed_limt);
        dialog.setTitle("Set Speed Limit");

        speedlimit=(EditText)dialog.findViewById(R.id.speedlimit);
        VehicalTrackingSMSCmdDTO Currentobj=new VehicalTrackingSMSCmdDTO();

        Button Submitdetil = (Button) dialog.findViewById(R.id.alertsubmit);
        Button cancel = (Button) dialog.findViewById(R.id.alertcancel);

        Submitdetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 speed =String.format("%03d", Integer.parseInt(speedlimit.getText().toString() ));
                DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermission())
                        Common.ShowSweetAlert(mContext,"You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                    else
                        Sendsms(CurrentSMSObj.getActualCommand()+speed+"#");

                }else{

                    Sendsms(CurrentSMSObj.getActualCommand()+speed+"#");

                }
                    dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void Sendsms( String msg) {

        setProgressDialog();

        try {
            DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");

            System.out.println("SEND SMS======================================"+DeviceSimNo+"  -- "+msg);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(DeviceSimNo, null,msg, null, null);
            countdowntimer= new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    VhehicalSMSlistAdpter.pDialogmain.dismiss();

                    if (CurrentSMSObj.getCommnadType().equals("stop electric"))
                    setErrormsg(R.string.stop_engine_ensure);
                    else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Supply electric"))
                    setErrormsg(R.string.start_engine_ensure);
                    else setErrormsg(R.string.common_error_sms);


                }

            }.start();
         //   Common.ShowSweetSucess(mContext,"Send Sms Sucessfully");
        }catch (Exception ex) {
            ex.printStackTrace();
        }



    }


        protected void setErrormsg(int errormsg) {
            // TODO Auto-generated method stub
            VhehicalSMSlistAdpter.pDialogmain.dismiss();
            Common.ShowSweetAlert(mContext,CurrentSMSObj.getTitle()+"  is failed,"+mContext.getResources().getString(errormsg));
        }


        public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }




    protected void ShowPasswordCreate_dialog() {
        // custom dialog

        final EditText txt_pin;
        createpindialog = new Dialog(mContext);
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





    protected void ShowPassword_dialog(final String msg) {
        // custom dialog

        final EditText txt_pin;
        final Dialog dialog = new Dialog( mContext);
        dialog.setContentView(R.layout.dialog_submitpassword);
        dialog.setTitle("Verify Engine Pin");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_pin);


        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);


        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_pin.getText().length()!=0){

                    if (txt_pin.getText().toString().equals(Enginepin)){

                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(msg.toString())
                                .setCancelText("No,cancel !")
                                .setConfirmText("Yes!")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Sendsms(CurrentSMSObj.getActualCommand());


                                        sDialog.cancel();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();


                        dialog.dismiss();
                    }
                    else
                        Common.ShowSweetAlert(mContext,mContext.getResources().getString(R.string.passwordnotmatch));

                }else{
                    txt_pin.setError("Please enter valid four digit engine pin .");
                }

            }
        });

        dialog.show();
    }


    protected void PostEnginePin(final String Pin) {
        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
        final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(mContext, "Progress wait.......");
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"UserServiceAPI/PostEnginePin",new Response.Listener<String>() {


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

                params.put("StudentID",SMSFuction.StudentId);
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

                ShowPassword_dialog("Are you sure to stop vehicle?");
                Common.ShowSweetSucess(mContext,jo.getString("message"));

            }else
            Common.ShowSweetAlert(mContext,jo.getString("message"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            createpindialog.dismiss();
        }
    }
}

    private void setProgressDialog() {

        pDialogmain = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialogmain.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogmain.setTitleText("Wait For Responce");

        if (CurrentSMSObj.getCommnadType().equals("stop electric"))
            pDialogmain.setContentText((mContext.getResources().getString(R.string.stop_engine_ensure)));
        else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Supply electric"))
            pDialogmain.setContentText((mContext.getResources().getString(R.string.start_engine_ensure)));
        else
        pDialogmain.setContentText((mContext.getResources().getString(R.string.common_error_sms)));
        pDialogmain.setCancelable(false);
        pDialogmain.show();
    }

    public static void setSucessDialog(Context context) {
        SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Sucess");
        pDialog.setContentText(CurrentSMSObj.getTitle()+" is execute sucessfully.");
        pDialog.setCancelable(true);
        pDialog.show();
    }

/*
    private void GetForgetPassword() {


        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
        final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(mContext, "Getting Engine pin please wait.......");

            //JSon object request for reading the json data
            stringRequest = new StringRequest(Request.Method.POST,Common.URL+"ParentAPI.asmx/SendEnginePin",new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    pDialog1.dismiss();

                    System.out.println("Responce of  GetForgetPassword----"+response);
                    Common.ShowSweetSucess(mContext," Engine pin send sucessfully on your register Mail-Id. ");

                    if (response.contains("false"))
                        Common.ShowSweetSucess(mContext," Engine pin send sucessfully on your register Mail-Id. ");
                    else
                        Common.ShowSweetAlert(mContext,"Error on sending mail.Please contact with contact@mykiddytracker.com ");


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
                            }
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("StudentId",SMSFuction.StudentId);
                    params.put("ParentId",Common.userid);

                    System.out.println("REq---GetForgetPassword------"+params);
                    return params;
                }

            };


            stringRequest.setTag("VTS");
            // Adding request to request queue
            reuestQueue.add(stringRequest);



    }




*/

    protected void GetForgetPassword() {
        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
      //  final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(mContext, "Getting Engine pin please wait.......");
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sending Engin Pin");
        pDialog.setContentText("Engine pin sending on your register Mail-Id..");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.URL+"ParentAPI.asmx/SendEnginePin",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  GetForgetPassword----"+response);
                pDialog.dismiss();

                Common.ShowSweetSucess(mContext," Engine pin send sucessfully on your register Mail-Id. ");

                if (response.contains("false"))
                    Common.ShowSweetSucess(mContext," Engine pin send sucessfully on your register Mail-Id. ");
                else
                    Common.ShowSweetAlert(mContext,"Error on sending mail.Please contact with contact@mykiddytracker.com ");

            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            er.printStackTrace();
                            System.out.println("-------------"+error.toString());
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentId",SMSFuction.StudentId);
                params.put("ParentId",Common.userid);

                System.out.println("REq---GetForgetPassword------"+params);


                return params;
            }


        };


        stringRequest.setTag("VTS");
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }


}
