package com.example.feng.version1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.bean.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {


    private EditText username_edit;
    private EditText password_edit;
    private Button login_btn;
    private User user;

    private String[] permissions = {    Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String user_name;
    private String pass_word;

    private final static String UrlPart = "/api/global/login";
    private final static String URL = PublicData.DOMAIN+UrlPart;
    private CookieJar cookieJar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        username_edit = findViewById(R.id.username_edit);
        password_edit = findViewById(R.id.password_edit);
        login_btn = findViewById(R.id.signin_button);
        user = User.getInstance();
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

    private void saveCookie(String name, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("Cookie", MODE_PRIVATE).edit();
        editor.putString("token",name);
        editor.putString("token_value",value);
        editor.apply();
    }
    private void getData() {
        RequestBody body = new FormBody.Builder()
                .add("userNo",user_name)
                .add("password",pass_word)
                .build();
        cookieJar = new CookieJar() {
            private final Map<String, List<Cookie>> cookiesMap = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                String host = url.host();
                List<Cookie> cookiesList = cookiesMap.get(host);
                if (cookiesList != null) {
                    cookiesMap.remove(host);
                }
                if (cookiesList != null) {
                    cookiesMap.put(host, cookiesList);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookieList = cookiesMap.get(url.host());
                return cookieList != null ? cookieList : new ArrayList<Cookie>();
            }
        };
        final Request request = new Request.Builder().url(LoginActivity.URL).post(body).build();
        final OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ToastUtil.ToastTextThread(LoginActivity.this,"登录失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null && response.isSuccessful()) {

                    String result = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            Headers headers = response.headers();
                            HttpUrl loginUrl = request.url();
                            List<Cookie> cookies = Cookie.parseAll(loginUrl, headers);
                            client.cookieJar().saveFromResponse(loginUrl, cookies);
                            saveCookie(cookies.get(0).name(), cookies.get(0).value());

                            JSONObject data = jsonObject.getJSONObject("data");
                            int userNum = data.getInt("userNo");
                            String userName = data.getString("username");
                            int admin = data.getInt("admin");
                            Intent intent_a = new Intent();
                            user.setuserNo(userNum);
                            user.setAdmin(admin);
                            user.setUserName(userName);
                            intent_a.setClass(LoginActivity.this, MainActivity.class);
                            ToastUtil.ToastTextThread(LoginActivity.this,"登录成功");
                            LoginActivity.this.finish();
                            startActivity(intent_a);
                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(LoginActivity.this,"帐号不存在或密码错误");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
