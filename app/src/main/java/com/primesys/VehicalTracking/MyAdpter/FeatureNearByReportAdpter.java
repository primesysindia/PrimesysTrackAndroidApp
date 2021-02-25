package com.primesys.VehicalTracking.MyAdpter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Dto.FeatureNearbyDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FeatureNearByReportAdpter extends RecyclerView.Adapter<FeatureNearByReportAdpter.ViewHolder>{

    private SharedPreferences.Editor editor;
    public List<FeatureNearbyDTO> dataList=new ArrayList<FeatureNearbyDTO>();
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

    public FeatureNearByReportAdpter(Context mContext, int card_device_current_report, ArrayList<FeatureNearbyDTO> deviceInfoList) {
        super();
        this.dataList = deviceInfoList;
        this.rowLayout = card_device_current_report;
        this.mContext = mContext;
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
        try {
          //  FeatureNearbyDTO item = dataList.get(position);
            //  holder.itemDesc.setText(item.get());
            holder.tv_device_name.setText("Name : "+dataList.get(position).getGpsDeviceName());
            Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
            holder.tv_device_name.setTypeface(typeFace);

            holder.tv_lasttime.setText("Time : "+Common.getDateCurrentTimeZone(Long.parseLong(dataList.get(position).getDate())));
            holder.tv_time_spent.setText("Time spent : "+dataList.get(position).getTimespent());
            holder.tv_signal_location.setText("Signal Location : "+dataList.get(position).getSignalLoaction());
            holder.tv_feature_details.setText("Feature Details : "+dataList.get(position).getFeatureDetail());
            holder.tv_location_departure.setText("Location Departure : "+dataList.get(position).getEndLocation());
            holder.tv_location_arraival.setText("Location Arrival : "+dataList.get(position).getStartLocation());

            holder.tv_section.setText("Section : "+dataList.get(position).getSection());



            holder.tv_lasttime.setTypeface(typeFace);
            holder.tv_time_spent.setTypeface(typeFace);
            holder.tv_signal_location.setTypeface(typeFace);
            holder.tv_feature_details.setTypeface(typeFace);
            holder.tv_location_departure.setTypeface(typeFace);
            holder.tv_location_arraival.setTypeface(typeFace);
                        holder.tv_section.setTypeface(typeFace);

            holder.itemView.setTag(position);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(PrimesysTrack.TAG,"-------------Null pos--"+position);
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_device_name, tv_lasttime, tv_signal_location,tv_section,
                tv_feature_details,tv_location_arraival,tv_location_departure,tv_time_spent;

        LinearLayout lay_report;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_device_name = (TextView) itemView.findViewById(R.id.tv_device_name);
            tv_signal_location = (TextView) itemView.findViewById(R.id.tv_signal_location);
            tv_lasttime = (TextView) itemView.findViewById(R.id.tv_lasttime);
            tv_feature_details = (TextView) itemView.findViewById(R.id.tv_feature_details);
            tv_location_arraival = (TextView) itemView.findViewById(R.id.tv_location_arraival);
            tv_location_departure = (TextView) itemView.findViewById(R.id.tv_location_departure);
            tv_time_spent = (TextView) itemView.findViewById(R.id.tv_time_spent);
            tv_section = (TextView) itemView.findViewById(R.id.tv_section);

            lay_report = (LinearLayout) itemView.findViewById(R.id.lay_report);


        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
