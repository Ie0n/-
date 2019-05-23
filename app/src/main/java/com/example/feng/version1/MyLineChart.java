package com.example.feng.version1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;

public class MyLineChart extends LineChart {

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
