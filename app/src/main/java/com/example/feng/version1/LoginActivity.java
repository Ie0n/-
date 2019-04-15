package com.example.feng.version1;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Task.MainActivity;
import com.example.feng.version1.bean.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    /**************基本变量**************/
    private EditText username_edit;
    private EditText password_edit;
    private Button login_btn;
    private User user;
    private int userID;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String user_name;
    private String pass_word;
    /**************辅助变量***************/
    private int result = 0;
    private final static String UrlPart = "/api/global/login";
    private final static String URL = PublicData.DOMAIN+UrlPart;

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

        username_edit = findViewById(R.id.username_edit);
        password_edit = findViewById(R.id.password_edit);
        login_btn = findViewById(R.id.signin_button);
        user = User.getInstance();
        /**
         *注册登记监听器
         **/
        //注册登录事件
        login_btn.setOnClickListener(this);
        getPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                login();
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

    private boolean login() {
        /**获取用户名和密码**/
        user_name = username_edit.getText().toString();
        pass_word = password_edit.getText().toString();
        if (null == user_name || user_name.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass_word.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return false;
        }
        getData();
        return true;
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

    private String clearChar(String s) {
        String replace = s.replace("\\", "");
        String replace2 = replace.substring(1, replace.length() - 1);
        return replace2;
    }

    private void getData() {
        RequestBody body = new FormBody.Builder()
                .add("userNo",user_name)
                .add("password",pass_word)
                .build();
        Request request = new Request.Builder().url(LoginActivity.URL).post(body).build();
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

                    String result = response.body().string();
                    Log.d("Result: ",result);
                    try {
                        String result1 = clearChar(result);
                        JSONObject jsonObject = new JSONObject(result1);
                        int status = jsonObject.getInt("status");
                        Log.d("Result: status ",""+status);
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            int userNum = data.getInt("userNo");
                            String userName = data.getString("username");
                            int admin = data.getInt("admin");
                            Intent intent_a = new Intent();
                            user.setuserNo(userNum);
                            user.setAdmin(admin);
                            user.setUserName(userName);
                            intent_a.setClass(LoginActivity.this, MainActivity.class);
                            Util.ToastTextThread(LoginActivity.this,"登录成功");
                            LoginActivity.this.finish();
                            startActivity(intent_a);
                        }else if (status == 1404){
                            Util.ToastTextThread(LoginActivity.this,"帐号不存在或密码错误");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
