package com.example.feng.version1.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "站点任务数据表")
public class SiteTaskEquipment {

    @SmartColumn(id = 0,name = "设备",autoMerge = true)
    private String deviceName;
    @SmartColumn(id = 1,name = "仪表",autoMerge = true)
    private String meterName;
    @SmartColumn(id = 2,name = "数据")
    private String data;
    @SmartColumn(id = 3,name = "时间")
    private String time;
    @SmartColumn(id = 4,name = "录入人")
    private String inPerson;

    public SiteTaskEquipment(String deviceName, String meterName, String data, String time, String inPerson) {
        this.deviceName = deviceName;
        this.meterName = meterName;
        this.data = data;
        this.time = time;
        this.inPerson = inPerson;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInPerson() {
        return inPerson;
    }

    public void setInPerson(String inPerson) {
        this.inPerson = inPerson;
    }
}
