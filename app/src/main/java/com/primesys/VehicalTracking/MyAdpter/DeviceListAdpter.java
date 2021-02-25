/*
package com.primesys.VehicalTracking.MyAdpter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

*/
/**
 * Created by root on 30/12/16.
 *//*


    public class DeviceListAdpter extends RecyclerView.Adapter<DeviceListAdpter.ViewHolder>
    {

        private SharedPreferences.Editor editor;
        public List<DeviceDataDTO> datalist=new ArrayList<DeviceDataDTO>();
        private int rowLayout;
        private static Context mContext;
        public   int position=0;
        public SharedPreferences sharedPreferences;
        public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
        public static Boolean pinexist=false;
        public static String Enginepin="";
        public Dialog createpindialog;
        public static SweetAlertDialog pDialogmain;
        public static CountDownTimer countdowntimer;


        public DeviceListAdpter(ArrayList<DeviceDataDTO> listdata, int row_list, Context context) {
            super();
            this.datalist = listdata;
            this.rowLayout = row_list;
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
            DeviceDataDTO item = datalist.get(position);
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
            return datalist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder  {
            public TextView txtchild,time;
            CircularNetworkImageView imgchild;
            public ViewHolder(View itemView)  {
                super(itemView);
                 LinearLayout lay = (LinearLayout) itemView.findViewById(R.id.lay_main);
                  imgchild = (CircularNetworkImageView) itemView.findViewById(R.id.img_child);
                txtchild = (TextView) itemView.findViewById(R.id.txt_child);
                LinearLayout lay_main = (LinearLayout) itemView.findViewById(R.id.lay_main);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }

                });


            }


        }




    }
*/
