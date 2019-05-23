package com.example.feng.version1.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.AddEquipmentActivity;
import com.example.feng.version1.ChooseDeviceActivity;
import com.example.feng.version1.ChooseTaskInputActivity;
import com.example.feng.version1.MessageEvent;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Task.SelectTabActivity;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.bean.User;
import com.yzq.testzxing.zxing.android.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.feng.version1.Public.PublicData.content;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private RelativeLayout add;
    private RelativeLayout input;
    private LinearLayout linearLayout;

    private static final String URL = PublicData.DOMAIN+"/api/user/getAllDevices";
    private ArrayList<String> deviceList,deviceNameList;
    private Intent intent;

    private User user;

    public static TaskFragment newInstance() {

        Bundle args = new Bundle();

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getInstance();
        intent = new Intent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        initView(view);
        setVisible();
        initData();
        return view;
    }
    private void initView(View view){
        add = view.findViewById(R.id.r1);
        linearLayout = view.findViewById(R.id.line2);
        add.setOnClickListener(this);
        input = view.findViewById(R.id.r2);
        input.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setVisible(){
        if (user.getAdmin() == 0){
            input.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.r2:
                intent.setClass(mContext, ChooseDeviceActivity.class);
                startActivity(intent);
                break;

            case R.id.r1:
                Intent intent2 = new Intent(mContext, ChooseTaskInputActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
    @NonNull
    private String getCookie() {
        SharedPreferences sp = getActivity().getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

    private void initData(){
        deviceList = new ArrayList<>();
        deviceNameList = new ArrayList<>();
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
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("devices");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                deviceList.add(jsonObject2.optString("deviceNo"));
                                deviceNameList.add(jsonObject2.optString("deviceName"));
                            }
                            intent.putStringArrayListExtra("deviceList",deviceList);
                            intent.putStringArrayListExtra("deviceNameList",deviceNameList);
                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(mContext,"当前没有数据信息");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        deviceList.clear();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }








}
