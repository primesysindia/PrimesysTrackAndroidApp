package com.primesys.VehicalTracking.Guest.Adpter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by root on 30/12/16.
 */

    public class GVhehicalAcclistAdpter extends RecyclerView.Adapter<GVhehicalAcclistAdpter.ViewHolder>{

        private SharedPreferences.Editor editor;
        public List<SmsNotificationDTO> smslist=new ArrayList<SmsNotificationDTO>();
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


        public GVhehicalAcclistAdpter(ArrayList<SmsNotificationDTO> smslistdata, int row_smslist, Context context) {
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
            SmsNotificationDTO item = smslist.get(position);
            //  holder.itemDesc.setText(item.get());
            holder.acctype.setText(item.getNotify_Title());
            holder.time.setText(item.getDate());


            Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
            holder.acctype.setTypeface(typeFace);
            holder.time.setTypeface(typeFace);

            holder.itemView.setTag(position);
        }


        @Override
        public int getItemCount() {
            return smslist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder  {
            public TextView acctype,time;

            public ViewHolder(View itemView)  {
                super(itemView);
                acctype = (TextView)itemView.findViewById(R.id.acctype);
                time = (TextView)itemView.findViewById(R.id.timeacc);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }

                });


            }


        }




    }
