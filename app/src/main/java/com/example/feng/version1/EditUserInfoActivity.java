package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feng.version1.bean.User;

public class EditUserInfoActivity extends AppCompatActivity {

    private Context mContext;
    private String name,id,editOrAdd,password;
    private int position;
    private User user;
    private EditText edit_name,edit_id,edit_password;
    private int managerNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        user = User.getInstance();
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        password = intent.getStringExtra("password");
        editOrAdd = intent.getStringExtra("status");
        if (editOrAdd.equals("1")){
            setEditCustomActionBar();
        }else {
            setAddCustomActionBar();
        }
        initView();

    }
    private void initView(){
        edit_name = findViewById(R.id.edit_item_user_name);
        edit_id = findViewById(R.id.edit_item_user_id);
        edit_password = findViewById(R.id.edit_item_user_password);
        edit_name.setText(name);
        edit_id.setText(id);
        edit_password.setText(password);
    }
    private void setAddCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        TextView text = mActionBarView.findViewById(R.id.title);
        text.setText("添加用户");
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        TextView confirm = mActionBarView.findViewById(R.id.confirm);
        managerNo = user.getuserNo();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserInfoActivity.this.finish();
            }
        });
    }
    private void setEditCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        TextView confirm = mActionBarView.findViewById(R.id.confirm);
        managerNo = user.getuserNo();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserInfoActivity.this.finish();
            }
        });
    }
    private void post(){

    }
}
