package com.primesys.VehicalTracking.Activity.EmployeeAcivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ShowAllEmployeeActivity extends Activity {

    private ListView listStudent;
    Context mContext=this;
    private ArrayList<DeviceDataDTO> childlist;

    StudentListAdpter padapter;
    public static String StudentId="0",StudentName,DeviceImieNo;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    public static Date Selected_Date;
    private Button d_cancel;
    private Button emp_holiday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_employee);
        emp_holiday=(Button) findViewById(R.id.emp_holiday_fab);
        listStudent = (ListView) findViewById(R.id.student_list);
        childlist= PrimesysTrack.mDbHelper.Show_Device_list();
        d_cancel=(Button)findViewById(R.id.d_cancel);
        d_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        padapter = new StudentListAdpter(mContext, R.layout.fragment_mapsidebar, childlist);
        listStudent.setAdapter(padapter);

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                StudentId = childlist.get(position).getId();
                StudentName= childlist.get(position).getName();
                DeviceImieNo=childlist.get(position).getImei_no();

                Log.e("===========",""+StudentId+"   "+DeviceImieNo);
                Showcalender_month(StudentId,StudentName);


            }
        });
        emp_holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent holiday=new Intent(mContext,EmpHolidayActivity.class);
                startActivity(holiday);
            }
        });

    }

    private void Showcalender_month(String studentId, String studentName) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        long dv = Long.valueOf(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"))*1000;
                         Selected_Date = new Date(dv);
                   //     currentdate  = new SimpleDateFormat("dd-MMM-yy").format(df);

                       // GetDailyMilageReport(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));
                        Intent intent = new Intent(mContext, ShowEmpAttendanceActivity.class);
                        intent.putExtra("StudentId", StudentId);
                        intent.putExtra("StudentName", StudentName);

                        startActivity(intent);


                    }
                }, year, month, day);

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -6);
        Date min = cal.getTime();
        cal.add(Calendar.MONTH, 6);

        Date max = cal.getTime();
        dpd.getDatePicker().setMaxDate(max.getTime());
        dpd.getDatePicker().setMinDate(min.getTime());
        dpd.show();
    }

}
