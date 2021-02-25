package com.primesys.VehicalTracking.Activity.EmployeeAcivity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.ChanagePassword;
import com.primesys.VehicalTracking.Activity.EmployeeAcivity.SimpleEmployee.DriverTasklistActivity;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Activity.UserProfileActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeoFencingHomeNew;
import com.primesys.VehicalTracking.ActivityMykiddyLike.NotificationCancel;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.EmpAttendanceDTO;
import com.primesys.VehicalTracking.Dto.MainSliderImageDTO;
import com.primesys.VehicalTracking.Dto.UserModuleDTO;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.GPSEnableSetting;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.FLAG_AUTO_CANCEL;
import static com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNew.Bottom_navigation;

public class EmpHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static CircularNetworkImageView profile_pic;
    RecyclerView ModuleRecycleview;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    Context mContext= this;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG="Rquest";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    ArrayList<MainSliderImageDTO> Sliderlist=new ArrayList<MainSliderImageDTO>();
    public SharedPreferences sharedPreferences;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";
    ArrayList<UserModuleDTO> Modulelist=new ArrayList<>();

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public  String key_Roll_id="Roll_id";

    SharedPreferences.Editor editor ;
    TextView txt_name;
    TextView txt_email;
    private SearchView searchView;
    public DrawerLayout drawer;
    private NavigationView navigationView;
    private boolean isfirst=true;

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    public static int num_columns;
    private String key_UserPic="USER_PIC";
    private  final String GCM_KEY_SEND = "gcm_key_send";


    private int year,month,day;
    private Calendar c;
    public static double ExistingVersionName=0;
    public static double MarketVersionName=0;
    private String package_name="";
    private String url="";
    private String ExistingVersion="";
    private String MarketVersion="";
    public  String key_Location_Enable="location_enable";
    private Calendar calender;
    Button btn_start,btn_end,btn_application_leave;
    Format formatter = new SimpleDateFormat("MMM");
    String s_month = formatter.format(new Date());
    private TextView tv_attendance_status;
    private Button btn_geofence;
    private int weekday;
    private ArrayList<DeviceDataDTO> childlist=new ArrayList<>();
    private ListView list_car;
    private String StudentId="";
    private StudentListAdpter padapter;
    Button btn_task_list;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);
        FindviewbyId();

        WifiManager mWifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo connInfo = mWifiManager.getConnectionInfo();


        Log.e("Wifi info --","------"+new Gson().toJson(connInfo));
        //Get IP Address
        int ipAddress = connInfo.getIpAddress();

        //helper=DBHelper.getInstance(mContext);
        package_name=getApplicationContext().getPackageName();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (btn_start.isActivated()){

                    if(Common.roleid.equals("14")){
                        UpdateAttendance("Start");


                    }else if (Common.roleid.equals("15")){

                        if (Common.getConnectivityStatus(mContext)&& PrimesysTrack.mDbHelper.Show_Device_list().size()==0) {
                            // Call Api to get track information
                            try {
                                GetDevicelist();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }else {
                            childlist=PrimesysTrack.mDbHelper.Show_Device_list();
                            ShowVehicaleDialog();

                        }
                    }
                }else {
                    Common.ShowSweetAlert(mContext,"You already start your work else today is holiday.");
                }


            }
        });


        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_end.isActivated())
                UpdateAttendance("End");
                else Common.ShowSweetAlert(mContext,"You may not start your work yet else today is holiday");

            }
        });

        btn_geofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Studentlist.StudentId=data.get(pos).getStudentId().toString();
                Intent intent = new Intent(mContext, GeoFencingHomeNew.class);
                intent.putExtra("StudentId", Common.Emp_id+"");

                startActivity(intent);
            }
        });


        btn_task_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                Date df = new Date(dv);
                                Intent task=new Intent(mContext,DriverTasklistActivity.class);
                                task.putExtra("day",dayOfMonth+"");
                                task.putExtra("month",(monthOfYear + 1)+"");
                                task.putExtra("year",year+"");
                                task.putExtra("user_id",Common.userid);

                                startActivity(task);

                               // GetTripReport(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                            }
                        }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()+10L*24L*60L*60L*1000L);


                dpd.show();



            }
        });

      //  EnableLocationProvider();

        if (Integer.parseInt(Common.roleid)==14){
            btn_geofence.setVisibility(View.VISIBLE);
            btn_task_list.setVisibility(View.GONE);

        }else if (Integer.parseInt(Common.roleid)==15){
            btn_geofence.setVisibility(View.GONE);
            btn_task_list.setVisibility(View.VISIBLE);
        }


     /*       //Start Service
        if (Common.getConnectivityStatus(mContext)) {
            try {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new GetLiveAppVersion().executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR,
                            (Void) null);
                } else {
                    new GetLiveAppVersion().execute();
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/

        btn_application_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,LeaveApplicationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void ShowVehicaleDialog() {

         // custom dialog

            final Dialog dialog = new Dialog(mContext);
            dialog.setTitle(mContext.getResources().getString(R.string.select_car));

            dialog.setContentView(R.layout.dialog_studentlist);
            dialog.setCancelable(false);
            dialog.closeOptionsMenu();
            dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            dialog.show();
            list_car=(ListView)dialog.findViewById(R.id.student_list);
            Button cancel = (Button) dialog.findViewById(R.id.d_cancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Home.viewPager.setCurrentItem(0);
                    dialog.dismiss();
//                getActivity().finish();
                    Bottom_navigation.setSelectedItemId(R.id.navigation_track);



                }
            });
//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
            padapter=new StudentListAdpter(mContext, R.layout.fragment_mapsidebar, childlist);
            list_car.setAdapter(padapter);

            list_car.setOnItemClickListener(new AdapterView.OnItemClickListener() {



                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Common.Emp_id = childlist.get(0).getId();
                    UpdateAttendance("Start");

                    dialog.dismiss();
                }
            });


            dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Weekday--",weekday+"");
        if (weekday==1){
            btn_end.setActivated(false);
            btn_start.setActivated(false);
            tv_attendance_status.setVisibility(View.VISIBLE);
            tv_attendance_status.setText("Today is Holiday.Feel free");
        }else
        GetEmpDay_status();

    }

    private void UpdateAttendance(final String att_type) {
        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"ParentAPI/UpdateAttendance",new Response.Listener<String>() {

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

                params.put("UserId", Common.userid);
                params.put("StudentId", Common.Emp_id);
                params.put("day", day+"");
                params.put("month", s_month+"");
                params.put("year", year+"");
                params.put("time", getDate());



                System.out.println("REq---UpdateAttendance-------"+params);
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


               JSONObject rs = new JSONObject(response);

            Common.ShowSweetSucess(mContext,rs.getString("message"));
            GetEmpDay_status();

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {


        }




    }


    private void FindviewbyId() {
        //Initialize data
        sharedPreferences = mContext.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        if (sharedPreferences.contains(key_id)) {
            Common.userid = sharedPreferences.getString(key_id, "");
            Common.roleid = sharedPreferences.getString(key_Roll_id, "");
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btn_task_list= (Button) findViewById(R.id.task_list);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        toggle.setDrawerIndicatorEnabled(false);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_nav_menu);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        // Scale it to 50 x 50
        //   Drawable ScaleDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50,50, true));
        // Set your new, scaled drawable "d"
        toggle.setHomeAsUpIndicator(drawable);
        toolbar.setTitle( getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(mContext.getResources().getColor(R.color.white));



        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_start= (Button) findViewById(R.id.btn_startwork);
        btn_end= (Button) findViewById(R.id.btn_endwork);
        btn_geofence= (Button) findViewById(R.id.btn_geofencing);

        btn_application_leave= (Button) findViewById(R.id.btn_application_leave);
        tv_attendance_status=(TextView) findViewById(R.id.tv_attendance_status);
        //setnavigation header

        View header = navigationView.inflateHeaderView(R.layout.nav_header_home);
        txt_name=(TextView)header.findViewById(R.id.nav_name);
        txt_email=(TextView)header.findViewById(R.id.nav_email);
        if(sharedPreferences.contains(key_fname)||sharedPreferences.contains(key_USER))
        {
            txt_email.setText(sharedPreferences.getString(key_USER,""));
            txt_name.setText(sharedPreferences.getString(key_fname,""));
        }
        else{
            txt_email.setText("");
            txt_name.setText("");

        }
        Common.userid = sharedPreferences.getString(key_id,"");

        Typeface typeFace = Typeface.createFromAsset(getAssets(), Common.fontType1);
        txt_name.setTypeface(typeFace);
        txt_email.setTypeface(typeFace);

        calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day=calender.get(Calendar.DAY_OF_MONTH);
        weekday=calender.get(Calendar.DAY_OF_WEEK);


    }
    private void GetEmpDay_status() {

            reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
            final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

            //JSon object request for reading the json data
            stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"ParentAPI/GetEmpDay_status",new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    parseStatusJSON(response);
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

                    params.put("UserId", Common.userid);
                    params.put("day", day+"");
                    params.put("month", s_month+"");
                    params.put("year", year+"");

                    Log.e("---------------","REq---GetEmpDay_status-------"+params);
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

    private void parseStatusJSON(String response) {
        Log.e("---------------","Respo---GetEmpDay_status-------"+response);

        try {

            JSONObject Jo=new JSONObject(response);
            EmpAttendanceDTO empdto=new EmpAttendanceDTO();
            empdto.setEmp_id(Jo.getString("emp_id"));
            Common.Emp_id=empdto.getEmp_id();
            empdto.setAttendance_id(Jo.getString("attendance_id")+"");
            empdto.setAtt_type(Jo.getString("att_type")+"");
            empdto.setType(Jo.getString("type")+"");
            empdto.setIs_start(Jo.getString("is_start"));
            empdto.setIs_end(Jo.getString("is_end"));
            empdto.setStart_time(Jo.getString("start_time")+"");
            empdto.setEnd_time(Jo.getString("end_time")+"");
            if (empdto.getAtt_type().equals("4")) {
                btn_end.setActivated(false);
                btn_start.setActivated(false);
                btn_start.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_start.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                btn_end.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_end.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("Today you are on leave.Feel free.");

            }else if (empdto.getAtt_type().equals("3")) {
                btn_end.setActivated(false);
                btn_start.setActivated(false);
                btn_start.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_start.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                btn_end.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_end.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("Today is Holiday.Feel free");

            }else if (empdto.getAtt_type().equals("5")&&empdto.getIs_start().equals("0")&&empdto.getIs_end().equals("0")){

                btn_end.setActivated(false);
                btn_start.setActivated(true);

                btn_start.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
                btn_start.setTextColor(ContextCompat.getColor(mContext,R.color.white));

                btn_end.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_end.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));



                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("Please start your work time for today.");

            }else if (empdto.getAtt_type().equals("2")&&empdto.getIs_start().equals("1")&&empdto.getIs_end().equals("0")){

                btn_end.setActivated(true);
                btn_start.setActivated(false);

                btn_start.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_start.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));

                btn_end.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
                btn_end.setTextColor(ContextCompat.getColor(mContext,R.color.white));

                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("Please end your work time for today.");


            }else if (empdto.getAtt_type().equals("2")&&empdto.getIs_start().equals("1")&&empdto.getIs_end().equals("1")){
                btn_end.setActivated(false);
                btn_start.setActivated(false);
                btn_start.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_start.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                btn_end.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_end.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("You finished today's work.Feel free.");
            }else if (empdto.getAtt_type().equals("2")&&empdto.getIs_start().equals("1")&&empdto.getIs_end().equals("0")){
                btn_end.setActivated(true);
                btn_start.setActivated(false);
                btn_start.setBackgroundColor(ContextCompat.getColor(mContext,R.color.disabled_background_color));
                btn_start.setTextColor(ContextCompat.getColor(mContext,R.color.disabled_text_color));
                btn_end.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
                btn_end.setTextColor(ContextCompat.getColor(mContext,R.color.white));

                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("You finished today's work.Feel free.");
            }
            else{
                tv_attendance_status.setVisibility(View.VISIBLE);
                /*btn_end.setVisibility(View.INVISIBLE);
                btn_start.setVisibility(View.INVISIBLE);*/
                tv_attendance_status.setVisibility(View.VISIBLE);
                tv_attendance_status.setText("You finished today's work.Feel free.");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }

    /*@Override
    public void onSliderClick(BaseSliderView slider) {
      //  Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }*/





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            if (Common.roleid.equalsIgnoreCase("1")||Common.roleid.equalsIgnoreCase("2")){
                Common.ShowSweetAlert(mContext,"You are not eligible for this feature.Please contact to admin.");
            }else {
                Intent Add=new Intent(mContext,UserProfileActivity.class);
                startActivity(Add);
            }


        }
       /* else if (id == R.id.nav_addsos) {
*//*
            Intent Add=new Intent(mContext,SosActivity.class);
            startActivity(Add);*//*

        }*/else if (id == R.id.nav_change_password) {

            Intent Add=new Intent(mContext,ChanagePassword.class);
            startActivity(Add);
        }
        else if (id == R.id.nav_signout) {
            if(sharedPreferences.contains(key_IS))
                isfirst = sharedPreferences.getBoolean(key_IS, true);

            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_DEVICE_LIST);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_MODULE);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SLIDER);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_GEOFENCE_LIST);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_MODULE);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SLIDER);

            if (!isfirst) {
                editor = sharedPreferences.edit();
                editor.putString(key_id, "");
                editor.putString(key_PASS, "");
                editor.putString(key_Roll_id, "");
                editor.putString(key_UserPic, "");
                editor.remove(GCM_KEY_SEND);
                editor.remove(key_IS);
                editor.commit();
            }else
                Common.ShowSweetAlert(mContext, "Please Login First");


            try{
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null){
                    LoginManager.getInstance().logOut();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent login=new Intent(mContext,LoginActivity.class);
                startActivity(login);
                finishAndRemoveTask();

            }
            else
            {
                Intent login=new Intent(mContext,LoginActivity.class);
                startActivity(login);
                finish();

            }





        } else if (id == R.id.nav_share) {
            shareIt();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "VTS");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sharemessaage));
        startActivity(sharingIntent);
    }


