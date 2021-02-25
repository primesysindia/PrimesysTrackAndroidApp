package com.primesys.VehicalTracking.Activity.EmployeeAcivity.SimpleEmployee;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.primesys.VehicalTracking.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DayMainViewHolder extends GroupViewHolder {

    private TextView day_name,tv_task_no,tv_task_date;
    static Calendar cal = Calendar.getInstance();
    Date curr_time;
    SimpleDateFormat dformat= new SimpleDateFormat("yyyy-MM-dd");

    public DayMainViewHolder(View itemView) {
        super(itemView);

        day_name = (TextView) itemView.findViewById(R.id.day_name);
        tv_task_no = (TextView) itemView.findViewById(R.id.tv_task_no);
        tv_task_date= (TextView)itemView.findViewById(R.id.tv_task_date);

    }

    @Override
    public void expand() {
        day_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        Log.i("Adapter", "expand");
    }

    @Override
    public void collapse() {
        Log.i("Adapter", "collapse");
        day_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);

    }

    public void setGroupName(ExpandableGroup group) {
        day_name.setText(group.getTitle());

        if (group.getItemCount()>0)
        tv_task_no.setText(group.getItemCount()+"");
        else {
            tv_task_no.setText("OFF");

        }

        if (group.getTitle().equalsIgnoreCase("Monday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 0);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }else if (group.getTitle().equalsIgnoreCase("Tuesday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 1);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }else if (group.getTitle().equalsIgnoreCase("Wednesday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 2);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }else if (group.getTitle().equalsIgnoreCase("Thursday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 3);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }else if (group.getTitle().equalsIgnoreCase("Friday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 4);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }else if (group.getTitle().equalsIgnoreCase("Saturday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 5);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }else if (group.getTitle().equalsIgnoreCase("Sunday")){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.add(Calendar.DATE, 6);
            tv_task_date.setText(dformat.format(cal.getTime())+"");
        }
    }
}
