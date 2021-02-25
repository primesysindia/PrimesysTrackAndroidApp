package com.primesys.VehicalTracking.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.primesys.VehicalTracking.ActivityMykiddyLike.HomeNew;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.PaymentTypeDTO;
import com.primesys.VehicalTracking.MyAdpter.StudentListAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.lang.Integer.parseInt;

public class DeviceLevelRenewService extends AppCompatActivity {
    Button btnRenew;
    Context renewContext=DeviceLevelRenewService.this;
    CircularNetworkImageView img_school;
    final Handler handler = new Handler();
     private String orderId;


    TextView text_name,text_username,text_date,text_status;
    RadioGroup ragpayment;
    TextView textprice_month,textprice_quarterly,textprice_half,textprice_yearly;
    SweetAlertDialog pDialog;
    String rupee=" {fa-inr}",UserMobNo="";
    Boolean valid=false;
    private RadioGroup ragpaymentmetod;
    private RadioButton rb_card,rb_paytm;
    double month,quat,half,year;
    ArrayList<PaymentTypeDTO> typelist=new ArrayList<PaymentTypeDTO>();
    // paypal code
    private static final String TAG = "paymentExample";
    private PaymentTypeDTO selected_paymet_type=new PaymentTypeDTO();

    private ArrayList<DeviceDataDTO> childlist;
    private ListView listStudent;
    private StudentListAdpter padapter;
    private Button btn_cancel;
    String amount = "10";
    String firstname = "Piyush";
    String email = "piyusshsable@gmail.com";
    String productInfo = "MyKiddyTracker";
    String phone_no="9762554098";
    String PaymentTypeId="",StudentId;
    String payment_extension="";
    private String Status;
    private SweetAlertDialog LoadProgress;
    private String Payment_PlanId="";
    private String UserPayID="0";
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private String CheckSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_level_renew_service);
        findViewByID();
        //  CheckStudent(renewContext);


        ragpayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.rb_monthly:
                        for (PaymentTypeDTO paymentTypeDTO : typelist) {
                            if (paymentTypeDTO.getPaymentType().equals("Monthly")) {
                                selected_paymet_type=paymentTypeDTO;
                                valid=true;
                                System.out.println(selected_paymet_type.getAmountOfPlan());

                            }
                        }
                        break;
                    case R.id.rb_half:
                        for (PaymentTypeDTO paymentTypeDTO : typelist) {
                            if (paymentTypeDTO.getPaymentType().equals("Half Yearly")) {
                                selected_paymet_type=paymentTypeDTO;
                                System.out.println(selected_paymet_type.getAmountOfPlan());
                                valid=true;

                            }
                        }		          break;
                    case R.id.rb_quarter:
                        for (PaymentTypeDTO paymentTypeDTO : typelist) {
                            if (paymentTypeDTO.getPaymentType().equals("Quarterly")) {
                                selected_paymet_type=paymentTypeDTO;
                                System.out.println(selected_paymet_type.getAmountOfPlan());
                                valid=true;

                            }
                        }		          break;
                    case R.id.rb_yearly:
                        for (PaymentTypeDTO paymentTypeDTO : typelist) {
                            if (paymentTypeDTO.getPaymentType().equals("Yearly")) {
                                selected_paymet_type=paymentTypeDTO;
                                System.out.println(selected_paymet_type.getAmountOfPlan());
                                valid=true;

                            }
                        }			          break;
                    default:
                        for (PaymentTypeDTO paymentTypeDTO : typelist) {
                            if (paymentTypeDTO.getPaymentType().equals("Monthly")) {
                                selected_paymet_type=paymentTypeDTO;
                                System.out.println(selected_paymet_type.getAmountOfPlan());
                                valid=true;

                            }
                        }
                        break;
                }
            }
        });


        //RENEW Button click code
        btnRenew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(valid){

                    if (rb_card.isChecked()) {

                        Intent data=new Intent(renewContext, PaymentWebview.class);
                        data.putExtra("Payment_TypeId",selected_paymet_type.getPaymentTypeId()+"");
                        data.putExtra("Payment_PlanId",selected_paymet_type.getPlanId()+"");

                        data.putExtra("Payment_Amount",selected_paymet_type.getAmountOfPlan()+"");
                        // data.putExtra("Payment_Amount","1");
                        data.putExtra("UserName",text_username.getText());
                        data.putExtra("MobNo",UserMobNo);
                        data.putExtra("FirstName",text_name.getText());
                        data.putExtra("Status",text_status.getText());
                        data.putExtra("StudentId",StudentId);
                        data.putExtra("UserPayID",UserPayID);

                        startActivity(data);
                    }else if (rb_paytm.isChecked()){
                        //  Common.ShowSweetAlert(renewContext,"Please use card payment.This service currently unavailable.");

                        Intent data = new Intent();
                        data.putExtra("Payment_TypeId",selected_paymet_type.getId()+"");
                        data.putExtra("Payment_PlanId",selected_paymet_type.getPlanId()+"");

                        data.putExtra("Payment_Amount",selected_paymet_type.getAmountOfPlan()+"");
                        // data.putExtra("Payment_Amount","1");
                        data.putExtra("UserName",text_username.getText());
                        data.putExtra("MobNo",UserMobNo);
                        data.putExtra("FirstName",text_name.getText());
                        data.putExtra("Status",text_status.getText());
                        data.putExtra("StudentId",StudentId);
                        data.putExtra("UserPayID",UserPayID);

                        PayTmProceed(data);

                    }else {
                        Common.ShowSweetAlert(renewContext, "Please select payment method card or paytm .");

                    }

                }else {
                    Common.ShowSweetAlert(renewContext, "Please select payment plan.");
                }


               /* if(valid){
                    Intent data=new Intent(renewContext, PaymentWebview.class);
                    data.putExtra("Payment_TypeId",selected_paymet_type.getPaymentTypeId()+"");
                    data.putExtra("Payment_PlanId",selected_paymet_type.getPlanId()+"");

                     data.putExtra("Payment_Amount",selected_paymet_type.getAmountOfPlan()+"");
                   // data.putExtra("Payment_Amount","1");
                    data.putExtra("UserName",text_username.getText());
                    data.putExtra("MobNo",UserMobNo);
                    data.putExtra("FirstName",text_name.getText());
                    data.putExtra("Status",text_status.getText());
                    data.putExtra("StudentId",StudentId);
                    data.putExtra("UserPayID",UserPayID);

                    startActivity(data);
                }else {
                    Common.showToast("Please select payment plan.", renewContext);
                }*/



            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    void findViewByID()
    {


        text_username=(TextView)findViewById(R.id.text_username);
        text_date=(TextView)findViewById(R.id.text_date1);
        text_name=(TextView) findViewById(R.id.text_name);
        text_status=(TextView) findViewById(R.id.text_status1);
        ragpayment=(RadioGroup)findViewById(R.id.rbt_services);
        ragpayment.clearCheck();
        textprice_month=(TextView) findViewById(R.id.textprice_month);
        textprice_quarterly=(TextView)findViewById(R.id.textprice_quarterly);
        textprice_half=(TextView)findViewById(R.id.textprice_half);
        textprice_yearly=(TextView)findViewById(R.id.textprice_yearly);
        btnRenew= (Button) findViewById(R.id.btn_renew);
        btn_cancel= (Button) findViewById(R.id.btn_cancel);
        ragpaymentmetod=(RadioGroup)findViewById(R.id.rbt_payment_method);
        ragpaymentmetod.clearCheck();
        rb_card=(RadioButton)findViewById(R.id.rb_card);
        rb_paytm=(RadioButton)findViewById(R.id.rb_paytm);
        
    }




