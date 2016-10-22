package com.primesys.VehicalTracking.Activity;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Utility.LruBitmapCache;

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

	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}

	public String getNews() {
		return news;
	}


	public void setNews(String news) {
		this.news = news;
	}


	public static synchronized APIController getInstance()
	{
		return apiInstance;
	}

	public RequestQueue getRequestQueue()
	{
		if(httpRequestqueue==null)
		{
			return Volley.newRequestQueue(getApplicationContext());
		}
		else
			return httpRequestqueue;
	}

	//Image Loader we have to implement custom cache
    public ImageLoader getImageLodaer()
    {
    	getRequestQueue();
    	if(picLoader==null)
    	{
    		picLoader=new ImageLoader(httpRequestqueue, new LruBitmapCache());
    	}
    	return this.picLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    //cancel pending request
    public void cancelPendingRequests(Object tag) {
        if (httpRequestqueue != null) {
        	httpRequestqueue.cancelAll(tag);
        }
    }
    
    
}
