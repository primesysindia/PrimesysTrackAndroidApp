package com.primesys.VehicalTracking.MyAdpter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Activity.DeviceLevelRenewService;
import com.primesys.VehicalTracking.Activity.DeviceReport.CurrentDeviceStatusActivity;
import com.primesys.VehicalTracking.Activity.DeviceReport.DeviceBatteryStatusActivity;
import com.primesys.VehicalTracking.Activity.DeviceReport.DeviceOnReportActivity;
import com.primesys.VehicalTracking.Activity.DeviceReport.MonitorSOSPressActivity;
import com.primesys.VehicalTracking.Activity.DeviceReport.NearbyFeatureCodeActivity;
import com.primesys.VehicalTracking.Activity.RenewServiceActivity;
import com.primesys.VehicalTracking.Activity.TripReportShow;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeofenceHistory;
import com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment.Mine_ModuleFragment;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.Dto.TripInfoDto;
import com.primesys.VehicalTracking.Dto.UserModuleDTO;
import com.primesys.VehicalTracking.Guest.GHomeTab;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

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

/**
 * Created by root on 19/12/16.
 */
public class GridModuleAdapter extends RecyclerView.Adapter<GridModuleAdapter.ViewHolder> {

    List<UserModuleDTO> Items;
    Context mContext;
    int position;
    private TextView dateView;
    private String currentdate;
    private StringRequest stringRequest;
    private String StartTime,EndTime;
    private SweetAlertDialog pDialog1;

