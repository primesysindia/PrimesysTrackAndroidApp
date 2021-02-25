package com.primesys.VehicalTracking.ActivityMykiddyLike;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Dto.DeviceSimNo;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Voice_Servilence extends AppCompatActivity {
	private RequestQueue reuestQueue;
	private StringRequest stringRequest;
	static Context context;
	private String TAG="Voice_Servilence";
//	private String DeviceSimNo="";
	TextView tv_msg,tv_error;
	public String StudentId="";
	Button yes,no,edit;
	public static SweetAlertDialog pDialogmain;

	public static DeviceSimNo devicedata;
	public static CountDownTimer countdowntimer;
	private String updated_simno="";
	private Dialog EditVsNumberDialog;
	@SuppressLint("NewApi")
	final static String REQ_SMS="android.Manifest.permission.SEND_SMS";
	public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
	public SharedPreferences sharedPreferences;
	private String key_DeviceSimNo="DeviceSimNo";
	private SharedPreferences.Editor editor;
	private String deviceSimNo="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice__servilence);
		context=Voice_Servilence.this;
		StudentId=getIntent().getStringExtra("StudentId");
		findViewbyid();
		yes.setOnClickListener(new OnClickListener() {
			

			@TargetApi(23)
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaveDeviceSimNO();
					
				 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					  if (!checkPermission())
		             requestPermission();
					  else
						  Sendsms(devicedata.getDeviceSimNumber());

				}else{
	
					 Sendsms(devicedata.getDeviceSimNumber());
	
				}
				 
				 

			}
		});
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ShowEditVsNumberDialog(devicedata);
				finish();
			}
		});
		
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowEditVsNumberDialog(devicedata);

			}
		});
	}

	private void findViewbyid() {
		tv_msg=(TextView) findViewById(R.id.tv_msg);
		yes=(Button) findViewById(R.id.vs_yes);
		no=(Button) findViewById(R.id.vs_no);
		edit=(Button) findViewById(R.id.vs_edit);
		tv_error=(TextView) findViewById(R.id.tv_vserror);
		tv_msg.setVisibility(View.GONE);
		yes.setVisibility(View.GONE);
		no.setVisibility(View.GONE);
		edit.setVisibility(View.GONE);
		tv_error.setVisibility(View.GONE);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GetDeviceMobileNo();

	}
	
