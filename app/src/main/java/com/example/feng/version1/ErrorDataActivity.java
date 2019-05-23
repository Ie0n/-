package com.example.feng.version1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Util.ExcelUtil;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.ErrorEquipment;
import com.example.feng.version1.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorDataActivity extends AppCompatActivity {

    private Context mContext;
    private SmartTable smartTable;
    private Button print;
    private User user;
    private List<ErrorEquipment>MeterList;
    private List<ErrorEquipment>ErrorEquipmentList;

    private static final String URL = PublicData.DOMAIN+"/api/user/getUnusualData";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_data);
        mContext = this;
        user = User.getInstance();
        setCustomActionBar();
        initView();
        setListener();
        initData();
    }

    private void initView(){
        smartTable = findViewById(R.id.table);
        print = findViewById(R.id.btn_output_unusual_data);
    }
    private void setListener(){
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printOutExcel();
            }
        });
    }

    @NonNull
    private String getCookie() {
        SharedPreferences sp = getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }
    private void initData(){
        MeterList = new ArrayList<>();
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
                            JSONArray array = data.getJSONArray("unusualData");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                MeterList.add(new ErrorEquipment(jsonObject2.optString("meterName"),
                                        jsonObject2.optString("data"),
                                        jsonObject2.optString("entryTime"),
                                        jsonObject2.optString("entryUsername"),
                                        jsonObject2.optString("site"),
                                        jsonObject2.optString("deviceName")
                                ));
                            }
                            smartTable.setData(MeterList);
                            smartTable.getConfig().setColumnTitleStyle(new FontStyle(54,Color.BLUE));
                            smartTable.getConfig().setTableTitleStyle(new FontStyle(80,Color.BLACK));
                            smartTable.getConfig().setContentStyle(new FontStyle(50,Color.BLACK));
                            smartTable.getConfig().setShowXSequence(false);
                            smartTable.getConfig().setShowYSequence(false);
                        }else if (status == 1201){
                            ToastUtil.ToastTextThread(mContext,"当前没有异常数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void printOutExcel(){
        String hh = ".xls";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String entry = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String result = "Error ".concat(entry).concat(hh);

        File file = new File(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel");
        if (!file.exists()) {
            file.mkdirs();
        }


        String[] title = {"站点名","设备名", "仪表", "数据","录入时间","录入人"};


        ExcelUtil.initExcel(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel",result, title);
        String test = Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel";
        Log.d("filePath is :",test);


        ExcelUtil.writeObjListToExcel(MeterList, Environment.getExternalStorageDirectory().toString()+
                File.separator +"DataExcel", result,mContext,1);

    }
    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_user_activity, null);
        TextView textView = mActionBarView.findViewById(R.id.title);
        textView.setText("异常数据");
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorDataActivity.this.finish();
            }
        });
    }
}