    private ArrayList<DeviceDataDTO> childlist;
    private ListView list_car;
    private StudentListAdpter padapter;
    String DeviceImieNo="0";
    private SweetAlertDialog Pdialog;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);

    int month = calendar.get(Calendar.MONTH);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);

    public GridModuleAdapter(Context context, int i, ArrayList<UserModuleDTO> mItems) {
        super();
        this.mContext=context;
        this.Items=mItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      //  View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_card_row, viewGroup, false);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_card_row, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        UserModuleDTO cat = Items.get(i);
        viewHolder.tv_module.setText(cat.getModuleTitle());
    }

    @Override
    public int getItemCount() {

        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_module;
        public ViewHolder(View itemView) {
            super(itemView);

            try{

                tv_module = (TextView)itemView.findViewById(R.id.title_module);
                Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
                tv_module.setTypeface(typeFace);

                itemView.setTag(position);

                itemView.setOnClickListener(this);
            }catch (ClassCastException c){
                c.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }



        }


        @Override
        public void onClick(View v) {
            final UserModuleDTO currentmodule= Items.get(getAdapterPosition());

            try {

                    if (currentmodule.getModule().equalsIgnoreCase("Track")&&!Common.PlatformRenewalStatus)
                    {
                        if(Common.roleid.equalsIgnoreCase("5")){
                            Intent switch1 = new Intent(itemView.getContext(), GHomeTab.class);
                            mContext.startActivity(switch1);
                        }else{
                            if (Common.TrackDay >= 0) {

                                Class CurrentClass=Class.forName("com.primesys.VehicalTracking."+Items.get(getAdapterPosition()).getModuleActivity());
                                Intent switch1 = new Intent(itemView.getContext(),CurrentClass);
                                mContext.startActivity(switch1);
                            }else
                                ShowPaymentDialog();
                        }

                    }else if (currentmodule.getModule().equalsIgnoreCase("RenewServiceActivity"))
                    {
                        if (Common.PlatformRenewalStatus){

                            Intent Add = new Intent(mContext, DeviceLevelRenewService.class);
                            mContext.startActivity(Add);
                        }else {

                            Intent Add = new Intent(mContext, RenewServiceActivity.class);
                            mContext.startActivity(Add);


                        }
                    }else if (currentmodule.getModule().equalsIgnoreCase("TripReport")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
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
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-90L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1L*24L*60L*60L*1000L);

                        dpd.show();

                    } else if (currentmodule.getModule().equalsIgnoreCase("DailyMileageReport")) {


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
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-90L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1L*24L*60L*60L*1000L);
                        dpd.show();

                    }else if (currentmodule.getModule().equalsIgnoreCase("MonthlyMileageReport")) {
                        String format = "dd-MM-yyyy hh:mm aa";
                        SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
                        Date today = new Date();
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(today);
                        Log.e("Today Time======",Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today))+"");
                        cal.add(Calendar.DAY_OF_MONTH, -30);
                        Date today30 = cal.getTime();
                        Log.e("Before Time======",Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today30))+"");

                        //GetMonthalyMilageReport(Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today30)),Common.getGMTTimeStampFromDate(sdfLocalFormat.format(today)));

                        ShowMaildialog();




                    }else if (currentmodule.getModule().equalsIgnoreCase("Trip Report")) {

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
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1L*24L*60L*60L*1000L);


                        dpd.show();


                    }else if (currentmodule.getModule().equalsIgnoreCase("GeofenceHistory")) {
                        //GeofenceHistory
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();

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
                                        GetGeofenceHistory(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());


                        dpd.show();
                    }else if (currentmodule.getModule().equalsIgnoreCase("CurrentDeviceStatus")) {
                        //GeofenceHistory
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
                        showPastTimeDialog(Calendar.getInstance().getTimeInMillis()/1000);
                   /*     // Launch Date Picker Dialog
                        DatePickerDialog dpd = new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // Display Selected date in textbox

                                        long dv = Long.valueOf(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"))*1000;
                                        Date df = new Date(dv);
                                        currentdate  = new SimpleDateFormat("dd-MMM-yy").format(df);

                                        showPastTimeDialog(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));


                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());


                        dpd.show();*/
                    }else if (currentmodule.getModule().equalsIgnoreCase("DeviceOnReport")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
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

                                        Intent startMain = new Intent(mContext,DeviceOnReportActivity.class);
                                        startMain.putExtra("gmtTimeStampFromDate",Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am")+"");
                                        startMain.putExtra("status","On");

                                        mContext.startActivity(startMain);

                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());


                        dpd.show();
                    }

                    else if (currentmodule.getModule().equalsIgnoreCase("DeviceOffReport")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
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

                                        Intent startMain = new Intent(mContext,DeviceOnReportActivity.class);
                                        startMain.putExtra("gmtTimeStampFromDate",Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am")+"");
                                        startMain.putExtra("status","Off");

                                        mContext.startActivity(startMain);



                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-24L*60L*60L*1000L);


                        dpd.show();
                    }
                    else if (currentmodule.getModule().equalsIgnoreCase("NearByPoint&CrossingInspection")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
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

                                        Intent startMain = new Intent(mContext,NearbyFeatureCodeActivity.class);
                                        startMain.putExtra("gmtTimeStampFromDate",Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am")+"");
                                        startMain.putExtra("featureCode","3");
                                        startMain.putExtra("Title",currentmodule.getModuleTitle());

                                        mContext.startActivity(startMain);



                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-24L*60L*60L*1000L);


                        dpd.show();
                    } else if (currentmodule.getModule().equalsIgnoreCase("NearByLevelCrossingInspection")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
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

                                        Intent startMain = new Intent(mContext,NearbyFeatureCodeActivity.class);
                                        startMain.putExtra("gmtTimeStampFromDate",Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am")+"");
                                        startMain.putExtra("featureCode","4");
                                        startMain.putExtra("Title",currentmodule.getModuleTitle());

                                        mContext.startActivity(startMain);



                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-24L*60L*60L*1000L);


                        dpd.show();
                    } else if (currentmodule.getModule().equalsIgnoreCase("NearBySEJInspection")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();
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

                                        Intent startMain = new Intent(mContext,NearbyFeatureCodeActivity.class);
                                        startMain.putExtra("gmtTimeStampFromDate",Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am")+"");
                                        startMain.putExtra("featureCode","5");
                                        startMain.putExtra("Title",currentmodule.getModuleTitle());

                                        mContext.startActivity(startMain);

                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-24L*60L*60L*1000L);


                        dpd.show();
                    }
                    else if (currentmodule.getModule().equalsIgnoreCase("DeviceBatteryStatus")) {
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();

                                        Intent startMain = new Intent(mContext,DeviceBatteryStatusActivity.class);
                                        startMain.putExtra("Title",currentmodule.getModuleTitle());
                                        mContext.startActivity(startMain);

                    }  else if(currentmodule.getModule().equalsIgnoreCase("MonitorSOSPress")){
                        DeviceImieNo= Mine_ModuleFragment.CurrentDeviceSelect.getImei_no();

                        DatePickerDialog dpd = new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // Display Selected date in textbox

                                        long dv = Long.valueOf(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"))*1000;
                                        Date df = new Date(dv);
                                        currentdate  = new SimpleDateFormat("dd-MMM-yy").format(df);
                                        Intent startMain = new Intent(mContext,MonitorSOSPressActivity.class);
                                        startMain.putExtra("gmtTimeStampFromDate",Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am")+"");
                                        //   startMain.putExtra("status","Off");

                                        mContext.startActivity(startMain);

                                    }
                                }, year, month, day);
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());


                        dpd.show();
                    }
                    else {
                        Class CurrentClass=Class.forName("com.primesys.VehicalTracking.Activity."+Items.get(getAdapterPosition()).getModuleActivity());
                        Intent switch1 = new Intent(itemView.getContext(),CurrentClass);
                        mContext.startActivity(switch1);
                    }



            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void getDeviceOnOffStatus(long gmtTimeStampFromDate) {


    }

    private void showPastTimeDialog(final long gmtTimeStampFromDate) {



        final EditText txt_time;
        final Dialog createpindialog = new Dialog(mContext);
        createpindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createpindialog.setContentView(R.layout.dialog_submitsleep);
        final TextView tv_title=(TextView)createpindialog.findViewById(R.id.d_title);
        tv_title.setText("Enter time in past min");

        txt_time = (EditText) createpindialog.findViewById(R.id.txt_time);


        Button Submitpin = (Button) createpindialog.findViewById(R.id.pinsubmit);

        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_time.getText().length()!=0){
                    int pastTime=Integer.parseInt(txt_time.getText().toString());

                    if (pastTime>=5&&pastTime<=60){
                        createpindialog.dismiss();

                        Intent startMain = new Intent(mContext,CurrentDeviceStatusActivity.class);
                        startMain.putExtra("gmtTimeStampFromDate",gmtTimeStampFromDate+"");
                        startMain.putExtra("pastTime",pastTime+"");

                        mContext.startActivity(startMain);

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



    private void ShowVehicaleDialog() {

        // custom dialog

        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle(mContext.getResources().getString(R.string.select_car));

        dialog.setContentView(R.layout.dialog_studentlist);
        dialog.setCancelable(false);
        dialog.closeOptionsMenu();
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.show();
        list_car=(ListView)dialog.findViewById(R.id.student_list);
        Button cancel = (Button) dialog.findViewById(R.id.d_cancel);
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  HomeNew.viewPager.setCurrentItem(0);
                dialog.dismiss();
//                getActivity().finish();
               // Bottom_navigation.setSelectedItemId(R.id.navigation_track);



            }
        });
