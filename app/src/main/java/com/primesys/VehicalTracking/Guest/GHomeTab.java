package com.primesys.VehicalTracking.Guest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.persondetail;
import com.primesys.VehicalTracking.Guest.Fragments.GACCReport;
import com.primesys.VehicalTracking.Guest.Fragments.GSMSFuction;
import com.primesys.VehicalTracking.Guest.Fragments.GShowMapFragment;
import com.primesys.VehicalTracking.Guest.Fragments.GhistoryFragment;
import com.primesys.VehicalTracking.MyAdpter.ShowMapAdapter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.GPSEnableSetting;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragment;
import com.primesys.VehicalTracking.VTSFragments.historyFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GHomeTab extends AppCompatActivity
        {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    public  Context context=GHomeTab.this;
    StringRequest stringRequest;
    RequestQueue reuestQueue;
    ListView personlist;
    final String TAG="REquest";
    ArrayList<DeviceDataDTO> tracklist=new ArrayList<DeviceDataDTO>();
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
    public static long MarketVersionName;
    public static long ExistingVersionName;
    public  String package_name="com.primesys.VehicalTracking";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EnableLocationProvider();

        try {
            findviewbyid();
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
                       // GShowMapFragment.StartUiCOuntDown();


                        break;
                    case 1:
                        break;
                    case 2:
                            GSMSFuction smsf=new GSMSFuction();
                            smsf.CheckStudent(context);

                        break;
                    case 3:


                         GACCReport acc=new GACCReport();
                            acc.CheckStudent(context);

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




    private void findviewbyid() throws ParseException {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        typeFace=Typeface.createFromAsset(context.getAssets(),"Times New Roman.ttf");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_home);
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setDrawerIndicatorEnabled(false);
        /*toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
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
        toggle.syncState();*/
        personlist = (ListView) findViewById(R.id.User_list);

        //Initialize data
        sharedPreferences = context.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();

/*
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
            GHomeTab.profile_pic.setImageBitmap(Common.getRoundedShape(bmp));

        }
        else {
            profile_pic.setImageResource(R.mipmap.ic_icon);
        }
        Common.userid = sharedPreferences.getString(key_id, "");

        Typeface typeFace = Typeface.createFromAsset(getAssets(), Common.fontType1);
        txt_name.setTypeface(typeFace);
        txt_email.setTypeface(typeFace);*/
        viewPager = (ViewPager) findViewById(R.id.viewpager);

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

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sure to exit Primesys Track?")
                .setCancelText("No,cancel !")
                .setConfirmText("Yes!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent login = new Intent(context, LoginActivity.class);
                        // set the new task and clear flags
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(login);

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


/*
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
        else if (id == R.id.nav_renew_device) {
            Intent Add = new Intent(context, RenewServiceActivity.class);
            startActivity(Add);
        }

      *//*  } else if (id == R.id.nav_block) {
            Intent Add=new Intent(context,BlockTracking.class);
            startActivity(Add);
        }*//*
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
    }*/


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



    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[4]);
        tabLayout.getTabAt(3).setIcon(tabIcons[5]);





    }

    private void setupViewPager(ViewPager viewPager) throws ParseException {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ShowMapFragment(), "Track");
        adapter.addFrag(new historyFragment(), "History");
        adapter.addFrag(new GSMSFuction(), "SMS Functions");
        adapter.addFrag(new GACCReport(), "Report");


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
                    fragment = Fragment.instantiate(context, GShowMapFragment.class.getName());
                    Log.e("INside ---", "GShowMapFragment---------------------------------");

                    break;
                case 1:
                   /* fragment = Fragment.instantiate(context, InfoFragment.class.getName());
                    Log.e("INside ---", "InfoFragment---------------------------------");*/
                    fragment = Fragment.instantiate(context, GhistoryFragment.class.getName());
                    Log.e("INside ---", "GhistoryFragment---------------------------------");
                    break;

                case 2:

                        fragment = Fragment.instantiate(context, GSMSFuction.class.getName());
                        Log.e("INside ---", "GGSMSFuction---------------------------------");


                    break;
                case 3:
                    fragment = Fragment.instantiate(context, GACCReport.class.getName());
                    Log.e("INside ---", "GGSMSFuction---------------------------------");

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
