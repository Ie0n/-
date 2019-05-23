package com.example.feng.version1.bean;

public class lineChartBean {
    private String date;
    private double data;

    public lineChartBean(String date, double data) {
        this.date = date;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
