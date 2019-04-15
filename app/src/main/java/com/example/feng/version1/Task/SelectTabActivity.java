package com.example.feng.version1.Task;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.R;
import com.example.feng.version1.adapter.EquipmentAdapter;
import com.example.feng.version1.bean.Equipment;

import java.util.ArrayList;
import java.util.List;

public class SelectTabActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private Context mContext;
    private List<Equipment> equipmentList;
    private EquipmentAdapter adapter;
    private String device;
    private TextView deviceTv;
    private String [] tabs= {
            "仪表一","仪表二","仪表三","仪表四","仪表五","仪表六","仪表七","仪表八",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tab);
        mContext = this;
        /******
         * 设置状态栏透明
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        Intent intent_task = getIntent();
        device = intent_task.getStringExtra("device");
        initView();
    }
    private void initView(){
        recyclerView = findViewById(R.id.rv_equipment);
        deviceTv = findViewById(R.id.text_equipment_name);
        deviceTv.setText(device);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        equipmentList = new ArrayList<>();
        initData();
        adapter = new EquipmentAdapter(mContext,equipmentList);
        adapter.setOnItemListener(new EquipmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "点击了第"+position+"个仪表", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("device",device);
                intent.putExtra("tab",tabs[position]);
                intent.setClass(mContext,ReadNumber.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initData(){
        for (int i = 0; i < 8; i++) {
            equipmentList.add(new Equipment(tabs[i],11L));
        }
    }

}
