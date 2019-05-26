package com.example.feng.version1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.example.feng.version1.MessageEvent;
import com.example.feng.version1.MyLineChart;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Util.ExcelUtil;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.SiteTaskEquipment;
import com.example.feng.version1.bean.User;
import com.example.feng.version1.bean.lineChartBean;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class TableFragment extends Fragment implements View.OnClickListener{

    private Context mContext;
    private SmartTable table;
    private Spinner spinner,spinnerSite,spinnerTask,spinnerSite2,spinnerDevice,spinnerMeter;
    private Myadapter arr_adapter,siteAdapter,taskAdapter,metersAdapter;
    private EditText start_year,start_month,start_day,end_year,end_month,end_day;
    private Button search_by_device,searchBySite,print,search_by_multi;
    private String selectDevice,selectSite,selectTask,selectSite2,selectDeviceId,selectMeter;
    private static final String DEVICE_URL = PublicData.DOMAIN+"/api/user/getAllDevices";
    private static final String METER_URL = PublicData.DOMAIN+"/api/user/getDataByDevice";
    private static final String LINE_URL = PublicData.DOMAIN+"/api/user/getMeterData";
    private static final String LOC_URL = PublicData.DOMAIN+"/api/user/getDataBySiteAndTask";
    private static final String METERS_URL = PublicData.DOMAIN+"/api/user/getDeviceMetersB";
    private List<String> deviceNameList,siteList,taskList,meterList;
    private static final String [] TASKLIST = {"例行任务","监督任务","全面任务","熄灯任务","特殊任务"};
    private static final String [] SITELIST = {"站点一","站点二","站点三"};
    private List<String> deviceIdList;
    private boolean isFirst = false;
    private List<lineChartBean> list = new ArrayList<>();

    private User user;

    private MyLineChart lineChart;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例


    List<Equipment> MeterList = new ArrayList<>();
    List<SiteTaskEquipment> locList = new ArrayList<>();


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
        initListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        siteList = new ArrayList<>();
        taskList = new ArrayList<>();
        siteList.addAll(Arrays.asList(SITELIST));
        taskList.addAll(Arrays.asList(TASKLIST));
        getData();
        meterList = new ArrayList<>();
        spinnerDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDeviceId = deviceIdList.get(position);
                if (selectSite2.equals("选择设备")){
                    return;
                }
                meterList.clear();
                getMeters(deviceIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //发送提交数据查询的请求
                //传进来的position 作为numlist的index
                selectDevice = deviceIdList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinnerSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSite = siteList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTask = taskList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSite2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSite2 = siteList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerMeter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMeter = meterList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMeters(String deviceNo){
        HttpUrl.Builder builder = HttpUrl.parse(METERS_URL).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("deviceNo",deviceNo);
        final Request request = new Request
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
                            JSONArray array = data.getJSONArray("meters");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                meterList.add(jsonObject2.optString("meterName"));
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    meterList.add("选择仪表");
                                    metersAdapter = new Myadapter(mContext,android.R.layout.simple_spinner_item, meterList);
                                    //设置样式
                                    metersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerMeter.setAdapter(metersAdapter);
                                    spinnerMeter.setSelection(meterList.size()-1,true);
                                }
                            });

                        }else if (status == 1404 || status == 1201){
                            ToastUtil.ToastTextThread(mContext,"没有数据/参数错误");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt("status");

                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("devices");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                deviceNameList.add(jsonObject2.optString("deviceName"));
                                deviceIdList.add(jsonObject2.optString("deviceNo"));
                            }
                            if (!isFirst){
                                deviceIdList.add("1");
                                deviceNameList.add("选择设备");
                                taskList.add("选择任务");
                                siteList.add("选择站点");
                            }
                            isFirst = true;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //适配器
                                    arr_adapter= new Myadapter(mContext, android.R.layout.simple_spinner_item, deviceNameList);
                                    taskAdapter = new Myadapter(mContext, android.R.layout.simple_spinner_item, taskList);
                                    siteAdapter = new Myadapter(mContext,android.R.layout.simple_spinner_item, siteList);
                                    //设置样式
                                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    //加载适配器
                                    spinnerSite2.setAdapter(siteAdapter);

                                    spinnerDevice.setAdapter(arr_adapter);
                                    spinner.setAdapter(arr_adapter);
                                    spinnerTask.setAdapter(taskAdapter);
                                    spinnerSite.setAdapter(siteAdapter);
                                    spinnerSite2.setSelection(siteList.size()-1,true);
                                    spinnerTask.setSelection(taskList.size()-1,true);
                                    spinnerSite.setSelection(siteList.size()-1,true);
                                    spinner.setSelection(deviceNameList.size()-1,true);
                                    spinnerDevice.setSelection(deviceNameList.size()-1,true);

                                }
                            });


                        }else if (status == 1404){
                            ToastUtil.ToastTextThread(mContext,"当前没有设备信息");
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
        print = view.findViewById(R.id. btn_output_data);
        spinner = view.findViewById(R.id.spinner);
        spinnerDevice = view.findViewById(R.id.spinner_device);
        spinnerSite2 = view.findViewById(R.id.spinner_site2);
        spinnerMeter = view.findViewById(R.id.spinner_meter);
        spinnerSite = view.findViewById(R.id.spinner_site);
        spinnerTask = view.findViewById(R.id.spinner_task);
        search_by_device = view.findViewById(R.id.btn_search_by_device);
        search_by_multi = view.findViewById(R.id.btn_search_by_multi);
        searchBySite = view.findViewById(R.id.btn_search_by_site);
        lineChart = view.findViewById(R.id.line_chart);
        start_year = view.findViewById(R.id.edit_start_year);
        start_month = view.findViewById(R.id.edit_start_month);
        start_day = view.findViewById(R.id.edit_start_day);
        end_year = view.findViewById(R.id.edit_end_year);
        end_month = view.findViewById(R.id.edit_end_month);
        end_day = view.findViewById(R.id.edit_end_day);
    }

    private void initListener(){
        searchBySite.setOnClickListener(this);
        search_by_device.setOnClickListener(this);
        search_by_multi.setOnClickListener(this);
        print.setOnClickListener(this);
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
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("metersData");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                MeterList.add(new Equipment(jsonObject2.optString("meterName"),
                                        jsonObject2.optString("data"),
                                        jsonObject2.optString("entryTime"),
                                        jsonObject2.optString("entryUsername"),
                                        jsonObject2.optString("site"),
                                        jsonObject2.optString("task")
                                ));
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    table.setVisibility(View.VISIBLE);
                                    print.setVisibility(View.VISIBLE);
                                }
                            });
                        }else if (status == 1404 || status == 1201){
                            ToastUtil.ToastTextThread(mContext,"当前设备没有仪表信息");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    print.setVisibility(View.GONE);
                                }
                            });
                            MeterList.add(new Equipment("无","无","无","无","无","无"));
                        }
                        table.setData(MeterList);
                        table.getConfig().setColumnTitleStyle(new FontStyle(54,Color.BLUE));
                        table.getConfig().setTableTitleStyle(new FontStyle(80,Color.BLACK));
                        table.getConfig().setContentStyle(new FontStyle(50,Color.BLACK));
                        table.getConfig().setShowXSequence(false);
                        table.getConfig().setShowYSequence(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String getBeginTime(){
        return "20".concat(start_year.getText().toString()).concat("-")
                .concat(start_month.getText().toString().concat("-"))
                .concat(start_day.getText().toString())
                .concat(" 00:00:01");
    }

    private String getEndTime(){
        return "20".concat(end_year.getText().toString()).concat("-")
                .concat(end_month.getText().toString().concat("-"))
                .concat(end_day.getText().toString())
                .concat(" 23:59:59");
    }

    private void getLineChartData(){
        HttpUrl.Builder builder = HttpUrl.parse(LINE_URL).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("deviceNo",selectDeviceId)
                .addQueryParameter("meterName",selectMeter)
                .addQueryParameter("beginTime",getBeginTime())
                .addQueryParameter("endTime",getEndTime());
        final Request request = new Request
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
                            JSONArray array = data.getJSONArray("resultData");
                            list.clear();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                list.add(new lineChartBean(jsonObject2.getString("entryTime"),
                                        Double.parseDouble(jsonObject2.getString("data"))));
                            }
                            if (list.size() != 0){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initChart(lineChart,list);
                                        showLineChart(list, "仪表数据", Color.CYAN);
                                    }
                                });
                            }
                        }else if (status == 1404 || status == 1201){
                            ToastUtil.ToastTextThread(mContext,"当前没有信息");
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
        deviceNameList.clear();
        deviceIdList.clear();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search_by_device:
                if (selectDevice.equals("1")){
                    Toast.makeText(mContext, "请先选择设备", Toast.LENGTH_SHORT).show();
                    return;
                }
                lineChart.setVisibility(View.GONE);
                MeterList.clear();
                locList.clear();
                getMeterData(selectDevice);
                break;
            case R.id.btn_search_by_site:
                if (selectSite.equals("选择站点")||selectTask.equals("选择任务")){
                    Toast.makeText(mContext, "请选择站点或任务", Toast.LENGTH_SHORT).show();
                    return;
                }
                lineChart.setVisibility(View.GONE);
                MeterList.clear();
                locList.clear();
                getLocData(selectSite,selectTask);
                break;
            case R.id.btn_output_data:
                if (locList.size() == 0 && MeterList.size() == 0){
                    Toast.makeText(mContext, "请先选择查看数据", Toast.LENGTH_SHORT).show();
                }
                if ((locList.size() == 0 || locList.size() == 1) || (MeterList.size() == 0 || MeterList.size() == 1)){
                    Toast.makeText(mContext, "当前没有数据", Toast.LENGTH_SHORT).show();
                }
                if (locList.size() != 0 && locList.size() != 1){
                    printLocOutExcel();
                }
                if (MeterList.size() != 0 && MeterList.size() != 1){
                    printDeviceOutExcel();
                }
                break;
            case R.id.btn_search_by_multi:
                if (selectSite2.equals("选择站点") || selectDevice.equals("选择任务")||selectMeter.equals("选择仪表")){
                    Toast.makeText(mContext, "请先选择查询条件", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(start_year.getText()) ||TextUtils.isEmpty(start_month.getText())||
                        TextUtils.isEmpty(start_day.getText())||TextUtils.isEmpty(end_year.getText())||
                        TextUtils.isEmpty(end_month.getText())||TextUtils.isEmpty(end_day.getText())){
                    Toast.makeText(mContext, "请完整输入查询日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLineChartData();
                print.setVisibility(View.GONE);
                table.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);

        }
    }

    private void getLocData(String site,String task){
        HttpUrl.Builder builder = HttpUrl.parse(LOC_URL).newBuilder();
        builder.addQueryParameter("userNo",String.valueOf(user.getuserNo()))
                .addQueryParameter("site",site)
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
                        int status = jsonObject.getInt("status");
                        if (status == 1200){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray array = data.getJSONArray("resultData");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject)array.get(i);
                                locList.add(new SiteTaskEquipment(jsonObject2.optString("deviceName"),
                                        jsonObject2.optString("meterName"),
                                        jsonObject2.optString("data"),
                                        jsonObject2.getString("entryTime"),
                                        jsonObject2.getString("entryUsername")
                                        ));
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    table.setVisibility(View.VISIBLE);
                                    print.setVisibility(View.VISIBLE);
                                }
                            });
                        }else if (status == 1404 || status == 1201){
                            ToastUtil.ToastTextThread(mContext,"当前没有数据信息");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    print.setVisibility(View.GONE);
                                }
                            });
                            locList.add(new SiteTaskEquipment("无","无","无","无","无"));
                        }
                        table.setData(locList);
                        table.getConfig().setColumnTitleStyle(new FontStyle(54,Color.BLUE));
                        table.getConfig().setTableTitleStyle(new FontStyle(80,Color.BLACK));
                        table.getConfig().setContentStyle(new FontStyle(50,Color.BLACK));
                        table.getConfig().setShowXSequence(false);
                        table.getConfig().setShowYSequence(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void printDeviceOutExcel(){
        String hh = ".xls";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String entry = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String result = "Dev ".concat(entry).concat(hh);

        File file = new File(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel");
        if (!file.exists()) {
            file.mkdirs();
        }


        String[] title = {"站点","仪表", "数据","录入时间","录入人"};


        ExcelUtil.initExcel(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel",result, title);
        String test = Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel";
        Log.d("filePath is :",test);


        ExcelUtil.writeObjListToExcel(MeterList, Environment.getExternalStorageDirectory().toString()+
                File.separator +"DataExcel", result,mContext,3);

    }

    private void printLocOutExcel(){
        String hh = ".xls";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String entry = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String result = "Loc ".concat(entry).concat(hh);

        File file = new File(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel");
        if (!file.exists()) {
            file.mkdirs();
        }


        String[] title = {"设备","仪表", "数据","录入时间","录入人"};


        ExcelUtil.initExcel(Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel",result, title);
        String test = Environment.getExternalStorageDirectory().toString()+

                File.separator +"DataExcel";
        Log.d("filePath is :",test);


        ExcelUtil.writeObjListToExcel(locList, Environment.getExternalStorageDirectory().toString()+
                File.separator +"DataExcel", result,mContext,2);

    }

    class Myadapter<T> extends ArrayAdapter{
        public Myadapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            int i = super.getCount();
            return i>0?i-1:i;
        }
    }


    private void initChart(MyLineChart lineChart, final List<lineChartBean>list1) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        Matrix matrix = new Matrix();
        // x轴放大4倍，y不变
        matrix.postScale(1.5f, 1.0f);
        // 设置缩放
        lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
        //设置XY轴动画效果

        lineChart.animateY(2500);
        lineChart.animateX(1500);

        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        rightYaxis.setEnabled(false);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String tradeDate = list1.get((int) value % list1.size()).getDate();
                return formatDate(tradeDate);
            }
        });

        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        rightYaxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(14f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        //是否绘制在图表里面
        legend.setDrawInside(false);
    }
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
    }

    public void showLineChart(List<lineChartBean> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            lineChartBean data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, (float) data.getData());
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    public static String formatDate(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }
}
