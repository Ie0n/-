package com.example.feng.version1.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "设备数据表")
public class Equipment {

    private String deviceName;
    private String meterName;
    private Long id;

    public Equipment(String deviceName, String meterName, String data, String time, String userName) {
        this.deviceName = deviceName;
        this.meterName = meterName;
        this.tabNum = data;
        this.time = time;
        this.userName = userName;
    }

    public Equipment(String id,String data,String time,String enterUserName){
        this.time = time;
        this.userName = enterUserName;
        this.tabNum = data;
        this.tabId = id;
    }

    public Equipment(String name, Long id){
        this.deviceName = name;
        this.id = id;
    }

    @SmartColumn(id = 0,name = "仪表",autoMerge = true)
    private String tabId;
    @SmartColumn(id = 1,name = "数据")
    private String tabNum;
    @SmartColumn(id = 2,name = "时间")
    private String time;
    @SmartColumn(id = 3,name = "录入人")
    private String userName;

    public String getName() {
        return deviceName;
    }

    public void setName(String name) {
        this.deviceName = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String gettabId() {
        return tabId;
    }

    public void settabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabNum() {
        return tabNum;
    }

    public void setTabNum(String tabNum) {
        this.tabNum = tabNum;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }
}
