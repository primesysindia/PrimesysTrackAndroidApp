package com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.primesys.VehicalTracking.ActivityMykiddyLike.AddsViewpagerPagerAdapter;
import com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNew;
import com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNewRailway;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.MainSliderImageDTO;
import com.primesys.VehicalTracking.Dto.UserModuleDTO;
import com.primesys.VehicalTracking.MyAdpter.GridModuleAdapter;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.MyCustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Mine_ModuleFragment extends Fragment {

    private Context context;
    private View rootView;
    RecyclerView ModuleRecycleview;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
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
    private int year,month,day;
    private Calendar c;
    private Context contextSl;
    private ViewPager swipeViewPager;
    private AddsViewpagerPagerAdapter swipeViewPagerAdapter;
    private int currentPage;
    Handler handler = new Handler();
    private ArrayList<DeviceDataDTO> childlist;
    private ListView listStudent;
    public static DeviceDataDTO CurrentDeviceSelect;
    private SweetAlertDialog pDialog;
    StudentListAdpter padapter;

    public Mine_ModuleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.content_main, container, false);
        context = container.getContext();
         contextSl = getActivity().getApplicationContext();

        FindviewbyId(rootView);
        Modulelist= PrimesysTrack.mDbHelper.Show_Module_list();
        Sliderlist=PrimesysTrack.mDbHelper.Show_Slider_list();


        try {

            if (Common.getConnectivityStatus(context)){

                if (Modulelist.size()>0){
                    Log.e("GridMod Offilnerespo", "0000000000000000000");

                    mAdapter = new GridModuleAdapter(context, 0, Modulelist);
                    ModuleRecycleview.setAdapter(mAdapter);
                }else  GetUserMOdeule();

                if (Sliderlist.size()>0&&((c.getTimeInMillis()/1000)-sharedPreferences.getLong(Common.HomeAPI_UpdateTime,c.getTimeInMillis()))<Common.HomeApiIntervalTime){

                    swipeViewPagerAdapter = new AddsViewpagerPagerAdapter(context, Sliderlist);
                    swipeViewPager.setAdapter(swipeViewPagerAdapter);
                    setAutoSwipe();
                }else  GetMainSliderImage();


            }else Common.ShowSweetAlert(context,context.getResources().getString(R.string.str_conn_error));


        }catch (Exception e){
            e.printStackTrace();
        }




        CheckStudent(context);


        return rootView;
    }

    private void FindviewbyId(View rootView) {
        //Initialize data
        sharedPreferences = context.getSharedPreferences("User_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        int gridViewEntrySize = getResources().getDimensionPixelSize(R.dimen.grip_view_entry_size);
        int gridViewSpacing = getResources().getDimensionPixelSize(R.dimen.grip_view_spacing);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int numColumns = (display.getWidth() ) / (gridViewEntrySize );

        /*ModuleRecycleview=(RecyclerView)rootView.findViewById(R.id.home_modulelist);
        mLayoutManager = new GridLayoutManager(context,numColumns);
        ModuleRecycleview.setLayoutManager(mLayoutManager);
*/
        ModuleRecycleview=(RecyclerView)rootView.findViewById(R.id.home_modulelist);
        mLayoutManager = new LinearLayoutManager(context);
        ModuleRecycleview.setLayoutManager(mLayoutManager);

      /*  DividerItemDecoration verticalDecoration = new DividerItemDecoration(ModuleRecycleview.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        ModuleRecycleview.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(ModuleRecycleview.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        ModuleRecycleview.addItemDecoration(horizontalDecoration);*/



        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

/*        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 250);
        ModuleRecycleview.setLayoutManager(layoutManager);*/

        swipeViewPager = (ViewPager)rootView.findViewById(R.id.add_viewPager);
        swipeViewPagerAdapter = new AddsViewpagerPagerAdapter(getActivity().getApplicationContext(), Sliderlist);
        swipeViewPager.setAdapter(swipeViewPagerAdapter);
        setAutoSwipe();

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

    }



    private void setAutoSwipe() {


        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == Sliderlist.size()) {
                    currentPage = 0;
                }
                swipeViewPager.setCurrentItem(currentPage++, true);
            }
        };
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 4000);
    }

    private void GetMainSliderImage() {
        {

            reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
            final MyCustomProgressDialog pDialog = Common.ShowProgress(context);

            //JSon object request for reading the json data
            stringRequest = new StringRequest(Request.Method.GET,Common.TrackURL+"ParentAPI/GetMainSliderImage",new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {


                    parseSliderJSON(response);
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

               /* @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("UserId", Common.userid);
                    System.out.println("REq----------"+params);
                    return params;
                }*/

            };


            stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);

            // Adding request to request queue
            reuestQueue.add(stringRequest);
        }




    }

    private void parseSliderJSON(String response) {

        Log.e("SLder Respo", response);
        try {

            if (Sliderlist.size()>0){
                Sliderlist.clear();
            }
            JSONArray jarray=new JSONArray(response);
            if (Sliderlist.size()>0)
                Sliderlist.clear();
            for (int i=0;i<jarray.length();i++)
            {
                JSONObject rs=jarray.getJSONObject(i);

                MainSliderImageDTO dto=new MainSliderImageDTO();
                dto.setSliderId(rs.getString("SliderId"));
                dto.setShopId(rs.getString("ShopId"));
                dto.setSliderImage(rs.getString("SliderImage"));
                dto.setCreatedAt(rs.getString("CreatedAt"));

                Sliderlist.add(dto);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            PrimesysTrack.mDbHelper.Insert_Slider_list(Sliderlist);
            swipeViewPagerAdapter = new AddsViewpagerPagerAdapter(context, Sliderlist);
            swipeViewPager.setAdapter(swipeViewPagerAdapter);
            setAutoSwipe();
            editor.putLong(Common.HomeAPI_UpdateTime,Common.getGMTTimeStampFromDate(day + "-"+ (month+ 1) + "-" + year+" 01:00 am"));
            editor.commit();
        }




    }


    private void GetUserMOdeule()
    {
        reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
        final MyCustomProgressDialog pDialog = (MyCustomProgressDialog) MyCustomProgressDialog.ctor(context);
        pDialog.setCancelable(false);

        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"ParentAPI/GetUserNewModuleList",new Response.Listener<String>() {


            @Override
            public void onResponse(String response)
            {

                Log.e("parseModuleJSON respo", response);
                parseModuleJSON(response);
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
                params.put("RoleId", Common.roleid);

                System.out.println("REq----------"+params);
                return params;
            }

        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setTag(TAG);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }






    private void parseModuleJSON(String response) {

        if (Modulelist.size() > 0) {
            Modulelist.clear();
        }
        try {
            JSONArray jarray = new JSONArray(response);
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject rs = jarray.getJSONObject(i);

                UserModuleDTO dto = new UserModuleDTO();
                dto.setModuleId("" + rs.getString("ModuleId"));
                dto.setModule("" + rs.getString("Module"));
                dto.setModuleTitle("" + rs.getString("ModuleTitle"));
                dto.setModuleDesc("" + rs.getString("ModuleDesc"));
                dto.setModuleActivity("" + rs.getString("ModuleActivity"));
                dto.setImageUrl("" + rs.getString("ImageUrl"));

                Modulelist.add(dto);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            mAdapter = new GridModuleAdapter(context, 0, Modulelist);
            ModuleRecycleview.setAdapter(mAdapter);
            PrimesysTrack.mDbHelper.Insert_Module_list(Modulelist);


            Log.e("Time----------",""+day + "-"+ (month + 1) + "-" + year+" 01:00 am");


        }




    }



    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }


    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if (Common.FeatureAddressEnable)
                        HomeNewRailway.Bottom_navigation.setSelectedItemId(R.id.navigation_track);
                    else
                        HomeNew.Bottom_navigation.setSelectedItemId(R.id.navigation_track);
                    return true;
                }
                return false;
            }
        });

    }



    public void CheckStudent(Context context1) {

        childlist=PrimesysTrack.mDbHelper.Show_Device_list();

        if (Common.getConnectivityStatus(context1)&& PrimesysTrack.mDbHelper.Show_Device_list().size()==0) {
            // Call Api to get track information
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new TrackInfrmation().executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR,
                            (Void) null);
                } else {
                    new TrackInfrmation().execute();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {

            if (childlist.size()>1)
                ShowListofStudent();
            else {
                CurrentDeviceSelect = childlist.get(0);
            }

        }


    }



  /*  private void ShowListofStudent() {
        // custom dialog

        final Dialog dialog = new Dialog(context);
        dialog.setTitle(context.getResources().getString(R.string.select_device)+" For SMS Function");

        dialog.setContentView(R.layout.dialog_studentlist);
        dialog.setCancelable(false);
        dialog.closeOptionsMenu();
      //  dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.show();
        listStudent=(ListView)dialog.findViewById(R.id.student_list);
        Button cancel = (Button) dialog.findViewById(R.id.d_cancel);
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment selectedFragment = Fragment.instantiate(context, HomeTrackFragment.class.getName());
                transaction.replace(R.id.track_container, selectedFragment);
                transaction.commit();
               dialog.dismiss();
            }
        });
//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
        padapter=new StudentListAdpter(context, R.layout.fragment_mapsidebar, childlist);
        listStudent.setAdapter(padapter);

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                     CurrentDeviceSelect = childlist.get(0);


                dialog.dismiss();
            }
        });


        dialog.show();
    }
*/

    private void ShowListofStudent() {

        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_device_list);
            dialog.setCancelable(false);
            // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.closeOptionsMenu();
            dialog.show();
            final TextView tv_title=(TextView)dialog.findViewById(R.id.d_title);
            tv_title.setText("Select Device");
            listStudent=(ListView)dialog.findViewById(R.id.student_list);
            Button cancel = (Button) dialog.findViewById(R.id.d_cancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Common.FeatureAddressEnable)
                        HomeNewRailway.Bottom_navigation.setSelectedItemId(R.id.navigation_track);
                    else
                        HomeNew.Bottom_navigation.setSelectedItemId(R.id.navigation_track);
                    dialog.dismiss();
                }
            });
