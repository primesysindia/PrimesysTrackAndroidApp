package com.primesys.VehicalTracking.Activity.EmployeeAcivity.SimpleEmployee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.primesys.VehicalTracking.Activity.ChanagePassword;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Activity.UserProfileActivity;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DriverEmpTaskSheduledDTO;
import com.primesys.VehicalTracking.Dto.DriverTaskDayMainDTO;
import com.primesys.VehicalTracking.Dto.UserModuleDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DriverEmpUsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener {
    private String TAG="Rquest";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private Context mContext=DriverEmpUsActivity.this;
    private RecyclerView task_recyclerView;
    ArrayList<DriverEmpTaskSheduledDTO> TaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> MonTaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> TueTaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> WenTaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> ThuTaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> FriTaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> SatTaskDataList=new ArrayList<>();
    ArrayList<DriverEmpTaskSheduledDTO> SunTaskDataList=new ArrayList<>();
    private RecyclerExpandAdapter task_adapter;
    private TextView trip_heading,totalkm;
    private Calendar calendar;
    private int year,month,day;
    private Toolbar toolbar;
    public SharedPreferences sharedPreferences;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";
    ArrayList<UserModuleDTO> Modulelist=new ArrayList<>();

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public  String key_Roll_id="Roll_id";
    private String key_UserPic="USER_PIC";

    SharedPreferences.Editor editor ;
    TextView txt_name;
    TextView txt_email;
    private SearchView searchView;
    public DrawerLayout drawer;
    private NavigationView navigationView;
    private boolean isfirst=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_emp_us);
        findViewById();
        GetDriverTaskList();
    }

    private void findViewById() {

        task_recyclerView = (RecyclerView) findViewById(R.id.task_recyclerView);
        trip_heading=(TextView)findViewById(R.id.trip_heading);
        totalkm=(TextView)findViewById(R.id.totalkm);
        task_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RecyclerView.ItemAnimator animator = task_recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
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



    }
    private void GetDriverTaskList() {

        reuestQueue= Volley.newRequestQueue(mContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(mContext);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"ParentAPI/GetDriverEmpDaywiseTaskList",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseTaskJSON(response);
                pDialog.dismiss();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("emp_user_id", Common.userid);

                Log.e("---------------","REq---GetDriverTaskList-------"+params);
                return params;
            }

        };

        stringRequest.setTag("");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    private void parseTaskJSON(String response) {
        Log.e("---------------","Response---GetDriverTaskList-------"+response);

        try{

            JSONArray jArray=new JSONArray(response);
            for (int i=0;i<jArray.length();i++)
            {
                JSONObject jo= jArray.getJSONObject(i);
                DriverEmpTaskSheduledDTO obj=new DriverEmpTaskSheduledDTO();

                obj.setAddress(jo.getString("address"));
                obj.setCar_name(jo.getString("car_name"));
                obj.setStartTime(jo.getString("time"));
                obj.setDay_name(jo.getString("day_name"));

                obj.setVehicleID(jo.getString("emp_car_id"));
                obj.setTask_id(jo.getString("task_id"));
                obj.setRoute(jo.getString("route"));
                obj.setReportStartTime(jo.getString("report_start_time"));
             //   obj.setStart_weekdate(jo.getString("start_weekdate"));


                TaskDataList.add(obj);

            }

            SetdataSort(TaskDataList);

         /*   if (TaskDataList.size()>0){

                SetdataSort(TaskDataList);
             *//*   this.movieComparator = (o1, o2) -> o1.getDay_name().compareTo(o2.getDay_name());
                Collections.sort(TaskDataList, movieComparator);
                mSectionedRecyclerAdapter = new MovieAdapterByName(TaskDataList);*//*

             *//*   mAdpter = new TaskListReportAdpter(TaskDataList, R.layout.row_task_list, mContext);
                TaskList.setAdapter(mAdpter);
                trip_heading.setText(Common.username+" you have following  pickup point on \n"
                        +" Date: "+day+"-"+month+"-"+year+ " and allocated Vehicle Name:"+ TaskDataList.get(0).getCar_name());
                totalkm.setText("Total pickup point : "+TaskDataList.size()+"");*//*

            }else {
                Common.ShowSweetAlert(mContext,"You don't have a pickup today.");
            }
*/
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void SetdataSort(ArrayList<DriverEmpTaskSheduledDTO> taskDataList) {
        MonTaskDataList.clear();
        TueTaskDataList.clear();
        WenTaskDataList.clear();
        ThuTaskDataList.clear();
        FriTaskDataList.clear();
        SatTaskDataList.clear();
        SunTaskDataList.clear();

        for (DriverEmpTaskSheduledDTO task:taskDataList) {

            if (task.getDay_name().equals("1")){
                MonTaskDataList.add(task);
            }else if (task.getDay_name().equals("2")){
                TueTaskDataList.add(task);

            }else if (task.getDay_name().equals("3")){
                WenTaskDataList.add(task);

            }else if (task.getDay_name().equals("4")){
                ThuTaskDataList.add(task);

            }else if (task.getDay_name().equals("5")){
                FriTaskDataList.add(task);

            }else if (task.getDay_name().equals("6")){
                SatTaskDataList.add(task);

            }else if (task.getDay_name().equals("7")){
                SunTaskDataList.add(task);

            }


        }

        ArrayList<DriverTaskDayMainDTO> daywiseTasklist=new ArrayList<>();
        daywiseTasklist.add(new DriverTaskDayMainDTO("Monday",MonTaskDataList,R.drawable.ic_close));
        daywiseTasklist.add(new DriverTaskDayMainDTO("Tuesday",TueTaskDataList,R.drawable.ic_close));
        daywiseTasklist.add(new DriverTaskDayMainDTO("Wednesday",WenTaskDataList,R.drawable.ic_close));
        daywiseTasklist.add(new DriverTaskDayMainDTO("Thursday",ThuTaskDataList,R.drawable.ic_close));
        daywiseTasklist.add(new DriverTaskDayMainDTO("Friday",FriTaskDataList,R.drawable.ic_close));
        daywiseTasklist.add(new DriverTaskDayMainDTO("Saturday",SatTaskDataList,R.drawable.ic_close));
        daywiseTasklist.add(new DriverTaskDayMainDTO("Sunday",SunTaskDataList,R.drawable.ic_close));

        Log.e("-------",MonTaskDataList.size()+"--"+TueTaskDataList.size());
        task_adapter = new RecyclerExpandAdapter(this, daywiseTasklist);
        task_recyclerView.setAdapter(task_adapter);
        trip_heading.setText(Common.username+" you have following Task for this week");
        totalkm.setText("Total pickup point : "+TaskDataList.size()+"");

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //task_adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // task_adapter.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_gettask:
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
                                task.putExtra("user_id", Common.userid);

                                startActivity(task);

                                // GetTripReport(Common.getGMTTimeStampFromDate(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"),Common.getGMTTimeStampFromDate(dayOfMonth+1 + "-"+ (monthOfYear + 1) + "-" + year+" 00:00 am"));



                            }
                        }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-30L*24L*60L*60L*1000L);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()+10L*24L*60L*60L*1000L);


                dpd.show();




                return true;

        }

        return false;
    }


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
      else if (id == R.id.nav_change_password) {

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
                editor.remove(key_IS);
                String GCM_KEY_SEND = "gcm_key_send";
                editor.remove(GCM_KEY_SEND);

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
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
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
    public void onSliderClick(BaseSliderView slider) {

    }

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


}
