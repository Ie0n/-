package com.example.feng.version1;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    ExecutorService cachedThreadPool = null;

    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }


    private Handler handler = new Handler();

    public Handler getHandler() {
        return handler;
    }

    static MyApplication instance = null;

    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cachedThreadPool = Executors.newCachedThreadPool();
    }
}
