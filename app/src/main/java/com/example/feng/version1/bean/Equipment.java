package com.example.feng.version1.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "设备数据表")
public class Equipment {

    private String deviceName;
    private String meterName;
    private String deviceId;
    private String task;
    private Long id;

    public Equipment(String deviceName, String meterName, String data, String time, String userName) {
        this.deviceName = deviceName;
        this.meterName = meterName;
        this.tabNum = data;
        this.time = time;
        this.userName = userName;
    }

    public Equipment(String id,String data,String time,String enterUserName,String site,String task){
        this.time = time;
        this.userName = enterUserName;
        this.tabNum = data;
        this.tabId = id;
        this.site = site;
        this.task = task;
    }

    public Equipment(String name, Long id){
        this.deviceName = name;
        this.id = id;
    }

    public Equipment(String name,String id){
        this.deviceName = name;
        this.deviceId = id;
    }

    @SmartColumn(id = 0,name = "站点",autoMerge = true)
    private String site;
    @SmartColumn(id = 1,name = "仪表",autoMerge = true)
    private String tabId;
    @SmartColumn(id = 2,name = "数据")
    private String tabNum;
    @SmartColumn(id = 3,name = "时间")
    private String time;
    @SmartColumn(id = 4,name = "录入人")
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
