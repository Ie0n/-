package com.example.feng.version1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.example.feng.version1.MessageEvent;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Util.Utils;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.User;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class TableFragment extends Fragment {

    private Context mContext;
    private SmartTable table;
    private Spinner spinner;
    private ArrayAdapter arr_adapter;
    private List<String> data_list;
    private static final String DEVICE_URL = PublicData.DOMAIN+"/api/user/getAllDevices";
    private static final String METER_URL = PublicData.DOMAIN+"/api/user/getDataByDevice";
    private List<String> deviceNameList;
    private List<String> deviceIdList;
    private User user;
    List<Equipment> MeterList = new ArrayList<>();

    public static TableFragment newInstance() {

        Bundle args = new Bundle();

        TableFragment fragment = new TableFragment();
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
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //发送提交数据查询的请求
                //传进来的position 作为numlist的index
                MeterList.clear();
                getMeterData(deviceIdList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void getData(){
        deviceNameList = new ArrayList<>();
        deviceIdList = new ArrayList<>();
        user = User.getInstance();
        HttpUrl.Builder builder = HttpUrl.parse(DEVICE_URL).newBuilder();
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
                        String result1 = clearChar(result);
                        JSONObject jsonObject = new JSONObject(result1);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("devices");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                deviceNameList.add(jsonObject2.optString("deviceName"));
                                deviceIdList.add(jsonObject2.optString("deviceNo"));
                                Log.d("设备数据",deviceNameList.get(i));
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //适配器
                                    arr_adapter= new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, deviceNameList);
                                    //设置样式
                                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //加载适配器
                                    spinner.setAdapter(arr_adapter);

                                }
                            });


                        }else if (status == 1404){
                            Utils.ToastTextThread(mContext,"当前没有设备信息");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void initView(View view){
        table = view.findViewById(R.id.table);
        spinner = view.findViewById(R.id.spinner);

    }

    private void getMeterData(String id){
        HttpUrl.Builder builder = HttpUrl.parse(METER_URL).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("deviceNo",id);
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
                            //把数据加入设备数据表
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("meters");

                            ArrayList<String> list= new ArrayList();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                list.add(jsonObject2.getString("meterId"));
                            }

                            Collections.sort(list);
                            Log.d("dddddddddddd",Arrays.toString(list.toArray()));
                            Log.d("dddddddddddd",list.get(0));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                MeterList.add(new Equipment(String.valueOf(list.indexOf(jsonObject2.getString("meterId"))+1),
                                        jsonObject2.optString("data"),
                                        jsonObject2.optString("entryTime"),
                                        jsonObject2.optString("entryUsername")));
                                Log.d("dddddddddddd",String.valueOf(list.indexOf(jsonObject2.getString("meterId"))+1));

                            }

                            table.setData(MeterList);
                            table.getConfig().setColumnTitleStyle(new FontStyle(54,Color.BLUE));
                            table.getConfig().setTableTitleStyle(new FontStyle(80,Color.BLACK));
                            table.getConfig().setContentStyle(new FontStyle(60,Color.BLACK));
                            table.getConfig().setShowXSequence(false);
                            table.getConfig().setShowYSequence(false);
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

}
