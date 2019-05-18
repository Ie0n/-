package com.example.feng.version1.Task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Util.ToastUtil;
import com.example.feng.version1.adapter.MetersAdapter;
import com.example.feng.version1.bean.StatusResponse;
import com.example.feng.version1.bean.User;
import com.example.feng.version1.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectTabActivity extends AppCompatActivity implements Callback,View.OnClickListener {

    private RecyclerView recyclerView;
    private Context mContext;
    private List<StatusResponse.DataBean.MetersBean> meters;
    private MetersAdapter adapter;
    private FloatingActionButton confirm;
    private String deviceName,deviceNo;
    private TextView deviceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tab);
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        Intent intent_task = getIntent();
        deviceName = intent_task.getStringExtra("deviceName");
        deviceNo = intent_task.getStringExtra("deviceNo");
        initView();
    }
    private void initView(){
        recyclerView = findViewById(R.id.rv_equipment);
        deviceTv = findViewById(R.id.text_equipment_name);
        confirm = findViewById(R.id.confirm_btn);
        confirm.setOnClickListener(this);
        deviceTv.setText(deviceName);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        meters = new ArrayList<>();
        initData();
        adapter = new MetersAdapter(mContext,meters);
        adapter.setOnItemListener(new MetersAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("device",deviceName);
                intent.putExtra("meterid",String.valueOf(meters.get(position).getMeterId()));
                intent.putExtra("tab",meters.get(position).getMeterName());
                Log.d("meterid",String.valueOf(meters.get(position).getMeterId()));
                intent.setClass(mContext,ReadNumber.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initData(){
        String url = PublicData.DOMAIN+"/api/user/getDeviceMeters?userNo="+User.getInstance().getuserNo()+"&deviceNo="+deviceNo;
        HttpRequest.getInstance().get(url,this,PublicData.getCookie(mContext));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d("res-",e.getMessage());
        ToastUtil.ToastTextThread(mContext,e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()){
            Gson gson = new GsonBuilder().create();
            String body = (response.body().string());
            StatusResponse metasResponse = gson.fromJson(body,StatusResponse.class);
            if (metasResponse.getStatus() == 1200){
                meters.addAll(metasResponse.getData().getMeters());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }else {
                ToastUtil.ToastTextThread(mContext,metasResponse.getStatusinfo().getMessage());
            }
        }else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_btn:
                showDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }
    private void showDialog(){

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.icon)
                .setTitle("提示")
                .setMessage("请先确认仪表数据是否录入完整")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "已上传所有已录入数据", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        SelectTabActivity.this.finish();
                    }
                }).create();
        dialog.show();
    }
}
