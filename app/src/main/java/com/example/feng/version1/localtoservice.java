package com.example.feng.version1;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;

import com.example.feng.version1.Public.PublicData;
import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
import com.ibm.micro.client.mqttv3.MqttException;
import com.ibm.micro.client.mqttv3.internal.MemoryPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class localtoservice extends Service {


    private String host = "tcp://39.106.214.166:1883";
    private String clientid="adfadgagd";
    private MqttConnectOptions options;
    private String userName = "test";
    private String passWord = "test";
    private ScheduledExecutorService scheduler;

    private Intent intent_msg;
    private MyReceiver myReceiver;
    public localtoservice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //注册广播
        try {
            myReceiver = new MyReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(PublicData.MSGARRIVAL);
            registerReceiver(myReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化Mqtt
        initMqtt();
        //每隔一段时间判断，如果未连接则connect
        startReconnect();

        return super.onStartCommand(intent, flags, startId);
    }
    private void initMqtt() {
        try {
            PublicData.client = new MqttClient(host,clientid,new MemoryPersistence());

        } catch (MqttException e) {
            e.printStackTrace();
        }
        options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
    }
    private void startReconnect() {
        //构造一个单线程执行程序
        scheduler = Executors.newSingleThreadScheduledExecutor();
        //设定该线程执行rate
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if(!PublicData.client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }
    public void connect(){
        MqttConnectThread mqttConnectThread = new MqttConnectThread();
        mqttConnectThread.start();

    }
    class MqttConnectThread extends Thread{
        public void run(){
            /**子线程发送广播**/
            try {
                PublicData.client.connect(options);
                final Intent intent = new Intent(PublicData.CONNECTSUCCESS);
                sendBroadcast(intent);
            } catch (MqttException e) {
                e.printStackTrace();
                final Intent intent = new Intent(PublicData.CONNECTDEFAULT);
                sendBroadcast(intent);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            scheduler.shutdown();
            PublicData.client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        //解除注册广播
        unregisterReceiver(myReceiver);
    }

    /** 启动发送给服务器数据的服务**/
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

//            try {
//
//                if (PublicData.MSGARRIVAL.equals(intent.getAction())) {
//                    intent_msg = new Intent(localtoservice.this,Msgprocess.class);
//                    startService(intent_msg);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

}
