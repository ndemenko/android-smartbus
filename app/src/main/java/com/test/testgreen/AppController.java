package com.test.testgreen;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.test.testgreen.logger.Logger;

import io.fabric.sdk.android.Fabric;

public class AppController extends Application {
    private static AppController instance;
    Context context;

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Logger.d("TAG", "AppController");
        context = getApplicationContext();
        instance = this;
    }

}
