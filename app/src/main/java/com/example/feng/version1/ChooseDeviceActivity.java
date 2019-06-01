package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.bean.User;
import com.yzq.testzxing.zxing.android.CaptureActivity;

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

import static com.example.feng.version1.Public.PublicData.content;

public class ChooseDeviceActivity extends AppCompatActivity{

    private Context mContext;
    private User user;
    private Spinner spinnerSite,spinnerTask;
    private Button confirm;
    private Myadapter siteAdapter,taskAdapter;
    private static final String [] TASKLIST = {"例行任务","监督任务","全面任务","熄灯任务","特殊任务"};
    private static final String [] SITELIST = {"站点一","站点二","站点三"};
    private ArrayList<String> siteList;
    private ArrayList<String> taskList;
    private static final int REQUEST_CODE_ADD = 0x0000;
    private static final String  DECODED_CONTENT_KEY = "codedContent";
    private ArrayList<String> deviceList,deviceNameList;
    private String site,task;

    private static final String URL_EXIT = PublicData.DOMAIN+"/api/admin/existDevice";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }
        setContentView(R.layout.activity_choose_device);
        mContext = this;
        setCustomActionBar();
        user = User.getInstance();

        deviceNameList = new ArrayList<>();
        deviceList = new ArrayList<>();
        Intent deviceIntent = getIntent();
        deviceList = deviceIntent.getStringArrayListExtra("deviceList");
        deviceNameList = deviceIntent.getStringArrayListExtra("deviceNameList");

        initView();
        initListener();
    }

    private void initView(){
        spinnerSite = findViewById(R.id.spinner_site);
        spinnerTask = findViewById(R.id.spinner_task);
        confirm = findViewById(R.id.start_rec);

        siteList = new ArrayList<>();
        taskList = new ArrayList<>();

        siteList.addAll(Arrays.asList(SITELIST));
        taskList.addAll(Arrays.asList(TASKLIST));
        siteList.add("选择站点");
        taskList.add("选择任务");

        //适配器
        siteAdapter = new Myadapter(mContext, android.R.layout.simple_spinner_item, siteList);
        taskAdapter = new Myadapter(mContext,android.R.layout.simple_spinner_item, taskList);
        //设置样式
        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinnerSite.setAdapter(siteAdapter);
        spinnerTask.setAdapter(taskAdapter);
        spinnerSite.setSelection(siteList.size()-1,true);
        spinnerTask.setSelection(taskList.size()-1,true);
    }
    private void initListener(){
        spinnerSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                site = siteList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task = taskList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (site == null || task == null){
                    Toast.makeText(mContext, "请选择任务或站点", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(mContext,CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK){
            if (data != null){
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                isExit(content,task);
            }
        }
    }

    @NonNull
    private String getCookie() {
        SharedPreferences sp = getSharedPreferences("Cookie", MODE_PRIVATE);
        return sp.getString("token", "access_token")
                .concat("=")
                .concat(sp.getString("token_value", "null"))
                .concat(";");
    }

    private void isExit(String id,final String task){
        HttpUrl.Builder builder = HttpUrl.parse(URL_EXIT).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("deviceNo",id)
                .addQueryParameter("task",task);
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
                        Log.d("ddddddd",task);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            //存在
                            ToastUtil.ToastTextThread(mContext,"该设备已录入");
                        }else if (status == 1201){
                            //不存在
                            Intent intent = new Intent();
                            intent.putExtra("deviceNo",content);
                            intent.putExtra("site",site);
                            intent.putExtra("task",task);
                            intent.setClass(mContext,AddEquipmentActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    class Myadapter<T> extends ArrayAdapter {
        public Myadapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            int i = super.getCount();
            return i>0?i-1:i;
        }
    }


    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_user_activity, null);
        TextView textView = mActionBarView.findViewById(R.id.title);
        textView.setText("录入设备");
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDeviceActivity.this.finish();
            }
        });
    }
}
