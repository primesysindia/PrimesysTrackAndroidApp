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
import com.primesys.VehicalTracking.Dto.MonitorSOSPressDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeviceBatteryAdapter extends RecyclerView.Adapter<DeviceBatteryAdapter.ViewHolder>{

    private SharedPreferences.Editor editor;
    public List<MonitorSOSPressDTO> dataList=new ArrayList<MonitorSOSPressDTO>();
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

    public DeviceBatteryAdapter(Context mContext, int card_device_sos_report, ArrayList<MonitorSOSPressDTO> deviceInfoList) {
        super();
        this.dataList = deviceInfoList;
        this.rowLayout = card_device_sos_report;
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
          //  NearbyFeatureCodeDTO item = dataList.get(position);
            //  holder.itemDesc.setText(item.get());

            holder.tv_device_name.setText("Name : "+dataList.get(position).getGpsDeviceName());
            holder.tv_time.setText("Time :"+ Common.getDateCurrentTimeZone(Long.parseLong(dataList.get(position).getTime())));
            holder.tv_voltage_level.setText("Battery Level :"+dataList.get(position).getVoltageLevel());
            holder.tv_gsm_signal_strength.setText("GSM Signal Strength :"+dataList.get(position).getGsmSignalStrength());

            Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
            holder.tv_device_name.setTypeface(typeFace);
            holder.tv_time.setTypeface(typeFace);
            holder.tv_gsm_signal_strength.setTypeface(typeFace);
            holder.tv_voltage_level.setTypeface(typeFace);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_device_name, tv_time, tv_gsm_signal_strength, tv_voltage_level;

        LinearLayout lay_report;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_device_name = (TextView) itemView.findViewById(R.id.tv_device_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_lasttime);
            tv_gsm_signal_strength = (TextView) itemView.findViewById(R.id.tv_location);
            tv_voltage_level = (TextView) itemView.findViewById(R.id.tv_voltage_level);
            lay_report = (LinearLayout) itemView.findViewById(R.id.lay_report);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
