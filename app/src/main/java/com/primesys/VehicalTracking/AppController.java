package com.primesys.VehicalTracking;

import android.app.Application;
import android.support.multidex.MultiDex;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}