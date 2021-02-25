package com.primesys.VehicalTracking.Activity.EmployeeAcivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.primesys.VehicalTracking.Dto.DriverEmpTaskSheduledDTO;
import com.primesys.VehicalTracking.Dto.DriverTaskDayMainDTO;
import com.primesys.VehicalTracking.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class RecyclerExpandAdapter extends ExpandableRecyclerViewAdapter<DayMainViewHolder, TaskChildHolder> {

    private Activity activity;

    public RecyclerExpandAdapter(Activity activity, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.activity = activity;
    }

    @Override
    public DayMainViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.group_view_holder, parent, false);

        return new DayMainViewHolder(view);
    }

    @Override
    public TaskChildHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_task_list, parent, false);

        return new TaskChildHolder(view);
    }

    @Override
    public void onBindChildViewHolder(TaskChildHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final DriverEmpTaskSheduledDTO phone = ((DriverTaskDayMainDTO) group).getItems().get(childIndex);
        holder.onBind(phone,group);
    }

    @Override
    public void onBindGroupViewHolder(DayMainViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupName(group);
    }
}
