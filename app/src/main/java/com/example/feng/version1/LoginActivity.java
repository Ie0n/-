package com.example.feng.version1;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feng.version1.Task.MainActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks {

    /**************基本变量**************/
    private EditText username;
    private EditText password;
    private Button login;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String user_name;
    private String pass_word;
    /**************辅助变量***************/
    private int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /******
         * 设置状态栏透明
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        /**
         *根据id找组件
         **/

        username = (EditText) findViewById(R.id.username_edit);
        password = (EditText) findViewById(R.id.password_edit);
        login = (Button) findViewById(R.id.signin_button);
        /**
         *注册登记监听器
         **/
        //注册登录事件
        login.setOnClickListener(this);
        getPermission();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                //无需密码登录
//                Intent intent_a = new Intent();
//                // 封装用户名信息
//                intent_a.putExtra("USERNAME", user_name);
//                intent_a.putExtra("PASSWORD",pass_word);
//                intent_a.setClass(LoginActivity.this,MainActivity.class);// 制定传递对象
//                startActivity(intent_a);


                //需要密码登录
//                 result=login();
//                if(1==result)
//                {
                    Intent intent_a = new Intent();
                    // 封装用户名信息
//                    intent_a.putExtra("USERNAME", user_name);
//                    intent_a.putExtra("PASSWORD",pass_word);
                    intent_a.setClass(LoginActivity.this,MainActivity.class);// 制定传递对象
                    startActivity(intent_a);
//                }
                break;
            default:
                break;
        }

    }
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }
    private int login(){
        /**获取用户名和密码**/
         user_name = username.getText().toString();
         pass_word = password.getText().toString();
        if (null == username || user_name.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
            return 0;
        }
        if (null == pass_word || pass_word.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return 0;
        }
        if (user_name.equals("1")  && pass_word.equals("1")) {
            return 1;
        }

        return 0;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
