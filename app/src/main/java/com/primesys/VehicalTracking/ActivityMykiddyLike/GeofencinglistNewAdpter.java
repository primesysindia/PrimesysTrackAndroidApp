package com.primesys.VehicalTracking.ActivityMykiddyLike;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by pt002 on 27/9/17.
 */

public class GeofencinglistNewAdpter extends RecyclerView.Adapter<GeofencinglistNewAdpter.ViewHolder> {
    private final int layoutResourceId;
    Context context;
    ArrayList<GeofenceDTO> data=null;
    public static String TrackPesonImage=null;
    ImageLoader imageLoader;
    Bitmap bitmap;

    public GeofencinglistNewAdpter(Context context, int layoutResourceId,
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
   //{"event":"geofence_list","fences":[{"alert_type":{"alert":{"email":"False","push_to_app":"True","sms":"False"},"in_out":{"in":"False","out":"True"}},"descr":"ji","details":{"lan":"73.77158805727959","lat":"18.59095228795742","radius":"9700.0","type":"circle"},"enable":"True","id":"929224645","name":"johi","status":"N"}]}

        final GeofenceDTO msg = data.get(position);
        viewHolder.name.setText(msg.getGeoName());
        viewHolder.address.setText(Common.getStringAddress(context,Double.parseDouble(msg.getLat()),Double.parseDouble(msg.getLang())));
        viewHolder.radius.setText("Radius : "+msg.getDistance()+" M");



    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView name,address,radius;
        Button edit,delete;
        ToggleButton activemode;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_fence_name);
            address = (TextView) itemView.findViewById(R.id.txt_fence_address);
            radius = (TextView) itemView.findViewById(R.id.txt_fence_radius);
            edit = (Button) itemView.findViewById(R.id.btn_fence_edit);
            delete = (Button) itemView.findViewById(R.id.btn_fence_delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent edit = new Intent(context, GeofencingUpdatedrawCircle.class);
                    edit.putExtra("GeoObj", data.get(getAdapterPosition()));
                    context.startActivity(edit);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        showDeletedilog(data.get(getAdapterPosition()));


                }
            });

        }
    }


    protected void showDeletedilog(final GeofenceDTO geoObj) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete");
        builder.setMessage("Do you want delete " + geoObj.getGeoName() + " geofence ?.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //	DeleteGeofence(geoObj);
                LoginActivity.mClient.sendMessage(make_fencedelete_JSON(geoObj));

                dialog.dismiss();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    protected String make_fencedelete_JSON(GeofenceDTO geoObj) {
        System.out.println("make_fencedelete_JSON****"+new Gson().toJson(geoObj));
        String fencedelete_string="{}";
        try{
            JSONObject jo = new JSONObject();

            jo.put("event",Common.EV_GEOFENCE_UPDATE);
            jo.put("student_id", GeoFencingHomeNew.GeoStudentId);
            jo.put("fence_id",geoObj.getGeoID());

            JSONObject jodata = new JSONObject();

            jodata.put("name",geoObj.getGeoName());
            jodata.put("descr",geoObj.getDiscripation());
            jodata.put("enable",geoObj.getEnable());
            jodata.put("status","D");


            JSONObject joalert_type = new JSONObject();

            JSONObject in_out = new JSONObject();

            in_out.put("in",geoObj.getStatusin());
            in_out.put("out",geoObj.getStatusout());

            JSONObject joalert = new JSONObject();

            joalert.put("push_to_app",geoObj.getAlertapp());
            joalert.put("email",geoObj.getAlertemail());
            joalert.put("sms",geoObj.getSms());


            JSONObject jodetails = new JSONObject();

            jodetails.put("lat",geoObj.getLat());
            jodetails.put("lan",geoObj.getLang());
            jodetails.put("radius",geoObj.getDistance());
            jodetails.put("type","circle");

            joalert_type.put("in_out",in_out);
            joalert_type.put("alert",joalert);


            jodata.put("alert_type",joalert_type);
            jodata.put("details",jodetails);

            jo.put("data",jodata);

            //	fencenew_string=gson.toJson(jo).replace("\\", "");

            fencedelete_string=jo.toString();

            System.err.println("Request  delete Fence======== "+fencedelete_string);

        }
        catch(Exception e)
        {

            e.printStackTrace();
        }
        return fencedelete_string;


    }

    protected String make_fenceUpdate_JSON(GeofenceDTO geoObj) {

        System.out.println("make_fenceUpdate_JSON****"+new Gson().toJson(geoObj));
        String fenceupdate_string="{}";
        try{
            JSONObject jo = new JSONObject();

            jo.put("event",Common.EV_GEOFENCE_UPDATE);
            jo.put("student_id", GeoFencingHomeNew.GeoStudentId);
            jo.put("fence_id",geoObj.getGeoID());

            JSONObject jodata = new JSONObject();

            jodata.put("name",geoObj.getGeoName());
            jodata.put("descr",geoObj.getDiscripation());
            jodata.put("enable",geoObj.getEnable());
            jodata.put("status","N");


            JSONObject joalert_type = new JSONObject();

            JSONObject in_out = new JSONObject();

            in_out.put("in",geoObj.getStatusin());
            in_out.put("out",geoObj.getStatusout());

            JSONObject joalert = new JSONObject();

            joalert.put("push_to_app",geoObj.getAlertapp());
            joalert.put("email",geoObj.getAlertemail());
            joalert.put("sms",geoObj.getSms());


            JSONObject jodetails = new JSONObject();

            jodetails.put("lat",geoObj.getLat());
            jodetails.put("lan",geoObj.getLang());
            jodetails.put("radius",geoObj.getDistance());
            jodetails.put("type","circle");

            joalert_type.put("in_out",in_out);
            joalert_type.put("alert",joalert);


            jodata.put("alert_type",joalert_type);
            jodata.put("details",jodetails);

            jo.put("data",jodata);

            //	fencenew_string=gson.toJson(jo).replace("\\", "");

            fenceupdate_string=jo.toString();

            System.err.println("Request  Update Fence======== "+fenceupdate_string);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return fenceupdate_string;

    }


}

