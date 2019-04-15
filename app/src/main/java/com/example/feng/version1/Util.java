package com.example.feng.version1;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void ToastTextThread(final Context context, final String text) {
        MyApplication.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
