package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Task.MainActivity;
import com.example.feng.version1.adapter.UserAdapter;
import com.example.feng.version1.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private Button edit,add;
    private Context mContext;
    private List<User> userList;
    private User user;
    private final static String URL = PublicData.DOMAIN+"/api/admin/getAllUsers";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mContext = this;
        user = User.getInstance();
        initView();
    }

    private void initView(){
        recyclerView = findViewById(R.id.rv_user);
        add = findViewById(R.id.btn_add_user);
        edit = findViewById(R.id.btn_edit_user_info);
        add.setOnClickListener(this);
        edit.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        userList = new ArrayList<>();
        getData();

    }

    private String clearChar(String s) {
        String replace = s.replace("\\", "");
        String replace2 = replace.substring(1, replace.length() - 1);
        return replace2;
    }

    @NonNull
    private String getCookie() {
        SharedPreferences sp = getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

    private void getData() {
        HttpUrl.Builder builder = HttpUrl.parse(URL).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()));
        Request request = new Request
                .Builder()
                .url(builder.build())
                .get()
                .header("Cookie", getCookie())
                .build();

        OkHttpClient client = new OkHttpClient();
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
                            JSONArray array = data.getJSONArray("users");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                userList.add(new User(jsonObject2.optString("username"),
                                        jsonObject2.optInt("userNo"),
                                        jsonObject2.optString("password")));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new UserAdapter(mContext,userList);
                                    recyclerView.setAdapter(adapter);
                                    adapter.setOnItemListener(new UserAdapter.OnItemListener() {
                                        @Override
                                        public void onItemClick(View view, int position, String name, String id,Map map) {
                                            Intent intent = new Intent();
                                            intent.putExtra("status","1");
                                            intent.putExtra("name",name);
                                            intent.putExtra("password",(String) map.get(position));
                                            Log.d("-rrr",""+map.get(position));
                                            intent.putExtra("id",id);
                                            intent.putExtra("position",position);
                                            intent.setClass(mContext,EditUserInfoActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }else if (status == 1404){
                            Util.ToastTextThread(UserActivity.this,"管理员用户账号错误或没有管理员权限");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_user:

                    userList.add(new User("张三",112441,"5465454"));
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.setClass(mContext,EditUserInfoActivity.class);
                    intent.putExtra("status","0");
                    startActivity(intent);
                break;

            default:
                break;
        }
    }

}
