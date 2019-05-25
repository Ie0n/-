package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.adapter.DeviceAdapter;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.User;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllDeviceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private List<Equipment> deviceList;
    private User user;
    private static final String URL = PublicData.DOMAIN+"/api/user/getAllDevices";
    private static final String DELETE_URL = PublicData.DOMAIN+"/api/admin/deleteDevice";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        mContext = this;
        setEditCustomActionBar();
        user = User.getInstance();
        initView();
    }
    private void initView(){
        recyclerView = findViewById(R.id.rv_device_list);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        deviceList = new ArrayList<>();
        getData();
    }

    @NonNull
    private String getCookie() {
        SharedPreferences sp = getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

    private void getData(){
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
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("devices");

                            final List<String> deviceNoList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                deviceNoList.add(jsonObject2.optString("deviceNo"));
                            }

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                deviceList.add(new Equipment(
                                        jsonObject2.optString("deviceName"),
                                        jsonObject2.optString("deviceNo")));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new DeviceAdapter(mContext,deviceList);
                                    adapter.setOnItemLongClickListener(new DeviceAdapter.onItemLongClickListener() {
                                        @Override
                                        public void onItemLongClick(View view, int position, String id,String name) {
                                            showPopWindows(view,id,name);
                                        }
                                    });
                                    adapter.setOnItemListener(new DeviceAdapter.OnItemListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Intent intent = new Intent();
                                            intent.setClass(mContext,AllMeterActivity.class);
                                            intent.putExtra("deviceNo",deviceNoList.get(position));
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(mContext,"当前暂时没有设备信息");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void setEditCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_user_activity, null);
        TextView textView = mActionBarView.findViewById(R.id.title);
        textView.setText("已录入设备");
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllDeviceActivity.this.finish();
            }
        });
    }
    private void showPopWindows(View v, final String id,final String name) {
        View mPopView = LayoutInflater.from(this).inflate(R.layout.popup, null);
        TextView textView = mPopView.findViewById(R.id.tv_delete_txt);
        textView.setText("删除该设备");
        final PopupWindow mPopWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = mPopView.getMeasuredWidth();
        int popupHeight = mPopView.getMeasuredHeight();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPopWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1]
                - popupHeight/3);
        mPopWindow.update();
        mPopView.findViewById(R.id.tv_delete_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDevice(id);
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
            }
        });
    }

    private void deleteDevice(String id){
        HttpUrl.Builder builder = HttpUrl.parse(DELETE_URL).newBuilder();
        builder
                .addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("deviceNo",id);
        Request request = new Request
                .Builder()
                .url(builder.build())
                .delete()
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
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            ToastUtil.ToastTextThread(AllDeviceActivity.this,"设备删除成功");
                            deviceList.clear();
                            getData();
                            EventBus.getDefault().post(new MessageEvent());
                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(AllDeviceActivity.this,"账号不合法或该账户不存在");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
