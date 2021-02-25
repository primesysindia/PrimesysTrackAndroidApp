package com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeoFencingHomeNew;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Rupesh Patil on 4/4/17.
 */

public class ShowfenceStudentAdpter extends RecyclerView.Adapter<ShowfenceStudentAdpter.ViewHolder> {

    int position;
    public static DeviceDataDTO user1;
    ArrayList<DeviceDataDTO> datal;
    public static Boolean trackInfo=false;
    public static ClickListener clickListener;

    Context context;
    ImageLoader imageLoader;
    int layoutResourceId;
    public ShowfenceStudentAdpter(Context context, int layoutResourceId,
                                  ArrayList<DeviceDataDTO> data) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.datal = data;
        trackInfo=false;
        GeoFencingHomeNew.GeoStudentId=Integer.parseInt(data.get(0).getId());

    }
    @Override
    public ShowfenceStudentAdpter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutResourceId, viewGroup, false);
        ShowfenceStudentAdpter.ViewHolder viewHolder = new ShowfenceStudentAdpter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceDataDTO gp=datal.get(position);

        holder.txtchild.setText(gp.getName());
        holder.imgchild.setTag(position);
        Picasso.with(context).
                load(Common.Relative_URL+gp.getPath().replaceAll(" ","%20"))
                .placeholder(R.drawable.student) //this is optional the image to display while the url image is downloading
                .error(R.drawable.student)
                .into(holder.imgchild)
        ;
    }

    @Override
    public int getItemCount() {

        return datal.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView txtchild;
        LinearLayout lay;
        CircularNetworkImageView imgchild;

        public ViewHolder(View itemView) {
            super(itemView);
            lay =(LinearLayout)itemView.findViewById(R.id.lay_main);
              imgchild=(CircularNetworkImageView)itemView.findViewById(R.id.img_child);
            txtchild=(TextView)itemView.findViewById(R.id.txt_child);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }


    }
    public void setOnItemClickListener(ClickListener clickListener) {
        clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

