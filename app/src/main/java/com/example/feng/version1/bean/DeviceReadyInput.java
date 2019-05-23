package com.example.feng.version1.bean;

public class DeviceReadyInput {
    private String site,deviceName,meterNo;

    public DeviceReadyInput(String site, String deviceName, String meterNo) {
        this.site = site;
        this.deviceName = deviceName;
        this.meterNo = meterNo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }
}
