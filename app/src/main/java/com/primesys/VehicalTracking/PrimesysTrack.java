package com.primesys.VehicalTracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Utility.Common;


public class PrimesysTrack extends MultiDexApplication {

    public static final String TAG = PrimesysTrack.class.getSimpleName();

    public static DBHelper mDbHelper;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor ;
    public static PrimesysTrack sInstance;

    public static  RequestQueue mRequestQueue;
    private String user_id;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private PowerManager powerManager;
    @Nullable
    public static Context getAppContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate() called");
        sInstance = this;
        mRequestQueue= Volley.newRequestQueue(sInstance);
        Common.getConnectivityStatus(sInstance);

        try {

            sharedPreferences=sInstance.getSharedPreferences("User_data", Context.MODE_PRIVATE);
            Common.getConnectivityStatus(sInstance);
            user_id= sharedPreferences.getString("user_id","0");

            mDbHelper=DBHelper.getInstance(getAppContext());
          /*  if (!Common.ServiceRunningcheck(sInstance)){
                 Log.e(TAG,"Inside Application Class Map_service Start----------");
                Intent startServiceIntent =new Intent(this, Map_service.class);
                startService(startServiceIntent);
            }*/



        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public synchronized static PrimesysTrack getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }else {
            return mRequestQueue;
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    /**
     * Get image loader.
     *
     * @return ImageLoader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
    private static boolean activityVisible;

    public static DBHelper getDatabaseHelper() {
        return mDbHelper;
    }


}
