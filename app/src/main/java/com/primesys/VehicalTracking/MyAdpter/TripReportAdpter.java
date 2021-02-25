package com.primesys.VehicalTracking.MyAdpter;


/**
 * Created by pt002 on 30/5/17.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Activity.TripReportShow;
import com.primesys.VehicalTracking.Dto.TripInfoDto;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;



    public class TripReportAdpter extends RecyclerView.Adapter<com.primesys.VehicalTracking.MyAdpter.TripReportAdpter.ViewHolder>{

        private SharedPreferences.Editor editor;
        public ArrayList<TripInfoDto> triplist=new ArrayList<TripInfoDto>();
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
        public static CountDownTimer countdowntimer;


        public TripReportAdpter(ArrayList<TripInfoDto> triplist, int row_triplist, Context context) {
            super();
            this.triplist = triplist;
            this.rowLayout = row_triplist;
            this.mContext = context;
            sharedPreferences=mContext.getSharedPreferences("User_data",Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }



        @Override
        public com.primesys.VehicalTracking.MyAdpter.TripReportAdpter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(mContext).inflate(rowLayout, viewGroup, false);

            System.out.println("  1  "+new com.primesys.VehicalTracking.MyAdpter.TripReportAdpter.ViewHolder(v)+"   2  "+viewGroup+"   3  "+rowLayout);


            return new com.primesys.VehicalTracking.MyAdpter.TripReportAdpter.ViewHolder(v);
        }




        @Override
        public void onBindViewHolder(com.primesys.VehicalTracking.MyAdpter.TripReportAdpter.ViewHolder holder, int position) {
            try {

                TripInfoDto item = triplist.get(position);
                TripInfoDto prev_item = null;

                if (position > 0) {
                    prev_item = triplist.get(position - 1);
                }
                holder.start_time.setText("Start Time : " + Common.getDateCurrentTimeZone(Long.parseLong(item.getSrctimestamp())));

                if (item.getSrc_adress() != null&&item.getSrc_adress().equalsIgnoreCase("")|| item.getSrc_adress().equalsIgnoreCase("null") )
                    holder.start_address.setText("Start Address : " + "Address not found");
                else holder.start_address.setText("Start Address : " + item.getSrc_adress());

                if (position > 0 && prev_item != null) {
                    holder.stoppage.setText("Stoppage Min : " + ((Long.parseLong(item.getSrctimestamp()) - Long.parseLong(prev_item.getDesttimestamp())) / 60) + "");

                } else {
                    holder.stoppage.setText("Stoppage Min : " + "--");
                }
                holder.avg_speed.setText("Average Speed : " + item.getAvgspeed());
                holder.max_speed.setText("Max Speed : " + item.getMaxspeed());
                holder.end_time.setText("End Time : " + Common.getDateCurrentTimeZone(Long.parseLong(item.getDesttimestamp())));

                if (item.getDest_address() != null&&item.getDest_address().equalsIgnoreCase("") || item.getDest_address().equalsIgnoreCase("null"))
                    holder.end_address.setText("End Address : " + "Address not found");
                else holder.end_address.setText("End Address : " + item.getDest_address());

                  if (TripReportShow.isKmEnable)
                    holder.totalkm.setText("Travelled Distance : " + String.format("%.2f", Double.parseDouble(item.getTotalkm())) + " Km");
                  else
                      holder.totalkm.setText("Travelled Distance : " + String.format("%.2f", Double.parseDouble(item.getTotalkm())*Common.miles_multiplyer) + " Miles");


                holder.itemView.setTag(position);

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return triplist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder  {
            public TextView start_time,start_address,stoppage,avg_speed,max_speed,end_time,end_address,totalkm;

            public ViewHolder(View itemView)  {
                super(itemView);
                start_time = (TextView)itemView.findViewById(R.id.tv_start_time);
                start_address = (TextView)itemView.findViewById(R.id.tv_start_address);
                stoppage = (TextView)itemView.findViewById(R.id.tv_stoppage_min);
                avg_speed = (TextView)itemView.findViewById(R.id.tv_avg_speed);
                max_speed = (TextView)itemView.findViewById(R.id.tv_max_speed);
                end_time = (TextView)itemView.findViewById(R.id.tv_stop_time);
                end_address = (TextView)itemView.findViewById(R.id.tv_stop_address);
                totalkm = (TextView)itemView.findViewById(R.id.tv_totalkm);




                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }

                });


            }


        }




    }
