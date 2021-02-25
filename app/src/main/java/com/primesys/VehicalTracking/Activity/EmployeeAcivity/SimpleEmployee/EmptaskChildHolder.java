package com.primesys.VehicalTracking.Activity.EmployeeAcivity.SimpleEmployee;

import android.view.View;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.DriverEmpTaskSheduledDTO;
import com.primesys.VehicalTracking.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;


public class EmptaskChildHolder extends ChildViewHolder {

    private TextView phoneName;

    public EmptaskChildHolder(View itemView) {
        super(itemView);

        phoneName = (TextView) itemView.findViewById(R.id.phone_name);
    }

    public void onBind(DriverEmpTaskSheduledDTO task, ExpandableGroup group) {
        phoneName.setText(task.getAddress());

        phoneName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0);

    }
}
