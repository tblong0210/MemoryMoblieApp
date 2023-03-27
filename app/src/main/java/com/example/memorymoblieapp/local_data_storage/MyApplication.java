package com.example.memorymoblieapp.local_data_storage;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.getInstance(getApplicationContext());
    }
}