//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
        padapter=new StudentListAdpter(mContext, R.layout.fragment_mapsidebar, childlist);
        list_car.setAdapter(padapter);

        list_car.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DeviceImieNo=childlist.get(position).getImei_no();
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
                                            GetTripReport(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                                        }
                                    }, year, month, day);
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                            dpd.getDatePicker().setMaxDate(System.currentTimeMillis()-1L*24L*60L*60L*1000L);


                            dpd.show();


                dialog.dismiss();
            }
        });


        dialog.show();

    }


    private void ShowPaymentDialog() {

        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(String.valueOf(mContext.getResources().getText(R.string.str_make_reachage_title)))
                .setContentText((String.valueOf(mContext.getResources().getText(R.string.str_make_reachage))))
                .setCancelText("No,Exit !")
                .setConfirmText("Make Payment!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent startMain = new Intent(mContext,RenewServiceActivity.class);
                        mContext.startActivity(startMain);

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
    }


    private void GetTripReport(final long gmtTimeStampFromDate, final long gmtTimeStampToDate) {

        pDialog1 = Common.ShowSweetProgress(mContext, " Getting Trip Report wait.......");
        pDialog1.setCancelable(true);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetDirectTripReport",new Response.Listener<String>() {
            //  stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/"+"UserServiceAPI/GetVehTotalKm",new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                Log.e("GetDirectTri","onResponse---GetDirectTripReport------"+response);

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

                params.put("DeviceImieNo", Mine_ModuleFragment.CurrentDeviceSelect.getImei_no());
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
        PrimesysTrack.mRequestQueue.add(stringRequest);


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
                itrip.putExtra("DeviceImieNo", DeviceImieNo);
                itrip.putExtra("StartDateTime",StartTime+"");
                itrip.putExtra("EndDateTime", EndTime+"");

                mContext.startActivity(itrip);
            }else {
                Common.ShowSweetAlert(mContext,"Trip report not found on selected date.Please try again");


            }
        }

    }


    private void GetDevicelist() {


        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "ParentAPI/GetEmpDevicelist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Responce---GetEmpDevicelist********************************************------"+response);
                parsingDeviceInfo (response);

                pDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        if (error!=null)
                            Log.e("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("UserId",Common.userid);

                System.out.println("REq---GetDevicelist------"+params);
                return params;
            }
        };
        stringRequest.setTag("");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        PrimesysTrack.mRequestQueue.add(stringRequest);


    }



    public void parsingDeviceInfo(String result) {
        try {
            Log.e("Track Info list", result);
            JSONArray joArray = new JSONArray(result);
            for (int i = 0; i < joArray.length(); i++) {
                JSONObject joObject = joArray.getJSONObject(i);
                DeviceDataDTO dmDetails = new DeviceDataDTO();
                dmDetails.setId(joObject.getString("StudentID"));
                dmDetails.setName(joObject.getString("Name"));

                dmDetails.setPath(joObject.getString("Photo").replaceAll("~", "").trim());
                dmDetails.setType(joObject.getString("Type"));
                dmDetails.setImei_no(joObject.getString("ImeiNo"));


                childlist.add(dmDetails);
            }


        } catch (Exception e) {
            Log.e("Exception", "" + e);
        } finally {
            //it work Better but take time to Load
            if (childlist.size() > 0) {
                //Insert Offeline data
                PrimesysTrack.mDbHelper.Insert_Device_list(childlist);
                ShowVehicaleDialog();

            } else {
                Common.ShowSweetAlert(mContext, "No device information found.");
            }

        }
    }




        //Get Total Km for One day
        private void GetDailyMilageReport(final long startgmtTimeStampFromDate, final long endgmtTimeStampFromDate) {

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

                    params.put("DeviceImieNo", Mine_ModuleFragment.CurrentDeviceSelect.getImei_no());
                    params.put("StartDateTime",startgmtTimeStampFromDate+"");
                    params.put("EndDateTime", endgmtTimeStampFromDate+"");


                    System.out.println("REq---GetVehReportlist------"+params);
                    return params;
                }

            };


            stringRequest.setTag("VTS");
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            PrimesysTrack.mRequestQueue.add(stringRequest);


        }

        private void ParseDistance(String response) {


            try {
                JSONObject jo=new JSONObject(response);
                if(jo.getString("error").equalsIgnoreCase("false")){

                    if (Common.mesurment_unit.contains("km")){
                        String TotalKm=String.format("%.2f",Double.parseDouble(jo.getString("message")));
                        SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("Total ="+TotalKm+" km");
                        pDialog.setContentText("For "+ Mine_ModuleFragment.CurrentDeviceSelect.getName()+" on "+currentdate);
                        pDialog.setCancelable(true);
                        pDialog.show();
                    }else{
                        String TotalKm=String.format("%.2f",Double.parseDouble(jo.getString("message"))*Common.miles_multiplyer);
                        SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("Total ="+TotalKm+" miles");
                        pDialog.setContentText("For "+ Mine_ModuleFragment.CurrentDeviceSelect.getName()+" on "+currentdate);
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



        private void GetMonthalyMilageReport(final long startgmtTimeStampFromDate, final long endgmtTimeStampFromDate)
        {

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

                    params.put("DeviceImieNo",  Mine_ModuleFragment.CurrentDeviceSelect.getImei_no());
                    params.put("StartDateTime",startgmtTimeStampFromDate+"");
                    params.put("EndDateTime", endgmtTimeStampFromDate+"");
                    params.put("DeviceName",  Mine_ModuleFragment.CurrentDeviceSelect.getName());


                    Log.e("--GetMonthalyReport----","REq---GetMonthalyMilageReport------"+params);
                    return params;
                }

            };


            stringRequest.setTag("VTS");
            //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            PrimesysTrack.mRequestQueue.add(stringRequest);


        }

        private void ParseMonthlyDistance(String response) {

            try {
                JSONObject jo=new JSONObject(response);
                if(jo.getString("error").equalsIgnoreCase("false")){

                    if (Common.mesurment_unit.contains("km")) {

                        String TotalKm = String.format("%.2f", Double.parseDouble(jo.getString("message")));
                        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText(" Monthly Total =" + TotalKm + " km");
                        pDialog.setContentText("for  " +  Mine_ModuleFragment.CurrentDeviceSelect.getName() + " on " + currentdate);
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
                        pDialog.setContentText("for  " +  Mine_ModuleFragment.CurrentDeviceSelect.getName() + " on " + currentdate);
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
                    Common.ShowSweetAlert(mContext,"No monthly mileage report found for " +  Mine_ModuleFragment.CurrentDeviceSelect.getName());

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Get Total Milage MOnthly
        private void GetVehMonthlyTotalKm(final String[] startgmtTimeStampFromDate, final String[] endgmtTimeStampFromDate, final String mailId)
        {

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

                    params.put("DeviceImieNo",  Mine_ModuleFragment.CurrentDeviceSelect.getImei_no());
                    params.put("StartDateTime",startgmtTimeStampFromDate[0].toString());
                    params.put("EndDateTime", endgmtTimeStampFromDate[0].toString());

                    params.put("MailId",mailId);

                    System.out.println("REq---GetVehMonthlyTotalKm------"+params);
                    return params;
                }

            };


            stringRequest.setTag("VTS");
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            PrimesysTrack.mRequestQueue.add(stringRequest);


        }



    private void ShowMaildialog() {
        // custom dialog
        final EditText txt_pin;
        final TextView txt_start_time,txt_end_time,d_title;;
        final Dialog dialog = new Dialog( mContext);
        final String[] trip_start_time = {""};
        final String[] trip_end_time = {""};
        dialog.setContentView(R.layout.dialog_trip_mailid);
      //  dialog.setTitle("Detail");

        d_title = (TextView) dialog.findViewById(R.id.d_title);

        txt_pin = (EditText) dialog.findViewById(R.id.txt_emailid);
        txt_start_time = (TextView) dialog.findViewById(R.id.txt_startdate);
        txt_end_time = (TextView) dialog.findViewById(R.id.txt_enddate);

        d_title.setText("Monthly Trip Report");
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
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-90L*24L*60L*60L*1000L);
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
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-90L*24L*60L*60L*1000L);
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
                    Common.ShowSweetSucess(mContext,"Monthly mileage report is sent to "+MailId+ ". ");
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




    private void GetGeofenceHistory(final long startdate, final long enddate)
    {

          Pdialog=Common.ShowSweetProgress(mContext,"Getting Monthly Report Wait.");
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetGeofenceHistory",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Response of  GetGeofenceHistory----"+response);
                  Pdialog.dismiss();
                ParseGeofenceReponce(response);


            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                               Pdialog.dismiss();

                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("DeviceImieNo",  Mine_ModuleFragment.CurrentDeviceSelect.getImei_no());
                params.put("StartDateTime",startdate+"");
                params.put("EndDateTime", enddate+"");

                System.out.println("REq---GetGeofenceHistory------"+params);
                return params;
            }

        };


        stringRequest.setTag(PrimesysTrack.TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        PrimesysTrack.mRequestQueue.add(stringRequest);


    }

    private void ParseGeofenceReponce(String response) {
        ArrayList<GeofenceDTO>geofenceList=new ArrayList<>();
        try {
            JSONArray Jarray=new JSONArray(response);
            if (Jarray.length()>0){

                for (int i=0;i<Jarray.length();i++ ) {
                    GeofenceDTO obj=new GeofenceDTO();
                    JSONObject jo= (JSONObject) Jarray.get(i);

                    obj.setLat(jo.getString("lat"));
                    obj.setLang(jo.getString("lan"));
                    obj.setAddress(jo.getString("address"));
                    obj.setTimestamp(jo.getLong("timestamp"));
                    obj.setSpeed(jo.getInt("speed"));
                    obj.setStatusout(jo.getString("status"));
                    obj.setLan_direction(jo.getString("lan_direction"));
                    obj.setLat_direction(jo.getString("lat_direction"));
                    geofenceList.add(obj);
                }


                Intent geo=new Intent(mContext,GeofenceHistory.class);
                geo.putExtra("geofencelist",  geofenceList);
                mContext.startActivity(geo);;

            }else {
                Common.ShowSweetAlert(mContext,"Geo-fence not found on selected date.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}




