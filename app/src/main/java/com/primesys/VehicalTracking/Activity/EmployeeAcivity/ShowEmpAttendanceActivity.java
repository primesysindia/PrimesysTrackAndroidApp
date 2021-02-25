package com.primesys.VehicalTracking.Activity.EmployeeAcivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.primesys.VehicalTracking.Dto.EmpDayUpdateStatusDTO;
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

public class ShowEmpAttendanceActivity extends AppCompatActivity {

    Context mContext = this;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG = "Rquest";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    public static ArrayList<EmpAttendanceDTO> attendsncelist = new ArrayList<>();
    String Student_Id = "0";
    ColorDrawable blue;
    TextView tv_emp_label;
    ColorDrawable green, yellow;
    public String[] strmonth;
    private CalendarPickerView calendar;
    Button btn_grant_leave, btn_makeabsent, btn_make_present;
    Date minDate,maxDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_emp_attendance);
        findViewById();
        Student_Id = getIntent().getExtras().getString("StudentId");

        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");//Edit here depending on your requirements

        String cur_date = monthFormat.format(ShowAllEmployeeActivity.Selected_Date);
        strmonth = cur_date.split("-");
        tv_emp_label.setText("Attendance of " + getIntent().getExtras().
                getString("StudentName") + "  in " + strmonth[1] + " - " + strmonth[0]);


        Calendar mincal = Calendar.getInstance();
        mincal.setTime(ShowAllEmployeeActivity.Selected_Date);
        Log.e("COunt-----min--", "--" + -mincal.get(Calendar.DAY_OF_MONTH));
        mincal.add(Calendar.DATE, -(mincal.get(Calendar.DAY_OF_MONTH) - 1));
         minDate = mincal.getTime();

        // Max date is next 7 days
        Calendar maxcal = Calendar.getInstance();
        maxcal.setTime(ShowAllEmployeeActivity.Selected_Date);
        Log.e("Count-----max--", "--" + (maxcal.getActualMaximum(Calendar.DATE) - maxcal.get(Calendar.DAY_OF_MONTH)));
        maxcal.add(Calendar.DATE, maxcal.getActualMaximum(Calendar.DATE) - maxcal.get(Calendar.DAY_OF_MONTH));
         maxDate = maxcal.getTime();
        Log.e("Mindate-----min--", "--" + minDate);
        Log.e("Maxdate-----max--", "--" + maxDate);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.init(minDate, maxDate) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE); //
        //  calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new dayDecorator()));

        GetEmpMonth_Attendance();

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {


            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });


        btn_grant_leave.setOnClickListener(new View.OnClickListener() {
            public ArrayList SelectedDates = new ArrayList();

            @Override
            public void onClick(View v) {

                if (calendar.getSelectedDates().size() > 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//Edit here depending on your requirements
                    SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");//Edit here depending on your requirements

                    dateFormat.setLenient(false);
                    String currentDateandTime1 = dateFormat.format(calendar.getSelectedDates().get(0));
                    String toast = "Selected: " + calendar.getSelectedDates();
                    for (int i = 0; i < calendar.getSelectedDates().size(); i++) {
                        String cur_date = monthFormat.format(calendar.getSelectedDates().get(i));
                        try {
                            String str[] = cur_date.split("-");

                            EmpDayUpdateStatusDTO obj = new EmpDayUpdateStatusDTO();
                            obj.setDay(str[2]);
                            obj.setMonth(str[1]);
                            obj.setYear(str[0]);
                            obj.setTime(dateFormat.format(calendar.getSelectedDates().get(i)));
                            obj.setEmp_id(Student_Id);
                            obj.setUser_id(Common.userid);

                            obj.setComment("");
                          /*  obj.setAtt_day_status("4");
                            obj.setAtt_day_status_name("Leave");*/

                            SelectedDates.add(obj);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Confirmation!");
                    pDialog.setContentText("Are you sure to grant leave for selected dates. ");
                    pDialog.setCancelable(true);
                    pDialog.setCancelText("No");
                    pDialog.setConfirmText("Yes");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.hide();
                            GrantLeaveApplication(new Gson().toJson(SelectedDates));
                        }
                    });
                    pDialog.show();



                    Log.e("LeaveApplication", "Selected time in millis: " + new Gson().toJson(SelectedDates));
                } else
                    Common.ShowSweetAlert(mContext, "Please select one or more date for grant leave.");

            }

        });
        btn_makeabsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDaystaus("1","Absent");
            }
        });
        btn_make_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {updateDaystaus("2","Present");
            }
        });
    }


    public void updateDaystaus(String att_type, String typename) {
        final ArrayList SelectedDates = new ArrayList();

        if (calendar.getSelectedDates().size() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//Edit here depending on your requirements
            SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");//Edit here depending on your requirements

            dateFormat.setLenient(false);
            String currentDateandTime1 = dateFormat.format(calendar.getSelectedDates().get(0));
            String toast = "Selected: " + calendar.getSelectedDates();
            for (int i = 0; i < calendar.getSelectedDates().size(); i++) {
                String cur_date = monthFormat.format(calendar.getSelectedDates().get(i));
                try {
                    String str[] = cur_date.split("-");
                    EmpDayUpdateStatusDTO obj = new EmpDayUpdateStatusDTO();
                    obj.setDay(str[2]);
                    obj.setMonth(str[1]);
                    obj.setYear(str[0]);
                    obj.setTime(dateFormat.format(calendar.getSelectedDates().get(i)));
                    obj.setEmp_id(Student_Id);
                    obj.setUser_id(Common.userid);
                    obj.setAtt_day_status(att_type);
                    obj.setAtt_day_status_name(typename);

                    SelectedDates.add(obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            pDialog.setTitleText("Confirmation!");
            pDialog.setContentText("Are you sure to mark as "+typename+" for selected dates. ");
            pDialog.setCancelable(true);
            pDialog.setCancelText("No");
            pDialog.setConfirmText("Yes");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    pDialog.hide();

                    SaveEmpUpdateDayApplication(new Gson().toJson(SelectedDates));
                }
            });
            pDialog.show();

            Log.e("LeaveApplication", "Selected time in millis: " + new Gson().toJson(SelectedDates));
        } else Common.ShowSweetAlert(mContext, "Please select one or more date for grant leave.");

    }

    private void findViewById() {
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        tv_emp_label = (TextView) findViewById(R.id.tv_emp_label);
        btn_grant_leave = (Button) findViewById(R.id.btn_grant_leave);
        btn_makeabsent = (Button) findViewById(R.id.btn_absent);
        btn_make_present = (Button) findViewById(R.id.btn_present);


    }

    private void SaveEmpUpdateDayApplication(final String data) {
        reuestQueue = Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "ParentAPI/SaveEmpUpdateDayApplication", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseUpdateJSON(response);
                pDialog.hide();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.d("Error", error + "");

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("SelectedDates", data);

                Log.e("---------------", "REq---SaveLeaveApplication-------" + params);
                return params;
            }

        };

        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);

        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    private void parseUpdateJSON(String response) {
        try {

            JSONObject rs = new JSONObject(response);

            if (rs.getString("error").equalsIgnoreCase("false")) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Success");
                pDialog.setContentText(rs.getString("message"));
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialog.hide();
                        calendar.init(minDate, maxDate) //
                                .inMode(CalendarPickerView.SelectionMode.MULTIPLE).withSelectedDates(Collections.<Date>emptyList()); //
                        GetEmpMonth_Attendance();
                    }
                });
                pDialog.show();
            } else {
                Common.ShowSweetAlert(mContext, rs.getString("message"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


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
                    params.put("Student_Id", Student_Id);
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
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
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
        calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new dayDecorator()));
    }

    private void GrantLeaveApplication(final String data) {
        reuestQueue = Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "ParentAPI/GrantLeaveApplication", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseGrantJSON(response);
                pDialog.hide();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Log.d("Error", error + "");

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("SelectedDates", data);

                Log.e("---------------", "REq---SaveLeaveApplication-------" + params);
                return params;
            }

        };

        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);

        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    private void parseGrantJSON(String response) {
        Log.e("---------------", "Respo---parseGrantJSON-------" + response);

        try {

            JSONObject rs = new JSONObject(response);

            if (rs.getString("error").equalsIgnoreCase("false")) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Success");
                pDialog.setContentText(rs.getString("message"));
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialog.hide();
                        calendar.init(minDate, maxDate) //
                                .inMode(CalendarPickerView.SelectionMode.MULTIPLE).withSelectedDates(Collections.<Date>emptyList()); //
                                           GetEmpMonth_Attendance();
                    }
                });
                pDialog.show();
            } else {
                Common.ShowSweetAlert(mContext, rs.getString("message"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


        }

    }


}


