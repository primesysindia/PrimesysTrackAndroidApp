package com.primesys.VehicalTracking.Activity.EmployeeAcivity;/*
package com.primesys.mitra.EmployeeAcivity;

*/
/**
 * Created by pt002 on 13/12/17.
 *//*


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.mitra.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import DTO.EmpAttendanceDTO;
import Utility.Common;
import Utility.MyCustomProgressDialog;


public class ShowEmpAttendanceActivity extends AppCompatActivity {

    private CaldroidFragment caldroidFragment;
    Context mContext= this;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG="Rquest";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    ArrayList<EmpAttendanceDTO> attendsncelist = new ArrayList<>();
    String Student_Id="0";
    ColorDrawable blue ;
    TextView tv_emp_label;
    ColorDrawable green, yellow;
    public String[] strmonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_emp_attendance);
        Student_Id=getIntent().getExtras().getString("StudentId");


        caldroidFragment = new CaldroidFragment();
        tv_emp_label= (TextView) findViewById(R.id.tv_emp_label);
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");//Edit here depending on your requirements

        String cur_date=monthFormat.format(ShowAllEmployeeActivity.Selected_Date);
        strmonth = cur_date.split("-");
        tv_emp_label.setText("Attendance of "+getIntent().getExtras().
                getString("StudentName")+"  in "+ strmonth[1]+" - "+strmonth[0]);
        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            cal.setTime(ShowAllEmployeeActivity.Selected_Date);
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            //      args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }


        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        // setCustomResourceForDates();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                //   Toast.makeText(getApplicationContext(), formatter.format(date),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                // Toast.makeText(getApplicationContext(), "Long click " + formatter.format(date),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    //   Toast.makeText(getApplicationContext(), "Caldroid view is created", Toast.LENGTH_SHORT).show();
                    //    setcalendermonth();
                    caldroidFragment.setShowNavigationArrows(false);
                    caldroidFragment.setEnableSwipe(false);

                    caldroidFragment.refreshView();
                    GetEmpMonth_Attendance();
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);

    }

 */
/*   private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, 0);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 10);
        Date greenDate = cal.getTime();



        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            Log.e("greenDate---","==="+greenDate);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
          //  caldroidFragment.setBackgroundDrawableForDate();
        }
    }*//*



    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(ShowAllEmployeeActivity.Selected_Date.getDate(), 0);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(ShowAllEmployeeActivity.Selected_Date.getDate(), cal.getActualMaximum(Calendar.DATE) - cal.get(Calendar.DAY_OF_MONTH));
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            green = new ColorDrawable(getResources().getColor(R.color.green));
            green = new ColorDrawable(Color.GREEN);
            yellow=new ColorDrawable(getResources().getColor(R.color.yellow));
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
            caldroidFragment.setMinDate(blueDate);
            caldroidFragment.setMaxDate(greenDate);

        }


    }


    private  void  setcalendermonth(){

        Calendar cal = Calendar.getInstance();

      */
/*  cal.setTime(ShowAllEmployeeActivity.Selected_Date);
        // Min date is last 7 days
        cal.add(cal.get(Calendar.DATE), -cal.get(Calendar.DAY_OF_MONTH));
        Date minDate = cal.getTime();*//*


      */
/*  // Max date is next 7 days
        cal.add(cal.get(Calendar.DATE), cal.getActualMaximum(Calendar.DATE) - cal.get(Calendar.DAY_OF_MONTH));
         Date maxDate = cal.getTime();
*//*



        for (int i=1;i<cal.getActualMaximum(Calendar.DATE);i++){


        }
      */
/*  // Set selected dates
        // From Date
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        Date fromDate = cal.getTime();

        // To Date
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 3);
        Date toDate = cal.getTime();

        // Set disabled dates
        ArrayList<Date> disabledDates = new ArrayList<Date>();
        for (int i = 5; i < 8; i++) {
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i);
            disabledDates.add(cal.getTime());
        }*//*


        // Customize
      */
/*  caldroidFragment.setMinDate(cal.getTime());
        caldroidFragment.setMaxDate(cal.getTime());*//*

*/
/*
        caldroidFragment.setMinDate(minDate);
        caldroidFragment.setMaxDate(maxDate);
      caldroidFragment.setBackgroundDrawableForDate(blue, minDate);
        caldroidFragment.setBackgroundDrawableForDate(blue, minDate);
*//*


        //   caldroidFragment.setDisableDates(disabledDates);
        //    caldroidFragment.setSelectedDates(fromDate, toDate);
        caldroidFragment.setShowNavigationArrows(false);
        caldroidFragment.setEnableSwipe(false);

        // caldroidFragment.refreshView();
    }

    */
