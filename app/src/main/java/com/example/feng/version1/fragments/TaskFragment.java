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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.AddEquipmentActivity;
import com.example.feng.version1.MessageEvent;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Task.SelectTabActivity;
import com.example.feng.version1.Util.Utils;
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
    private TextView userid;
    private RelativeLayout add;
    private RelativeLayout input;

    private static final String TAG = "-dd";

    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_ADD = 0x0000;
    private static final int REQUEST_CODE_INPUT = 0x0001;
    private static final String URL = PublicData.DOMAIN+"/api/user/getAllDevices";

    private List<String> deviceList,deviceNameList;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        initView(view);

        return view;
    }
    private void initView(View view){
        add = view.findViewById(R.id.r1);
        add.setOnClickListener(this);
        input = view.findViewById(R.id.r2);
        input.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private String clearChar(String s) {
        String replace = s.replace("\\", "");
        String replace2 = replace.substring(1, replace.length() - 1);
        return replace2;
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
        user = User.getInstance();
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
                            JSONArray array = data.getJSONArray("devices");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                deviceList.add(jsonObject2.optString("deviceNo"));
                                deviceNameList.add(jsonObject2.optString("deviceName"));
                            }

                        }else if (status == 1404){
                            Utils.ToastTextThread(mContext,"当前没有数据信息");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        //录入信息
        if (requestCode == REQUEST_CODE_INPUT && resultCode == RESULT_OK) {

            if (data != null){
                content = data.getStringExtra(DECODED_CONTENT_KEY);

                if (deviceList.contains(content)){
                    int i = deviceList.indexOf(content);
                    Intent intent = new Intent();
                    intent.putExtra("deviceNo",content);
                    intent.putExtra("deviceName",deviceNameList.get(i));
                    intent.setClass(mContext,SelectTabActivity.class);// 制定传递对象
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, "请先录入该设备", Toast.LENGTH_SHORT).show();
                }


            }


        }
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK){
            if (data != null){
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                if (deviceList.contains(content)){
                    Utils.ToastTextThread(mContext,"该设备已录入");
                }else {
                    //未录入设备 打开录入设备

                    Intent intent = new Intent();
                    intent.putExtra("deviceNo",content);
                    intent.setClass(mContext,AddEquipmentActivity.class);
                    startActivity(intent);

                }
            }
        }

        //添加设备

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 按下设备键扫码，根据url查询得到设备号
             * 若与publicdata中设备号一致，则进入MeterNum.class
             * */
            case R.id.r1:
                Intent intent = new Intent(mContext,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INPUT);
                break;

            case R.id.r2:
                Intent intent2 = new Intent(mContext,
                        CaptureActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_ADD);
                break;
            default:
                break;
        }
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
