package com.example.feng.version1.bean;

public class Meter {

    private String meterName;
    private String meterId;

    public Meter(String meterName,String meterId){
        this.meterName = meterName;
        this.meterId = meterId;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
}
