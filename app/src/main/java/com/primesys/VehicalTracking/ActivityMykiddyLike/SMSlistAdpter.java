package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Activity.APIController;
import com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment.SMSFuctionFragment;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.SMSFuction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SMSlistAdpter extends RecyclerView.Adapter<SMSlistAdpter.ViewHolder>{

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
    final Calendar c = Calendar.getInstance();
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);
    private final int CALL_REQUEST = 100;

    public SMSlistAdpter(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata, int row_smslist, Context context) {
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
        holder.smstitle.setText(item.getTitle().trim());
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
                    Log.e(APIController.TAG+"----------",CurrentSMSObj.getCommnadType());

                    if (CurrentSMSObj.getCommnadType().contains("Sleep Down"))
                    {
                        ShowPasswordCreate_dialog();
                    }
                    else  if (CurrentSMSObj.getCommnadType().contains("Add Family numbers"))
                    {
                        ShowAddFamilyMember_dialog();
                    } else  if (CurrentSMSObj.getCommnadType().contains("Calls Reminder mode"))
                    {
                       ShowAddcallmode_dialog();
                    } else  if (CurrentSMSObj.getCommnadType().contains("Weekly Working period"))
                    {
                        ShowPeriod_dialog(0);
                    } else  if (CurrentSMSObj.getCommnadType().contains("Saturday Working period"))
                    {
                        ShowPeriod_dialog(1);
                    } else  if (CurrentSMSObj.getCommnadType().contains("Sunday Working Period"))
                    {
                        ShowPeriod_dialog(2);
                    } else  if (CurrentSMSObj.getCommnadType().contains("Call Volume Setting"))
                    {
                       Showcallvol_dialog();
                    }else  if (CurrentSMSObj.getCommnadType().contains("Make Call With Patrolman"))
                    {
                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Confirmation?")
                                .setContentText("Do you really want to do "+CurrentSMSObj.getTitle() +" ?.")
                                .setConfirmText("Confirm!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        callPhoneNumber();

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

                    else
                    ShowConfirmDialog(CurrentSMSObj);
                }

            });


        }


    }

    private void Showcallvol_dialog() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_call_volume, null);
        final Spinner call = (Spinner) subView.findViewById(R.id.call_volume_spinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Call Volume");
        builder.setMessage("");
        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();

        call.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public String Selected_call_mode;

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Selected_call_mode =pos+"";
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CurrentSMSObj.setActualCommand(CurrentSMSObj.getActualCommand()+call.getSelectedItem()+"#");
                Log.e("------------",CurrentSMSObj.getActualCommand());
                ShowConfirmDialog(CurrentSMSObj);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    private void ShowAddcallmode_dialog()
    {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_call_mode, null);
        final Spinner call = (Spinner) subView.findViewById(R.id.callmode_spinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Call Mode");
        builder.setMessage("");
        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();

        call.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public String Selected_call_mode;

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Selected_call_mode =pos+"";
                }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CurrentSMSObj.setActualCommand(CurrentSMSObj.getActualCommand()+call.getSelectedItemPosition()+"#");
                Log.e("------------",CurrentSMSObj.getActualCommand());
                ShowConfirmDialog(CurrentSMSObj);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    protected void ShowAddFamilyMember_dialog() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_addfamily_number, null);
        final EditText papano = (EditText)subView.findViewById(R.id.papano);
        final EditText monno = (EditText)subView.findViewById(R.id.monno);
        final EditText uncle_no = (EditText)subView.findViewById(R.id.uncle_no);
        final Button submit = (Button) subView.findViewById(R.id.alertsubmit);


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Set Family Number");
        builder.setMessage("");
        builder.setView(subView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1=papano.getText().toString();
                String num2=monno.getText().toString();
                String num3=uncle_no.getText().toString();
                if(num1.length()<=0&&num1.length()<10){
                    papano.setError("Please fill up Mobile No correctly.");
                }else  if(num2.length()<=0&&num2.length()<10){
                    monno.setError("Please fill up Mobile No correctly.");
                }else  if(num3.length()<=0&&num3.length()<10){
                    uncle_no.setError("Please fill up Mobile No correctly.");
                }else {
                    String actual_command="FN,A,PAPA,"+num1+",MUMMY,"+num2+",UNCLE,"+num3+"#";
                    CurrentSMSObj.setActualCommand(actual_command);
                    ShowConfirmDialog(CurrentSMSObj);
                    alertDialog.dismiss();
                }


            }
        });


    }

    private void ShowPeriod_dialog(final int weekday) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.dialog_period_setting, null);
        final EditText strat_time = (EditText)subView.findViewById(R.id.start_time);
        final EditText end_time = (EditText)subView.findViewById(R.id.end_time);
        final Button submit = (Button) subView.findViewById(R.id.alertsubmit);
        final int[] starthr = new int[1];
        final int[] endhr = {0};


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Set Weekly Working Period");
        builder.setMessage("Your device will be on during this selected time");
        builder.setView(subView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();

        strat_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                strat_time.setError(null);
                                mHour = hourOfDay;
                                mMinute = minute;
                                starthr[0] =hourOfDay;
                                strat_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog1.show();
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                end_time.setError(null);

                                mHour = hourOfDay;
                                mMinute = minute;
                                endhr[0] =hourOfDay;
                                end_time.setText(hourOfDay + ":" + minute);

                             //   end_time.setText(end_time.getText());
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1=strat_time.getText().toString();
                if (num1.length()<5)
                    num1="0"+num1;
                String num2=end_time.getText().toString();
                if (num2.length()<5)
                    num2="0"+num2;
                if(num1.length()<=0&&num1.length()<5){
                    strat_time.setError("Please fill up time correctly.");
                }else  if(num2.length()<=0&&num2.length()<5) {
                    end_time.setError("Please fill up time correctly.");
                }else if(starthr[0]>endhr[0]) {
                    end_time.setError("Please fill up end time correctly grater than start time.");
                }else  {
                    String actual_command="PERIOD,1,1,"+weekday+","+num1+"-"+num2+"#";
                    CurrentSMSObj.setActualCommand(actual_command);
                    Log.e("------------",CurrentSMSObj.getActualCommand());

                    ShowConfirmDialog(CurrentSMSObj);
                    alertDialog.dismiss();
                }


            }
        });


        alertDialog.show();

    }

    protected void ShowPasswordCreate_dialog() {
        // custom dialog

        final EditText txt_time;
        final Dialog createpindialog = new Dialog(mContext);
        createpindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createpindialog.setContentView(R.layout.dialog_submitsleep);
        final TextView tv_title=(TextView)createpindialog.findViewById(R.id.d_title);
        tv_title.setText("Enter Time to sleep down");

        txt_time = (EditText) createpindialog.findViewById(R.id.txt_time);


        Button Submitpin = (Button) createpindialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_time.getText().length()!=0){
                    int time=Integer.parseInt(txt_time.getText().toString());

                    if (time>=5&&time<=60){
                        CurrentSMSObj.setActualCommand(CurrentSMSObj.getActualCommand()+time+"#");
                        ShowConfirmDialog(CurrentSMSObj);
                        createpindialog.dismiss();

                    }else {
                        Common.ShowSweetAlert(mContext,"You can put time between 5 to 60 minutes.Please enter valid time.");
                    }


                }else{
                    txt_time.setError("Please enter valid four digit engine pin .");
                }

            }
        });

        createpindialog.show();
    }





    private void Sendsms( String msg) {


        try {
            DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
            if (!DeviceSimNo.equalsIgnoreCase("")) {
               // setProgressDialog();

                System.out.println("SEND SMS========idcard==============================" + DeviceSimNo +
                        "  -- " );
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + DeviceSimNo.trim()));
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
        SMSlistAdpter.pDialogmain.dismiss();
        Common.ShowSweetAlert(mContext,CurrentSMSObj.getTitle()+"  is failed,"+mContext.getResources().getString(errormsg));
    }


    private void setProgressDialog() {

        pDialogmain = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialogmain.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogmain.setTitleText("Wait For Response");

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



    public void ShowConfirmDialog(final VehicalTrackingSMSCmdDTO currentSMSObj) {
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirmation?")
                .setContentText("Do you really want to do "+currentSMSObj.getTitle() +" ?.")
                .setConfirmText("Confirm!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Sendsms(currentSMSObj.getActualCommand());


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
        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
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
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentId", SMSFuctionFragment.StudentId);
                params.put("ParentId",Common.userid);

                System.out.println("REq---GetForgetPassword------"+params);


                return params;
            }


        };


        stringRequest.setTag("VTS");
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


        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
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
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
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
            EditVsNumberDialog.dismiss();
        }
    }



    public void callPhoneNumber()
    {
        DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
        if (!DeviceSimNo.equalsIgnoreCase(""))
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + DeviceSimNo));
                    mContext.startActivity(callIntent);
                }
                else {
                    ActivityCompat.requestPermissions((Activity) SMSFuction.context, new String[] {
                                    Manifest.permission.CALL_PHONE,
                            },
                            CALL_REQUEST);
                }
            }else {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + DeviceSimNo));
                mContext.startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }else{

        }
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.call_permission_denied_message), Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
