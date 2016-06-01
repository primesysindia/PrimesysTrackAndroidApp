package com.primesys.VehicalTracking.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class FileUploadServer {
	FileInputStream fileInputStream;
	String UploadFile;
	File Uploadfilepath;
	String Upload_filename;
	String testFile_path="";
	String testFile_name="";
	byte[] bFileConversion; 
	ProgressDialog pDialog;
	private String ParentId;
	StringRequest stringRequest;
	RequestQueue reuestQueue;
	final String TAG="REquest";

	public void FileToByteArrayConversion(String ParentId,String docPath, String filename,Context context) throws IOException
	{

		this.ParentId=ParentId;
		Upload_filename=filename;
		Uploadfilepath = new File(docPath);
		bFileConversion = new byte[(int) Uploadfilepath.length()];
		//convert file into array of bytes
		fileInputStream = new FileInputStream(Uploadfilepath);
		fileInputStream.read(bFileConversion);
		UploadFile=Base64.encodeToString(bFileConversion, Base64.DEFAULT);

		if (null!=UploadFile) {
			if (Common.conectionStatus) {

				FileUploadAsync(context);
				}
			}

			Log.e("ParentId", ParentId);
			Log.e("FileName", Upload_filename);
			Log.e("ByteArray", UploadFile);

		}

		
     
	
/*
	public void FileToByteArrayConversion(String docPath, String filename) throws IOException
	{  
		 
		Uploadfilepath = new File(docPath);
		Upload_filename=Uploadfilepath.getName();
		bFileConversion = new byte[(int) Uploadfilepath.length()];
		//convert file into array of bytes
		fileInputStream = new FileInputStream(Uploadfilepath);
		fileInputStream.read(bFileConversion);
		UploadFile=Base64.encodeToString(bFileConversion, Base64.DEFAULT);
	
		if (null!=UploadFile) {		
			if (Common.conectionStatus) 
				 FileUploadAsync(context);
		}
	}

*/


	private void FileUploadAsync(Context context) {


		reuestQueue = Volley.newRequestQueue(context);

		final ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setTitle("Loding wait.......");
		pDialog.setCancelable(true);
		pDialog.show();
		//JSon object request for reading the json data
		stringRequest = new StringRequest(Request.Method.POST,Common.URL+"UserServiceAPI/uploadPhoto",new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				parseData(response);


				pDialog.hide();
			}
		},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pDialog.hide();
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

				params.put("fileupload", UploadFile);

				params.put("filename", Common.userid + ".jpg");
				params.put("userid", Common.userid);
				/*System.out.println(Upload_filename + "\n" + Common.userid + " \n");

				System.out.println(UploadFile);*/
				System.err.println("photo Req--- "+params);
				return params;
			}
		};
		stringRequest.setTag(TAG);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		reuestQueue.add(stringRequest);


	}

	protected void parseData(String result) {

		System.out.println(result);



	}


}
