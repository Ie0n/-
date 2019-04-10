package com.example.feng.version1.Viemitem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.feng.version1.R;
import com.example.feng.version1.Task.MainActivity;
import com.example.feng.version1.DataAnalyse.DataAnalyse;
import com.example.feng.version1.MyHome.MyHome;

/**
 * Created by feng on 2019/3/21.
 */

public class Bottom extends LinearLayout implements View.OnClickListener {

    //  public Button homePage;
    public Button task;
    public Button anayse;
    public Button myHome;
    public Activity activity_from;

    public Bottom(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.buttom_layout,this);

        //     homePage = (Button)findViewById(R.id.home_page);
        task = (Button)findViewById(R.id.task);
        anayse = (Button)findViewById(R.id.anayse);
        myHome = (Button)findViewById(R.id.my_home);

        //  homePage.setOnClickListener(this);
        task.setOnClickListener(this);
        anayse.setOnClickListener(this);
        myHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
//            case R.id.home_page:
//                Intent intent_homepage = new Intent(activity_from,HomePage.class);
//                activity_from.startActivity(intent_homepage);
//                break;
            case R.id.task:
                Intent intent_states = new Intent(activity_from,MainActivity.class);
                activity_from.startActivity(intent_states);
                break;
            case R.id.anayse:
                Intent intent_sports = new Intent(activity_from,DataAnalyse.class);
                activity_from.startActivity(intent_sports);
                break;
            case R.id.my_home:
                Intent intent_myhome = new Intent(activity_from,MyHome.class);
                activity_from.startActivity(intent_myhome);
                break;
            default:
                break;
        }
    }
}
