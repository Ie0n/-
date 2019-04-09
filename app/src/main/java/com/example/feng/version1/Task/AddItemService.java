package com.example.feng.version1.Task;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import com.example.feng.version1.Public.PublicData;

public class AddItemService extends Service {
    public AddItemService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //在表TOTA添加设备最大最小值
        ContentValues values =new ContentValues();
        if(PublicData.db != null){
            values.put("device",PublicData.device1 );
            values.put("meter", PublicData.meter1);
            values.put("top", PublicData.d1_meter1_top);
            values.put("low", PublicData.d1_meter1_low);
            PublicData.db.insert("TOTA", null, values);
            values.clear();
        }
        //在表DEURL添加设备url信息
        if(PublicData.dbdevice != null){
            for(int i =0;i <2;i++){
                values.put("url",PublicData.deviceurlArray[i]);
                values.put("dev", PublicData.devicename[i]);
                PublicData.dbdevice.insert("DEURL", null, values);
                values.clear();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
