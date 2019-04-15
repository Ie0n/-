package com.example.feng.version1.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.example.feng.version1.R;
import com.example.feng.version1.bean.Equipment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableFragment extends Fragment {

    private Context mContext;
    private SmartTable table;
    private Spinner spinner;
    private ArrayAdapter arr_adapter;
    private List data_list;

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
        initData();
        return view;
    }

    private void initData(){
        List<Equipment> list = new ArrayList<>();
        String time = stampToDate(System.currentTimeMillis());
        list.add(new Equipment("设备一",100L,1,time,"张三",10));
        list.add(new Equipment("设备一",100L,2,time,"张三",3));
        list.add(new Equipment("设备一",100L,3,time,"张三",4));
        list.add(new Equipment("设备一",100L,4,time,"张三",7));
        list.add(new Equipment("设备一",100L,5,time,"张三",5));
        list.add(new Equipment("设备一",100L,6,time,"张三",6));
        list.add(new Equipment("设备一",100L,7,time,"张三",2));
        list.add(new Equipment("设备一",100L,8,time,"张三",3));

        table.setData(list);
        table.getConfig().setColumnTitleStyle(new FontStyle(65,Color.BLUE));
        table.getConfig().setTableTitleStyle(new FontStyle(80,Color.BLACK));
        table.getConfig().setContentStyle(new FontStyle(70,Color.BLACK));
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
    }

    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    private void initView(View view){
        table = view.findViewById(R.id.table);
        spinner = view.findViewById(R.id.spinner);
        data_list = new ArrayList<String>();

        data_list.add("设备一");
        data_list.add("设备二");
        data_list.add("设备三");
        data_list.add("设备四");

        //适配器
        arr_adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
    }
}
