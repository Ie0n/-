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
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Util.Utils;
import com.example.feng.version1.adapter.DeviceAdapter;
import com.example.feng.version1.adapter.MeterAdapter;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.Meter;
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

public class AllMeterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Meter> meterList;
    private Context mContext;
    private User user;
    private String deviceNo;
    private MeterAdapter adapter;
    private static final String URL = PublicData.DOMAIN+"/api/user/getDeviceMeters";
    private static final String DELETE_URL = PublicData.DOMAIN+"/api/user/deleteMeter";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_list);
        mContext = this;
        setEditCustomActionBar();
        user = User.getInstance();
        Intent intent = getIntent();
        deviceNo = intent.getStringExtra("deviceNo");
        initView();
    }

    private void initView(){
        recyclerView = findViewById(R.id.rv_meter_list);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        meterList = new ArrayList<>();
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

    private void getData(){
        HttpUrl.Builder builder = HttpUrl.parse(URL).newBuilder();
        builder
                .addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("deviceNo",deviceNo);
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
                    try {
                        String result1 = clearChar(result);
                        JSONObject jsonObject = new JSONObject(result1);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("meters");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                meterList.add(new Meter(
                                        jsonObject2.optString("meterName"),
                                        jsonObject2.optString("meterId")));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new MeterAdapter(mContext,meterList);
                                    adapter.setOnItemLongClickListener(new MeterAdapter.onItemLongClickListener() {
                                        @Override
                                        public void onItemLongClick(View view, int position, String id) {
                                            showPopWindows(view,id);
                                        }
                                    });
                                    adapter.setOnItemListener(new MeterAdapter.OnItemListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                        }
                                    });
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }else if (status == 1404){
                            Utils.ToastTextThread(mContext,"设备id错误或当前设备没有仪表信息");
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
        textView.setText("该设备仪表");
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllMeterActivity.this.finish();
            }
        });
    }
    private void showPopWindows(View v, final String id) {
        View mPopView = LayoutInflater.from(this).inflate(R.layout.popup, null);
        TextView textView = mPopView.findViewById(R.id.tv_delete_txt);
        textView.setText("删除该仪表");
        final PopupWindow mPopWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //获取弹窗的宽高
        mPopView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = mPopView.getMeasuredWidth();
        int popupHeight = mPopView.getMeasuredHeight();
        //获取父控件位置
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //设置显示位置
        mPopWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1]
                - popupHeight/3);
        mPopWindow.update();
        mPopView.findViewById(R.id.tv_delete_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发起网络请求删除当前id的item
                deleteMeter(id);
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
            }
        });
    }
    private void deleteMeter(String id){
        HttpUrl.Builder builder = HttpUrl.parse(DELETE_URL).newBuilder();
        builder
                .addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("meterId",id);
        Log.d("!!!!!",id+"-----"+String.valueOf(user.getuserNo()));
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

                    String result = response.body().string();
                    Log.d("Result: ",result);
                    try {
                        String result1 = clearChar(result);
                        JSONObject jsonObject = new JSONObject(result1);
                        int status = jsonObject.getInt("status");
                        Log.d("Result: status ",""+status);
                        if (status == 1200){
                            Utils.ToastTextThread(AllMeterActivity.this,"设备删除成功");
                            meterList.clear();
                            getData();
                            EventBus.getDefault().post(new MessageEvent());
                        }else if (status == 1404){
                            Utils.ToastTextThread(AllMeterActivity.this,"账号不合法或该账户不存在");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
