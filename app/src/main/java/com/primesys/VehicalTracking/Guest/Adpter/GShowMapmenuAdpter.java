package com.primesys.VehicalTracking.Guest.Adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircleTransform;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/4/16.
 */

public class GShowMapmenuAdpter extends RecyclerView.Adapter<GShowMapmenuAdpter.MyViewHolder> {

    private final Context context;
    private List<DeviceDataDTO> menuList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public com.primesys.VehicalTracking.Utility.CircularNetworkImageView img;
        public MyViewHolder(View view) {
            super(view);
            img = (com.primesys.VehicalTracking.Utility.CircularNetworkImageView) view.findViewById(R.id.img_menu);
            title = (TextView) view.findViewById(R.id.txt_menu);
        }
    }


    public GShowMapmenuAdpter(Context trackContext, ArrayList<DeviceDataDTO> moviesList) {
        this.menuList = moviesList;
        this.context=trackContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mapmenubar, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DeviceDataDTO menu = menuList.get(position);
        holder.title.setText(menu.getName());
        Picasso.with(context).
                load(Common.Relative_URL+menu.getPath().replaceAll(" ","%20")).transform(new CircleTransform())
                .placeholder(R.drawable.student) //this is optional the image to display while the url image is downloading
                .error(R.drawable.student)
                .into(holder.img)
        ;    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}