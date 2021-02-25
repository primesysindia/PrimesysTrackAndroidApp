
package com.primesys.VehicalTracking.Guest.Adpter;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Activity.AccReportlist;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.Guest.Fragments.GACCReport;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.ACCReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GVhehicalReportlistAdpter extends RecyclerView.Adapter<GVhehicalReportlistAdpter.ViewHolder>{

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
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;



    private String key_DeviceSimNo="DeviceSimNo";
    public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
    public static Boolean pinexist=false;
    public static String Enginepin="";
    public Dialog createpindialog;
    public static SweetAlertDialog pDialogmain;
    public static CountDownTimer countdowntimer;
    public  String currentdate="";


    public GVhehicalReportlistAdpter(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata, int row_smslist, Context context) {
        super();

        Log.e("GVhehicalReportlistAdpter","Inside --------------------GVhehicalReportlistAdpter");
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
                    if (CurrentSMSObj.getActualCommand().equalsIgnoreCase("ACC Report")) {


                  /*     IncomingSms in=new IncomingSms();
                        in.ParseACCONMsg(mContext,"ACC  !!!IMEI:355488020931188     N20.900905,E77.810837");
*/
                        Intent intent = new Intent(mContext, AccReportlist.class);
                        mContext.startActivity(intent);

                    }
                    else if (CurrentSMSObj.getActualCommand().equalsIgnoreCase("Total KM")) {

                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);

                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                        // Launch Date Picker Dialog
                        DatePickerDialog dpd = new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // Display Selected date in textbox

                                        long dv = Long.valueOf(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"))*1000;
                                        Date df = new Date(dv);
                                        currentdate  = new SimpleDateFormat("dd-MMM-yy").format(df);

                                        GetVehTotalKm();


                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

                        dpd.show();

                    }else if (CurrentSMSObj.getActualCommand().equalsIgnoreCase("Monthly Total KM")) {

                        Common.ShowSweetAlert(mContext, mContext.getResources().getString(R.string.str_guest_error_msg));


                    }else if (CurrentSMSObj.getActualCommand().equalsIgnoreCase("Trip Report")) {

                        Common.ShowSweetAlert(mContext, mContext.getResources().getString(R.string.str_guest_error_msg));

                    }
                    }


            });


        }


    }

    private void ShowTripMaildialog(final long gmtTimeStampFromDate, final long gmtTimeStampFromDate1) {

            // custom dialog

            final EditText txt_pin;
            final Dialog dialog = new Dialog( mContext);
            dialog.setContentView(R.layout.dialog_mailid);
            dialog.setTitle("Enter Mail-Id ");
            txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);


            Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);

            // if button is clicked, close the custom dialog
            Submitpin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String MailId=txt_pin.getText().toString();

                    System.out.println("************************************"+MailId);
                    if(Validemail(MailId))
                    {
                        GetTripReport(gmtTimeStampFromDate,gmtTimeStampFromDate1,MailId);
                        Common.ShowSweetSucess(mContext,"Trip report is sent to "+MailId+ ".");
                        dialog.dismiss();
                    }



                }
            });

            dialog.show();

    }

    private void GetTripReport(final long gmtTimeStampFromDate, final long gmtTimeStampToDate, final String mailId) {

        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
     //   final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(mContext, "Calculating Km wait.......");

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetTripReport",new Response.Listener<String>() {
            //  stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/"+"UserServiceAPI/GetVehTotalKm",new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  Trip report Km ----"+response);

                ParseTripReponce(response);
              //  pDialog1.dismiss();

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  pDialog1.hide();
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

                params.put("DeviceImieNo", ACCReport.DeviceImieNo);
                params.put("StartDateTime",gmtTimeStampFromDate+"");
                params.put("EndDateTime", gmtTimeStampToDate+"");
                params.put("Email", mailId+"");


                System.out.println("REq---GetTripReport------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    private void ParseTripReponce(String response) {
        try {
            JSONObject jo=new JSONObject(response);
            if(jo.getString("error").equalsIgnoreCase("false")){


            }else if (jo.getString("error").equalsIgnoreCase("true")&&response.contains("message"))
                Common.ShowSweetAlert(mContext,jo.getString("message"));
            else
                Common.ShowSweetAlert(mContext,"Getting error in getting trip report.Please try again");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void ShowMaildialog(final long startTimeStampFromDate, final long endStampFromDate) {
        // custom dialog

         final EditText txt_pin;
         final Dialog dialog = new Dialog( mContext);
        dialog.setContentView(R.layout.dialog_mailid);
        dialog.setTitle("Enter Mail-Id ");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);


        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String MailId=txt_pin.getText().toString();

                System.out.println("************************************"+MailId);
                if(Validemail(MailId))
                {
                    GetVehMonthlyTotalKm(startTimeStampFromDate,endStampFromDate,MailId);
                    Common.ShowSweetSucess(mContext,"Monthly mileage report is sent to "+MailId+ ".");
                    dialog.dismiss();
                }



            }
        });

        dialog.show();
    }


    public  boolean Validemail(String mail){
        Boolean valid=true;

         if(mail.length()==0)
        {

            Common.ShowSweetAlert(mContext, "Enter E-Mail Address !");

            valid=false;
        }
        else if(!mail.matches(Common.EMAIL_REGEX))
        {

            Common.ShowSweetAlert(mContext, "Enter Valid E-Mail Address!");

            valid=false;
        }
        return valid;
    }

    

    //Get Totao Km for One day
    private void GetVehTotalKm() {


        Random random = new Random();

        final int random_km = random.nextInt(90) + 10;
        final SweetAlertDialog Pdialog = Common.ShowSweetProgress(mContext, "Getting Report....");
        Pdialog.show();
        CountDownTimer CDTOthclick = new CountDownTimer(2 * 1000, 1000) {
            int i = 0;

            public void onTick(long millisUntilFinished) {
                i--;
            }

            public void onFinish() {
                SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Total/km=" + random_km + " km");
                pDialog.setContentText("For  " + GACCReport.StudentName + " on " + currentdate);
                pDialog.setCancelable(true);
                pDialog.show();


                Pdialog.dismiss();
            }
        }.start();


    }

    //Get Total Milage MOnthly
    private void GetVehMonthlyTotalKm(final long startgmtTimeStampFromDate, final long endgmtTimeStampFromDate, final String mailId) {

        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
        //JSon object request for reading the json data
         stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetVehMonthlyTotalKm",new Response.Listener<String>() {
      //  stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/"+"UserServiceAPI/GetVehMonthlyTotalKm",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  ddddddddddddddddddddddddddddddddddddddddd----"+response);

               // ParseDistance(response);

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
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

                params.put("DeviceImieNo", ACCReport.DeviceImieNo);
                params.put("StartDateTime",startgmtTimeStampFromDate+"");
                params.put("EndDateTime", endgmtTimeStampFromDate+"");
                params.put("MailId", mailId);
                params.put("DeviceName", ACCReport.StudentName);


                System.out.println("REq---GetVehMonthlyTotalKm------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }




}
