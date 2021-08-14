package com.example.android_etpj.sharedpreference;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataLocal.init(getApplicationContext());
    }
}
