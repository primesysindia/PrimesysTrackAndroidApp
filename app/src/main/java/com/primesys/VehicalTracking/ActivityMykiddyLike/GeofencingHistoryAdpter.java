package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment.Mine_ModuleFragment;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;


/**
 * Created by Rupesh Patil on 4/4/17.
 */

public class GeofencingHistoryAdpter extends RecyclerView.Adapter<GeofencingHistoryAdpter.ViewHolder> {
    private final int layoutResourceId;
    Context context;
        ArrayList<GeofenceDTO> data=null;
        public static String TrackPesonImage=null;
        ImageLoader imageLoader;
        Bitmap bitmap;

    public GeofencingHistoryAdpter(Context context, int layoutResourceId,
                                   ArrayList<GeofenceDTO> data) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data=data;
    }

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                        View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(layoutResourceId, viewGroup, false);
                        ViewHolder viewHolder = new ViewHolder(v);
                        return viewHolder;
                        }




                @Override
                public void onBindViewHolder(ViewHolder viewHolder, int position) {


                    final GeofenceDTO msgdata = data.get(position);

                    viewHolder.txt_device.setText("Device Name : "+ Mine_ModuleFragment.CurrentDeviceSelect.getName());
                    viewHolder.txt_fence_address.setText("Address : "+msgdata.getAddress());
                    viewHolder.txt_fence_status.setText("Status : "+msgdata.getStatusout());
                    viewHolder.txt_fence_time.setText("Time : "+ Common.getDateCurrentTimeZone(msgdata.getTimestamp()));




                }

                @Override
                public int getItemCount() {

                        return data.size();
                        }

                public class ViewHolder extends RecyclerView.ViewHolder  {

                    public TextView txt_device,txt_fence_address,txt_fence_status,txt_fence_time;

                    public ViewHolder(View itemView) {
                        super(itemView);
                        txt_device = (TextView) itemView.findViewById(R.id.txt_device);
                        txt_fence_address = (TextView) itemView.findViewById(R.id.txt_fence_address);
                        txt_fence_status = (TextView) itemView.findViewById(R.id.txt_fence_status);
                        txt_fence_time = (TextView) itemView.findViewById(R.id.txt_fence_time);



                    }
                }

    }