/*

    public void CheckStudent(Context context1) {
        helper = DBHelper.getInstance(context1);


            childlist=PrimesysTrack.mDbHelper.Show_Device_list();

            if (childlist.size()>1)
                ShowListofStudent();
            else {
                StudentId = childlist.get(0).getId();
                //StudentName= childlist.get(0).getName();
               // DeviceImieNo = childlist.get(0).getImei_no();
              //  Log.e("DeviceImieNo=========",DeviceImieNo);

            }
        }

*/


    @Override
    protected void onResume() {
        super.onResume();

        childlist=PrimesysTrack.mDbHelper.Show_Device_list();
        Log.e("Childlist-------",childlist.size()+"");
        ShowListofStudent();

       /* if (childlist.size()>1)
            ShowListofStudent();
        else
            StudentId = childlist.get(0).getId();*/
    }

    private void ShowListofStudent() {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(renewContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_studentlist);
            dialog.setCancelable(false);
            dialog.closeOptionsMenu();
            dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            dialog.show();

            final TextView tv_title=(TextView)dialog.findViewById(R.id.d_title);
            tv_title.setText("Select Device");
            listStudent = (ListView) dialog.findViewById(R.id.student_list);
            Button cancel = (Button) dialog.findViewById(R.id.d_cancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startMain = new Intent(renewContext,HomeNew.class);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    dialog.dismiss();
                }
            });
    //		StudentId=Integer.parseInt(myAdapter.getItem(0).getId());
            padapter = new StudentListAdpter(renewContext, R.layout.fragment_mapsidebar, childlist);
            listStudent.setAdapter(padapter);

            listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    StudentId = childlist.get(position).getId();
                    if(Common.connectionStatus)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new GetPaymentPlanDetails().executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
                        } else {
                            new GetPaymentPlanDetails().execute();
                        }
                    }

                    dialog.dismiss();
                }
            });


            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        return super.onCreateOptionsMenu(menu);

    }
    // onclick menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // make the request for taking the values from the DB
    class GetPaymentPlanDetails extends AsyncTask<Void, Void, String>
    {
        String url=Common.URL+"ParentAPI.asmx/GetPaymentPlanDetails";
        //	String url="http://192.168.1.23:81/API/ParentAPI.asmx/GetPaymentType";
        @Override
        protected void onPreExecute() {
            pDialog = Common.ShowSweetProgress(renewContext,"Loading");

        }
        @Override
        protected String doInBackground(Void... params) {
            String result="Data Not Found";

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost post=new HttpPost(url);
            try {
                List<NameValuePair> postParameter = new ArrayList<NameValuePair>(1);
                postParameter.add(new BasicNameValuePair("UserID",Common.WebUserId));
                postParameter.add(new BasicNameValuePair("StudentID",StudentId));


                post.setEntity(new UrlEncodedFormEntity(postParameter));
                System.out.println("PGetPaymentPlanDetailsReq-----------"+postParameter.toString());
                HttpResponse response = httpclient.execute(post);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            System.out.println("PaymetRepo-----------"+result.toString());

            if (result.contains("error")) {
                CustomDialog.displayDialog("Invalid User.Contact Admin",renewContext);
            } else
                parseData(result);
        }
    }
    void parseData(String result)
    {
        try
        {
            JSONArray array=new JSONArray(result);
            for(int i=0;i<array.length();i++){
                JSONObject jo=array.getJSONObject(i);
                PaymentTypeDTO type=new PaymentTypeDTO();
                type.setPaymentTypeId(jo.getString("PaymentTypeID"));
                type.setPlanId(jo.getString("PaymentPlanID"));

                type.setAmountOfPlan(parseInt(jo.getString("Amount")));
               // type.setDaysOfplan(parseInt(jo.getString("DaysOfplan")));
                type.setPaymentType(jo.getString("PaymentType"));
                typelist.add(type);
            }


            settypedata(typelist);

			/*text_name.setText(jo.getString("Name"));
			text_username.setText(jo.getString("UserName"));
			month=Double.parseDouble(jo.getString("OneMonth"));
			textprice_month.setText(jo.getString("OneMonth")+rupee);
			quat=Double.parseDouble(jo.getString("Quarterly"));
			textprice_quarterly.setText(jo.getString("Quarterly")+rupee);
			half=Double.parseDouble(jo.getString("HalfYear"));
			textprice_half.setText(jo.getString("HalfYear")+rupee);
			year=Double.parseDouble(jo.getString("OneYear"));
			textprice_yearly.setText(jo.getString("OneYear")+rupee);
			System.out.println(jo.getString(jo.getString("expiry_date")));
			text_date.setText(jo.getString(jo.getString("expiry_date")));
			text_status.setText(checkDate(jo.getString("expiry_date")));*/
        }catch(Exception e)
        {
            System.err.print(e);
        }
    }
    private void settypedata(ArrayList<PaymentTypeDTO> typelist) {

        for (PaymentTypeDTO paymentTypeDTO : typelist) {
            if (paymentTypeDTO.getPaymentType().equals("Monthly")) {
                textprice_month.setText(paymentTypeDTO.getAmountOfPlan() +" "+getResources().getString(R.string.rs));
            }else if (paymentTypeDTO.getPaymentType().equals("Quarterly")) {
                textprice_quarterly.setText(paymentTypeDTO.getAmountOfPlan()+" "+getResources().getString(R.string.rs));

            }else if (paymentTypeDTO.getPaymentType().equals("Half Yearly")) {
                textprice_half.setText(paymentTypeDTO.getAmountOfPlan()+" "+getResources().getString(R.string.rs));

            }
            else if (paymentTypeDTO.getPaymentType().equals("Yearly")) {
                textprice_yearly.setText(paymentTypeDTO.getAmountOfPlan()+" "+getResources().getString(R.string.rs));

            }


        }

        if(Common.connectionStatus)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetDevicePaymentInfo().executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
            } else {
                new GetDevicePaymentInfo().execute();
            }
        }

    }



    // make the request for taking the values from the DB
    class GetDevicePaymentInfo extends AsyncTask<Void, Void, String>
    {
        String url=Common.URL+"ParentAPI.asmx/GetDevicePaymentInfo";
        //	String url="http://192.168.1.23:81/API/ParentAPI.asmx/GetPaymentInfo";
        @Override
        protected void onPreExecute() {
            pDialog=Common.ShowSweetProgress(renewContext,"Fetching Data...");

        }
        @Override
        protected String doInBackground(Void... params) {
            String result="Data Not Found";

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost post=new HttpPost(url);
            try {
                List<NameValuePair> postParameter = new ArrayList<NameValuePair>(1);
                postParameter.add(new BasicNameValuePair("RoleID",Common.roleid));
                postParameter.add(new BasicNameValuePair("StudentID",StudentId));
                post.setEntity(new UrlEncodedFormEntity(postParameter));
                System.out.println("GetDevicePaymentInfo req-----------"+postParameter.toString());
                HttpResponse response = httpclient.execute(post);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                result = e.getMessage();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            System.out.println("GetDevicePaymentInfo-----------"+result.toString());

            if (result.contains("error")) {
                CustomDialog.displayDialog("Invalid User.Contact Admin",renewContext);
            } else
                parseUserData(result);
        }
    }


    public void parseUserData(String result) {
        try
        {
            JSONArray array=new JSONArray(result);


            JSONObject jo=array.getJSONObject(0);

            text_name.setText(jo.getString("FullName"));
            text_username.setText(jo.getString("EmailID"));
            text_date.setText(jo.getString("ExpiryDate"));
            text_status.setText(jo.getString("Status"));

            if (result.contains("UserPayId"))
                UserPayID=jo.getString("UserPayId");
            else UserPayID="0";
            if (result.contains("MobileNo"))
                UserMobNo=jo.getString("MobileNo");
            else UserMobNo="0001100011";
            if (result.contains("OrderID"))
                orderId=jo.getString("OrderID");
            else        initOrderId() ;

        }catch(Exception e)
        {
            System.err.print(e);
        }
    }


    // check the current date is greater than the expiray date
    String checkDate(String exDate)
    {
        String status="Activated";
        Date ex=new Date(exDate);
        Date currentDate=new Date();

        if(!(ex==max(ex, currentDate)))
            status="Expired";

        return status;
    }

    public  Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }



    private void PayTmProceed(Intent data) {
        amount = data.getStringExtra("Payment_Amount");
        email = data.getStringExtra("UserName");
        phone_no=data.getStringExtra("MobNo");
        firstname = data.getStringExtra("FirstName");
        PaymentTypeId= data.getStringExtra("Payment_TypeId");
        Status= data.getStringExtra("Status");
        StudentId=data.getStringExtra("StudentId");
        Payment_PlanId=data.getStringExtra("Payment_PlanId");
        UserPayID=data.getStringExtra("UserPayID");

        GetPaytm_ChecksumGeneration(orderId,email,amount,phone_no);
    }


    private void GetPaytm_ChecksumGeneration(final String orderId, final String email, final String amount, final String phone_no)
    {
        reuestQueue= Volley.newRequestQueue(renewContext); //getting Request object from it
        final MyCustomProgressDialog pDialog = Common.ShowProgress(renewContext);

        String URL1="http://192.168.1.102:8080/TrackingAppDB/TrackingAPP/UserServiceAPI/GetPaytm_ChecksumGeneration";
        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"UserServiceAPI/GetPaytm_ChecksumGeneration",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("GetPaytm_ChecksumGeneration respo", response);
                parseJSON(response);
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

                params.put("OrderId",orderId);
                params.put("UserId", Common.userid);
                params.put("Amount", amount);
                params.put("MobNo", phone_no);
                params.put("Email", email);


                System.out.println("REq--CHEcksum--------"+params);
                return params;
            }

        };

        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        reuestQueue.add(stringRequest);

    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "MKT" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getProductionService();
        //PaytmPGService Service = PaytmPGService.getStagingService();


        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID" , Common.MID);
        paramMap.put("ORDER_ID" , orderId);
        paramMap.put("CUST_ID" , Common.userid);
        paramMap.put("INDUSTRY_TYPE_ID" ,Common.INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID" , Common.CHANNLE_ID);
        paramMap.put("TXN_AMOUNT" , amount);
        paramMap.put("WEBSITE" , Common.WEBSITE);
        paramMap.put("EMAIL" , email);
        paramMap.put("MOBILE_NO" , phone_no);
        paramMap.put("CALLBACK_URL" , Common.CALLBACK_URL+orderId);
        paramMap.put("CHECKSUMHASH" ,CheckSum );
        //   paramMap.put("CHECKSUMHASH" , GetPaytm_ChecksumGeneration(ORDER_ID,CUST_ID)"w2QDRMgp1/BNdEnJEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder Order = new PaytmOrder(paramMap);


        Service.initialize(Order, null);



        /* {
                                "TXNID": "4203335",
                                    "BANKTXNID": "",
                                    "ORDERID": "ORDER1409950517",
                                    "TXNAMOUNT": "1",
                                    "STATUS": "TXN_SUCCESS",
                                    "TXNTYPE": "",
                                    "CURRENCY": "INR",
                                    "GATEWAYNAME": "ICICI",
                                    "RESPCODE": "01",
                                    "RESPMSG": "Txn Successfull.",
                                    "BANKNAME": "HDFC",
                                    "MID": "robosf49909586699899",
                                    "PAYMENTMODE": "CC",
                                    "REFUNDAMT": "",
                                    "TXNDATE": "2013­04­19 14:35:50.775483",
                                    "CHECKSUMHASH": "1fP7njAc3HodnsbhpN6UQYbHeOJ9pQyNQukzHm7cNhawG9qdxrvFIWVEQaDbIbElynCS3KWyU+ekPbgAMTzkYn3a31Gd/uiO5uJBUuHARiU="
                            }*/

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.e("LOG", "Payment Transaction : " + inResponse);
                        //   Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                        // E/LOG: Payment Transaction : Bundle[{GATEWAYNAME=WALLET,
                        // TXNDATE=2017-08-17 12:13:22.0, PAYMENTMODE=PPI,
                        // STATUS=TXN_FAILURE, MID=Primes84973633105435,
                        // ORDERID=MYKIDDYORDER100004342, CURRENCY=INR,
                        // TXNID=70000169199, TXNAMOUNT=149.00, BANKTXNID=,
                        // BANKNAME=,
                        // RESPMSG=Wallet does not allow multiple withdrawls within a minute. Please try to withdraw after some time.,
                        // RESPCODE=269,
                        // CHECKSUMHASH=HyHQIAar/vS/OEXNU/2ir/82htgWInq6AF5VuOyeUGDzXMYfGj6qHeqS1ngo4ceDkuOWdpMWhCmi4qXZGXev1W0VBM/sl1jMdG6lDvcvpZI=}]


                        //  08-17 12:50:13.031 3873-3873/com.primesys.mitra E/LOG: Payment Transaction : Bundle[{GATEWAYNAME=WALLET, TXNDATE=2017-08-17 12:49:14.0, PAYMENTMODE=PPI, STATUS=TXN_SUCCESS, MID=Primes84973633105435, ORDERID=MYKIDDYORDER200005750, CURRENCY=INR, TXNID=70000169420, TXNAMOUNT=149.00, BANKTXNID=858004, BANKNAME=, RESPMSG=Txn Successful., RESPCODE=01, CHECKSUMHASH=IUtp2Lr0kseSCwiLkBg6liagaxm41c7yuu5YgM4OK6Puv6mo0PVaP0FZGpPxxaLw2VjhCpD5FbCOa6cgKsG0N1AGsPnxBv1IIPH04l3YtJU=}]


                        try{
                            String mid = inResponse.getString("MID");
                            String RESPMSG = inResponse.getString("RESPMSG");
                            Log.e("LOG------------", "Payment Transaction Json: " + mid+"      "+RESPMSG);


                            if (inResponse.getString("RESPCODE").equalsIgnoreCase("01")){
                                //Update Payment Information
                                String paymentId = inResponse.getString("TXNID");
                                try{
                                    if (Common.PlatformRenewalStatus){
                                        UpdateDevicePaymentDetails update=new UpdateDevicePaymentDetails(UserPayID,StudentId,Common.userid, paymentId,PaymentTypeId,Status,Payment_PlanId,amount,renewContext);
                                        update.updateDevicePaymentDetails();
                                      /*  Intent intent = new Intent(getApplicationContext(),DeviceLevelRenewService.class);
                                        startActivity(intent);*/
                                        //  finish();

                                    }else {
                                        UpdatePaymentDetails update=new UpdatePaymentDetails(StudentId,Common.userid, paymentId,PaymentTypeId,Status,renewContext);
                                        update.updatePaymentDetails();
                                       /* Intent intent = new Intent(getApplicationContext(),RenewServiceActivity.class);
                                        startActivity(intent);*/
                                        //finish();

                                    }

                                    PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_DEVICE_LIST);

                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }else if (inResponse.getString("RESPCODE").equalsIgnoreCase("141")){
                                Common.ShowSweetAlert(renewContext,"Transaction cancelled by your side.Please try again.");
                            }else if (inResponse.getString("RESPCODE").equalsIgnoreCase("227")){
                                Common.ShowSweetAlert(renewContext,"Payment Failed due to a Bank Failure. Please try after some time.");
                            }else if (inResponse.getString("RESPCODE").equalsIgnoreCase("810")){
                                Common.ShowSweetAlert(renewContext,"Transaction cancelled by your side.Please try again.");
                            }else if (inResponse.getString("RESPCODE").equalsIgnoreCase("8103")){
                                Common.ShowSweetAlert(renewContext,"Transaction cancelled by your side post login. You had in-sufficient Wallet balance for completing transaction..");
                            }else {
                                Common.ShowSweetAlert(renewContext,"Transaction failed due to  "+RESPMSG+"Please try after some time.");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }




                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                        Log.e("LOG", "Payment Transaction Failed  networkNotAvailable" );

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                        Log.e("LOG", "Payment Transaction Failed  clientAuthenticationFailed" + inErrorMessage);

                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Log.e("LOG", "Payment Transaction Failed  onErrorLoadingWebPage" + inErrorMessage);

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        Log.e("LOG", "Payment Transaction Failed  onBackPressedCancelTransaction" );

                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.e("LOG", "Payment Transaction Failed  onTransactionCancel" + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }


    private void parseJSON(String response) {

        Log.e("parseJSON respo", response);

        try
        {
            JSONObject jo=new JSONObject(response);
            if (jo.getString("error").equals("false")) {
                CheckSum=jo.getString("message");

                onStartTransaction();
            }else {
                Common.ShowSweetAlert(renewContext,"Security issue occur,please try again.");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

}
