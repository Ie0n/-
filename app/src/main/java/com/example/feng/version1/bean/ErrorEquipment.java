package com.example.feng.version1.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "异常数据表")
public class ErrorEquipment {
    @SmartColumn(id = 1,name = "设备",autoMerge = true)
    private String device;
    @SmartColumn(id = 0,name = "站点",autoMerge = true)
    private String site;
    @SmartColumn(id = 2,name = "仪表",autoMerge = true)
    private String tabId;
    @SmartColumn(id = 3,name = "数据")
    private String tabNum;
    @SmartColumn(id = 4,name = "时间")
    private String time;
    @SmartColumn(id = 5,name = "录入人")
    private String userName;

    public ErrorEquipment(String id, String data, String time, String enterUserName, String site, String device){
        this.time = time;
        this.userName = enterUserName;
        this.tabNum = data;
        this.tabId = id;
        this.site = site;
        this.device = device;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabNum() {
        return tabNum;
    }

    public void setTabNum(String tabNum) {
        this.tabNum = tabNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
