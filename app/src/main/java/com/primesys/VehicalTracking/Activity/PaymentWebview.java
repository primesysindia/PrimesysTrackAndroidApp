package com.primesys.VehicalTracking.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentWebview extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    WebView webviewPayment;
    Handler mHandler;
    Context webContext=PaymentWebview.this;
    final Activity activity = this;

    String RESULT = "result";
    String PAYMENT_ID = "paymentId";

    String amount = "10";
    String firstname = "Piyush";
    String email = "piyusshsable@gmail.com";
    String productInfo = "MyKiddyTracker";
    String phone_no="8055855139";
    String PaymentTypeId="",StudentId;
    //////////////////////
    String payment_extension="";
    private String Status;
    private SweetAlertDialog LoadProgress;
    private String Payment_PlanId="";
    private String UserPayID="0";

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);

        amount = getIntent().getStringExtra("Payment_Amount");
        email = getIntent().getStringExtra("UserName");
        phone_no=getIntent().getStringExtra("MobNo");
        firstname = getIntent().getStringExtra("FirstName");
        PaymentTypeId= getIntent().getStringExtra("Payment_TypeId");
        Status= getIntent().getStringExtra("Status");
        StudentId=getIntent().getStringExtra("StudentId");
        Payment_PlanId=getIntent().getStringExtra("Payment_PlanId");
        UserPayID=getIntent().getStringExtra("UserPayID");
        //	txnid=getIntent().getStringExtra("transid");
        webviewPayment = (WebView) findViewById(R.id.webView1);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);
        webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        //	webviewPayment.addJavascriptInterface(new PayUJavaScriptInterfaces(activity), "PayUMoney");
        //webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(activity), "PayUMoney");
        StringBuilder url_s = new StringBuilder();

		/*
		 *
		 *  This Is URL With TEST Server\
		 */
        url_s.append(Common.PaymentURL);

		/*
		 *This Is URL With Live Server\
		 */
     //  url_s.append("https://secure.payu.in/_payment");

        //Log.e(TAG, "call url " + url_s);


        webviewPayment.postUrl(url_s.toString(), EncodingUtils.getBytes(getPostString(), "utf-8"));
        /**
         * Get Value
         *
         */
        //Common.showToast(Common.Amount, webContext);
        payment_extension=getIntent().getStringExtra("payment_extension");
        firstname=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("username");
        productInfo="MyKiddyTracker"+""+payment_extension;


        webviewPayment.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url)
            {
                // TODO hide your progress image
                super.onPageFinished(view, url);
            }
        });

        webviewPayment.setWebViewClient(new WebViewClient() {



            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
              /*  LoadProgress=Common.ShowSweetProgress(webContext,"Loading..Please Wait.....!");
                LoadProgress.setCancelable(true);
                LoadProgress.show();*/

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // LoadProgress.dismiss();
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                Log.e("Error", "Exception caught!");
                Common.ShowSweetAlert(webContext,"Got Error...Please Try Again!");
                //  LoadProgress.dismiss();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                //    message += "\"SSL Certificate Error\" Do you want to continue anyway?.. YES";
                String  messagealert = " Do you want to continue payment with PayUmoney?.. YES";

                Log.e("Eroorrrrrrrrrrrrr",message);

                final SweetAlertDialog pDialog = new SweetAlertDialog(webContext, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("Message");
                pDialog.setContentText(messagealert);
                pDialog.setCancelable(true);
                pDialog.setConfirmText("OK,Proceed");
                pDialog.setCancelText("NO");

                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        handler.proceed();
                        pDialog.dismiss();
                    }
                });

                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent startMain = new Intent(webContext,Home.class);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);

                    }
                });

                pDialog.show();

            }

        });


	}




    private String getPostString()
    {
        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(Common.key);
        post.append("&");
        post.append("txnid=");
        post.append(Common.txnid+System.currentTimeMillis());
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(phone_no);
        post.append("&");
        post.append("surl=");
        post.append(Common.PayU_surl);
        post.append("&");
        post.append("furl=");
        post.append(Common.PayU_furl);
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();
		/* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
        MessageDigest digest=null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        checkSumStr.append(Common.key);
        checkSumStr.append("|");
        checkSumStr.append(Common.txnid+System.currentTimeMillis());
        checkSumStr.append("|");
        checkSumStr.append(amount);
        checkSumStr.append("|");
        checkSumStr.append(productInfo);
        checkSumStr.append("|");
        checkSumStr.append(firstname);
        checkSumStr.append("|");
        checkSumStr.append(email);
        checkSumStr.append("|||||||||||");
        checkSumStr.append(Common.salt);

        digest.update(checkSumStr.toString().getBytes());

        hash = bytesToHexString(digest.digest());
        post.append("hash=");
        post.append(hash);
        post.append("&");
        Log.i(TAG, "SHA result is " + hash);

        post.append("service_provider=");
        post.append("payu_paisa");
        Log.e("Payment"  ,""+post.toString());
        return post.toString();
    }

    private JSONObject getProductInfo()
    {
        try {
            //create payment part object
            JSONObject productInfo = new JSONObject();
            JSONObject jsonPaymentPart = new JSONObject();
            jsonPaymentPart.put("name", "TapFood");

            jsonPaymentPart.put("description", "Lunchcombo");
            jsonPaymentPart.put("value", "500");
            jsonPaymentPart.put("isRequired", "true");
            jsonPaymentPart.put("settlementEvent", "EmailConfirmation");

            //create payment part array
            JSONArray jsonPaymentPartsArr = new JSONArray();
            jsonPaymentPartsArr.put(jsonPaymentPart);

            //paymentIdentifiers
            JSONObject jsonPaymentIdent = new JSONObject();
            jsonPaymentIdent.put("field", "CompletionDate");
            jsonPaymentIdent.put("value", "31/10/2012");

            //create payment part array
            JSONArray jsonPaymentIdentArr = new JSONArray();
            jsonPaymentIdentArr.put(jsonPaymentIdent);

            productInfo.put("paymentParts", jsonPaymentPartsArr);
            productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

            Log.e(TAG, "product Info = " + productInfo.toString());
            return productInfo;


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //@JavascriptInterface
    private final class PayUJavaScriptInterface {

        PayUJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void success(long id, final String paymentId) {


            //Update Payment Information
            try{
                if (Common.PlatformRenewalStatus){
                    UpdateDevicePaymentDetails update=new UpdateDevicePaymentDetails(UserPayID,StudentId,Common.userid, paymentId,PaymentTypeId,Status,Payment_PlanId,amount,webContext);
                    update.updateDevicePaymentDetails();
                }else {
                    UpdatePaymentDetails update=new UpdatePaymentDetails(StudentId,Common.userid, paymentId,PaymentTypeId,Status,webContext);
                    update.updatePaymentDetails();
                }

                PrimesysTrack.mDbHelper.truncateTables(DBHelper.TABLE_DEVICE_LIST);

            }catch(Exception e){
                e.printStackTrace();
            }




            Intent intent = new Intent(getApplicationContext(),RenewServiceActivity.class);
            startActivity(intent);
            finish();



            try{
                mHandler.post(new Runnable() {
                    public void run() {
                        mHandler = null;

                        Intent intent = new Intent(getApplicationContext(),RenewServiceActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }catch(Exception e){
                System.err.print("##################### :"+e.getMessage());
            }

        }

        @JavascriptInterface
        public void failure(final String id, String error) {
			/*mHandler.post(new Runnable() {
		               @Override
		               public void run() {
		                   failPayment();
		               }
		           });*/
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent();
                    intent.putExtra(RESULT, params);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();


        new SweetAlertDialog(webContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("To complete the payment you should stay on this page.")
                .setCancelText("STAY ON THIS PAGE")
                .setConfirmText("LEAVE  THIS PAGE")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent startMain = new Intent(webContext,Home.class);
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
