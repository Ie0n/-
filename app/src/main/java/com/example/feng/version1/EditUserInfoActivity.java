package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.bean.User;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditUserInfoActivity extends AppCompatActivity {

    private String name,id,editOrAdd,password;
    private int position;
    private User user;
    private EditText edit_name,edit_id,edit_password;
    private static final String EDIT_URL = PublicData.DOMAIN+"/api/admin/changeUser";
    private static final String ADD_URL = PublicData.DOMAIN+"/api/admin/addUser";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }
        setContentView(R.layout.activity_edit_user_info);
        user = User.getInstance();
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        password = intent.getStringExtra("password");
        editOrAdd = intent.getStringExtra("status");
        initView();
        if (editOrAdd.equals("1")){
            setEditCustomActionBar();
            edit_name.setText(name);
            edit_id.setText(id);
            edit_id.setEnabled(false);
            edit_password.setText(password);
        }else {
            setAddCustomActionBar();
        }

    }
    private void initView(){
        edit_name = findViewById(R.id.edit_item_user_name);
        edit_id = findViewById(R.id.edit_item_user_id);
        edit_password = findViewById(R.id.edit_item_user_password);
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
        TextView confirm = mActionBarView.findViewById(R.id.bar_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_id.setEnabled(true);
                addUser();
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
        TextView confirm = mActionBarView.findViewById(R.id.bar_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserInfoActivity.this.finish();
            }
        });
    }

    @NonNull
    private String getCookie() {
        SharedPreferences sp = getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

    private void post() {
        RequestBody body = new FormBody.Builder()
                .add("userNo",String.valueOf(user.getuserNo()))
                .add("changeUserNo",id)
                .add("username",edit_name.getText().toString())
                .add("password",edit_password.getText().toString())
                .build();
        final Request request = new Request
                .Builder()
                .url(EditUserInfoActivity.EDIT_URL)
                .post(body)
                .header("Cookie", getCookie())
                .build();
        final OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fail","获取数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null && response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            ToastUtil.ToastTextThread(EditUserInfoActivity.this,"修改成功");
                            EventBus.getDefault().post(new MessageEvent());
                            EditUserInfoActivity.this.finish();
                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(EditUserInfoActivity.this,"用户账号错误或用户不存在");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
    private void addUser(){
        RequestBody body = new FormBody.Builder()
                .add("userNo",String.valueOf(user.getuserNo()))
                //待添加用户账号，6位整数，不能以 0 开头
                .add("addUserNo",edit_id.getText().toString())
                .add("username",edit_name.getText().toString())
                .add("password",edit_password.getText().toString())
                .build();
        final Request request = new Request
                .Builder()
                .url(EditUserInfoActivity.ADD_URL)
                .post(body)
                .header("Cookie", getCookie())
                .build();
        final OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fail","获取数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null && response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            ToastUtil.ToastTextThread(EditUserInfoActivity.this,"添加成功");
                            EventBus.getDefault().post(new MessageEvent());
                            EditUserInfoActivity.this.finish();
                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(EditUserInfoActivity.this,"用户账号或用户名不能为空或已被占用");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

        });
    }

}
