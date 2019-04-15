package com.example.feng.version1;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.feng.version1.adapter.EquipmentAdapter;
import com.example.feng.version1.bean.Equipment;

import java.util.ArrayList;
import java.util.List;

public class AddEquipmentActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Context mContext;
    private FloatingActionButton add,confirm;
    private List<Equipment> equipmentList;
    private EquipmentAdapter adapter;
    private int count = 0;
    private String [] tabs= {
            "仪表一","仪表二","仪表三","仪表四","仪表五","仪表六","仪表七","仪表八"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initView();
    }
    private void initView(){
        recyclerView = findViewById(R.id.rv_equipment);
        add = findViewById(R.id.add);
        confirm = findViewById(R.id.confirm);
        add.setOnClickListener(this);
        confirm.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        equipmentList = new ArrayList<>();
        adapter = new EquipmentAdapter(mContext,equipmentList);
        adapter.setOnItemListener(new EquipmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                if (equipmentList.size()<8){
                    equipmentList.add(new Equipment(tabs[count],11L));
                    count++;
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext,"已超过最多仪表数量",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.confirm:
                Toast.makeText(mContext, "成功添加设备", Toast.LENGTH_SHORT).show();
                AddEquipmentActivity.this.finish();
        }
    }
}
