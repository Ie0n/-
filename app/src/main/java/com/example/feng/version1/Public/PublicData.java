package com.example.feng.version1.Public;

import android.database.sqlite.SQLiteDatabase;

import com.example.feng.version1.db.MyDatabaseHelper;
import com.example.feng.version1.db.myDatabasedeviceHelper;
import com.ibm.micro.client.mqttv3.MqttClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2019/3/21.
 */

public class PublicData {
    PublicData(){}

        /******************服务器*********************/
        //服务器广播
        public final static String CONNECTSUCCESS           = "com.example.feng.CONNECTSUCCESS";
        public final static String CONNECTDEFAULT           = "com.example.feng.DEFAULT";
        public final static String MSGARRIVAL               =  "com.example.feng.MSGARRIVAL";
        //创建服务器实例，用来连接服务器
        public static MqttClient client;
        //发给服务器要出的的JSON数据
        public static JSONObject jo;
    /***************数据库********************/
    public static MyDatabaseHelper myDatabaseHelper;
    public static SQLiteDatabase db;
    public static com.example.feng.version1.db.myDatabasedeviceHelper myDatabasedeviceHelper;
    public static SQLiteDatabase dbdevice;

    /******************设备相关*********************/
    public final static String device1="d1";
    public final static String device2="d2";
    public final static String device3="d3";

    public final static String[] deviceurlArray={"http://www.epub360.com/manage/book/dp2ut2/","http://weixin:qq.com/r/OjtudvrEuKfHrb4V924o"};
    public final static String[] devicename={"d1","d2"};
    /******************表计相关*********************/
    public final static String meter1="meter1";

    public final static double d1_meter1_top=1.0;
    public final static double d1_meter1_low=1.0;



//
//    Map params = new HashMap();
//    public void paramsput(){
//        params.put("d1_meter1_top",1.2);
//        params.put("d1_meter1_low",1.0);
//
//    }

    /******************标志位相关*******************/
    //表计是否读取完整
    public final static boolean meterflag = false;

    /******************扫码相关*******************/
    public static String devitemchoice = null;
    public static String content = null;



}
