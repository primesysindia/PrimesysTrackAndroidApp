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
import com.primesys.VehicalTracking.Dto.RailDeviceInfoDto;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeviceOnoffReportAdpter extends RecyclerView.Adapter<DeviceOnoffReportAdpter.ViewHolder>{

    private SharedPreferences.Editor editor;
    public List<RailDeviceInfoDto> dataList=new ArrayList<RailDeviceInfoDto>();
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

    public DeviceOnoffReportAdpter(Context mContext, int card_device_current_report, ArrayList<RailDeviceInfoDto> deviceInfoList) {
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
          //  RailDeviceInfoDto item = dataList.get(position);
            //  holder.itemDesc.setText(item.get());
            holder.tv_device_name.setText("Name : "+dataList.get(position).getName());
            Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
            holder.tv_device_name.setTypeface(typeFace);
            if (dataList.get(position).getDeviceOnStatus()==0){
                holder.tv_device_status.setText("Device Status : Off");
            }
            else {
                holder.lay_report.setBackgroundColor(mContext.getResources().getColor(R.color.green));

                holder.tv_device_status.setText("Device Status : On");
                holder.tv_lasttime.setVisibility(View.VISIBLE);
                holder.tv_location.setVisibility(View.VISIBLE);
                if (dataList.get(position).getTime()!=null&&!dataList.get(position).getTime().equalsIgnoreCase("null"))
                    holder.tv_lasttime.setText("Time : "+Common.getDateCurrentTimeZone(Long.parseLong(dataList.get(position).getTime())));

                if (dataList.get(position).getDeviceOnStatus()==1){
                    holder.tv_location.setText("Address : "+Common.getStringAddress(mContext,Double.parseDouble(dataList.get(position).getLat()), Double.parseDouble(dataList.get(position).getLang())));

                }
                /*if (dataList.get(position).getRailFeatureDto()!=null){
                    StringBuilder sb = new StringBuilder("");
                    sb.append("Section :"+dataList.get(position).getRailFeatureDto().getSection());
                    sb.append("\n"+(String.format("Location: %.2f",dataList.get(position).getRailFeatureDto().getKiloMeter() + ((dataList.get(position).getRailFeatureDto().getDistance()))* 0.001)) + " Km ");
                    sb.append("\n "+String.format("And far  %.2f", dataList.get(position).getRailFeatureDto().getNearByDistance())+ " meter from ");
                    sb.append("\n"+dataList.get(position).getRailFeatureDto().getFeatureDetail());
                    holder.tv_location.setText(sb);
                }else {
                    holder.tv_location.setText("Device is not on railway track.");

                }*/

            }

            holder.tv_device_status.setTypeface(typeFace);
            holder.tv_lasttime.setTypeface(typeFace);
            holder.tv_location.setTypeface(typeFace);


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
        public TextView tv_device_name, tv_device_status, tv_lasttime, tv_location;

        LinearLayout lay_report;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_device_name = (TextView) itemView.findViewById(R.id.tv_device_name);
            tv_device_status = (TextView) itemView.findViewById(R.id.tv_device_status);
            tv_lasttime = (TextView) itemView.findViewById(R.id.tv_lasttime);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_lasttime.setVisibility(View.GONE);
            tv_location.setVisibility(View.GONE);
            lay_report = (LinearLayout) itemView.findViewById(R.id.lay_report);

        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
