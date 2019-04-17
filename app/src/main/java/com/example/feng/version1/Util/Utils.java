package com.example.feng.version1.Util;

import android.content.Context;
import android.widget.Toast;

import com.example.feng.version1.MyApplication;

public class Utils {
    public static void ToastTextThread(final Context context, final String text) {
        MyApplication.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
