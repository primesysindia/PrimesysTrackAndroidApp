package com.primesys.VehicalTracking.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }


}