/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_list_with_refine, menu);

        MenuItem menuItem = menu.findItem(R.id.countnotification);
        menuItem.setIcon(buildCounterDrawable(Common.mNotificationsCount, R.drawable.ic_action_cart));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {
            onBackPressed();
            return true;
        }else if (id==R.id.countnotification){
            Intent In=new Intent(mContext,Cart_Itemlist.class);
            mContext.startActivity(In);

        }else if (id==R.id.action_search) {
        }



        return super.onOptionsItemSelected(item);
    }

*/



    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          /*  super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);

            this.finish();*/
        }


        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sure to exit MykidTrack?")
                .setCancelText("No,cancel !")
                .setConfirmText("Yes!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);

                        sDialog.cancel();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();


    }



    private long value(String version)
    {
        Long result = null;
        try {

            version = version.trim();
            if (version!=null&&version.contains("")) {
                final int index = version.lastIndexOf("");
                result= value(version.substring(0, index)) * 100 + value(version.substring(index + 1));
            } else {
                result=Long.valueOf(version);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    class GetLiveAppVersion extends AsyncTask<Void, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params) {
            String result="";
            try {

                String curVersion = getPackageManager().getPackageInfo(package_name, 0).versionName;
                String newVersion = curVersion;
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + package_name + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first().ownText();
                result=newVersion.toString();
                Log.e(PrimesysTrack.TAG,"GetLiveAppVersion----result--"+newVersion);

                MarketVersionName=value(result);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String MarketVersionName1) {

            PackageInfo pInfo = null;

            Log.e(PrimesysTrack.TAG,"getPackageName------"+getPackageName());

            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //get the app version Name for display
            ExistingVersion=pInfo.versionName;

            Log.e(PrimesysTrack.TAG,"pInfopInfopInfo------"+pInfo.versionName);
            System.out.println("----------web_update------------"+ExistingVersion+"-----"+MarketVersion);

            ExistingVersionName = value(pInfo.versionName);
            System.out.println("----------web_update------------"+ExistingVersion+"-----"+MarketVersion);


            if (MarketVersionName>ExistingVersionName) {
                //  CreateUpdateNotification();
                ShowUpdateDialog();

            }

        }
    }

    private void ShowUpdateDialog() {


        System.out.println("----------If CheckForUpdate------------");

        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Update Available..")
                .setContentText("You are using  version "+ ExistingVersion + "\n Version " + MarketVersion + " is available in play store. \nDo you which to download it?")
                .setCancelText("Not Now")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {


                        try {
                            //Check whether Google Play store is installed or not:
                            mContext.getPackageManager().getPackageInfo("com.android.vending", 0);

                            url = "market://details?id=" + "com.primesys.mitra";
                        } catch ( final Exception e ) {
                            e.printStackTrace();
                            url = "https://play.google.com/store/apps/details?id=" + "com.primesys.mitra";
                        }


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        sDialog.cancel();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }


    void CreateUpdateNotification() {


        try {
            //Check whether Google Play store is installed or not:
            this.getPackageManager().getPackageInfo("com.android.vending", 0);

            url = "market://details?id=" + "com.primesys.mitra";
        } catch ( final Exception e ) {
            e.printStackTrace();
            url = "https://play.google.com/store/apps/details?id=" + "com.primesys.mitra";
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        int notificationId = 135;


        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(getResources().getString(R.string.app_name)))
                .setContentText("Update " + getResources().getString(R.string.app_name))
                .setContentInfo("New update available of "+getResources().getString(R.string.app_name))
                .setAutoCancel(true);

        Intent answerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        answerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        answerIntent.setAction("Yes");
        PendingIntent pendingIntentYes = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        notificationBuilder.addAction(R.drawable.thumbs_up, "Yes,Update", pendingIntentYes);


        Intent NoanswerIntent = new Intent(mContext, NotificationCancel.class);
        NoanswerIntent.putExtra("notificationId",notificationId);
        answerIntent.setAction("No");
        PendingIntent pendingIntentNo =PendingIntent.getBroadcast(getApplicationContext(), 0, NoanswerIntent, 0);

        notificationBuilder.addAction(R.drawable.thumbs_down, "No", pendingIntentNo);

        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(contentIntent);


        notificationBuilder.getNotification().flags |= FLAG_AUTO_CANCEL;

        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        notification.flags = FLAG_AUTO_CANCEL;
        notification.defaults |= DEFAULT_SOUND;
        notifyMgr.notify(135, notification);




    }




    private void EnableLocationProvider() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {


        }
        if (gps_enabled != true && network_enabled != true) {
            GPSEnableSetting go = new GPSEnableSetting();
            go.GPSDialog(mContext);
        }else {
        }
    }


   public String getDate()
    {
        SimpleDateFormat sdT = new SimpleDateFormat("a");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sDate= sdf.format(date)+" "+sdT.format(date);
        return sDate;
    }









    private void GetDevicelist() {


        reuestQueue = Volley.newRequestQueue(mContext);

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL + "ParentAPI/GetEmpDevicelist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Responce---GetEmpDevicelist********************************************------"+response);
                parsingDeviceInfo (response);

                pDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        if (error!=null)
                            Log.e("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("UserId",Common.userid);

                System.out.println("REq---GetDevicelist------"+params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }



    public void parsingDeviceInfo(String result) {
        try{
            Log.e("Track Info list",result);
            JSONArray joArray=new JSONArray(result);
            for (int i = 0; i < joArray.length(); i++) {
                JSONObject joObject =joArray.getJSONObject(i);
                DeviceDataDTO dmDetails=new DeviceDataDTO();
                dmDetails.setId(joObject.getString("StudentID"));
                dmDetails.setName(joObject.getString("Name"));

                dmDetails.setPath(joObject.getString("Photo").replaceAll("~", "").trim());
                dmDetails.setType(joObject.getString("Type"));
                dmDetails.setImei_no(joObject.getString("ImeiNo"));



                childlist.add(dmDetails);
            }


        }catch(Exception e){
            Log.e("Exception", ""+e);
        }finally{
            //it work Better but take time to Load
            if (childlist.size()>0) {
                //Insert Offeline data
                PrimesysTrack.mDbHelper.Insert_Device_list(childlist);
                    ShowVehicaleDialog();

            }else{
                Common.ShowSweetAlert(mContext,"No device information found." );
            }

        }
    }


}





/*
public class EmpHomeActivity extends Activity {
    private Toolbar toolbar;
    Button btn_start,btn_end;
    TextView tv_attendance;
    Calendar calendar=Calendar.getInstance();
    int day=calendar.da


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);
        findViewById();

        GetEmpDaystatus();
    }

    private void findViewById() {
s
        btn_start= (Button) findViewById(R.id.btn_startwork);
        btn_end= (Button) findViewById(R.id.btn_endwork);
        tv_attendance= (TextView) findViewById(R.id.tv_application_leave);s
    }

}
*/
