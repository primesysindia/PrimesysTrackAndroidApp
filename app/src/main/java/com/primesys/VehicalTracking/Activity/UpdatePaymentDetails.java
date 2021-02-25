package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

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

public class UpdatePaymentDetails {
	String userId,paymentid,typeofpayment,status,studentId;
Context context;
	public UpdatePaymentDetails(String studentId, String d_userId, String d_paymentid, String d_typeofpayment, String d_status, Context d_webContext) {
		this.userId=d_userId;
		this.paymentid=d_paymentid;
		this.typeofpayment=d_typeofpayment;
		this.status=d_status;
		this.context=d_webContext;
		this.studentId=studentId;
	}

	public void updatePaymentDetails(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new PaymentInfoTask().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
		} else {
			new PaymentInfoTask().execute();
		}
	}
	// make the request for taking the values from the DB
	class PaymentInfoTask extends AsyncTask<Void, Void, String>
	{

		@Override
		protected void onPreExecute() {

		}
		@Override
		protected String doInBackground(Void... params) {
			String result="Data Not Found";

			HttpClient httpclient=new DefaultHttpClient();
			HttpPost post=new HttpPost(Common.URL+"ParentAPI.asmx/UpdateUserPayment");
			try {
				List<NameValuePair> postParameter = new ArrayList<NameValuePair>();
				postParameter.add(new BasicNameValuePair("ParentId",Common.userid));
				postParameter.add(new BasicNameValuePair("typeofpayment",typeofpayment));
				postParameter.add(new BasicNameValuePair("paymentId", paymentid));
				postParameter.add(new BasicNameValuePair("ActiveStatus", status));

				post.setEntity(new UrlEncodedFormEntity(postParameter));
				Log.e("PaymentInfoTask REq-----", postParameter.toString()+"");
				HttpResponse response = httpclient.execute(post);
				result = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				result = e.getMessage();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			Log.e("PaymentInfoTask REspo-----", result.toString()+"");
			if(result.contains("false"))
			{
				Common.showToast("Payment is made successfully",context );

				Intent startMain = new Intent(context,LoginActivity.class);
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
