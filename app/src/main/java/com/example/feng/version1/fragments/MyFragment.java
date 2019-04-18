package com.example.feng.version1.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.feng.version1.LoginActivity;
import com.example.feng.version1.MessageEvent;
import com.example.feng.version1.MyApplication;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.UserActivity;
import com.example.feng.version1.Util.ExcelUtil;
import com.example.feng.version1.Util.Utils;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.feng.version1.Public.PublicData.clearChar;

public class MyFragment extends Fragment implements View.OnClickListener {

    private Button logout,manager,excel_out;
    private Context mContext;
    private User user;
    private TextView user_name_text,user_id_text;
    private int count = 1;
    private int[] ints;
    private List<Equipment> demoBeanList;
    private static final String URL = PublicData.DOMAIN+"/api/user/getAllData";

    public static MyFragment newInstance() {

        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        user = User.getInstance();
        ints = new int[400];
        for (int i = 0; i < 300; i++) {
            ints[i+1] = i;
        }
        initView(view);
        //拿到user
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
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
                        String result1 = clearChar(result);
                        JSONObject jsonObject = new JSONObject(result1);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            demoBeanList = new ArrayList<>();
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("allData");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                demoBeanList.add(new Equipment(
                                        jsonObject2.optString("deviceName"),
                                        jsonObject2.optString("meterName"),
                                        jsonObject2.optString("data"),
                                        jsonObject2.optString("entryTime"),
                                        jsonObject2.optString("entryUsername")
                                        ));
                            }



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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initView(View view){
        logout = view.findViewById(R.id.logout);
        manager = view.findViewById(R.id.manager);
        excel_out = view.findViewById(R.id.btn_excel_out);
        user_name_text = view.findViewById(R.id.text_user_name);
        user_id_text = view.findViewById(R.id.text_user_id);
        logout.setOnClickListener(this);
        manager.setOnClickListener(this);
        excel_out.setOnClickListener(this);
        if (user.getAdmin() == 1){
            manager.setVisibility(View.VISIBLE);
        }
        user_id_text.setText(String.valueOf(user.getuserNo()));
        user_name_text.setText(user.getUserName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                getActivity().finish();
                startActivity(new Intent(mContext,LoginActivity.class));
                break;
            case R.id.manager:
                startActivity(new Intent(mContext,UserActivity.class));
                break;
            case R.id.btn_excel_out:
                printOutExcel();
                break;
        }
    }
    private void printOutExcel(){



        String name = "/excel";
        String num = String.valueOf(ints[count]);
        String hh = ".xls";
        String result = name.concat(num).concat(hh);
        Log.d("dddd",result);

        File file = new File(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel");
        if (!file.exists()) {
            file.mkdirs();
        }




        String[] title = {"设备名","仪表名", "数据", "录入时间","录入人"};

//        filePath = filePath + excelFileName;

        String excelFileName = "/demo.xls";

//        filePath =  filePath+ excelFileName;

        ExcelUtil.initExcel(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel",result, title);
        String test = Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel";
        Log.d("filePath is :",test);


        ExcelUtil.writeObjListToExcel(demoBeanList, Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel", result,mContext);

        count++;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        demoBeanList.clear();
        getData();
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