//		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
            padapter=new StudentListAdpter(context, R.layout.fragment_mapsidebar, childlist);
            listStudent.setAdapter(padapter);

            listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    CurrentDeviceSelect = childlist.get(position);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //Track Informatiion
    class TrackInfrmation extends AsyncTask<Void, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog =Common.ShowSweetProgress(context, "Progress wait.......");
        }
        @Override
        protected String doInBackground(Void... params) {
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost   httpost=new HttpPost(Common.URL+"ParentAPI.asmx/GetTrackInfo");
                List<NameValuePair> param=new ArrayList<NameValuePair>(1);
                param.add(new BasicNameValuePair("ParentId", Common.userid));
                httpost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse response = httpclient.execute(httpost);
                result= EntityUtils.toString(response.getEntity());
                Log.e("response List Map", ""+result);
            }catch(Exception e){
                result=e.getMessage();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            parsingTrackInfo(result);

        }
    }
    public void parsingTrackInfo(String result) {
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
                dmDetails.setImei_no(joObject.getString("DeviceID"));

                childlist.add(dmDetails);
            }


        }catch(Exception e){
            Log.e("Exception", ""+e);
        }finally{
            //it work Better but take time to Load
            if (childlist.size()>0) {

                //Insert Offeline data
                PrimesysTrack.mDbHelper.Insert_Device_list(childlist);
                if (childlist.size()>1)
                    ShowListofStudent();
                else
                    CurrentDeviceSelect=childlist.get(0);

            }else{
                Common.ShowSweetAlert(context,"No User Information" );
            }

        }
    }




}
