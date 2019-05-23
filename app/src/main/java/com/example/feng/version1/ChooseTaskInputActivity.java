package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.Task.SelectTabActivity;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.adapter.DeviceInputAdapter;
import com.example.feng.version1.bean.DeviceReadyInput;
import com.example.feng.version1.bean.User;
import com.yzq.testzxing.zxing.android.CaptureActivity;

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

import static com.example.feng.version1.Public.PublicData.content;

public class ChooseTaskInputActivity extends AppCompatActivity{

    private Context mContext;
    private RecyclerView recyclerView;
    private DeviceInputAdapter adapter;
    private List<DeviceReadyInput>list;
    private Spinner spinner;
    private Myadapter arr_adapter;
    private List<String> taskList;
    private User user;
    private Button input;
    private static final String [] TASKLIST = {"例行任务","监督任务","全面任务","熄灯任务","特殊任务"};
    private static final String URL2 = PublicData.DOMAIN.concat("/api/user/getDevicesByTask");
    private static final String URL = PublicData.DOMAIN+"/api/user/getAllDevices";
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private ArrayList<String> deviceList,deviceNameList;
    private static final int REQUEST_CODE_INPUT = 0x0001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_task_input);
        mContext = this;
        user = User.getInstance();
        setCustomActionBar();
        initView();
    }

    private void initView(){
        list = new ArrayList<>();
        input = findViewById(R.id.btn_start_rec);
        spinner = findViewById(R.id.spinner_task_input);
        recyclerView = findViewById(R.id.rv_device_input_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(mContext,CaptureActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_INPUT);
            }
        });
        initSpinner();
        initData();
    }

    private void initSpinner(){
        taskList = new ArrayList<>();
        taskList.addAll(Arrays.asList(TASKLIST));
        taskList.add("选择任务");
        arr_adapter= new Myadapter(mContext, android.R.layout.simple_spinner_item, taskList);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setSelection(taskList.size()-1,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //发送提交数据查询的请求
                //传进来的position 作为numlist的index
                getData(taskList.get(position));
                adapter = new DeviceInputAdapter(mContext,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void getData(String taskName){
        HttpUrl.Builder builder = HttpUrl.parse(URL2).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("task",taskName);
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
                                list.add(new DeviceReadyInput(jsonObject2.optString("site"),
                                        jsonObject2.optString("deviceName"),
                                        jsonObject2.optString("meterNum")
                                ));
                            }

                        }else if (status == 1404 || status == 1201){
                            ToastUtil.ToastTextThread(mContext,"当前设备没有仪表信息");

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
        if (requestCode == REQUEST_CODE_INPUT && resultCode == RESULT_OK) {

            if (data != null){
                content = data.getStringExtra(DECODED_CONTENT_KEY);

                if (deviceList.contains(content)){
                    int i = deviceList.indexOf(content);
                    Intent intent = new Intent();
                    intent.putExtra("deviceNo",content);
                    intent.putExtra("deviceName",deviceNameList.get(i));
                    intent.setClass(mContext,SelectTabActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, "请先录入该设备", Toast.LENGTH_SHORT).show();
                }
            }
        }
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


    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_user_activity, null);
        TextView textView = mActionBarView.findViewById(R.id.title);
        textView.setText("录入数据");
        getSupportActionBar().setCustomView(mActionBarView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView back = mActionBarView.findViewById(R.id.pic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseTaskInputActivity.this.finish();
            }
        });
    }
}