/**
     * Save current states of the Caldroid here
     *//*

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

    }



    private void GetEmpMonth_Attendance() {
        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.JAVAURL+"ParentAPI/GetEmpMonth_Attendance",new Response.Listener<String>() {

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
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

              */
/*  params.put("Student_Id", Common.userid);
                params.put("Month", s_month+"");
                params.put("Year", year+"");*//*


                try {
                    params.put("Student_Id", Student_Id);
                    params.put("Month", strmonth[1]);
                    params.put("Year", strmonth[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                System.out.println("REq---GetEmpMonth_Attendance-------"+params);
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

    private void parseUpdateJSON(String response) {
        Log.e("parseUpdateJSON Respo", response);
        try {


            JSONArray jarray=new JSONArray(response);
            for (int i=0;i<jarray.length();i++){
                JSONObject rs = (JSONObject) jarray.get(i);
                EmpAttendanceDTO obj=new EmpAttendanceDTO();

                obj.setEmp_id(rs.getString("emp_id"));
                obj.setAttendance_id(rs.getString("attendance_id")+"");
                obj.setAtt_type(rs.getString("att_type")+"");
                obj.setType(rs.getString("type")+"");
                obj.setComment(rs.getString("comment")+"");
                obj.setDay(rs.getString("day")+"");
                obj.setMonth(rs.getString("month")+"");
                obj.setYear(rs.getString("year")+"");
                obj.setIs_grant(rs.getString("is_grant")+"");
                obj.setCreated_by(rs.getString("created_by")+"");
                obj.setCreated_at(rs.getString("created_at")+"");
                attendsncelist.add(obj);

            }

            if (attendsncelist.size()>0){
                ShowAttenadnceStatus(attendsncelist);

            }else Common.ShowSweetAlert(mContext,"For this month did not found any attendance.");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {


        }




    }

    private void ShowAttenadnceStatus(ArrayList<EmpAttendanceDTO> attendsncelist) {

        ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
        ColorDrawable green = new ColorDrawable(Color.GREEN);
        ColorDrawable red = new ColorDrawable(Color.RED);
        ColorDrawable yellow=new ColorDrawable(getResources().getColor(R.color.yellow));

        Calendar cal = Calendar.getInstance();
        cal.setTime(ShowAllEmployeeActivity.Selected_Date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int myMonth=cal.get(Calendar.MONTH);

        Log.e("ShowAttenadnce-", myMonth+"----"+blue+"---"+yellow);
        caldroidFragment.setBackgroundDrawableForDate(yellow, ShowAllEmployeeActivity.Selected_Date);

        while (myMonth==cal.get(Calendar.MONTH)) {
            for (EmpAttendanceDTO obj:attendsncelist) {
                Log.e("-----myMonth-------", cal.getTime().getDate()+"--"+obj.getDay());

                if (cal.getTime().getDay()!=0&&Integer.parseInt(obj.getDay())==cal.getTime().getDate()){

                    Log.e("------------", cal.getTime().getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                    if (obj.getAtt_type().equalsIgnoreCase("2")){
                        caldroidFragment.setBackgroundDrawableForDate(green, cal.getTime());
                        caldroidFragment.refreshView();
                        Log.e("----1111111111--------", cal.getTime().getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                    }
                    else if (obj.getAtt_type().equalsIgnoreCase("1"))
                    {
                        caldroidFragment.setBackgroundDrawableForDate(blue, cal.getTime());
                        Log.e("----2222222222--------", cal.getTime().getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());
                        caldroidFragment.refreshView();


                    }else if (obj.getAtt_type().equalsIgnoreCase("3"))
                    {
                        caldroidFragment.setBackgroundDrawableForDate(red, cal.getTime());
                        Log.e("----333333333------", cal.getTime().getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());
                        caldroidFragment.refreshView();


                    }else if (obj.getAtt_type().equalsIgnoreCase("4"))
                    {
                        caldroidFragment.setBackgroundDrawableForDate(yellow, cal.getTime());
                        Log.e("----4444444444444------", cal.getTime().getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());
                        caldroidFragment.refreshView();


                    }




                }else if (cal.getTime().getDay()==0){
                    caldroidFragment.setBackgroundDrawableForDate(red, cal.getTime());
                    caldroidFragment.refreshView();
                    Log.e("----55555555--------", cal.getTime().getDate()+"--"+obj.getDay()+"--"+obj.getAtt_type());

                }else {

                   */
/*if(!caldroidFragment.getBackgroundForDateTimeMap().containsKey(cal.getTime()))
                   {
                       caldroidFragment.setBackgroundDrawableForDate(blue, cal.getTime());
                       caldroidFragment.refreshView();

                   }*//*

                }
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);

        }

    }


}


*/
