package com.primesys.VehicalTracking.MyAdpter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Dto.DriverEmpTaskSheduledDTO;
import com.primesys.VehicalTracking.R;

import java.util.ArrayList;


/**
 * Created by pt002 on 5/2/18.
 */


public class TaskListReportAdpter extends RecyclerView.Adapter<TaskListReportAdpter.ViewHolder>{

    private SharedPreferences.Editor editor;
    public ArrayList<DriverEmpTaskSheduledDTO> tasklist=new ArrayList<DriverEmpTaskSheduledDTO>();
    private int rowLayout;
    private static Context mContext;
    public   int position=0;

    public SharedPreferences sharedPreferences;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    String DeviceSimNo="";
    private String TAG="ADPetr";
    private String speed="";
    private int TAG_CODE_PERMISSION_SMS=400;



    public TaskListReportAdpter(ArrayList<DriverEmpTaskSheduledDTO> triplist, int row_triplist, Context context) {
        super();
        this.tasklist = triplist;
        this.rowLayout = row_triplist;
        this.mContext = context;
        sharedPreferences=mContext.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    @Override
    public TaskListReportAdpter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(rowLayout, viewGroup, false);

        System.out.println("  1  "+new TaskListReportAdpter.ViewHolder(v)+"   2  "+viewGroup+"   3  "+rowLayout);


        return new TaskListReportAdpter.ViewHolder(v);
    }




    @Override
    public void onBindViewHolder(TaskListReportAdpter.ViewHolder holder, int position) {
        try {

            DriverEmpTaskSheduledDTO item = tasklist.get(position);

            holder.start_time.setText("Pickup Time : " + item.getStartTime());

            if (item.getAddress() != null&&item.getAddress().equalsIgnoreCase("")|| item.getAddress().equalsIgnoreCase("null") )
                holder.start_address.setText("Start Address : " + "Address not found");
            else holder.start_address.setText("Start Address : " + item.getAddress());
            holder.vehicale_name.setText("Vehicale Name : "+item.getCar_name());
            holder.itemView.setTag(position);
            holder.tv_route.setText("Route : "+item.getRoute());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return tasklist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView start_time,start_address,vehicale_name,tv_route;
                //stoppage,avg_speed,max_speed,end_time,end_address,totalkm;

        public ViewHolder(View itemView)  {
            super(itemView);
            start_time = (TextView)itemView.findViewById(R.id.tv_start_time);
            start_address = (TextView)itemView.findViewById(R.id.tv_start_address);
            vehicale_name = (TextView)itemView.findViewById(R.id.tv_vehicale_name);
            tv_route = (TextView)itemView.findViewById(R.id.tv_route);

           /* avg_speed = (TextView)itemView.findViewById(R.id.tv_avg_speed);
            max_speed = (TextView)itemView.findViewById(R.id.tv_max_speed);
            end_time = (TextView)itemView.findViewById(R.id.tv_stop_time);
            end_address = (TextView)itemView.findViewById(R.id.tv_stop_address);
            totalkm = (TextView)itemView.findViewById(R.id.tv_totalkm);*/




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }

            });


        }


    }




}
