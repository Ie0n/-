package com.example.feng.version1.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "设备数据表")
public class Equipment {

    private String deviceName;
    private Long id;

    public Equipment(String deviceName, Long id, int tabId, String time, String userName,int tabNum) {
        this.deviceName = deviceName;
        this.id = id;
        this.tabId = tabId;
        this.time = time;
        this.userName = userName;
        this.tabNum = tabNum;
    }

    public Equipment(String name, Long id){
        this.deviceName = name;
        this.id = id;
    }

    @SmartColumn(id = 0,name = "仪表",autoMerge = true)
    private int tabId;
    @SmartColumn(id = 1,name = "数据")
    private int tabNum;
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

    public int gettabId() {
        return tabId;
    }

    public void settabId(int tabId) {
        this.tabId = tabId;
    }

    public int getTabNum() {
        return tabNum;
    }

    public void setTabNum(int tabNum) {
        this.tabNum = tabNum;
    }
}
