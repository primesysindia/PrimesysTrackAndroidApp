package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.Dto.persondetail;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.GPSEnableSetting;
import com.primesys.VehicalTracking.VTSFragments.ACCReport;
import com.primesys.VehicalTracking.VTSFragments.SMSFuction;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragment;
import com.primesys.VehicalTracking.VTSFragments.VTSFunction;
import com.primesys.VehicalTracking.VTSFragments.historyFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    public  Context context=Home.this;
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    ListView personlist;
    final String TAG="REquest";
    ArrayList<GmapDetais> tracklist=new ArrayList<GmapDetais>();
    ArrayList<persondetail> tracklistcopy=new ArrayList<persondetail>();
    public SharedPreferences sharedPreferences;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";

    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    public  String key_Roll_id="Roll_id";
    private String key_UserPic="UserPic";

    SharedPreferences.Editor editor ;
    private boolean isfirst;
    private NavigationView navigationView;
    private TextView txt_name,txt_email;
    public static CircularNetworkImageView profile_pic;
    private ImageLoader imageLoader;
    private String defaultImage;
    private int cnt=0;
    private ShowMapAdapter myAdapter;
    public static Boolean trackInfo=false;
    private DBHelper helper= DBHelper.getInstance(Home.this);
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_map,
            R.drawable.ic_history,
            R.drawable.ic_history,
            R.drawable.ic_vtsfuc,
            R.drawable.ic_smsfuc,
            R.drawable.ic_report

    };
    private Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EnableLocationProvider();

        findviewbyid();
        if(Common.getConnectivityStatus(context)){
            GetAllTrackperson();
        }
        personlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {



              GmapDetais user=tracklist.get(position);

                trackInfo = false;
            /*    BitmapDrawable bitmap_draw = (BitmapDrawable) imgchild.getDrawable();
                Bitmap bmp = bitmap_draw.getBitmap();
                ShowMap.bmp1 = bmp;
*/

                try {

                    URL url = new URL(Common.Relative_URL + user.getPath().replaceAll(" ", "%20"));
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    ShowMap.bmp1 = Common.getRoundedShape(myBitmap);


                    LoginActivity.mClient.sendMessage(StopTracKEvent());
                    LoginActivity.mClient.sendMessage(makeJSON(user.getId()));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        setupTabIcons();

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeFace);
                    ((TextView) tabViewChild).setTextSize(15);
                }
            }
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
               /* Toast.makeText(context,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();*/

                switch (position) {
                    case 0:
                       // ShowMapFragment.StartUiCOuntDown();


                        break;
                    case 1:
                        break;
                    case 2:

                        if (Common.VtsFuncAllow){
                            VTSFunction vts=new VTSFunction();
                            vts.CheckStudent(context);
                        }else if (Common.VtsSmsAllow)
                        {
                            SMSFuction smsf=new SMSFuction();
                            smsf.CheckStudent(context);
                        }else if (Common.AccReportAllow){
                            ACCReport acc=new ACCReport();
                            acc.CheckStudent(context);
                        }

                        break;
                    case 3:
                        /*if (Common.AccReportAllow){
                            ACCReport acc=new ACCReport();
                            acc.CheckStudent(context);
                        }else if (Common.VtsSmsAllow) {
                     SMSFuction smsf = new SMSFuction();
                     smsf.CheckStudent(context);
                 }*/
                        if (Common.VtsFuncAllow){
                            VTSFunction vts=new VTSFunction();
                            vts.CheckStudent(context);
                        }else if (Common.AccReportAllow)
                        { ACCReport acc=new ACCReport();
                            acc.CheckStudent(context);


                        }else if (Common.VtsSmsAllow){
                            SMSFuction smsf=new SMSFuction();
                            smsf.CheckStudent(context);
                        }

                        break;

                    case 4:

                        if (Common.AccReportAllow){
                            ACCReport acc=new ACCReport();
                            acc.CheckStudent(context);

                        }else if (Common.VtsSmsAllow)
                        {
                            SMSFuction smsf=new SMSFuction();
                            smsf.CheckStudent(context);
                        }

                        break;

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

    }

    private void GetAllTrackperson() {


        reuestQueue = Volley.newRequestQueue(context);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"ParentAPI.asmx/GetTrackInfo",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseData(response);


                pDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                            parseData(new String(error.networkResponse.data));
                        }
                    }
                })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ParentId", Common.userid);

                System.err.println("Track person Req--- "+params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);


    }

    protected void parseData(String result) {

        Log.e("HOmE Responce----", result);
        // TODO Auto-generated method stub
        try{
            JSONArray joArray=new JSONArray(result);
            for (int i = 0; i < joArray.length(); i++) {
                JSONObject joObject =joArray.getJSONObject(i);
                GmapDetais dmDetails=new GmapDetais();
                if (i<=0) {
                    defaultImage=joObject.getString("Photo").replaceAll("~", "").trim();
                    if (Common.roleid.contains("5")) {
                        Common.currTrack="demo_student";

                    }else{
                        Common.currTrack=joObject.getString("StudentID");
                    }
                }
                dmDetails.setId(joObject.getString("StudentID"));
                dmDetails.setName(joObject.getString("Name"));

                dmDetails.setPath(joObject.getString("Photo").replaceAll("~", "").trim());
                dmDetails.setType(joObject.getString("Type"));
                tracklist.add(dmDetails);
            }


        }catch(Exception e){
            Log.e("Exception", "" + e);
        }finally{
            //it work Better but take time to Load
            if (tracklist.size()>0) {
                myAdapter=new ShowMapAdapter(context, R.layout.fragment_mapsidebar, tracklist,imageLoader);
                personlist.setAdapter(myAdapter);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
              /*  personlist.performItemClick(
                        personlist.getAdapter().getView(0, null, null),
                        0,
                        personlist.getAdapter().getItemId(0)); */
                cnt++;
            }else{
                Common.showToast("No User Information",context );
            }

        }





    }





   /* @Override
    protected void onRestart() {
        super.onRestart();
        if(Common.getConnectivityStatus(context)){
            GetAllTrackperson();
        }
    }*/

    private void findviewbyid() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        typeFace=Typeface.createFromAsset(context.getAssets(),"Times New Roman.ttf");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_home);
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.   closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);

                }
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        personlist = (ListView) findViewById(R.id.User_list);

        //Initialize data
        sharedPreferences = context.getSharedPreferences("User_data", Context.MODE_PRIVATE);
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
            txt_name.setText("");

        }

        String Photo=sharedPreferences.getString(key_UserPic,"");

        if (!Photo.equals(""))
        {
            Bitmap bmp = Common.bytebitmapconversion(Photo);
            Home.profile_pic.setImageBitmap(Common.getRoundedShape(bmp));

        }
        else {
            profile_pic.setImageResource(R.mipmap.ic_icon);
        }
        Common.userid = sharedPreferences.getString(key_id, "");

        Typeface typeFace = Typeface.createFromAsset(getAssets(), Common.fontType1);
        txt_name.setTypeface(typeFace);
        txt_email.setTypeface(typeFace);
        viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);

        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
      //  GetImage();


    }



    private void EnableLocationProvider() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if ( locationManager == null ) {
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){}
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex){}
        if ( !gps_enabled && !network_enabled ){


        }
        if (gps_enabled!=true && network_enabled!= true) {
            GPSEnableSetting go=new GPSEnableSetting();
            go.GPSDialog(context);
        }
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


        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sure to exit Primesys Track?")
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

            Intent Add=new Intent(context,UserProfileActivity.class);
            startActivity(Add);
        }else if (id == R.id.nav_change_password) {

            Intent Add=new Intent(context,ChanagePassword.class);
            startActivity(Add);
        }
       /* else if (id == R.id.nav_newrequest) {
            Intent Add=new Intent(context,NewRequest.class);
            startActivity(Add);

        } else if (id == R.id.nav_block) {
            Intent Add=new Intent(context,BlockTracking.class);
            startActivity(Add);
        }*/
        else if (id == R.id.nav_signout) {
            if(sharedPreferences.contains(key_IS))
                isfirst = sharedPreferences.getBoolean(key_IS, true);

            helper.truncateTables(DBHelper.TABLE_DEVICE_LIST);
            if (!isfirst) {
                editor = sharedPreferences.edit();
                editor.putString(key_id, "");
                editor.putString(key_PASS, "");
                editor.putString(key_Roll_id, "");
                editor.putString(key_UserPic, "");

                editor.remove(key_IS);

                editor.commit();

            }else {
                Common.ShowSweetAlert(context, "Please Login First");

            }


            try{
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null){
                    LoginManager.getInstance().logOut();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Intent startMain = new Intent(context,LoginActivity.class);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finishAndRemoveTask();

            }
            else
            {
                Intent startMain = new Intent(context,LoginActivity.class);
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
        helper.truncateTables("db_history");
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

        Log.e("Send req For track chiled---", trackSTring+" "+Id);
        return trackSTring;
    }



    //Stop track
    String StopTracKEvent() {
        String trackSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event","stop_track");
            trackSTring=jo.toString();
        }
        catch(Exception e)
        {

        }
        return trackSTring;
        // TODO Auto-generated method stub

    }




    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        System.err.println("--------------"+Common.VtsFuncAllow+"  "+Common.VtsSmsAllow);
       if (Common.VtsFuncAllow)
            tabLayout.getTabAt(2).setIcon(tabIcons[3]);
        if (Common.VtsSmsAllow&&Common.VtsFuncAllow)
        {
            tabLayout.getTabAt(3).setIcon(tabIcons[4]);
            if (Common.AccReportAllow)
                tabLayout.getTabAt(4).setIcon(tabIcons[5]);

        }
        else if (Common.VtsSmsAllow&&!Common.VtsFuncAllow)
        {
            tabLayout.getTabAt(2).setIcon(tabIcons[4]);
            if (Common.AccReportAllow)
                tabLayout.getTabAt(3).setIcon(tabIcons[5]);
        }



    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ShowMapFragment(), "Track");
        adapter.addFrag(new historyFragment(), "History");

       if (Common.VtsFuncAllow)
           adapter.addFrag(new VTSFunction(), "VTS Function");
        if (Common.VtsSmsAllow)
            adapter.addFrag(new SMSFuction(), "SMS Functions");
        if (Common.AccReportAllow)
            adapter.addFrag(new ACCReport(), "Report");


        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

       /* @Override
        public Fragment getItem(int position) {
            Log.e("INside ---","Fragment---------------------------------"+position);

            return mFragmentList.get(position);

        }
*/
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }



        @Override
        public Fragment getItem(int position) {
            Fragment fragment =null;
            switch (position) {
                case 0:
                    fragment = Fragment.instantiate(context, ShowMapFragment.class.getName());
                    Log.e("INside ---", "ShowMapFragment---------------------------------");

                    break;
                case 1:
                   /* fragment = Fragment.instantiate(context, InfoFragment.class.getName());
                    Log.e("INside ---", "InfoFragment---------------------------------");*/
                    fragment = Fragment.instantiate(context, historyFragment.class.getName());
                    Log.e("INside ---", "historyFragment---------------------------------");
                    break;
                case 2:
                    if (Common.VtsFuncAllow)
                    fragment = Fragment.instantiate(context, VTSFunction.class.getName());
                    else
                    fragment = Fragment.instantiate(context, SMSFuction.class.getName());


                    Log.e("INside ---", "historyFrsagment---------------------------------");


                    break;
                case 3:
                    if (Common.AccReportAllow)
                    {
                        fragment = Fragment.instantiate(context, ACCReport.class.getName());
                        Log.e("INside ---", "AccReportAllow---------------------------------");

                    }else
                    {
                        fragment = Fragment.instantiate(context, SMSFuction.class.getName());
                        Log.e("INside ---", "SMSFuction---------------------------------");

                    }

                    break;
                case 4:
                    fragment = Fragment.instantiate(context, ACCReport.class.getName());
                    Log.e("INside ---", "SMSFuction---------------------------------");

                    break;
            }
            return fragment;
        }




        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