private void GetDeviceMobileNo() {
		

		reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
		final ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setTitle("Progress wait.......");
		pDialog.setCancelable(false);
		pDialog.setIndeterminateDrawable(getResources().getDrawable(R.mipmap.ic_icon));
		pDialog.show();
		//JSon object request for reading the json data
		stringRequest = new StringRequest(Method.POST,Common.TrackURL+"UserServiceAPI/GetDeviceSimNo",new Response.Listener<String>() {
		//stringRequest = new StringRequest(Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/SQLQuiz/Postscore",new Response.Listener<String>() {


			@Override
			public void onResponse(String response) {

				System.out.println("Responce of Get sim no----"+response);

				parseMObJSON(response);
				pDialog.dismiss();


			}

		},
		new Response.ErrorListener() {


			@Override
			public void onErrorResponse(VolleyError error) {
				pDialog.hide();
				if (error!=null)
					Log.e("Error", error.toString());
				SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
				pDialog.setTitleText("Message");
				pDialog.setContentText("your network might be slowdown.Please try again. ");
				pDialog.setCancelable(true);
				pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						finish();
					}
				});
				pDialog.show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("StudentID",StudentId);
				params.put("ComandType","monitor");

				System.out.println("REq----get mob------"+params);
				return params;
			}

		};


		stringRequest.setTag(TAG);
		stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
		// Adding request to request queue
		reuestQueue.add(stringRequest);
	}

	protected void parseMObJSON(String result) {
		// TODO Auto-generated method stub
		
//nce of Get sim no----{"DeviceSimNumber":"9145734716",
		//"":"0","":"1","":
		//	"monitor","ActualCommand":"MONITOR#","error":"false"}

		try {
			JSONObject jo=new JSONObject(result);
			if (jo.getString("error").equalsIgnoreCase("false")) {
				 devicedata=new DeviceSimNo();
				devicedata.setDeviceSimNumber(jo.getString("DeviceSimNumber"));
				devicedata.setActualCommand(jo.getString("ActualCommand"));
				devicedata.setCommnadType(jo.getString("CommnadType"));
				devicedata.setVSCallback(jo.getString("VSCallback"));
				devicedata.setVSEnabled(jo.getString("VSEnabled"));
				devicedata.setMessage(jo.getString("Message"));

				if (devicedata.getVSEnabled().equals("0")) {
						if (devicedata.getDeviceSimNumber().equals("")||devicedata.getDeviceSimNumber().equalsIgnoreCase("NUll")) {
							Common.ShowSweetAlert(context, "Device SIM number is not "
                                                                + "configured .Please enter device SIM number.");
							ShowEditVsNumberDialog(devicedata);
						}else 
							setvsdata();
				} else {
					/*Common.ShowSweetAlert(
							"Voice surveillance is not enable for this device, please contact"
							+ " with contact@mykiddytracker.com ",
							context);*/

					SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
					pDialog.setTitleText("Message");
					pDialog.setContentText("Voice surveillance is not enable for this device, please contact"
							+ " with contact@mykiddytracker.com ");
					pDialog.setCancelable(true);
					pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							finish();
						}
					});
					pDialog.show();


				}
			}else {
				SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
				pDialog.setTitleText("Message");
				pDialog.setContentText("your network might be slowdown.Please try again. ");
				pDialog.setCancelable(true);
				pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						finish();
					}
				});
				pDialog.show();
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//finish();
		}
		
				
	}
	
	
	
	private void ShowEditVsNumberDialog(DeviceSimNo devicedata) {

		// custom dialog
		
		final EditText simno;
		final Button submit;
		 EditVsNumberDialog = new Dialog(context);
		 EditVsNumberDialog.setContentView(R.layout.vs_editno_dialog);
		 EditVsNumberDialog.setTitle("Update device SIM No ");

	
		simno=(EditText)EditVsNumberDialog.findViewById(R.id.edit_no);
		TextView d_title = (TextView) EditVsNumberDialog.findViewById(R.id.d_title);
		d_title.setText("New sim no");
		submit=(Button) EditVsNumberDialog.findViewById(R.id.vsnosubmit);

			if (devicedata.getDeviceSimNumber().equals("")||devicedata.getDeviceSimNumber().equals("NUll")) 
				simno.setText(devicedata.getDeviceSimNumber());
	
		
		
		
		// if button is clicked, close the custom dialog
			submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updated_simno=simno.getText().toString();
                if (!updated_simno.equals("")&&updated_simno.length()>=10)
				PostDeviceSimno(updated_simno);
                else
                	Common.ShowSweetAlert(context, "Please enter valid device sim number.");
			}
		});

			EditVsNumberDialog.show();
			
	}

	protected void PostDeviceSimno(final String simno) {
		

		reuestQueue= Volley.newRequestQueue(context); //getting Request object from it
		final ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setTitle("Progress wait.......");
		pDialog.setCancelable(false);
		pDialog.setIndeterminateDrawable(getResources().getDrawable(R.mipmap.ic_icon));
		pDialog.show();
		//JSon object request for reading the json data
		stringRequest = new StringRequest(Method.POST,Common.TrackURL+"UserServiceAPI/PostDeviceSimno",new Response.Listener<String>() {
		//stringRequest = new StringRequest(Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/SQLQuiz/Postscore",new Response.Listener<String>() {


			@Override
			public void onResponse(String response) {
				pDialog.hide();

				System.out.println("Responce of mobileno---"+response);

				parseupdateJSON(response);
			


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

				params.put("StudentID",StudentId);
				params.put("Simno",simno);

				System.out.println("REq----post mobileno------"+params);
				return params;
			}

		};


		stringRequest.setTag(TAG);
		// Adding request to request queue
		reuestQueue.add(stringRequest);
	}

	protected void parseupdateJSON(String response) {
		
		try {
			JSONObject jo=new JSONObject(response);
			
			if (jo.getString("error").equals("false")) {
				devicedata.setDeviceSimNumber(updated_simno);
				deviceSimNo = devicedata.getDeviceSimNumber();
				SaveDeviceSimNO();
				setvsdata();
				Common.ShowSweetSucess(context, jo.getString("message"));

			}else{
				Common.ShowSweetAlert(context, jo.getString("message"));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			EditVsNumberDialog.dismiss();
		}
	}

	private void setvsdata() {
		tv_msg.setVisibility(View.VISIBLE);
		yes.setVisibility(View.VISIBLE);
		no.setVisibility(View.VISIBLE);
		edit.setVisibility(View.VISIBLE);
		tv_msg.setText("Please confirm "+devicedata.getDeviceSimNumber()+" is device SIM Number ? ");
	}

	void Sendsms(final String parentMob)
	{
		/* pDialogmain = new ProgressDialog(context);
		 pDialogmain.setTitle("Voice surveillance..");
		 pDialogmain.setMessage("Voice surveillance is being initiated please wait for call from device."
		 		+ "(Ensure calling device has talk time balance) ...");
		 pDialogmain.setCancelable(false);
		 pDialogmain.setIndeterminateDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
		 pDialogmain.show();*/

		/* pDialogmain = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
		pDialogmain.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialogmain.setTitleText("Voice surveillance..");
		pDialogmain.setContentText("Voice surveillance is being initiated please wait for call from device."
				+ "(Ensure calling device has talk time balance) ...");

		pDialogmain.setCancelable(false);
		pDialogmain.show();*/
		 
		try {


				new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
						.setTitleText("Confirmation?")
						.setContentText("Voice surveillance is being initiated please wait for call from device."
						+ "(Ensure calling device has talk time balance) ...")
						.setConfirmText("Confirm!")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {

								try {
									Log.e(PrimesysTrack.TAG,"Yesss in Send sms--"+parentMob);
									Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + parentMob));
									smsIntent.putExtra("sms_body", devicedata.getActualCommand());
									context.startActivity(smsIntent);

									// setSucessDialog(mContext);
								}catch (Exception e){
									e.printStackTrace();
								}



								sDialog.dismissWithAnimation();
							}
						})
						.setCancelText("Cancel")
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sDialog.cancel();
							}
						})
						.show();

		     }catch (Exception ex) {
		     ex.printStackTrace();
		     }
	}



	protected void setErrormsg() {
		try{
				// TODO Auto-generated method stub
			//Voice_Servilence.pDialogmain.dismiss();

			tv_error.setVisibility(View.VISIBLE);
			tv_error.setText(getResources().getString(R.string.voice__servilence_error)	);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
	
	 public static void closeActivity(){
		 ((Activity) context).finish();
	   }

	 @Override
	 public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
	     switch (requestCode) {
	         case MY_PERMISSIONS_REQUEST_SMS: {

	             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	                 // permission was granted
					 Sendsms(devicedata.getDeviceSimNumber());
	             } else {
	                 // permission denied
	            	 Common.ShowSweetAlert(context, "You denied permission of sending SMS. If you really want to use Voice surveillance allow that one. ");

	             }
	             return;
	         }
	     }
	 }
	 
	  private void requestPermission(){
			
          ActivityCompat.requestPermissions(Voice_Servilence.this,new String[]{android.Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SMS);
/*
	        if (ActivityCompat.shouldShowRequestPermissionRationale(Voice_Servilence.this,android.Manifest.permission.SEND_SMS)){
		 
	        } else {
	 
	            ActivityCompat.requestPermissions(Voice_Servilence.this,new String[]{android.Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SMS);
	        }*/
	    }
	  
		 public boolean checkPermission(){
		        int result = ContextCompat.checkSelfPermission(Voice_Servilence.this, android.Manifest.permission.SEND_SMS);
		        if (result == PackageManager.PERMISSION_GRANTED){
		 
		            return true;
		 
		        } else {
		 
		            return false;
		 
		        }
		    }

	void SaveDeviceSimNO(){

		sharedPreferences=context.getSharedPreferences("User_data",Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		editor.putString(key_DeviceSimNo, deviceSimNo);
		editor.commit();
	}
}
