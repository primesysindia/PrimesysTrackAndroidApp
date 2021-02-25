package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.primesys.VehicalTracking.Activity.APIController;
import com.primesys.VehicalTracking.Activity.AboutPageActivity;
import com.primesys.VehicalTracking.Activity.ChanagePassword;
import com.primesys.VehicalTracking.Activity.ContactUSActivity;
import com.primesys.VehicalTracking.Activity.DeviceLevelRenewService;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Activity.RenewServiceActivity;
import com.primesys.VehicalTracking.Activity.UserProfileActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment.Mine_ModuleFragment;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.FeatureAddressDetailsDTO;
import com.primesys.VehicalTracking.Dto.RailWayAddressDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.SMSFuction;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragmentFeatureAddressEnable;
import com.primesys.VehicalTracking.VTSFragments.history_API_FragmentFearureaddress;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.FLAG_AUTO_CANCEL;

;

public class HomeNewRailway extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static String defaultImage;
    private TextView txt_name,txt_email;
    public static CircularNetworkImageView profile_pic;
    private ImageLoader imageLoader;
    static Context context;
    private String TAG="Rquest";
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public  String key_Roll_id="Roll_id";
    private String key_UserPic="UserPic";
    private static String KEY_LOCATION="Location";

    SharedPreferences.Editor editor ;
    private NavigationView navigationView;
    private boolean isfirst=true;
    private static DBHelper helper;
    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    public static int num_columns;
    private int year,month,day;
    private Calendar c;
    public static double ExistingVersionName=0;
    public static double MarketVersionName=0;
    private String package_name="";
    private String url="";
    private String ExistingVersion="";
    private String MarketVersion="";
    public static BottomNavigationView Bottom_navigation;
    private Typeface typeFace;
    private DrawerLayout drawer;
    private ListView personlist;
    private static ArrayList<DeviceDataDTO> tracklist=new ArrayList<>();
    HashSet<String> setDeviceType=new HashSet<String>();
    private ArrayList<RailWayAddressDTO> railWayAddressList=new ArrayList<>();
    static StringRequest stringRequest;
    static RequestQueue reuestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_railway_home_activity);
        context=HomeNewRailway.this;
        FindviewbyId();
        package_name=getApplicationContext().getPackageName();
        EnableLocationProvider();


        //Satrt Service
        if (Common.getConnectivityStatus(context)) {
            try {
              /*  if (Common.SosMax_callAllow>0) {

                    Intent SOsIntent =new Intent(getBaseContext(), LockService.class);
                    startService(SOsIntent);


                }


*/
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
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
        }

        Bottom_navigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        try {

                            Fragment selectedFragment = null;
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            switch (item.getItemId()) {
                                case R.id.navigation_track:
                                    selectedFragment = Fragment.instantiate(context, ShowMapFragmentFeatureAddressEnable.class.getName());
                                    transaction.replace(R.id.track_container, selectedFragment);
                                    transaction.commit();
                                    return true;

                                case R.id.navigation_history:
                                    selectedFragment = Fragment.instantiate(context,history_API_FragmentFearureaddress.class.getName());
                                    transaction.replace(R.id.track_container, selectedFragment);
                                    transaction.commit();
                                    return true;
                                case R.id.navigation_monitor:
                                    try {
                                        LoginActivity.mClient.sendMessage(StopTracKEvent());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    selectedFragment = Fragment.instantiate(context, SMSFuction.class.getName());
                                    transaction.replace(R.id.track_container, selectedFragment);
                                    transaction.commit();
                                    return true;
                                case R.id.navigation_mine:
                                    try {
                                        LoginActivity.mClient.sendMessage(StopTracKEvent());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    selectedFragment = Fragment.instantiate(context, Mine_ModuleFragment.class.getName());
                                    transaction.replace(R.id.track_container, selectedFragment);
                                    transaction.commit();

                                    return true;
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return true;

                    }
                });

        Bottom_navigation.setSelectedItemId(R.id.navigation_track);

        //Manually displaying the first fragment - one time only
    /*   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.track_container, Fragment.instantiate(context, HomeTrackFragment.class.getName()));
        transaction.commit();*/

    }




    private void FindviewbyId() {
        try{
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        typeFace= Typeface.createFromAsset(context.getAssets(),"Times New Roman.ttf");
        Bottom_navigation = (BottomNavigationView) findViewById(R.id.navigationmenu);

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
        personlist = (ListView) findViewById(R.id.User_list);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            Menu nav_Menu = navigationView.getMenu();
            if (Common.FeatureAddressEnable)
            nav_Menu.findItem(R.id.nav_download_address).setVisible(true);
            else
                nav_Menu.findItem(R.id.nav_download_address).setVisible(false);

            if (Integer.parseInt(Common.schoolId)==1051)
                nav_Menu.findItem(R.id.nav_change_password).setVisible(false);
            else
                nav_Menu.findItem(R.id.nav_change_password).setVisible(true);

            //setnavigation header
        View header = navigationView.inflateHeaderView(R.layout.nav_header_home);
        txt_name = (TextView) header.findViewById(R.id.nav_name);
        txt_email = (TextView) header.findViewById(R.id.nav_email);
        profile_pic = (CircularNetworkImageView) header.findViewById(R.id.Proimage);


        if( PrimesysTrack.sharedPreferences.contains(key_USER))
        {
            txt_email.setText(PrimesysTrack.sharedPreferences.getString(key_USER,""));
            txt_name.setText(PrimesysTrack.sharedPreferences.getString(key_fname,""));
            //  Picasso.with(context).load(Common.Relative_URL+Common.userid+"_"+Common.userid+".jpg").transform(new CircleTransform()).into(profile_pic);

        }
        else{
            txt_email.setText("");
            txt_name.setText("");

        }

        String Photo=PrimesysTrack.sharedPreferences.getString(key_UserPic,"");

        if (!Photo.equals(""))
        {
            Bitmap bmp = Common.bytebitmapconversion(Photo);
            HomeNewRailway.profile_pic.setImageBitmap(Common.getRoundedShape(bmp));

        }
        else {
            profile_pic.setImageResource(R.mipmap.ic_icon);
        }

        Typeface typeFace = Typeface.createFromAsset(getAssets(), Common.fontType1);
        txt_name.setTypeface(typeFace);
        txt_email.setTypeface(typeFace);
        c = Calendar.getInstance();
         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        }catch ( Exception e){
            e.printStackTrace();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            if (Common.roleid.equalsIgnoreCase("1")||Common.roleid.equalsIgnoreCase("2")){
                Common.ShowSweetAlert(context,"You are not eligible for this feature.Please contact to admin.");
            }else {
                if (Integer.parseInt(Common.schoolId)==1051)
                    Common.ShowSweetAlert(context,"You are not eligible for this feature.Please contact to admin or contact@primesystech.com.");

                else {
                    Intent Add = new Intent(context, UserProfileActivity.class);
                    startActivity(Add);
                }
            }

        }
        else if (id == R.id.nav_change_password) {

            if (Integer.parseInt(Common.schoolId)==1051)
                Common.ShowSweetAlert(context,"You are not eligible for this feature.Please contact to admin or contact@primesystech.com.");

            else {
                Intent Add = new Intent(context, ChanagePassword.class);
                startActivity(Add);
            }
        }
        else if (id == R.id.nav_renew_device) {


            if (Common.PlatformRenewalStatus){
                Intent Add = new Intent(context, DeviceLevelRenewService.class);
                startActivity(Add);
            }else {
                Intent Add = new Intent(context, RenewServiceActivity.class);
                startActivity(Add);
            }


       } else if (id == R.id.nav_aboutus) {
            Intent Add=new Intent(context,AboutPageActivity.class);
            startActivity(Add);
        }else if (id == R.id.nav_contactus) {
            Intent Add=new Intent(context,ContactUSActivity.class);
            startActivity(Add);
        }
        else if (id == R.id.nav_signout) {
            if(PrimesysTrack.sharedPreferences.contains(key_IS))
                isfirst = PrimesysTrack.sharedPreferences.getBoolean(key_IS, true);

            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_DEVICE_LIST);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_MODULE);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SLIDER);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_GEOFENCE_LIST);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_MODULE);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SLIDER);
            PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_SMSFUNC_LIST);


            if (!isfirst) {
                editor = PrimesysTrack.sharedPreferences.edit();
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
                Common.ShowSweetAlert(context, "Please Login First");


                try {
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    if (accessToken != null) {
                        LoginManager.getInstance().logOut();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Intent startMain = new Intent(context, LoginActivity.class);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    finishAndRemoveTask();

                } else {
                    Intent startMain = new Intent(context, LoginActivity.class);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    finish();

                }

        } else if (id == R.id.nav_share) {
                shareIt();

        }
         else if (id == R.id.nav_download_address) {
            getFeartureAddress();

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


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.home_menu, menu);

     *//*   // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.ic_notification);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils2.setBadgeCount(this, icon, 2);*//*
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        *//*menu.findItem(R.id.adduser).setIcon(new IconDrawable(this, IconValue.fa_user_plus).colorRes(R.color.primary).actionBarSize());
        menu.findItem(R.id.Invitation).setIcon(new IconDrawable(this, IconValue.fa_send_o).colorRes(R.color.primary).actionBarSize());*//*
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           *//* case R.id.adduser:
                if(Common.getConnectivityStatus(context)){
                    Intent intent =new Intent(context,AddNewFriend.class);
                    startActivity(intent);
                }
                else
                    Common.ShowSweetAlert(context,"Turn ON Internet Connection");
                return true;*//*

            case R.id.back:
                onBackPressed();
                return true;
            *//*case R.id.Invitation:
                if(Common.getConnectivityStatus(context)){

                    Intent i=new Intent(context,TrackSwitch.class);
                    startActivity(i);


                }
                else
                    Common.ShowSweetAlert(context, "Turn ON Internet Connection");
                return true;*//*

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    // JSON request to get the history
    String makeJSONHistory(String date)
    {
        PrimesysTrack.mDbHelper.truncateTables("db_history");
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event","get_tracking_history");
            if(Common.roleid.equals("5"))
                jo.put("student_id","demo_student");
            else
                jo.put("student_id", Common.currTrack);
            jo.put("timestamp", Common.convertToLong(date));
            trackSTring=jo.toString();
        }
        catch(Exception e)
        {

        }
        return trackSTring;
    }


    protected String makeJSON(String Id) {
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event","start_track");
            if(Common.roleid.equals("5"))
            {
                jo.put("student_id","demo_student");
                Common.currTrack="demo_student";
            }else{
                jo.put("student_id",Integer.parseInt(Id));
                Common.currTrack=Id;
            }
            trackSTring=jo.toString();
        }
        catch(Exception e)
        {

        }

        Log.e("Send req  chiled---", trackSTring+" "+Id);
        return trackSTring;
    }



    @Override
    public void onBackPressed() {


        try {

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

            if (PrimesysTrack.sharedPreferences.getString(key_Roll_id, "").equals("16")){
                    Common.userid = PrimesysTrack.sharedPreferences.getString(key_id, "0");
                    Common.roleid ="16";
                super.onBackPressed();

            }else {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Sure to exit Primesys Track?")
                        .setCancelText("No!")
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

        }catch (Exception e){
            e.printStackTrace();
        }



    }



    private long value(String string) {
        string = string.trim();
        if( string.contains( "." )){
            final int index = string.lastIndexOf( "." );
            return value( string.substring( 0, index ))* 100 + value( string.substring( index + 1 ));
        }
        else {
            return Long.valueOf( string );
        }


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
                System.out.println("----------web_update--------curVersion----"+curVersion);

                String newVersion = curVersion;
              /*  newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + package_name)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();*/
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + package_name+ "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

                result=newVersion.toString();
                MarketVersion=result;

                MarketVersionName=value(result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String MarketVersionName1) {

            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //get the app version Name for display
            ExistingVersion=pInfo.versionName;
            
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

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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
                            context.getPackageManager().getPackageInfo("com.android.vending", 0);

                            url = "market://details?id=" + "com.primesys.VehicalTracking";
                        } catch ( final Exception e ) {
                            e.printStackTrace();
                            url = "https://play.google.com/store/apps/details?id=" + "com.primesys.VehicalTracking";
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


    private void EnableLocationProvider() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {


        }
   /*     if (gps_enabled != true && network_enabled != true) {
            GPSEnableSetting go = new GPSEnableSetting();
            go.GPSDialog(context);


        }else {
            try {
                if (sharedPreferences.getBoolean(key_Location_Enable, false)&&!Common.ServiceRunningcheck(context)){
                    Intent startServiceIntent =new Intent(getBaseContext(), Map_service.class);
                    startService(startServiceIntent);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
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


        Intent NoanswerIntent = new Intent(context, NotificationCancel.class);
        NoanswerIntent.putExtra("notificationId",notificationId);
        answerIntent.setAction("No");
        PendingIntent pendingIntentNo =PendingIntent.getBroadcast(getApplicationContext(), 0, NoanswerIntent, 0);

        notificationBuilder.addAction(R.drawable.thumbs_down, "No", pendingIntentNo);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(contentIntent);


        notificationBuilder.getNotification().flags |= FLAG_AUTO_CANCEL;

        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        notification.flags = FLAG_AUTO_CANCEL;
        notification.defaults |= DEFAULT_SOUND;
        notifyMgr.notify(135, notification);




    }






    //Stop track
    String StopTracKEvent() {
        String trackSTring = "{}";
        try {
            JSONObject jo = new JSONObject();
            jo.put("event", "stop_track");
            trackSTring = jo.toString();

            Log.e(APIController.TAG,"------------************//Stoptrack calll");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackSTring;
        // TODO Auto-generated method stub

    }


    public HashSet getUniqueDeviceType(ArrayList<DeviceDataDTO> tracklist){
        for (DeviceDataDTO data:tracklist) {
                setDeviceType.add(data.getType());
        }

        return setDeviceType;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
          //  Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }




    private void getFeartureAddress() {
        reuestQueue = Volley.newRequestQueue(context);
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading data,please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        //JSon object request for reading the json data
      //  stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8080/TrackingAppDB/TrackingAPP/UserServiceAPI/GetFeatureAddress", new Response.Listener<String>() {
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetFeatureAddress", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try{

                    Log.e(PrimesysTrack.TAG,"getFeartureAddress  data respo--------------"+response);

                    parseFeatureAddressData(response);
                    pDialog.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                }

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
               // params.put("ParentId", "55");
                params.put("ParentId", Common.userid);

                Log.e(PrimesysTrack.TAG,"getFeartureAddress  Req--- " + params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    private void  parseFeatureAddressData(String response) {

        try {
            // mMap.clear();

            JSONArray joArray = new JSONArray(response);
            for (int i=0;i<joArray.length();i++){

                JSONObject jsonObject= (JSONObject) joArray.get(i);
                RailWayAddressDTO railDto=new RailWayAddressDTO();
                ArrayList<FeatureAddressDetailsDTO> FeatureAddressDetailslist=new ArrayList<FeatureAddressDetailsDTO>();

                railDto.setChainage(jsonObject.getString("chainage"));
                railDto.setDivision(jsonObject.getString("division"));
                railDto.setFileName(jsonObject.getString("fileName"));
                railDto.setLine(jsonObject.getString("line"));
                railDto.setMode(jsonObject.getString("mode"));
                railDto.setRailWay(jsonObject.getString("railWay"));
                railDto.setStationFrom(jsonObject.getString("stationFrom"));
                railDto.setStationTo(jsonObject.getString("stationTo"));
                railDto.setTrolley(jsonObject.getString("trolley"));

                JSONArray jsonArrayAddressDetails=jsonObject.getJSONArray("featureAddressDetail");

                for (int j = 0; j < jsonArrayAddressDetails.length(); j++) {
                    FeatureAddressDetailsDTO addressDto=new FeatureAddressDetailsDTO();

                    JSONObject jsonAddObject= (JSONObject) jsonArrayAddressDetails.get(j);
                    addressDto.setDistance(jsonAddObject.getDouble("distance"));
                    addressDto.setFeature_image(jsonAddObject.getString("feature_image").replaceAll("~", "").trim());
                    addressDto.setFeatureCode(jsonAddObject.getInt("featureCode"));
                    addressDto.setFeatureDetail(jsonAddObject.getString("featureDetail"));
                    addressDto.setKiloMeter(jsonAddObject.getDouble("kiloMeter"));
                    addressDto.setLatitude(jsonAddObject.getDouble("latitude"));
                    addressDto.setLongitude(jsonAddObject.getDouble("longitude"));
                    addressDto.setSection(jsonAddObject.getString("section"));
                    addressDto.setBlockSection(jsonAddObject.getString("blockSection"));

                    FeatureAddressDetailslist.add(addressDto);
                    //   addMarkerToMap(addressDto,j,jsonObject.getString("stationFrom"),jsonObject.getString("stationTo"));
                    //SetAllAddressLocation(addressDto,j);

                }
                railDto.setFeatureAddressDetail(FeatureAddressDetailslist);
                Log.e(PrimesysTrack.TAG,"FeatureAddressDetailslist------size---"+FeatureAddressDetailslist.size());
                railWayAddressList.add(railDto);


            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {

            if (railWayAddressList.size()>0){
                PrimesysTrack.mDbHelper.insertFeatureAddress(railWayAddressList);
                Common.ShowSweetSucess(context,context.getResources().getString(R.string.update_address_succes));
            }else Common.ShowSweetAlert(context,context.getResources().getString(R.string.update_address_unsucces));
          /*  for (RailWayAddressDTO dto:railWayAddressList) {

                for (int i=0;i<dto.getFeatureAddressDetail().size();i++){
                    addMarkerToMap(dto.getFeatureAddressDetail().get(i),i);

                }

            }*/
        }


    }
}
