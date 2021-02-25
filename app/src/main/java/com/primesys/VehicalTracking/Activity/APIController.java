package com.primesys.VehicalTracking.Activity;

import android.app.Application;
import android.os.StrictMode;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class APIController extends Application {

	public static final String TAG=APIController.class.getSimpleName();
    public String news;
    public String school_logo,school_name;
    
    
    public int read=0; //0 =ur 1=r

	//Request Queue
	private RequestQueue httpRequestqueue;
	//load image from server
	private ImageLoader picLoader;

	//singelTone class instance
	private static APIController apiInstance;


	
	@Override
	public void onCreate() {
		super.onCreate();
		apiInstance=this;
		// when you create a new application you can set the Thread and VM Policy
		   StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		   .detectCustomSlowCalls() // API level 11, to use with StrictMode.noteSlowCode
		   .detectDiskReads()
		   .detectDiskWrites()
		   .detectNetwork()
		   .penaltyLog()
		   .penaltyFlashScreen() // API level 11
		   .build());

		//If you use StrictMode you might as well define a VM policy too

		  /* StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		   .detectLeakedSqlLiteObjects()
		   .detectLeakedClosableObjects() // API level 11
		   .setClassInstanceLimit(Class.forName(“com.apress.proandroid.SomeClass”), 100)
		   .penaltyLog()
		   .build());
		 }*/
	}


}
