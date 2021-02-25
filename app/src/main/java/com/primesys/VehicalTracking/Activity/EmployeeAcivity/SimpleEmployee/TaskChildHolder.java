package com.primesys.VehicalTracking.Activity.EmployeeAcivity.SimpleEmployee;

import android.view.View;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.DriverEmpTaskSheduledDTO;
import com.primesys.VehicalTracking.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class TaskChildHolder extends ChildViewHolder {

    public TextView start_time,start_address,vehicale_name,tv_route,tv_task_date;

    public TaskChildHolder(View itemView) {
        super(itemView);
        start_time = (TextView)itemView.findViewById(R.id.tv_start_time);
        start_address = (TextView)itemView.findViewById(R.id.tv_start_address);
        vehicale_name = (TextView)itemView.findViewById(R.id.tv_vehicale_name);
        tv_route = (TextView)itemView.findViewById(R.id.tv_route);

        //phoneName = (TextView) itemView.findViewById(R.id.phone_name);
    }

    public void onBind(DriverEmpTaskSheduledDTO task, ExpandableGroup group) {
        start_time.setText("Pickup Time : " + task.getStartTime());
        if (task.getAddress() != null&&task.getAddress().equalsIgnoreCase("")|| task.getAddress().equalsIgnoreCase("null") )
            start_address.setText("Start Address : " + "Address not found");
        else start_address.setText("Start Address : " + task.getAddress());
        vehicale_name.setText("Vehicale Name : "+task.getCar_name());
        tv_route.setText("Route : "+task.getRoute());
    }
}
