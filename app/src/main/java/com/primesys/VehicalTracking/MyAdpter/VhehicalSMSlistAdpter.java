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
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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
    private String updated_simno="";

    private String key_DeviceSimNo="DeviceSimNo";
    public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
    public static Boolean pinexist=false;
    public static String Enginepin="";
    public Dialog createpindialog;
    public static SweetAlertDialog pDialogmain;
    public static  CountDownTimer countdowntimer;
    private Dialog EditVsNumberDialog;


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
                        intent.putExtra("StudentId",SMSFuction.CuurentDeviceDelect.getId());
                        mContext.startActivity(intent);
                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Forget Pin"))
                    {
                        GetForgetPassword();

                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Set Geofence"))
                    {
                        Show_geofence_dilog(CurrentSMSObj);

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

                            else*/ Sendsms(currentSMSObj.getActualCommand());

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

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.speed_limt);
        final TextView tv_title=(TextView)dialog.findViewById(R.id.d_title);
        tv_title.setText("Set Speed Limit");

         final EditText speedlimit = (EditText) dialog.findViewById(R.id.speedlimit);
        VehicalTrackingSMSCmdDTO Currentobj=new VehicalTrackingSMSCmdDTO();

        Button Submitdetil = (Button) dialog.findViewById(R.id.alertsubmit);
        Button cancel = (Button) dialog.findViewById(R.id.alertcancel);

        Submitdetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (speedlimit.getText().length()>0) {

                    speed = String.format("%03d", Integer.parseInt(speedlimit.getText().toString()));
                    DeviceSimNo = sharedPreferences.getString(key_DeviceSimNo, "");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        /*if (!checkPermission())
                            Common.ShowSweetAlert(mContext, "You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                        else*/
                            Sendsms(CurrentSMSObj.getActualCommand() + speed + "#");

                    } else {

                        Sendsms(CurrentSMSObj.getActualCommand() + speed + "#");

                    }
                    dialog.dismiss();
                }else {
                    Common.ShowSweetAlert(mContext,"Please enter speed limit.");
                }
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

     private void Show_geofence_dilog(final VehicalTrackingSMSCmdDTO currentSMSObj) {


            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.geofence_limit);
            final TextView tv_title=(TextView)dialog.findViewById(R.id.d_title);
            tv_title.setText("Set Geo-fence Limit");

            VehicalTrackingSMSCmdDTO Currentobj=new VehicalTrackingSMSCmdDTO();

            final Spinner fence_limit = (Spinner) dialog.findViewById(R.id.limit_spinner);
            Button Submitdetil = (Button) dialog.findViewById(R.id.alertsubmit);
            Button cancel = (Button) dialog.findViewById(R.id.alertcancel);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                    R.array.geofence_limit_array, R.layout.custum_spinner_text);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fence_limit.setAdapter(adapter);

            Submitdetil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fence_limit.getSelectedItemPosition()>0) {

                     int   limit = Integer.parseInt(fence_limit.getSelectedItem().toString())/100;
                        DeviceSimNo = sharedPreferences.getString(key_DeviceSimNo, "");
                        Log.e("Limit--------------","------------------------"+limit);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
/*                            if (!checkPermission())
                                Common.ShowSweetAlert(mContext, "You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                            else*/
                                Sendsms(CurrentSMSObj.getActualCommand() + limit + "#");

                        } else {

                            Sendsms(CurrentSMSObj.getActualCommand() + limit + "#");

                        }
                        dialog.dismiss();

                    }else Common.ShowSweetAlert(mContext,"Please select geo-fence limit.");
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
/*
    private void Sendsms( String msg) {
        try {
            DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
            if (!DeviceSimNo.equalsIgnoreCase("")) {
                setProgressDialog();

                System.out.println("SEND SMS======================================" + DeviceSimNo + "  -- " + msg);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(DeviceSimNo, null, msg, null, null);
                countdowntimer = new CountDownTimer(60000, 1000) {

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

            }else {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Error");
                pDialog.setContentText( "Device sim no not found.Please enter device sim no");
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ShowEditVsNumberDialog("");
                        pDialog.dismiss();
                    }

                });
                pDialog.show();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }



    }*/


        private void Sendsms( String msg) {


            try {
                DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
                if (!DeviceSimNo.equalsIgnoreCase("")) {
                    // setProgressDialog();

                    System.out.println("SEND SMS======================================" + DeviceSimNo +
                            "  -- " + msg+"  respo --"+CurrentSMSObj.getAnsFromDevice());
                    try {
                        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + DeviceSimNo));
                        smsIntent.putExtra("sms_body", msg.trim());
                        mContext.startActivity(smsIntent);

                        // setSucessDialog(mContext);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                /*countdowntimer = new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        SMSlistAdpter.pDialogmain.dismiss();

                        if (CurrentSMSObj.getCommnadType().equals("stop electric"))
                            setErrormsg(R.string.stop_engine_ensure);
                        else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Supply electric"))
                            setErrormsg(R.string.start_engine_ensure);
                        else setErrormsg(R.string.common_error_sms);


                    }

                }.start();*/
                    //   Common.ShowSweetSucess(mContext,"Send Sms Sucessfully");

                }else {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Error");
                    pDialog.setContentText( "Device sim no not found.Please enter device sim no");
                    pDialog.setCancelable(true);
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ShowEditVsNumberDialog("");
                            pDialog.dismiss();
                        }

                    });
                    pDialog.show();
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }



        }



        protected void setErrormsg(int errormsg) {
            // TODO Auto-generated method stub
            VhehicalSMSlistAdpter.pDialogmain.dismiss();
            Common.ShowSweetAlert(mContext,CurrentSMSObj.getTitle()+"  is failed,"+mContext.getResources().getString(errormsg));
        }


       /* public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }*/




    protected void ShowPasswordCreate_dialog() {
        // custom dialog

        final EditText txt_pin;

        final Dialog createpindialog = new Dialog(mContext);
        createpindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createpindialog.setContentView(R.layout.dialog_submitpassword);
        createpindialog.setCancelable(true);
        final TextView tv_title=(TextView)createpindialog.findViewById(R.id.d_title);
        tv_title.setText("Create Engine Pin");
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
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_submitpassword);
        final TextView tv_title=(TextView)dialog.findViewById(R.id.d_title);
        tv_title.setText("Verify Engine Pin");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_pin);

        final Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);


        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(txt_pin.getText().length()!=0)
                {

                    if (txt_pin.getText().toString().equals(Enginepin))
                    {

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

                params.put("StudentID",SMSFuction.CuurentDeviceDelect.getId());
                params.put("Pin",Pin);

                System.out.println("REq---PostEnginePin------"+params);
                return params;
            }

        };

        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    protected void parseupdateJSON(String response, String pin) {

        try {
            JSONObject jo=new JSONObject(response);

            if (jo.getString("error").equals("false"))
            {
                Enginepin=pin;
                pinexist=true;

                ShowPassword_dialog("Are you sure to stop vehicle?");
                Common.ShowSweetSucess(mContext,jo.getString("message"));

            }else Common.ShowSweetAlert(mContext,jo.getString("message"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if (createpindialog!=null)
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
        pDialog.setTitleText("Success");
        pDialog.setContentText(CurrentSMSObj.getTitle()+" is execute successfully.");
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

                    params.put("StudentId",GSMSFuction.StudentId);
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
        pDialog.setTitleText("Sending Engine Pin");
        pDialog.setContentText("Engine pin sending on your register Mail-Id..");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.URL+"ParentAPI.asmx/SendEnginePin",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  GetForgetPassword----"+response);
                pDialog.dismiss();

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

                params.put("StudentId",SMSFuction.CuurentDeviceDelect.getId());
                params.put("ParentId",Common.userid);

                System.out.println("REq---GetForgetPassword------"+params);


                return params;
            }


        };


        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }



    public void ShowEditVsNumberDialog(String deviceSimNo) {

        // custom dialog

        final EditText simno;
        final Button submit;

         EditVsNumberDialog = new Dialog(mContext);
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
                    Common.ShowSweetAlert(mContext,"Please enter valid device sim number.");

            }
        });

        EditVsNumberDialog.show();

    }

    protected void PostDeviceSimno(final String simno) {


        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
        final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(mContext, "Progress wait.......");

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
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            parseSimupdateJSON(new String(error.networkResponse.data));
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID", SMSFuction.CuurentDeviceDelect.getId());
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
                sharedPreferences=mContext.getSharedPreferences("User_data",Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();

                editor.putString(key_DeviceSimNo, DeviceSimNo);
                editor.commit();
              //  tv_msg.setText("Please confirm "+DeviceSimNo+" is device SIM Number ? ");
                Common.ShowSweetSucess(mContext,jo.getString("message"));

            }else
            Common.ShowSweetAlert(mContext,jo.getString("message"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if (EditVsNumberDialog!=null)
                EditVsNumberDialog.dismiss();
        }
    }

}
