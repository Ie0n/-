package com.example.feng.version1.Public;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by feng on 2019/3/21.
 */

public class PublicData {
    PublicData(){}

    public final static String DOMAIN = "http://101.201.234.246:8080";

    /******************扫码相关*******************/
    public static String devitemchoice = null;
    public static String content = null;


    @NonNull
    public static String  getCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

}
