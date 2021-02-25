package com.primesys.VehicalTracking.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Activity.EmployeeAcivity.DriverEmpUsActivity;
import com.primesys.VehicalTracking.Activity.EmployeeAcivity.EmpHomeActivity;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeoFencingHomeNew;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeofencingNewdrawCircle;
import com.primesys.VehicalTracking.ActivityMykiddyLike.GeofencingUpdatedrawCircle;
import com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNew;
import com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNewRailway;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.Dto.LoginDetails;
import com.primesys.VehicalTracking.Dto.MessageMain;
import com.primesys.VehicalTracking.Guest.GDatabaseHelper;
import com.primesys.VehicalTracking.Guest.GSignup;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragment;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragmentFeatureAddressEnable;
import com.primesys.VehicalTracking.googlelogin.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    public static Client mClient;
    private Button login,btn_guest;
    private EditText text_username,text_password;
    private final String key_IS = "IS_FIRST";
    private final String key_USER = "USER";
    private final String key_PASS = "PASS";
    private final String key_id = "User_Id";
    private final String key_fname = "Fname";
    private final String key_Login_Status = "LOGIN_STATUS";
    private String url="URL";
    public static LoginDetails Ldetails = new LoginDetails();
    public static ArrayList<MessageMain> arrayTeacher = new ArrayList<MessageMain>();
    public static ArrayList<String> arrayParent = new ArrayList<String>();
    ObjectAnimator anim;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
  //  public static  SharedPreferences mPrefsGCM;
    private String regId="";
    public  final String key_REG_ID = "REG_ID";
    public static final String REG_ID = "REG_ID";
    private static final String APP_VERSION = "appVersion";
    private static final String GCM_KEY_SEND = "gcm_key_send";
   // private GoogleCloudMessaging gcm;
    boolean isfirst;
    TextView forget_password,register,lblTerms;
    Context login_context=LoginActivity.this;
    String username,password;
    private StringRequest stringRequest;
    private RequestQueue reuestQueue;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    private String key_Roll_id="Roll_id";
    private String key_groupname="GroupName";
    public static final String TAG="LoginTag";
    private SignInButton btn_googlesignin;
    private LoginButton fbloginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken accessToken;
    private Toolbar toolbar;
    private String facebbok_id,str_email,str_name;
    private SweetAlertDialog pDialog;
    String emailid, mobilenumber;
    static String deviceid;
    String key_User_id="User_Id";
    public static  Boolean getdata=false;

    private String phpurl="http://www.mykiddytracker.com/php/getAppId.php";
    private RadioGroup rgType;
    private String key_url_type="Url_Type";
    private CheckBox termAndCondition;
    private GDatabaseHelper GdbHelper;
    private ImageView img_splash;
    private String key_Admin_id="=Admin_Id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        sharedPreferences=login_context.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        deviceid = getDeviceID();
        emailid = getRegisteredID();
       Fabric.with(this, new Crashlytics());
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(!sharedPreferences.contains(key_IS))
            isfirst = sharedPreferences.getBoolean(key_IS, true);

        if (!isfirst) {
            setContentView(R.layout.screen_off_show);

            try {
                img_splash= (ImageView) findViewById(R.id.img_splash);
                Animation animation = AnimationUtils.loadAnimation(login_context, R.anim.custom_progress_dialog_animation);
                // la.startAnimation(animation);
                anim = ObjectAnimator.ofFloat(img_splash, "rotation", 0, 360);
                anim.setDuration(1000);
                anim.setRepeatCount(animation.INFINITE);
                anim.setRepeatMode(ObjectAnimator.RESTART);
                anim.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            btn_guest= (Button) findViewById(R.id.btn_guest);

            username = sharedPreferences.getString(key_USER,"");
            password = sharedPreferences.getString(key_PASS,"");
            Common.Url_Type = sharedPreferences.getString(key_url_type,"pt");

            System.err.println(" Inside is not first====="+username + " " + password);
            if(Common.getConnectivityStatus(login_context))
            {
                try{
                    postLoginRequest();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
          else {
                pDialog = new SweetAlertDialog(login_context, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Alert");
                pDialog.setContentText("Turn on internet connection!");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        finish();
                              }
                });
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }else {


            setContentView(R.layout.activity_login);
            findbyId();
            GdbHelper = new GDatabaseHelper(login_context, getFilesDir().getAbsolutePath());
            Log.e("-pathToSav-Logim--","----------"+ getFilesDir().getAbsolutePath());

            try {
                GdbHelper.prepareDatabase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            //SeT Username
            text_username.setText(sharedPreferences.getString(key_USER, ""));
            login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {

                        username = text_username.getText().toString().trim();
                        password = text_password.getText().toString().trim();

                        if (Common.getConnectivityStatus(login_context)) {
                            if (validate()){

                                if (termAndCondition.isChecked()) {
                                    postLoginRequest();

                                }
                                else {
                                    Common.ShowSweetAlert(login_context,"Please accept terms and conditions");

                                }
                            }



                        } else {
                            Common.ShowSweetAlert(login_context, "Turn on Internet Connection!");

                        }
                    } catch (Exception e) {
                        Log.e("Login Exception", e.getMessage());
                    }
                }

            });

            forget_password.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        Intent forget_password = new Intent(login_context,Forget_password.class);
                        startActivity(forget_password);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent forget_password = new Intent(login_context, SignupActivity.class);
                    startActivity(forget_password);
                }
            });

            btn_googlesignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent google = new Intent(login_context, SignInActivity.class);
                    google.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(google);
                }
            });


            ////Facebook Login
            fbloginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fbloginButton.setReadPermissions(Arrays.asList("public_profile, email"));        // If using in a fragment
                    //  loginButton.setFragment(this);
                    // Other app specific specialization


                }
            });


            // Callback registration
            callbackManager = CallbackManager.Factory.create();

            // Callback registration
            fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {

                                        // Application code

                                        try {
                                            //    Toast.makeText(LoginActivity.this,object.toString(),Toast.LENGTH_LONG).show();

                                             facebbok_id = object.getString("id");
                                             str_name = object.getString("name");
                                             str_email = object.getString("email");

                                            PostFacebookReq(facebbok_id, str_name, str_email);

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,location");
                        request.setParameters(parameters);
                        request.executeAsync();


                }

                @Override
                public void onCancel() {
                    Log.d("On cancel", "On cancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("On error", error.toString());
                }
            });


             rgType = (RadioGroup) findViewById(R.id.rg_type);

            rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.rb_pt){
                        Common.Url_Type="pt";

                           Common.URL="http://mykidtracker.in:81/API/";
                        Common.Relative_URL="http://mykidtracker.in:81";

                        }else if(checkedId == R.id.rb_vts){

                        Common.URL="http://mykidtracker.in:90/API/";
                        Common.Relative_URL="http://mykidtracker.in:90";
                        new SweetAlertDialog(login_context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Implemented In Feture..!")
                                .show();
                    }
                }
            });


            lblTerms.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {

                        final Dialog dialog = new Dialog(login_context);
                        dialog.setContentView(R.layout.maindialog);
                        dialog.setTitle("Agreement & Terms");
                        dialog.setCancelable(true);

                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

                        // set up text
                        TextView text = (TextView) dialog
                                .findViewById(R.id.TextView01);
                        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
                        text.setTypeface(custom_font);
                        text.setText(R.string.lots_of_text);
                        text.setPadding(5, 5, 5, 5);
                        Button buttoncancel = (Button) dialog
                                .findViewById(R.id.button1);
                        buttoncancel.setBackgroundColor(getResources().getColor(
                                R.color.colorPrimaryDark));
                        buttoncancel.setTextColor(getResources().getColor(
                                android.R.color.white));
                        buttoncancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

        }

        //FOr Guest User
        btn_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guest=new Intent(login_context, GSignup.class);
                startActivity(guest);
            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void findbyId() {

        // TODO Auto-generated method stub
        login= (Button) findViewById(R.id.btn_login);
        btn_guest= (Button) findViewById(R.id.btn_guest);

        register=(TextView) findViewById(R.id.link_signup);
       /* Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        login.setTypeface();peface(custom_font);*/
        text_username=(EditText)findViewById(R.id.input_email);
        text_password=(EditText)findViewById(R.id.input_password);
        //register=(TextView)findViewById(R.id.tv_reg);
        forget_password=(TextView)findViewById(R.id.tv_forgot_password);
        //forget_password.setTypeface(custom_font);
        btn_googlesignin=(SignInButton)findViewById(R.id.btn_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        termAndCondition = (CheckBox) findViewById(R.id.check_accept);
        lblTerms = (TextView) findViewById(R.id.lbl_terms);
        fbloginButton = (LoginButton) findViewById(R.id.fb_login_button);

    }


    // making the request for JSON Object
    void postLoginRequest()
    {
        reuestQueue= Volley.newRequestQueue(login_context); //getting Request object from it
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Login Wait");
        pDialog.setCancelable(false);
        if (isfirst)
            pDialog.show();
        Log.e(PrimesysTrack.TAG,"Login req-----postLoginRequest------ ");

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"LoginServiceAPI.asmx/GetLoginDetails",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               Log.e(PrimesysTrack.TAG,"Login result----------- " + response);
                if (response.contains("Invalid Userid/Passward")) {

                    Common.ShowSweetAlert(login_context, "Invalid Username or Password !");

                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
					/*Intent intent=new Intent(login_context,LoginActivity.class);
					startActivity(intent);*/
                } else {
                    editor = sharedPreferences.edit();
                    editor.putBoolean(key_IS, false);
                    editor.putString(key_USER, username);
                    editor.putString(key_PASS, password);
                    editor.putString(key_fname, Common.username);

                    editor.commit();
                    parseJSON(response);
                }
                if (pDialog.isShowing())
                    pDialog.dismiss();


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(PrimesysTrack.TAG,"Login erroer-----postLoginRequest------ "+new Gson().toJson(error));
                        System.out.println(error.toString());

                        if (pDialog.isShowing())
                            pDialog.dismiss();

                            if (error instanceof NetworkError) {
                            } else if (error instanceof ServerError) {
                            } else if (error instanceof AuthFailureError) {
                            } else if (error instanceof ParseError) {
                            } else if (error instanceof NoConnectionError) {
                            } else if (error instanceof TimeoutError) {
                              //  Toast.makeText(login_context, "Oops. Timeout error!", Toast.LENGTH_LONG).show();

                            }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.err.println("IN GET Login METHOD" + username + " " + password);
                params.put("username", username);
                params.put("password", password);
                params.put("App_Device_Id", deviceid);
              //  params.put("App_Emailid", emailid);
                params.put("App_Emailid", username);


                System.out.println("Login REquesr " + params.toString());

                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }


    //parse the result
    void parseJSON(String result)
    { JSONObject jo=null;

        JSONArray array = null;
        try {
            array = new JSONArray(result);
             jo = array.getJSONObject(0);
            Common.roleid = jo.getString("Role_ID");
            Common.userid = jo.getString("UserID");
            Common.username = jo.getString("userName");
            Common.Student_Count = jo.getInt("Student_Count");
            Common.schoolId=jo.getString("school_id");
            if(result.contains("WebUserID"))
            Common.WebUserId = jo.getString("WebUserID");

            if(result.contains("dist_unit"))
                Common.mesurment_unit = jo.getString("dist_unit");

            if (result.contains("PlatformRenewalStatus")){
                if (jo.getString("PlatformRenewalStatus").equals("1"))
                    Common.PlatformRenewalStatus =false;
                   else
                      Common.PlatformRenewalStatus = true;
            }

            if(!Common.PlatformRenewalStatus){
                if (result.contains("daysr"))
                    Common.TrackDay=Integer.parseInt(jo.getString("daysr"));
                else Common.TrackDay=-2;

            }
//            Common.SERVERIP = jo.getString("SocketUrl");
            Common.SERVERIP="192.168.1.102";

            Common.PORT=Integer.parseInt(jo.getString("SocketPort"));
            Common.TrackURL = "http://"+ Common.SERVERIP+":8080/TrackingAppDB/TrackingAPP/";

           if (jo.getString("VtsFuncAllow").equals("0"))
            Common.VtsFuncAllow =true;
            if (jo.getString("VtsSmsAllow").equals("0"))
            Common.VtsSmsAllow = true;

           if (jo.getString("ACCReportAllow").equals("0"))
                    Common.AccReportAllow=true;
          /*  if (jo.getString("ACCSqliteEnable").equals("0"))
                Common.ACCSqliteEnable=true;*/
            if (jo.getString("ACCSqliteEnable").equals("0"))
                Common.FeatureAddressEnable=true;
            else  Common.FeatureAddressEnable=false;

            Common.ACCSmsDeleteCheckCount=Integer.parseInt(jo.getString("ACCSmsDeleteCheckCount"));
            Common.ACCSMSDeleteNo=Integer.parseInt(jo.getString("ACCSMSDeleteNo"));
            if (result.contains("MarkerTimeDiff"))
            Common.MarkerTimeDiff=Integer.parseInt(jo.getString("MarkerTimeDiff"));
            if (result.contains("TrackReqTimeout"))
                Common.TrackReqTimeout=Integer.parseInt(jo.getString("TrackReqTimeout"));

            if (result.contains("PolylineDistLimit"))
                Common.PolylineDistLimit=Integer.parseInt(jo.getString("PolylineDistLimit"));

            if (result.contains("WrongwayTolerance"))
                Common.WrongWay_tolerance=Double.parseDouble(jo.getString("WrongwayTolerance"));

            if (result.contains("DeviceStatusReqTime"))
                Common.DeviceStatusReq_Time=Integer.parseInt(jo.getString("DeviceStatusReqTime"));
       //     System.out.println("TrackDay----" + Common.TrackDay);

            editor = sharedPreferences.edit();

            editor.putString(key_Roll_id, Common.roleid);
            editor.putString(key_User_id, Common.userid);
            editor.putString(key_fname, Common.username);

            editor.commit();
            // setting data for join
             Ldetails.setClass_id(jo.getString("ClassName"));

            Ldetails.setSchool_id(jo.getString("school_id"));
            Ldetails.setUserType(Common.roleid);
            Common.email=jo.getString("EmailID");
            Ldetails.setEmailId(jo.getString("EmailID"));
            Ldetails.setMobileNumber(jo.getString("MobileNo"));

            Common.markerImg=jo.getString("Photo");
            Toast.makeText(login_context, Common.markerImg, Toast.LENGTH_LONG);
            System.out.println("Photo----"+ Common.markerImg);
            Common.user_regType=jo.getString("RegistrationType");
            Log.e(TAG, Common.user_regType);




        PrimesysTrack.mDbHelper.getLastTimeStamp();
        //************Added by rupesh
        editor = sharedPreferences.edit();
        editor.putString(key_Roll_id, Common.roleid);
        editor.putString(key_url_type, Common.Url_Type);
        editor.putString("User_Id", Common.userid);

        editor.commit();

           /* if (checkPlayServices()) {
                //	RegisterGCM_Key();
                registerGCM();
            }
*/

            if (!Common.PlatformRenewalStatus) {
                if (Common.TrackDay > 0) {

                    if (Common.roleid.equals("5")) {
                        Client.SERVERIP = Common.SERVERIP;
                        arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, HomeNew.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    } else if (Common.roleid.equals("7")) {
                        Client.SERVERIP = Common.SERVERIP;
                        arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        if (Common.FeatureAddressEnable){
                            Intent intent = new Intent(login_context, HomeNewRailway.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(login_context, HomeNew.class);
                            startActivity(intent);
                        }
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    } else if (Common.roleid.equals("3")) {
                        Client.SERVERIP = Common.SERVERIP;
                        arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, HomeNew.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }
                    else if (Common.roleid.equals("15")) {
                        // Client.SERVERIP = Common.SERVERIP;
                        // arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();

                        Log.e("-----------","Common.roleid.equals----"+ Common.roleid);
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, DriverEmpUsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }
                    else if (Common.roleid.equals("14")) {
                        Client.SERVERIP = Common.SERVERIP;
                        // arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, EmpHomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }
                    else if (Common.roleid.equals("16")) {
                        Client.SERVERIP = Common.SERVERIP;
                        // arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Common.adminId=Common.userid;
                        editor.putString(key_Admin_id, Common.userid);
                        editor.commit();
                        Intent intent = new Intent(login_context, AdminParentActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }
                } else {
                    ShowPaymentDialog();
                }
            }else {

                    if (Common.roleid.equals("5")) {
                        Client.SERVERIP = Common.SERVERIP;
                        arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, HomeNew.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }else if(Common.roleid.equals("7"))
                    {
                        Client.SERVERIP = Common.SERVERIP;
                        arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        if (Common.FeatureAddressEnable){
                            Intent intent = new Intent(login_context, HomeNewRailway.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(login_context, HomeNew.class);
                            startActivity(intent);
                        }

                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }else if(Common.roleid.equals("3"))
                    {
                        Client.SERVERIP = Common.SERVERIP;
                        arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, HomeNew.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    } else if (Common.roleid.equals("15")) {
                         Client.SERVERIP = Common.SERVERIP;
                        // arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();

                        Log.e("-----------","Common.roleid.equals----"+Common.roleid);
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, DriverEmpUsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }else if (Common.roleid.equals("14")) {
                        Client.SERVERIP = Common.SERVERIP;
                        // arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Intent intent = new Intent(login_context, EmpHomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }
                    else if (Common.roleid.equals("16")) {
                        Client.SERVERIP = Common.SERVERIP;
                        // arrayTeacher = new ArrayList<MessageMain>();
                        new connectTask().execute();
                        PrimesysTrack.mDbHelper.truncateTables("db_user");
                        Common.adminId=Common.userid;
                        editor.putString(key_Admin_id, Common.userid);
                        editor.commit();
                        Intent intent = new Intent(login_context, AdminParentActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                    }
                    else if (Common.roleid.equals("17")) {

                        Client.SERVERIP = Common.SERVERIP;
                    arrayTeacher = new ArrayList<MessageMain>();
                    new connectTask().execute();
                    PrimesysTrack.mDbHelper.truncateTables("db_user");
                    if (Common.FeatureAddressEnable){
                        Intent intent = new Intent(login_context, HomeNewRailway.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(login_context, HomeNew.class);
                        startActivity(intent);
                    }

                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
                }
            }

    } catch (JSONException e) {
        e.printStackTrace();
        CustomDialog.displayDialog("Error While Establishing Connection To Server! Please Contact Admin.",login_context);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }finally
    {
        getdata=true;
    }



    }

    private void ShowPaymentDialog() {
        try{
            new SweetAlertDialog(login_context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(String.valueOf(login_context.getResources().getText(R.string.str_make_reachage_title)))
                    .setContentText((String.valueOf(login_context.getResources().getText(R.string.str_make_reachage))))
                    .setCancelText("No,Exit !")
                    .setConfirmText("Make Payment!")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent startMain = new Intent(login_context,RenewServiceActivity.class);
                            startActivity(startMain);

                            sDialog.cancel();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);

                            sDialog.cancel();
                            finish();
                        }
                    })
                    .show();

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    //parse the result
    void parsefbJSON(String result)
    {                Log.e("fb Login respo--",result.toString());

        if(result.contains("error"))
        {
            Common.showToast("Check once again there is Error !", login_context);
        }
        else{

            try
            {
                sharedPreferences=login_context.getSharedPreferences("User_data", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                JSONObject jo=new JSONObject(result);
                username=jo.getString("username");
                boolean isExist = jo.getBoolean("user_exist");
                password=jo.getString("password");
                
                //to invoke logintask in LoginActivity
                if(isExist){

                    editor.putBoolean(key_IS,false);
                    editor.putString(key_USER, username);
                    editor.putString(key_PASS, password);
                    editor.commit();
                    Intent ilogitask=new Intent(login_context,LoginActivity.class);
                    startActivity(ilogitask);
                    finish();
                }


            }

            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }



    public class connectTask extends AsyncTask<String, String, Client> {
        @Override
        protected Client doInBackground(String... message) {
            try {
                mClient = new Client(new Client.OnMessageReceived() {
                    @Override
                    // here the messageReceived method is implemented
                    public void messageReceived(String message) {
                        publishProgress(message);
                    }
                    public void messageSend(MessageMain message) {
                    }
                    @Override
                    public void messageSend(String message) {
                    }
                });
                mClient.run();
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String comMsg = values[0];


            Log.e("Socket respo ----", "Socket respo ----"+comMsg);
            try {
                if (comMsg.contains(Common.EV_REG_SUCC)) {
                    // Get Register Key for GCM


                    Log.e("GCM_KEY_SEND----", "Socket GCM_KEY_SEND-------- ----"+sharedPreferences.getBoolean(GCM_KEY_SEND, false)+"--------"+regId);

                    if (regId!=""&&!sharedPreferences.getBoolean(GCM_KEY_SEND, false)) {

                        //	RegisterGCM_Key();
                        sendRegisterationIdToServer(regId);
                    }
                }else if (comMsg.contains(Common.EV_MSG_RECEIVED)) {

                    JSONObject jo = new JSONObject(comMsg);
                    JSONObject jData = jo.getJSONObject("data");
                    MessageMain msg = new MessageMain();
                    msg.setDate_time(jData.getLong("timestamp"));
                    msg.setEvent(jo.getString("event"));
                    msg.setType(jData.getString("type"));
                    msg.setFrom(jData.getString("from"));
                    msg.setMesageText(jData.getString("msg"));
                    msg.setTo(jData.getString("to"));

                   /* if(!jData.isNull("student_id"))
                        PrimesysTrack.mDbHelper.insertMessage(msg,jData.getString("student_id"));
                    else
                        PrimesysTrack.mDbHelper.insertMessage(msg);
                    try {
                        if(APIController.getInstance().getRead()==0)
                            NotifyParent("" + msg.getTo(), "" + msg.getMesageText());
                    } catch (Exception e) {
                    }
                    if(DemoUserActivity.adapter!=null)
                        DemoUserActivity.adapter.notifyDataSetChanged();
                    if (MainActivity.arrayList != null&&MainActivity.mAdapter!=null) {
                        MainActivity.arrayList.add(msg);
                        MainActivity.mAdapter.notifyDataSetInvalidated();
                        MainActivity.mAdapter.notifyDataSetChanged();
                    } else {
                        MainActivity.arrayList = new ArrayList<MessageMain>();
                        MainActivity.arrayList.add(msg);
                        if (MainActivity.mAdapter != null) {
                            MainActivity.mAdapter.notifyDataSetInvalidated();
                            MainActivity.mAdapter.notifyDataSetChanged();
                        }
                    }*/
                }
              //  System.out.println("LoginActivity.connectTask.onProgressUpdate()"+comMsg);

                if (comMsg.contains(Common.EV_CURR_LOC)) {

					/*ShowGMapC.changeLocation(comMsg);*/
                    if (Common.FeatureAddressEnable)
                        ShowMapFragmentFeatureAddressEnable.changeLocation(comMsg);
                    else
                        ShowMapFragment.changeLocation(comMsg);

                }
                else if (comMsg.contains(Common.EV_TOD_LOC)) {

                    PrimesysTrack.mDbHelper.insertLocation(parseLocation(comMsg));

                } else if (comMsg.contains(Common.EV_GEOFENCE_SUCCESS)) {
                    System.err.println("Reponce Socket-----  Geofence "+comMsg);

                    GeofencingNewdrawCircle.parseNewFenceJSON(comMsg);

                    //GeofenceDTO geofence=parseGeofence(comMsg);
                }
                else if (comMsg.contains(Common.EV_GEOFENCE_LIST)) {

                    GeoFencingHomeNew.parsefencelistJSON(comMsg);

                } else if (comMsg.contains(Common.EV_GEOFENCE_NOTFOUND)) {

                    GeoFencingHomeNew.parsefencelistJSON(comMsg);

                }else if (comMsg.contains(Common.EV_GEOFENCEUPDATE_SUCCESS)) {

                    try{

                        if(GeofencingUpdatedrawCircle.context!=null&&!((Activity) GeofencingUpdatedrawCircle.context ).isFinishing())
                            GeofencingUpdatedrawCircle.parseUpdateFenceJSON(comMsg);
                        if(GeoFencingHomeNew.context!=null&&!((Activity) GeoFencingHomeNew.context ).isFinishing())
                            GeoFencingHomeNew.parseUpdateFenceJSON(comMsg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else if (comMsg.contains(Common.EV_GCMADDED_SUCCESS)) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(GCM_KEY_SEND, true);

                    editor.commit();
                }else if (comMsg.contains(Common.EV_GCMDELETED_SUCCESS)) {

                    GeoFencingHomeNew.parseDeleteFenceJSON(comMsg);


                    editor.commit();
                }else if (comMsg.contains(Common.EV_GEOFENCE_DEVICENOTCONN)){

                    try{
                        if (GeofencingNewdrawCircle.context!=null&&!((Activity) GeofencingNewdrawCircle.context ).isFinishing())
                            GeofencingNewdrawCircle.parseDeviceNotConnectError(comMsg);

                        if(GeofencingUpdatedrawCircle.context!=null&&!((Activity) GeofencingUpdatedrawCircle.context ).isFinishing())
                            GeofencingUpdatedrawCircle.parseDeviceNotConnectError(comMsg);
                        if(GeoFencingHomeNew.context!=null&&!((Activity) GeoFencingHomeNew.context ).isFinishing())
                            GeoFencingHomeNew.parseDeviceNotConnectError(comMsg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //  if (GeofencingUpdatedrawCircle.Update)
                    //   GeofencingUpdatedrawCircle.parseDeviceNotConnectError(comMsg);
                    // else
                    //    GeofencingHome.parseDeviceNotConnectError(comMsg);

                }
                else if (comMsg.contains(Common.EV_GEOFENCE_ERROR)) {

                    parse_geofence_deviceerror_JSON(comMsg);

                }else if (comMsg.contains(Common.EV_DEVICE_STATUS))
                {
                    //  HomeActivity.setDeviceData(comMsg);

                } else if(comMsg.contains(Common.EV_ERROR))
                    socketErrorMsg(comMsg);




            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
    void socketErrorMsg(String errmsg)
    {
        try{
            JSONObject jo=new JSONObject(errmsg);
            JSONObject jData=jo.getJSONObject("data");
            //	Common.showToast(jData.getString("error_msg"), login_context);
        }catch(Exception e)
        {

        }
    }
    ArrayList<LocationData> parseLocation(String msg) {
        ArrayList<LocationData> list=new ArrayList<LocationData>();
        try {
            JSONObject jMain=new JSONObject(msg);
            JSONArray array=jMain.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                LocationData l = new LocationData();

                if (jo.getString("lat_direction").equalsIgnoreCase("N")&&jo.getString("lan_direction").equalsIgnoreCase("E")) {

                    l.setLat(jo.getDouble("lat"));
                    l.setLan(jo.getDouble("lan"));

                }else if (jo.getString("lat_direction").equalsIgnoreCase("N")&&jo.getString("lan_direction").equalsIgnoreCase("W")) {

                    l.setLat(jo.getDouble("lat"));
                    l.setLan(-jo.getDouble("lan"));

                }
                else if (jo.getString("lat_direction").equalsIgnoreCase("S")&&jo.getString("lan_direction").equalsIgnoreCase("E")) {

                    l.setLat(-jo.getDouble("lat"));
                    l.setLan(jo.getDouble("lan"));

                }else if (jo.getString("lat_direction").equalsIgnoreCase("S")&&jo.getString("lan_direction").equalsIgnoreCase("W")) {

                    l.setLat(-jo.getDouble("lat"));
                    l.setLan(-jo.getDouble("lan"));

                }
                l.setSpeed(jo.getInt("speed"));
                l.setTimestamp(jo.getLong("timestamp"));
                list.add(l);
                //	Toast.makeText(login_context, "dATAC ---->  "+msg,Toast.LENGTH_LONG).show();
                Log.e("Location data login", ""+msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }




    ///If the device is not connected to our portal and user try to Add / Update Geo Fence, error will be returned.
    public void parse_geofence_deviceerror_JSON(String comMsg) {
        try
        {
            Common.ShowSweetAlert(login_context, "Can't reach your device please try again.");
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }


  /*  @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Common.showToast(""+arg0, login_context);

    }

    @Override
    public void onConnected(Bundle arg0) {
        getDeviceCordinates();

    }
*/






    /*public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        login.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
*/

 /*   @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }*/



    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = text_username.getText().toString();
        String password = text_password.getText().toString();

        if (email.isEmpty()) {
            text_username.setError("Please enter valid username!");
            text_username.requestFocus();

            valid = false;
        } else if (password.isEmpty()) {
            text_password.setError("Please enter valid password!");
            text_password.requestFocus();

            valid = false;
        }


        return valid;
    }

    //   {Response:  responseCode: 200, graphObject: {"id":"991669157563492","name":"Rupesh Patil","email":"patil.rupesh4892@gmail.com","gender":"male","birthday":"08\/04\/1992"}, error: null}



    // making the request for JSON Object
    void PostFacebookReq(String facebbok_id, final String str_name, final String str_email)
    {
        reuestQueue= Volley.newRequestQueue(this); //getting Request object from it
        final SweetAlertDialog pDialog1 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog1.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog1.setTitleText("Login Wait");
        pDialog1.setCancelable(false);
        pDialog1.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.URL+"LoginServiceAPI.asmx/SaveDemoUser",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.err.println("IN Fb Login respo " + response);

                    parsefbJSON(response);


               if (pDialog1.isShowing())
                pDialog1.hide();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pDialog1.isShowing())
                            pDialog1.hide();                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            System.out.println(error.toString());
                            parsefbJSON(new String(error.networkResponse.data));
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.err.println("IN Fb Login METHOD" + str_email + " " + str_name);

                params.put("name",str_name);
                params.put("email",str_email);
                params.put("contact","");
                params.put("message", "From FaceBook");
                params.put("registrationType", "FaceBook");
                params.put("city", Common.user_regCity);

                Log.e("FB Login req--", params.toString());
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        reuestQueue.add(stringRequest);
    }
    // get the registration id for android
    String getDeviceID() {
        return Settings.Secure.getString(login_context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    // read the registered gmail id of the device
    String getRegisteredID() {
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        String gmail = null;

        for (Account account : list) {
            if (account.type.equalsIgnoreCase("com.google")) {
                gmail = account.name;
                break;
            }
        }
        return gmail;
    }
    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public String registerGCM() {

       /* try{
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            regId = getRegistrationId(login_context);

            if (TextUtils.isEmpty(regId)) {

                registerInBackground();

                Log.e("RegisterActivity",
                        "registerGCM - successfully registered with GCM server - regId: "
                                + regId);
            } else {
                //  Toast.makeText(getApplicationContext(),"RegId already available. RegId: " + regId,Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

        return regId;
    }

    private String getRegistrationId(Context context) {
        String registrationId = sharedPreferences.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = sharedPreferences.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
      /*  new AsyncTask<Void, Void, String>() {
            @SuppressLint("MissingPermission")
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(login_context);
                    }
                    regId = gcm.register(Common.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(login_context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                    ex.printStackTrace();
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //Toast.makeText(getApplicationContext(),"Registered with GCM Server." + msg, Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);*/
    }

    protected void sendRegisterationIdToServer(String regId2) {

        String result="";
        try {
            if (Integer.parseInt(Common.roleid)==15||Integer.parseInt(Common.roleid)==14){
                postGcmKeyToServer(regId);

            }else {
                LoginActivity.mClient.sendMessage(makeGcmRegJSON(regId));
            }
            result="shareRegIdWithAppErlangServer------------Post REgisetr Key Successfully to App Server.";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postGcmKeyToServer(final String regId) {

        reuestQueue= Volley.newRequestQueue(login_context); //getting Request object from it
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/PostGcmKeyToServer",new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                System.out.println("------postGcmKeyToServer----------------"+response);
                parseGcmKeyPost(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //    pDialog.hide();
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcm_key",regId);
                params.put("name", Common.username);
                params.put("user_id", Common.userid);

                Log.e("---------------------","REq---postGcmKeyToServer  -------" + params);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        reuestQueue.add(stringRequest);


    }

    private void parseGcmKeyPost(String response) {
        Log.e("---------------------","REspo---parseGcmKeyPost  -------" + response);

        try {
            JSONObject jo=new JSONObject(response);
            if (jo.getString("error").equalsIgnoreCase("false")) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(GCM_KEY_SEND, true);

                editor.commit();
                //   Common.ShowSweetSucess(login_context, jo.getString("message"));

            }//else 	Common.ShowSweetAlert(login_context, jo.getString("message"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void storeRegistrationId(Context context, String regId) {
        //  sendRegisterationIdToServer(regId);

        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.putBoolean(GCM_KEY_SEND, true);

        editor.commit();
    }


    private String makeGcmRegJSON(String regId) {

        String gcmSTring="{}";
        try{
            JSONObject jo=new JSONObject();
            jo.put("event", Common.EV_GEOFENCE_REG_KEY);
            jo.put("parent_id",Integer.parseInt(Common.userid));
            jo.put("reg_key",regId);
            gcmSTring=jo.toString();
            System.err.print("MakeGcmRegJSON Event Fire---"+gcmSTring);

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        return gcmSTring;

    }


}
