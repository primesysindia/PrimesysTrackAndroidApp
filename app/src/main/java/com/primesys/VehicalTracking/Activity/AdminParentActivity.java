package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNew;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.AssociatedParentDTO;
import com.primesys.VehicalTracking.MyAdpter.AllGroupRecyclerviewAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class AdminParentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        ActivityCompat.OnRequestPermissionsResultCallback ,
        SwipeRefreshLayout.OnRefreshListener,AllGroupRecyclerviewAdpter.RecyleAdapterListener {
    SharedPreferences.Editor editor ;
    SharedPreferences sharedPreferences;
    private NavigationView navigationView;
    private boolean isfirst=true;
    private static DBHelper helper;
    TabLayout tabLayout;
    Toolbar toolbar;
    Context mContext=AdminParentActivity.this;
    private Typeface typeFace;
    private DrawerLayout drawer;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public  String key_Roll_id="Roll_id";
    private String key_UserPic="UserPic";
    private static String KEY_LOCATION="Location";
    private TextView txt_name,txt_email;
    public static CircularNetworkImageView profile_pic;
    private ImageLoader imageLoader;
    private RecyclerView mGroupListRecyclerview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<AssociatedParentDTO> groupListData=new ArrayList<>();
    private AllGroupRecyclerviewAdpter mGrouplistAdpter;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private TextView tv_nodata_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parent);
        findViewById();


    }

    private void findViewById() {

        sharedPreferences = mContext.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        if (sharedPreferences.contains(key_id)) {
            Common.userid = sharedPreferences.getString(key_id, "");
            Common.roleid = sharedPreferences.getString(key_Roll_id, "");
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        typeFace= Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_home);
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setDrawerIndicatorEnabled(false);
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initialize data
        sharedPreferences = mContext.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //setnavigation header


        View header = navigationView.inflateHeaderView(R.layout.nav_header_home);
        txt_name = (TextView) header.findViewById(R.id.nav_name);
        txt_email = (TextView) header.findViewById(R.id.nav_email);
        profile_pic = (CircularNetworkImageView) header.findViewById(R.id.Proimage);

        if (sharedPreferences.contains(key_id)) {
            Common.userid = sharedPreferences.getString(key_id, "");
            Common.roleid = sharedPreferences.getString(key_Roll_id, "");

        }


        if( sharedPreferences.contains(key_USER))
        {
            txt_email.setText(sharedPreferences.getString(key_USER,""));
            txt_name.setText(sharedPreferences.getString(key_fname,""));
            //  Picasso.with(context).load(Common.Relative_URL+Common.userid+"_"+Common.userid+".jpg").transform(new CircleTransform()).into(profile_pic);

        }
        else{
            txt_email.setText("");
            txt_name.setText("");;

        }

        String Photo=sharedPreferences.getString(key_UserPic,"");

        if (!Photo.equals(""))
        {
            Bitmap bmp = Common.bytebitmapconversion(Photo);
            HomeNew.profile_pic.setImageBitmap(Common.getRoundedShape(bmp));

        }
        else {
            profile_pic.setImageResource(R.mipmap.ic_icon);
        }
        Common.userid = sharedPreferences.getString(key_id, "");

        Typeface typeFace = Typeface.createFromAsset(getAssets(), Common.fontType1);
        txt_name.setTypeface(typeFace);
        txt_email.setTypeface(typeFace);

        mGroupListRecyclerview = (RecyclerView) findViewById(R.id.group_list);
        mGroupListRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mGroupListRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mGroupListRecyclerview.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        mGrouplistAdpter = new AllGroupRecyclerviewAdpter(mContext, groupListData, (AllGroupRecyclerviewAdpter.RecyleAdapterListener) this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mGroupListRecyclerview.setLayoutManager(mLayoutManager);
        mGroupListRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mGroupListRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mGroupListRecyclerview.setAdapter(mGrouplistAdpter);
        tv_nodata_found=findViewById(R.id.tv_nodata_found);


        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getExistGroupData();
                    }
                }
        );




    }

    private void getExistGroupData() {
    reuestQueue= Volley.newRequestQueue(mContext);
        swipeRefreshLayout.setRefreshing(true);
/*
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Creating Account");
        pDialog.setCancelable(false);
        pDialog.show();*/
    //JSon object request for reading the json data
    stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetAssociatedParent",new Response.Listener<String>() {
     //   stringRequest = new StringRequest(Request.Method.POST,"http://192.168.1.102:8080/TrackingAppDB/TrackingAPP/UserServiceAPI/GetAssociatedParent",new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            if(response!=null)
                parsegetExistGroupDataJSON(response);
            swipeRefreshLayout.setRefreshing(false);
        }
    },
            new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            //	pDialog.hide();
            if(error.networkResponse != null && error.networkResponse.data != null){
                VolleyError er = new VolleyError(new String(error.networkResponse.data));
                error = er;
                Log.e("Response ",error.toString());
                swipeRefreshLayout.setRefreshing(false);

            }

        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();

                params.put("UserId",Common.adminId);

            Log.e(PrimesysTrack.TAG,"getExistGroupData---req--- "+params+"");
            return params;
        }

    };
        stringRequest.setTag("");
    RetryPolicy retryPolicy = new DefaultRetryPolicy(
            20 * 1000,
            0,
            1
    );
        stringRequest.setRetryPolicy(retryPolicy);
        reuestQueue.add(stringRequest);
}

    private void parsegetExistGroupDataJSON(String response) {
        Log.e(TAG,"Respo---parsegetExistGroupDataJSON-----"+response);

        // {"updatedDate":null,"fcm":{"id":1,"active":true,"createdBy":null,"createdDate":null,"fcmKey":"1234","updatedDate":null,"updatedBy":null},"updatedBy":null,"phoneNum":"123456789","createdBy":null,"address":{"longtd":0,"updatedDate":null,"state":"test2","addLine2":null,"addLine1":null,"addressId":1,"updatedBy":null,"country":null,"city":"test1","pincode":null,"createdBy":null,"latd":0,"active":false,"createdDate":null},"email":"test@testing.test","roles":[{"roleDescription":null,"createdBy":null,"updatedDate":null,"active":true,"role":"enduser","createdDate":null,"updatedBy":null,"roleId":3}],"dob":null,"name":"test","userId":1,"active":true,"gender":"test","vendors":[{"franchise":false,"storeName":null,"createdBy":null,"updatedDate":null,"active":true,"vendorId":2,"createdDate":null,"updatedBy":null}],"createdDate":null}

        if (groupListData.size()>0)
            groupListData.clear();
        try {
            JSONArray Jarray=new JSONArray(response);
            if (Jarray.length()>0){
                for (int i=0;i<Jarray.length();i++){
                    JSONObject jo= (JSONObject) Jarray.get(i);
                    AssociatedParentDTO dto=new AssociatedParentDTO();
                    dto.setAddress(jo.getString("Address"));
                    dto.setName(jo.getString("Name"));
                    dto.setEmailId(jo.getString("EmailId"));
                    dto.setUserId(jo.getString("UserId"));
                    dto.setMobileNo(jo.getString("MobileNo"));
                    dto.setTotalChild(jo.getString("TotalChild"));
                    groupListData.add(dto);



                }
                mGrouplistAdpter.notifyDataSetChanged();

            }else {
                tv_nodata_found.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*super.onBackPressed();
           /* Intent startMain = new Intent(Intent.ACTION_MAIN);
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
        else if (id == R.id.nav_renew_device) {


            if (Common.PlatformRenewalStatus){
                Intent Add = new Intent(mContext, DeviceLevelRenewService.class);
                startActivity(Add);
            }else {
                Intent Add = new Intent(mContext, RenewServiceActivity.class);
                startActivity(Add);
            }


        }/* else if (id == R.id.nav_addnewdevice) {
            Intent Add=new Intent(mContext,ShowExistingDeviceActivity.class);
            startActivity(Add);
        }*/
        else if (id == R.id.nav_signout) {
            if(sharedPreferences.contains(key_IS))
                isfirst = sharedPreferences.getBoolean(key_IS, true);

            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_DEVICE_LIST);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_MODULE);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SLIDER);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_GEOFENCE_LIST);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_MODULE);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SLIDER);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SMSFUNC_LIST);


            if (!isfirst) {
                editor = sharedPreferences.edit();
                editor.putString(key_id, "");
                editor.putString(key_PASS, "");
                editor.putString(key_Roll_id, "");
                editor.putString(key_UserPic, "");

                editor.remove(key_IS);
                editor.remove(KEY_LOCATION);
                String GCM_KEY_SEND = "gcm_key_send";
                editor.remove(GCM_KEY_SEND);

                editor.commit();

            }else
                Common.ShowSweetAlert(mContext, "Please Login First");


            try {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Intent startMain = new Intent(mContext, LoginActivity.class);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finishAndRemoveTask();

            } else {
                Intent startMain = new Intent(mContext, LoginActivity.class);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
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
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "PrimesysTrack");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sharemessaage));
        startActivity(sharingIntent);
    }


    @Override
    public void onRefresh() {
        getExistGroupData();

    }

    @Override
    public void onIconClicked(int position) {

    }

    @Override
    public void onIconImportantClicked(int position) {

    }

    @Override
    public void onMessageRowClicked(int position) {
        try {
            Common.userid=groupListData.get(position).getUserId();
            Common.roleid="7";
            if (sharedPreferences.contains(KEY_LOCATION))
            {
                editor.remove(KEY_LOCATION);
                editor.commit();

            }
            Intent newparent = new Intent(mContext, HomeNew.class);
            startActivity(newparent);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onRowLongClicked(int position) {

    }
}
