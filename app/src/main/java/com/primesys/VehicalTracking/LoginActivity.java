package com.primesys.VehicalTracking;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.maps.GoogleMap;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.Dto.LoginDetails;
import com.primesys.VehicalTracking.Dto.MessageMain;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragment;
import com.primesys.VehicalTracking.googlelogin.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    public static Client mClient;
    private Button login;
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

    boolean isfirst;
    TextView forget_password,register;
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
    public static String roll_id;
    public static  Boolean getdata=false;
    private DBHelper helper;
    private String phpurl="http://www.mykiddytracker.com/php/getAppId.php";
    private RadioGroup rgType;
    private String key_url_type="Url_Type";

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
        if(!sharedPreferences.contains(key_IS))
            isfirst = sharedPreferences.getBoolean(key_IS, true);

        if (!isfirst) {

            username = sharedPreferences.getString(key_USER,"");
            password = sharedPreferences.getString(key_PASS,"");
            Common.Url_Type = sharedPreferences.getString(key_url_type,"pt");

            System.err.println(" Inside is not first====="+username + " " + password);
            if(Common.getConnectivityStatus(login_context))
            {
                try{  postLoginRequest();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
           /* else if (!isfirst) {
                Login_off();
            }*/else
               /* new SweetAlertDialog(login_context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Turn on Internet Connection!")
                        .show();*/
                Common.ShowSweetAlert(login_context, "Turn on Internet Connection!");
        }else {


            setContentView(R.layout.activity_login);
            findbyId();


            //SeT Username
            text_username.setText(sharedPreferences.getString(key_USER, ""));
            login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {

                        username = text_username.getText().toString().trim();
                        password = text_password.getText().toString().trim();

                        if (Common.getConnectivityStatus(login_context)) {
                            if (validate())
                                postLoginRequest();

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

               /* GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,GraphResponse response) {
                                JSONObject json;
                                // Application code

                                try {
                                     json=new JSONObject(String.valueOf(response));
                                   JSONObject jo= json.getJSONObject("graphObject");
                                    Log.e("Facebook Respo=======",jo.getString("email")+jo.getString("name"));
                                  //  PostFacebookReq(response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            //    Log.v("LoginActivity----------", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

*/
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void findbyId() {

        // TODO Auto-generated method stub
        login= (Button) findViewById(R.id.btn_login);
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

        fbloginButton = (LoginButton) findViewById(R.id.fb_login_button);
         helper = DBHelper.getInstance(login_context);

    }


    // making the request for JSON Object
    void postLoginRequest()
    {
        reuestQueue= Volley.newRequestQueue(login_context); //getting Request object from it
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Login Wait");
        pDialog.setCancelable(false);
        pDialog.show();
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.URL+"LoginServiceAPI.asmx/GetLoginDetails",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Login result " + response);

                if (response.contains("true")) {

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
                    editor.commit();
                    parseJSON(response);
                }

                pDialog.hide();


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            System.out.println(error.toString());
                            parseJSON(new String(error.networkResponse.data));
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
                params.put("App_Emailid", emailid);

                System.out.println("Login REquesr " + params.toString());

                return params;
            }
        };
        stringRequest.setTag(TAG);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }


    //parse the result
    void parseJSON(String result)
    { JSONObject jo=null;
        System.out.println("parseJSON--login--" + result);

        JSONArray array = null;
        try {
            array = new JSONArray(result);
             jo = array.getJSONObject(0);
            Common.roleid = jo.getString("Role_ID");
            Common.userid = jo.getString("UserID");
            Common.username = jo.getString("userName");
            Common.Student_Count = jo.getInt("Student_Count");
          //  Common.TrackDay=Integer.parseInt(jo.getString("daysr"));
       //     System.out.println("TrackDay----" + Common.TrackDay);

            editor = sharedPreferences.edit();

            editor.putString(key_Roll_id, Common.roleid);
            editor.putString(key_User_id, Common.userid);
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
            System.out.println("Photo----"+Common.markerImg);
            Common.user_regType=jo.getString("RegistrationType");
            Log.e(TAG, Common.user_regType);




        DBHelper helper = DBHelper.getInstance(login_context);
        helper.getLastTimeStamp();
        //************Added by rupesh
        editor = sharedPreferences.edit();
        editor.putString(key_Roll_id,Common.roleid)
        ;editor.putString(key_url_type,Common.Url_Type);
            editor.putString("User_Id",Common.userid);
        editor.commit();

        roll_id=sharedPreferences.getString(key_Roll_id,"");


            if (Common.roleid.equals("5")) {
                Client.SERVERIP = Common.SERVERIP;
                arrayTeacher = new ArrayList<MessageMain>();
                new connectTask().execute();
                DBHelper.getInstance(login_context).truncateTables("db_user");
                Intent intent = new Intent(login_context, Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
            }else
            if(Common.roleid.equals("7"))
            {
                Client.SERVERIP = Common.SERVERIP;
            arrayTeacher = new ArrayList<MessageMain>();
            new connectTask().execute();
            DBHelper.getInstance(login_context).truncateTables("db_user");
            Intent intent = new Intent(login_context, Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
        }/*else if (Common.roleid.equals("8")) {
            Client.SERVERIP = Common.SERVERIP;
            arrayTeacher = new ArrayList<MessageMain>();
            playSound=0;
            new connectTask().execute();
            DBHelper.getInstance(login_context).truncateTables("db_user");
            Intent intent = new Intent(login_context, HomeTrackQuizUser.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
        }else if (Common.roleid.equals("9")) {
            Client.SERVERIP = Common.SERVERIP;
            arrayTeacher = new ArrayList<MessageMain>();
            playSound=0;
            new connectTask().execute();
            DBHelper.getInstance(login_context).truncateTables("db_user");
            Intent intent = new Intent(login_context, QuizGridDashBord.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
        }else if (Common.roleid.equals("10")) {
            Client.SERVERIP = Common.SERVERIP;
            arrayTeacher = new ArrayList<MessageMain>();
            playSound=0;
            new connectTask().execute();
            DBHelper.getInstance(login_context).truncateTables("db_user");
            Intent intent = new Intent(login_context, HomeStandaredUser.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_out);
        }*/



    } catch (JSONException e) {
        e.printStackTrace();
        CustomDialog.displayDialog("Error While Establishing Connection To Server! Please Contact Admin",login_context);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }finally
    {
        getdata=true;
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
                System.out.print(e);
            }
        }
    }



    public class connectTask extends AsyncTask<String, String, Client> {
        @Override
        protected Client doInBackground(String... message) {
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
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String comMsg = values[0];


            Log.e("Socket respo ----", "Socket respo ----"+comMsg);
            try {
                if (comMsg.contains(Common.EV_MSG_RECEIVED)) {

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
                        helper.insertMessage(msg,jData.getString("student_id"));
                    else
                        helper.insertMessage(msg);
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
                System.out.println("LoginActivity.connectTask.onProgressUpdate()"+comMsg);

                if (comMsg.contains(Common.EV_CURR_LOC)) {

					/*ShowGMapC.changeLocation(comMsg);*/
                    ShowMapFragment.changeLocation(comMsg);

                }
                else if (comMsg.contains(Common.EV_TOD_LOC)) {

                    DBHelper helper = DBHelper.getInstance(login_context);
                    helper.insertLocation(parseLocation(comMsg));
                }
             /*   else if (comMsg.contains(Common.EV_DEVICE_STATUS))
                    HomeActivity.setDeviceData(comMsg);
                else if (comMsg.contains(Common.EV_GRP_CREAT))
                    CreateGroup.parseJSON(comMsg);
                else if(comMsg.contains(Common.EV_ERROR))
                    socketErrorMsg(comMsg);
                else if(comMsg.contains(Common.EV_GRPS_LIST))
                    DemoUserActivity.parseJSON(comMsg);
                else if(comMsg.contains(Common.EV_MEM_LIST))

                    MainActivity.parseJSON(comMsg);
                else if(comMsg.contains(Common.EV_FRND_SUCC)){
                    AddUserActivity.parseJSON(comMsg);
                    Common.showToast("Friend added successfully", login_context);
                }else if(comMsg.contains(Common.EV_FRND_Alredy)){

                    System.out.println("Inside Friend already added ");
                    Common.showToast("Friend already added", login_context);
                }
                else if(comMsg.contains(Common.EV_FRND_LIST)){
                    if(Common.flag==0)
                        DemoUserActivity.restParseJSON(comMsg);
                    else
                        AddParticpants.parseJSON(comMsg);
                }
                else if (comMsg.contains(Common.EV_GRP_ADD))
                    Common.showToast("Member added successfully", login_context);
                else if(comMsg.contains(Common.EV_MSG_ACK))
                {
                    DBHelper helper = DBHelper.getInstance(login_context);
                    try
                    {


                        JSONObject jo=new JSONObject(comMsg);
                        JSONObject jData=jo.getJSONObject("data");
                        Log.e("DAta....", jData.toString());
                        helper.UpdateMessage(jData.getLong("ref_id"),jData.getLong("timestamp"));
                        if (MainActivity.mAdapter != null) {
                            MainActivity.mAdapter.notifyDataSetInvalidated();
                            MainActivity.mAdapter.notifyDataSetChanged();
                        }
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }*/

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
                l.setLat(jo.getDouble("lat"));
                l.setLan(jo.getDouble("lan"));
                l.setSpeed(jo.getInt("speed"));
                l.setTimestamp(jo.getLong("timestamp"));
                list.add(l);
                //	Toast.makeText(login_context, "dATAC ---->  "+msg,Toast.LENGTH_LONG).show();
                Log.e("Location data from login", ""+msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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
/*
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_username.setError("enter a valid email address");
            valid = false;
        } else {
            text_username.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            text_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            text_password.setError(null);
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
        // Adding request to request queue
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



}
