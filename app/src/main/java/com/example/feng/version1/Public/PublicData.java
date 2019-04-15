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

    public static boolean isManager = false;

    public final static String[] deviceurlArray={"http://www.epub360.com/manage/book/dp2ut2/","http://weixin:qq.com/r/OjtudvrEuKfHrb4V924o"};
    public final static String[] devicename={"d1","d2"};
    /******************表计相关*********************/
    public final static String meter1="meter1";

    public final static double d1_meter1_top=1.0;
    public final static double d1_meter1_low=1.0;

    public final static String DOMAIN = "http://101.201.234.246:8080";


    /******************标志位相关*******************/
    //表计是否读取完整
    public final static boolean meterflag = false;

    /******************扫码相关*******************/
    public static String devitemchoice = null;
    public static String content = null;


    public static String clearChar(String s) {
        String replace = s.replace("\\", "");
        String replace2 = replace.substring(1, replace.length() - 1);
        return replace2;
    }

    @NonNull
    public static String  getCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

}
