package com.primesys.VehicalTracking.Activity;

/**
 * Created by pt002 on 14/3/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.primesys.VehicalTracking.Utility.Common;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


public class UpdateDevicePaymentDetails {
    private final String amount;
    String userId,paymentid,payment_typeid,status,studentId,payment_planid,user_payid;
    Context context;
    public UpdateDevicePaymentDetails(String userPayID, String studentId, String d_userId, String d_paymentid, String d_typeofpayment, String d_status, String payment_PlanId, String amount,Context d_webContext) {
        this.userId=d_userId;
        this.paymentid=d_paymentid;
        this.payment_typeid=d_typeofpayment;
        this.status=d_status;
        this.context=d_webContext;
        this.payment_planid=payment_PlanId;
        this.studentId=studentId;
        this.user_payid=userPayID;
        this.amount=amount;
    }

    public void updateDevicePaymentDetails(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new SaveDevicePayment().executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
        } else {
            new SaveDevicePayment().execute();
        }
    }
    // make the request for taking the values from the DB
    class SaveDevicePayment extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(Void... params) {
            String result="Data Not Found";

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost post=new HttpPost(Common.URL+"ParentAPI.asmx/SaveDevicePayment");
            try {
               
                List<NameValuePair> postParameter = new ArrayList<NameValuePair>();
                postParameter.add(new BasicNameValuePair("UserID",Common.WebUserId));
                postParameter.add(new BasicNameValuePair("PaymentPlanID",payment_planid));
                postParameter.add(new BasicNameValuePair("StudentID",studentId));
                postParameter.add(new BasicNameValuePair("PaymentType",payment_typeid ));
                postParameter.add(new BasicNameValuePair("UserPayID",user_payid ));
                postParameter.add(new BasicNameValuePair("ActiveStatus", status));   
                postParameter.add(new BasicNameValuePair("CashAmount", amount));


                postParameter.add(new BasicNameValuePair("TransActionID", paymentid));
                post.setEntity(new UrlEncodedFormEntity(postParameter));
                System.out.println("SaveDevicePayment-------------Req-"+postParameter.toString());
                HttpResponse response = httpclient.execute(post);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                result = e.getMessage();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("PaymentInfoTask-------------respo-"+result);
            if(result.contains("false"))
            {
                Common.showToast("Payment is made successfully",context );

                Intent startMain = new Intent(context,Home.class);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMain);

            }
            else
                Common.showToast("Check once again there is Error !", context);
        }
    }
}
