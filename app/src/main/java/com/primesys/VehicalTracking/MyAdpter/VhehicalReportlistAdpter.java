
package com.primesys.VehicalTracking.MyAdpter;


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
import com.primesys.VehicalTracking.Activity.TripReportShow;
import com.primesys.VehicalTracking.Dto.TripInfoDto;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.ACCReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VhehicalReportlistAdpter extends RecyclerView.Adapter<VhehicalReportlistAdpter.ViewHolder>{

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
    private SweetAlertDialog pDialog1;
    private SweetAlertDialog Pdialog;
    private String EndTime,StartTime;


    public VhehicalReportlistAdpter(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata, int row_smslist, Context context) {
        super();

        Log.e("GVhehicalReportAdpter","Inside --------------------GVhehicalReportlistAdpter");
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

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
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

                                        GetDailyMilageReport(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));


                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.show();

                    }else if (CurrentSMSObj.getActualCommand().equalsIgnoreCase("Monthly Total KM")) {
                        String format = "dd-MM-yyyy hh:mm aa";
                        SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
                        Date today = new Date();
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(today);
                        Log.e("Today Time======",Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today))+"");
                        cal.add(Calendar.DAY_OF_MONTH, -30);
                        Date today30 = cal.getTime();
                        Log.e("Before Time======",Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today30))+"");

                        GetMonthalyMilageReport(Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today30)),Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today)));



                    }else if (CurrentSMSObj.getActualCommand().equalsIgnoreCase("Trip Report")) {

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
                                        GetTripReport(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());


                        dpd.show();


                    }
                    }


            });


        }


    }
