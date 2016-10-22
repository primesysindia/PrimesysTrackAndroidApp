package com.primesys.VehicalTracking.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.Utility.GraphicsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserProfileActivity extends Activity {

	StringRequest stringRequest,stringRequest1;
	RequestQueue reuestQueue,reuestQueue1;
	ImageLoader imageLoader;
	ImageView edit;
	Bitmap bm;
	final static int REQUEST_CAMERA=100;
	final static int SELECT_FILE=200;
	
	//For Camera Image 7/9/2015
	final int CAMERA_CAPTURE = 1;
	// keep track of cropping intent
	final int PIC_CROP = 2;
	// captured picture uri
	private Uri picUri;

	//For Camera Image 7/9/2015
	
	Context proContext=UserProfileActivity.this;

	EditText text_user,txt_address,txt_contactno,txt_email,txt_gender,text_role,text_party;
	CircularNetworkImageView profile_pic;
	String docPath="",filename="";
	public static final String TAG="ProfileTag";
	public  String userId;
	 SweetAlertDialog pDialog;
	private Button updateApply;
	FileInputStream fileInputStream;
	String UploadFile="";
	File Uploadfilepath;
	String Upload_filename;

	byte[] bFileConversion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		findViewByID();
		
	//profile_pic.setImageBitmap(getRoundedShape(bm));
	
		GetProfileInfo();
		 

		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		updateApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(valid())
					if(Common.conectionStatus)
						postProfileUpdateRequest();
					else
						Common.ShowSweetAlert(proContext, "Please enable internet connection");
			}
				
			
		});
		
		 
	//	text_user.addTextChangedListener(watcher);
	//	txt_address.addTextChangedListener(watcher);
	//	txt_contactno.addTextChangedListener(watcher);
	}

	//find View ById
	void findViewByID()
	{
		updateApply=(Button)findViewById(R.id.btnUpdateProfile);
		text_user=(EditText)findViewById(R.id.text_user);
		txt_address=(EditText)findViewById(R.id.txt_address);
		txt_contactno=(EditText)findViewById(R.id.txt_contactno);
		txt_email=(EditText)findViewById(R.id.txt_email);
		txt_gender=(EditText)findViewById(R.id.txt_gender);
		profile_pic=(CircularNetworkImageView)findViewById(R.id.profile_pic);
		edit=(ImageView)findViewById(R.id.edit);
	}
	/*TextWatcher watcher = new TextWatcher(){

	    @Override
	    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	    
	    }

	    @Override
	    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	    }

	    @Override
	    public void afterTextChanged(Editable editable) {
	   
				
					//postProfileUpdateRequest();
			
	    	//Toast.makeText(getApplicationContext(), "user_text.getted ", Toast.LENGTH_SHORT).show();
	    }
	};*/
	void GetProfileInfo()
	{
		reuestQueue=Volley.newRequestQueue(proContext); //getting Request object from it
		final SweetAlertDialog pDialog = new SweetAlertDialog(proContext, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText("Please Wait....");
		pDialog.setCancelable(true);
		pDialog.show();
		//JSon object request for reading the json data
		stringRequest = new StringRequest(Method.POST, Common.URL+"ParentAPI.asmx/GetProfileInformation",new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				parseProfileJSON(response);
				Log.e("Userprofile Responce----", response);

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
					parseProfileJSON(new String(error.networkResponse.data));
				}
			}
		}) {
			
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("UserId", Common.userid+"");
				Log.e("Userprofile Req----", params+"");


				return params;
			}

		};
		stringRequest.setTag(TAG);
		// Adding request to request queue
		reuestQueue.add(stringRequest);
	}
	void parseProfileJSON(String result)
	{
		/*CircularNetworkImageView netCircle=new CircularNetworkImageView(proContext);
		try
		{
			JSONObject jo=new JSONObject(result);
			if(jo!=null)
			{

				text_user.setText(jo.getString("Name"));
				txt_email.setText(jo.getString("EmailID"));
				if(jo.has("ContactNumber"))
					txt_contactno.setText(jo.getString("ContactNumber"));
				//waiting for address by nilesh implementaed
				if(jo.has("Address")){
					txt_address.setText(jo.getString("Address"));
				}
				//Set_Role(jo.getString("roleid"));
				if(jo.has("photo")){
				String photo=jo.getString("photo");
				if(photo!=""){
					ImageLoader.ImageCache imageCache = new LruBitmapCache();
					imageLoader = new ImageLoader(
							Volley.newRequestQueue(proContext), imageCache);
					(profile_pic).setImageUrl(
							Common.Relative_URL+photo, imageLoader);
					((CircularNetworkImageView) profile_pic)
					.setDefaultImageResId(R.drawable.student);
					((CircularNetworkImageView) profile_pic)
					.setErrorImageResId(R.drawable.student);
							}else{
						Bitmap bitmap = ((BitmapDrawable)profile_pic.getDrawable()).getBitmap();	
						GraphicsUtil graphicUtil = new GraphicsUtil();
						// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic,(float)1.5,92));
						
						profile_pic.setImageBitmap(graphicUtil.getCircleBitmap(
								bitmap, 16));				
						}
				}
				
				else{
					Bitmap bitmap = ((BitmapDrawable)profile_pic.getDrawable()).getBitmap();	
					profile_pic.setImageBitmap(netCircle.getRoundedShape(bitmap));
				}
				
				
				
				
			}
		}
		catch(Exception e)
		{
			Log.e("In Exception in Photo",""+e);
		//	new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
			//.setTitleText(getResources().getString(R.string.server_error))
			//.show();
		}*/

	/*	try
		{
			System.out.println("Profile Respo11111----"+result);
			JSONArray joarray=new JSONArray(result);
			for (int i = 0; i < joarray.length(); i++) {
				JSONObject jo= joarray.getJSONObject(i);
				text_user.setText(jo.getString("Name"));
				txt_address.setText(jo.getString("Address"));
				txt_contactno.setText(jo.getString("MobileNo"));
				txt_email.setText(jo.getString("EmailID"));
				if (jo.getString("Gender").contains("F")) {
					text_gender.setText("Female");
				}else if(jo.getString("Gender").contains("M")) {
					text_gender.setText("Male");
				}
				*//*String Photo=jo.getString("Photo").trim();
				String Temp=Photo.replaceAll("~", " ").trim();
				ImageLoader.ImageCache imageCache = new LruBitmapCache();
				imageLoader = new ImageLoader(
						Volley.newRequestQueue(proContext), imageCache);
				(profile_pic).setImageUrl(
						Common.relativeurl+Temp.replaceAll(" ","%20"), imageLoader);
				((CircularNetworkImageView) profile_pic)
				.setDefaultImageResId(R.drawable.student);
				((CircularNetworkImageView) profile_pic)
				.setErrorImageResId(R.drawable.student);

				try{
					Bitmap bitmap = ((BitmapDrawable)profile_pic.getDrawable()).getBitmap();
					profile_pic.setImageBitmap(getRoundedShape(bitmap));
				}catch(Exception e){
					Log.e(TAG,""+e.getMessage());
				}*//*

				String Photo=jo.getString("Photo").trim();

				Bitmap bitmap=null;
				bitmap=DBHelper.getInstance(proContext).getBitMap(Common.userid+"");
				if (bitmap != null) {
					profile_pic.setImageBitmap(getRoundedShape(bitmap));
				}else{

					if(Photo.equalsIgnoreCase("No Photo")){

						Drawable myDrawable = proContext.getResources().getDrawable(R.drawable.student);
						Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
						profile_pic.setImageBitmap(anImage);
					}else{
						Bitmap bmp = Common.bytebitmapconversion(Photo);
						profile_pic.setImageBitmap(getRoundedShape(bmp));}
				}
			}


		}	catch(Exception e)
		{
			Log.e(TAG, ""+e);
		}
*/


		try
		{
			System.out.println("Profile Respo11111----"+result);
			JSONArray joarray=new JSONArray(result);
			for (int i = 0; i < joarray.length(); i++) {
				JSONObject jo= joarray.getJSONObject(i);
				text_user.setText(jo.getString("Name"));
				txt_address.setText(jo.getString("Address"));
				txt_contactno.setText(jo.getString("MobileNo"));
				txt_email.setText(jo.getString("EmailID"));
				if (jo.getString("Gender").contains("F")) {
					txt_gender.setText("Female");
				}else if(jo.getString("Gender").contains("M")) {
					txt_gender.setText("Male");
				}
				/*String Photo=jo.getString("Photo").trim();
				String Temp=Photo.replaceAll("~", " ").trim();
				ImageLoader.ImageCache imageCache = new LruBitmapCache();
				imageLoader = new ImageLoader(
						Volley.newRequestQueue(proContext), imageCache);
				(profile_pic).setImageUrl(
						Common.relativeurl+Temp.replaceAll(" ","%20"), imageLoader);
				((CircularNetworkImageView) profile_pic)
				.setDefaultImageResId(R.drawable.student);
				((CircularNetworkImageView) profile_pic)
				.setErrorImageResId(R.drawable.student);

				try{
					Bitmap bitmap = ((BitmapDrawable)profile_pic.getDrawable()).getBitmap();
					profile_pic.setImageBitmap(getRoundedShape(bitmap));
				}catch(Exception e){
					Log.e(TAG,""+e.getMessage());
				}*/

				String Photo=jo.getString("Photo").trim();

				Bitmap bitmap=null;
				bitmap= DBHelper.getInstance(proContext).getBitMap(Common.userid+"");
				if (bitmap != null) {
					profile_pic.setImageBitmap(getRoundedShape(bitmap));
				}else{

					if(Photo.equalsIgnoreCase("No Photo")){

						Drawable myDrawable = proContext.getResources().getDrawable(R.drawable.student);
						Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
						profile_pic.setImageBitmap(anImage);
					}else{
						Bitmap bmp = Common.bytebitmapconversion(Photo);
						profile_pic.setImageBitmap(getRoundedShape(bmp));}
				}
			}


		}	catch(Exception e)
		{
			Log.e(TAG, ""+e);
		}

	}

	private void Set_Role(String role) {
		// TODO Auto-generated method stub
		if (role.equals("2")) {
			
			text_role.setText("Sales Executive");
		}else if(role.equals("3")) {
			text_role.setText("Senior Executive");

		}else if(role.equals("1")) {
			text_role.setText("Admin");

		}else if(role.equals("4")) {
			text_role.setText("Godown Keeper");

		}
		
	}

	// show the rounded Image 
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 150;
		int targetHeight = 150;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
				targetHeight,Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap,
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;
	}

	// select image dialog that shows two option camera & select from gallery
	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",	"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(proContext);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);*/

					//	07/09/2015	For Camera Image
					Intent captureIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					// we will handle the returned data in onActivityResult
					startActivityForResult(captureIntent, CAMERA_CAPTURE);
					//	07/09/2015	For Camera Image

				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	// get the on Activity result back
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_CAPTURE) {
			
				picUri = data.getData();
				// carry out the crop operation
				performCrop();
			} 
			else if (requestCode == PIC_CROP) {
				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped bitmap
							Bitmap thePic = extras.getParcelable("data");
							
							
							//Upload Images On server 07/09/2015
							
							String tempPath = getPath(picUri, UserProfileActivity.this);
							Bitmap bm;
							BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
							bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
							//profile_pic.setImageBitmap(getRoundedShape(bm));
							// retrieve a reference to the ImageView
							ImageView picView = (ImageView) findViewById(R.id.profile_pic);
							// display the returned cropped image
							GraphicsUtil graphicUtil = new GraphicsUtil();
							// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic,(float)1.5,92));
							
							picView.setImageBitmap(graphicUtil.getCircleBitmap(
									thePic, 16));

							try{
								/*FileUploadServer upload=new FileUploadServer();

								upload.FileToByteArrayConversion("" + Common.userid, tempPath, Common.userid + ".jpg", proContext);*/
								FileToByteArrayConversion(tempPath, Common.userid + ".jpg");

								Common.ShowSweetSucess(proContext,"photo Uploaded Successfully" );
							}catch(Exception ex)
							{
								Log.e(TAG, ""+ex);
							}
							//For IMAge upload
							
							
							
							
							
				
				
				
				
			}
			
			//07/09/2015 For Image Crop
			
			
			
			else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();

				String tempPath = getPath(selectedImageUri, UserProfileActivity.this);
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
			

				CircularNetworkImageView netCircle=new CircularNetworkImageView(proContext);
				profile_pic.setImageBitmap(netCircle.getCircularBitmap(bm));
				
			
			
				try{
					/*FileUploadServer upload=new FileUploadServer();

					upload.FileToByteArrayConversion("" + Common.userid, tempPath, Common.userid + ".jpg", proContext);*/


					FileToByteArrayConversion(tempPath, Common.userid + ".jpg");


					Common.ShowSweetSucess(proContext,"photo Uploaded Successfully" );
				}catch(Exception ex)
				{
					Log.e(TAG, ""+ex);
				}
			}
		}
	}

	private void FileToByteArrayConversion(String tempPath, String filename) {

		try {
			Uploadfilepath = new File(tempPath);
			Upload_filename=Uploadfilepath.getName();
			bFileConversion = new byte[(int) Uploadfilepath.length()];
			//convert file into array of bytes
			fileInputStream = new FileInputStream(Uploadfilepath);
			fileInputStream.read(bFileConversion);
			UploadFile= Base64.encodeToString(bFileConversion, Base64.DEFAULT);

			//Log.e("Uplod photo file",UploadFile);


			if (!UploadFile.equals("")) {
				if (Common.conectionStatus)
					FileUploadAsync();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	// read Path of saved image from the sd card
	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_back, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.back) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void postProfileUpdateRequest()
	{
		reuestQueue1=Volley.newRequestQueue(proContext); //getting Request object from it
		 pDialog = new SweetAlertDialog(proContext, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText("Updating wait.......");
		pDialog.setCancelable(true);
		pDialog.show();
		//JSon object request for reading the json data
		
		stringRequest1 = new StringRequest(Method.POST, Common.URL+"ParentAPI.asmx/UpdteProfileInformation",new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				parseUpdateProfileJSON(response);
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
					parseUpdateProfileJSON(new String(error.networkResponse.data));
				}
			}
		}) {
			
			
			//Nilesh IMplemented Code 1-8-2015
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String,String>();
			
				/*
				String name=text_user.getText().toString().trim();
				String address=txt_address.getText().toString().trim();
				String contactno=txt_contactno.getText().toString().trim();*/

				
				params.put("UserId", Common.userid);
				params.put("Name", text_user.getText().toString().trim());
				params.put("Address",txt_address.getText().toString().trim());
				params.put("MobileNumber", txt_contactno.getText().toString().trim());
				params.put("EmailId", txt_email.getText().toString().trim());
				params.put("Gender",txt_gender.getText().toString().trim());

				System.out.println(params.toString());
				return params;
			}
		};
		stringRequest1.setTag(TAG);
		// Adding request to request queue
		reuestQueue1.add(stringRequest1);
	}
	void parseUpdateProfileJSON(String result1)
	{
		
	
		System.out.println("------Respo parseUpdateProfileJSON--"+result1);
		try
		{
			JSONObject jo=new JSONObject(result1);
	

				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setTitleText(jo.getString("message"))
				.show();
			

		}
		catch(Exception e)
		{
			new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
			.setTitleText(getResources().getString(R.string.server_error))
			.show();
		}

	}
	
	
	boolean valid()
	{
		boolean valid=true;
		String MOB_NO="\\d{10}";

		text_user.getText().toString().trim();
		txt_address.getText().toString().trim();
		txt_contactno.getText().toString().trim();
		if(text_user.length()==0)
		{
			Common.ShowSweetAlert(proContext, "Enter user Name");
			valid=false;
		}
		else if(txt_email.length()==0)
	{
		Common.ShowSweetAlert(proContext, "Enter Email Id Number");
		valid=false;
	}
	/*	else if(txt_contactno.length()==0)
		{
			Common.ShowSweetAlert(proContext, "Enter Mobile Number");
			valid=false;
		}*/
	/*	else if(!txt_contactno.matches(MOB_NO))
			{
				Common.ShowSweetAlert(proContext, "Mobile Number is 10 Digit");
				valid=false;
			}
	*/
		return valid;
	}
	private void performCrop() {
		// take care of exceptions
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 220);
			cropIntent.putExtra("outputY", 220);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}



	private void FileUploadAsync() {


		reuestQueue = Volley.newRequestQueue(proContext);

		final ProgressDialog pDialog = new ProgressDialog(proContext);
		pDialog.setTitle("Loding wait.......");
		pDialog.setCancelable(true);
		pDialog.show();
		//JSon object request for reading the json data
		stringRequest = new StringRequest(Method.POST, Common.URL+"ParentAPI.asmx/UploadParentPhoto",new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				System.out.println(response);


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
						//	parseData(new String(error.networkResponse.data));
						}
					}
				})  {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("Photo", UploadFile);

				params.put("ImageName", Common.userid + ".jpg");
				params.put("UserID", Common.userid);
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

	
}
