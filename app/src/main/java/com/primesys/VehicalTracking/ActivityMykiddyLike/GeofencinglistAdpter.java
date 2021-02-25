package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;

import java.util.ArrayList;


/**
 * Created by Rupesh Patil on 4/4/17.
 */

public class GeofencinglistAdpter extends RecyclerView.Adapter<GeofencinglistAdpter.ViewHolder> {
    private final int layoutResourceId;
    Context context;
        ArrayList<GeofenceDTO> data=null;
        public static String TrackPesonImage=null;
        ImageLoader imageLoader;
        Bitmap bitmap;

    public GeofencinglistAdpter(Context context, int layoutResourceId,
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


                    final GeofenceDTO msg = data.get(position);
                    // Log.e("student",s.getFirstName());



                    //	ImageView delete = (ImageView) convertView.findViewById(R.id.geodelete);

                    viewHolder.name.setText("\t\t"+""+"\t" + msg.getGeoName());



                }

                @Override
                public int getItemCount() {

                        return data.size();
                        }

                public class ViewHolder extends RecyclerView.ViewHolder  {

                    public TextView name;
                    public CircularNetworkImageView proimg;

                    public ViewHolder(View itemView) {
                        super(itemView);
                        name = (TextView) itemView.findViewById(R.id.geofencename);



                    }
                }

    }

