package com.primesys.VehicalTracking.Activity.EmployeeAcivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Dto.EmpAttendanceDTO;
import com.primesys.VehicalTracking.Dto.LeaveApplicationDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LeaveApplicationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    EditText edit_comment;
    Button submit;
    private CalendarPickerView calendar;
    Context mContext=this;
    ArrayList SelectedDates=new ArrayList();
    private String TAG="Rquest";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private String[] strmonth;
    public static ArrayList<EmpAttendanceDTO> attendsncelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        findViewById();


        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");//Edit here depending on your requirements

        String cur_date = monthFormat.format(new Date());
        strmonth = cur_date.split("-");
       // tv_emp_label.setText("Attendance of " + getIntent().getExtras().
         //       getString("StudentName") + "  in " + strmonth[1] + " - " + strmonth[0]);

        GetEmpMonth_Attendance();

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Log.d("onDateSelected", "Selected time in millis: " +date);

                Log.d("onDateSelected", "Selected time in millis: " +date.getDay()+"-"+date.getMonth()+"-"+date.getYear());

            }

            @Override
            public void onDateUnselected(Date date) {
                Log.d("onDateUnselected", "Selected time in millis: " + calendar.getSelectedDates());

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (calendar.getSelectedDates().size()>0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//Edit here depending on your requirements
                    SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");//Edit here depending on your requirements

                    dateFormat.setLenient(false);
                    String currentDateandTime1 = dateFormat.format(calendar.getSelectedDates().get(0));
                    String toast = "Selected: " + calendar.getSelectedDates();
                    if (edit_comment.getText().length() > 0) {
                        for (int i = 0; i < calendar.getSelectedDates().size(); i++) {
                            String cur_date = monthFormat.format(calendar.getSelectedDates().get(i));
                            try {
                                String str[] = cur_date.split("-");

                                LeaveApplicationDTO obj = new LeaveApplicationDTO();
                                obj.setDay(str[2]);
                                obj.setMonth(str[1]);
                                obj.setYear(str[0]);
                                obj.setTime(dateFormat.format(calendar.getSelectedDates().get(i)));
                                obj.setUser_id(Common.userid);
                                obj.setComment(edit_comment.getText().toString());

                                SelectedDates.add(obj);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        SaveLeaveApplication(new Gson().toJson(SelectedDates));

                    } else {
                        Common.ShowSweetAlert(mContext, "Please enter comment in comment box.");
                    }
                    Log.e("LeaveApplicatio", "Selected time in millis: " + new Gson().toJson(SelectedDates));
                }else Common.ShowSweetAlert(mContext,"Please select one or more date for leave.");

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        Common.ShowSweetSucess(mContext,"Select single or multiple date for leave.");
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edit_comment= (EditText) findViewById(R.id.edit_comment);
        submit= (Button) findViewById(R.id.btn_leave_submit);
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

     /*   final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);*/
        final Calendar nextmonth = Calendar.getInstance();
        nextmonth.add(Calendar.MONTH, 2);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
     /*   for (int i = 0; i < 5; i++) {
          today.add(Calendar.DAY_OF_MONTH, 1);
          dates.add(today.getTime());
        }*/
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(new Date(), nextmonth.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE) //
                .withSelectedDates(dates);


    }

    private void SaveLeaveApplication(final String data) {

        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"ParentAPI/SaveEmpLeaveApplication",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseLeaveJSON(response);
                pDialog.hide();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                            Log.d("Error", error+"");

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("SelectedDates",data);

                Log.e("---------------","REq---SaveLeaveApplication-------"+params);
                return params;
            }

        };

        stringRequest.setTag(TAG);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    private void parseLeaveJSON(String response) {
        try {

            JSONObject rs = new JSONObject(response);

            if (rs.getString("error").equalsIgnoreCase("false")){
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Success");
                pDialog.setContentText(rs.getString("message"));
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                    }
                });
                pDialog.show();
            }else {
                Common.ShowSweetAlert(mContext,rs.getString("message"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {


        }

    }



    private void GetEmpMonth_Attendance() {
        reuestQueue = Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialoga = Common.ShowProgress(mContext);
        pDialoga.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "ParentAPI/GetEmpMonth_Attendance", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseJSON(response);
                pDialoga.hide();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialoga.hide();
                        if (error != null)
                            Log.d("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

              /*  params.put("Student_Id", Common.userid);
                params.put("Month", s_month+"");
                params.put("Year", year+"");*/

                try {
                    params.put("Student_Id", Common.Emp_id);
                    params.put("Month", strmonth[1]);
                    params.put("Year", strmonth[0]);
                    params.put("user_id", Common.userid);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("REq---GetEmpMonth_Attendance-------" + params);
                return params;
            }

        };

        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);

        // Adding request to request queue
        reuestQueue.add(stringRequest);

    }

    private void parseJSON(String response) {
        Log.e("parseUpdateJSON Respo", response);
        try {


            JSONArray jarray = new JSONArray(response);
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject rs = (JSONObject) jarray.get(i);
                EmpAttendanceDTO obj = new EmpAttendanceDTO();

                obj.setEmp_id(rs.getString("emp_id"));
                obj.setAttendance_id(rs.getString("attendance_id") + "");
                obj.setAtt_type(rs.getString("att_type") + "");
                obj.setType(rs.getString("type") + "");
                obj.setComment(rs.getString("comment") + "");
                obj.setDay(rs.getString("day") + "");
                obj.setMonth(rs.getString("month") + "");
                obj.setYear(rs.getString("year") + "");
                obj.setIs_grant(rs.getString("is_grant") + "");
                obj.setCreated_by(rs.getString("created_by") + "");
                obj.setCreated_at(rs.getString("created_at") + "");
                attendsncelist.add(obj);

            }

            if (attendsncelist.size() > 0) {
                ShowAttenadnceStatus(attendsncelist);

            } else Common.ShowSweetAlert(mContext, "For this month did not found any attendance.");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


        }

    }

    private void ShowAttenadnceStatus(ArrayList<EmpAttendanceDTO> attendsncelist) {
        calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new EmpdayDecorator()));
    }
}
