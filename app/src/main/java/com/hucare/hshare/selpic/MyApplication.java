package com.hucare.hshare.selpic;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author huzeliang
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
