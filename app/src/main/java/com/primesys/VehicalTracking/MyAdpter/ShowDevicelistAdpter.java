package com.primesys.VehicalTracking.MyAdpter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.R;

import java.util.ArrayList;

public class ShowDevicelistAdpter extends RecyclerView.Adapter<ShowDevicelistAdpter.ViewHolder>{

    private final Context mContext;
    private SharedPreferences.Editor editor;
    public ArrayList<DeviceDataDTO> datalist= new ArrayList<>();
    private int rowLayout;


    public ShowDevicelistAdpter(Context context, int row_smslist, ArrayList<DeviceDataDTO> smslistdata) {
        super();
        this.datalist = smslistdata;
        this.rowLayout = row_smslist;
        this.mContext = context;

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
        holder.name.setText(item.getName());
        holder.imei.setText(item.getImei_no());
        holder.exp_date.setText(item.getExpiary_date());


        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,imei,exp_date;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_name);
            imei = (TextView) itemView.findViewById(R.id.text_imei);
            exp_date = (TextView) itemView.findViewById(R.id.text_exp_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }
}