/*
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

    }*/

    private void GetTripReport(final long gmtTimeStampFromDate, final long gmtTimeStampToDate) {

        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
         pDialog1 = Common.ShowSweetProgress(mContext, " Getting Trip Report wait.......");
        pDialog1.setCancelable(true);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetDirectTripReport",new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                Log.e("GetDirectTripReport--onResponse","onResponse---GetDirectTripReport------"+response);

                ParseTripReponce(response);

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
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("DeviceImieNo", ACCReport.DeviceImieNo);
                params.put("StartDateTime",gmtTimeStampFromDate+"");
                params.put("EndDateTime", gmtTimeStampToDate+"");
                StartTime=gmtTimeStampFromDate+"";
                EndTime=gmtTimeStampToDate+"";

                Log.e("GetDirectTripReport","REq---GetDirectTripReport------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
       stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    private void ParseTripReponce(String response) {

        Double total=0.0;
        try {
            if (TripReportShow.TripList!=null)
                TripReportShow.TripList.clear();
            JSONArray jArray=new JSONArray(response);
            if(jArray.length()>0) {

                for (int i = 0; i < jArray.length(); i++) {
                    TripInfoDto trip = new TripInfoDto();
                    JSONObject jo = (JSONObject) jArray.get(i);


                    trip.setAvgspeed(jo.getString("avgspeed"));
                    trip.setDestlat(jo.getString("destlat"));
                    trip.setDestlon(jo.getString("destlon"));
                    trip.setDestspeed(jo.getString("destspeed"));
                    trip.setDesttimestamp(jo.getString("desttimestamp"));
                    trip.setDevice(jo.getString("device"));
                    trip.setDevicename(jo.getString("devicename"));
                    trip.setMaxspeed(jo.getString("maxspeed"));
                    trip.setReport_id(jo.getString("report_id"));
                    trip.setSrclat(jo.getString("srclat"));
                    trip.setSrclon(jo.getString("srclon"));
                    trip.setSrcspeed(jo.getString("srcspeed"));
                    trip.setSrctimestamp(jo.getString("srctimestamp"));
                    trip.setTotalkm(jo.getString("totalkm"));
                    trip.setSrc_adress(jo.getString("src_adress"));
                    trip.setDest_address(jo.getString("dest_address"));

                    TripReportShow.TripList.add(trip);
                    total=total+Double.parseDouble(trip.getTotalkm());

                }


/*

                for (int j = 0; j < TripReportShow.TripList.size(); j++) {
                    TripInfoDto item = TripReportShow.TripList.get(j);
                    total=total+Double.parseDouble(item.getTotalkm());

                    TripReportShow.TripList.get(j).setSrc_adress(Common.getStringAddress(mContext, Double.parseDouble(item.getSrclat()), Double.parseDouble(item.getSrclon())));
                    TripReportShow.TripList.get(j).setDest_address(Common.getStringAddress(mContext, Double.parseDouble(item.getDestlat()), Double.parseDouble(item.getDestlon())));
                }
*/


            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        e.printStackTrace();
    }finally {
            pDialog1.dismiss();

            if (TripReportShow.TripList.size()>0){
                Intent itrip=new Intent(mContext, TripReportShow.class);
                itrip.putExtra("TripTotal",total+"");
                itrip.putExtra("DeviceImieNo", ACCReport.DeviceImieNo);
                itrip.putExtra("StartDateTime",StartTime+"");
                itrip.putExtra("EndDateTime", EndTime+"");

                mContext.startActivity(itrip);
            }else {
                Common.ShowSweetAlert(mContext,"Trip report not found on selected date.Please try again");


            }
        }

    }

    private void ShowMaildialog() {
        // custom dialog
         final EditText txt_pin;
         final TextView txt_start_time,txt_end_time;;
         final Dialog dialog = new Dialog( mContext);
        final String[] trip_start_time = {""};
        final String[] trip_end_time = {""};
        dialog.setContentView(R.layout.dialog_trip_mailid);
        dialog.setTitle("Detail");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);
        txt_start_time = (TextView) dialog.findViewById(R.id.txt_startdate);
        txt_end_time = (TextView) dialog.findViewById(R.id.txt_enddate);
        txt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                txt_start_time.setText("Start Date : "+currentdate);
                                trip_start_time[0] =String.valueOf(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                            }
                        }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1L*24L*60L*60L*1000L);


                dpd.show();
            }
        });
        txt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                txt_end_time.setText("End Date :"+currentdate);
                                trip_end_time[0] =String.valueOf(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                            }
                        }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1L*24L*60L*60L*1000L);


                dpd.show();
            }
        });

        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String MailId=txt_pin.getText().toString();

                System.out.println("************************************"+MailId);
                if(Validemail(MailId, trip_start_time[0],trip_end_time[0]))
                {
                    String format = "dd-MM-yyyy hh:mm aa";
                    SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
                    Date today = new Date();
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(today);
                    cal.add(Calendar.DAY_OF_MONTH, -30);
                    Date today30 = cal.getTime();

                    GetVehMonthlyTotalKm(trip_start_time,trip_end_time,MailId);

                  //  GetVehMonthlyTotalKm(startTimeStampFromDate,endStampFromDate,MailId);
                    Common.ShowSweetSucess(mContext,"Monthly mileage report is sent to "+MailId+ ".");
                    dialog.dismiss();
                }



            }
        });

        dialog.show();
    }



    public  boolean Validemail(String mail, String trip_start_time, String trip_end_time){
        Boolean valid=true;

         if(mail.length()==0)
        {

            Common.ShowSweetAlert(mContext, "Please enter valid email address!");

            valid=false;
        }
        else if(!mail.matches(Common.EMAIL_REGEX))
        {

            Common.ShowSweetAlert(mContext, "Please enter valid email address!");

            valid=false;
        } else if(trip_start_time.length()<=0)
         {

             Common.ShowSweetAlert(mContext, "Please enter start date!");

             valid=false;
         } else if(trip_end_time.length()<=0)
         {

             Common.ShowSweetAlert(mContext, "Please enter end date!");

             valid=false;
         }else if(Long.parseLong(trip_end_time)<=Long.parseLong(trip_start_time))
         {

             Common.ShowSweetAlert(mContext, "End date must be greater than start date!");

             valid=false;
         }
        return valid;
    }

    

    //Get Total Km for One day
    private void GetDailyMilageReport(final long startgmtTimeStampFromDate, final long endgmtTimeStampFromDate) {

        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
        final SweetAlertDialog pDialog1 = Common.ShowSweetProgress(mContext, "Calculating Km wait.......");

        //JSon object request for reading the json data
         stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetDailyMilageReport",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Responce of  Calculating Km ----"+response);

                ParseDistance(response);
                pDialog1.dismiss();

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

                params.put("DeviceImieNo", ACCReport.DeviceImieNo);
                params.put("StartDateTime",startgmtTimeStampFromDate+"");
                params.put("EndDateTime", endgmtTimeStampFromDate+"");


                System.out.println("REq---GetVehReportlist------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    private void ParseDistance(String response) {


        try {
            JSONObject jo=new JSONObject(response);
            if(jo.getString("error").equalsIgnoreCase("false")){

                if (Common.mesurment_unit.contains("km")){
                    String TotalKm=String.format("%.2f",Double.parseDouble(jo.getString("message")));
                    SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Total ="+TotalKm+" km");
                    pDialog.setContentText("For "+ACCReport.StudentName+" on "+currentdate);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }else{
                    String TotalKm=String.format("%.2f",Double.parseDouble(jo.getString("message"))*Common.miles_multiplyer);
                    SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Total ="+TotalKm+" miles");
                    pDialog.setContentText("For "+ACCReport.StudentName+" on "+currentdate);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

            }else if (jo.getString("error").equalsIgnoreCase("true")&&response.contains("message"))
                Common.ShowSweetAlert(mContext,jo.getString("message"));
            else
                Common.ShowSweetAlert(mContext,"Getting error in calculating total km .Please try again");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //Get Total Milage MOnthly
    private void GetMonthalyMilageReport(final long startgmtTimeStampFromDate, final long endgmtTimeStampFromDate)
    {

        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
        Pdialog=Common.ShowSweetProgress(mContext,"Getting Monthly Report Wait.");
        //JSon object request for reading the json data
         stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetMonthalyMilageReport",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

              Log.e("","Responce of  GetMonthalyMilageReport----"+response);
                Pdialog.dismiss();
               ParseMonthlyDistance(response);

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println("************************"+error.toString());
                            Pdialog.dismiss();

                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("DeviceImieNo", ACCReport.DeviceImieNo);
                params.put("StartDateTime",startgmtTimeStampFromDate+"");
                params.put("EndDateTime", endgmtTimeStampFromDate+"");
                params.put("DeviceName", ACCReport.StudentName);


               Log.e("--GetMonthalyReport----","REq---GetMonthalyMilageReport------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
      //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    private void ParseMonthlyDistance(String response) {

        try {
            JSONObject jo=new JSONObject(response);
            if(jo.getString("error").equalsIgnoreCase("false")){

                if (Common.mesurment_unit.contains("km")) {

                    String TotalKm = String.format("%.2f", Double.parseDouble(jo.getString("message")));
                    final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText(" Monthly Total =" + TotalKm + " km");
                    pDialog.setContentText("for  " + ACCReport.StudentName + " on " + currentdate);
                    pDialog.setCancelable(true);
                    pDialog.setConfirmText("OK");
                    pDialog.setCancelText("Get On Mail");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                        }
                    });
                    pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ShowMaildialog();
                            pDialog.dismiss();

                        }
                    });
                    pDialog.show();
                }else {

                    String TotalKm = String.format("%.2f", Double.parseDouble(jo.getString("message"))*Common.miles_multiplyer);
                    final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText(" Monthly Total =" + TotalKm + " miles");
                    pDialog.setContentText("for  " + ACCReport.StudentName + " on " + currentdate);
                    pDialog.setCancelable(true);
                    pDialog.setConfirmText("OK");
                    pDialog.setCancelText("Get On Mail");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                        }
                    });
                    pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ShowMaildialog();
                            pDialog.dismiss();

                        }
                    });
                    pDialog.show();
                }
            }else if(jo.getString("error").equalsIgnoreCase("true")){
                Common.ShowSweetAlert(mContext,"No monthly mileage report found for " + ACCReport.StudentName);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Get Total Milage MOnthly
    private void GetVehMonthlyTotalKm(final String[] startgmtTimeStampFromDate, final String[] endgmtTimeStampFromDate, final String mailId)
    {

        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it
     //   Pdialog=Common.ShowSweetProgress(mContext,"Getting Monthly Report Wait.");
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetWeekalyTripReportOnMail",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Response of  GetVehMonthlyTotalKm----"+response);
              //  Pdialog.dismiss();

            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                        //    Pdialog.dismiss();

                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("DeviceImieNo", ACCReport.DeviceImieNo);
                params.put("StartDateTime",startgmtTimeStampFromDate[0].toString());
                params.put("EndDateTime", endgmtTimeStampFromDate[0].toString());
                //params.put("DeviceName", ACCReport.StudentName);

                params.put("MailId",mailId);

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
