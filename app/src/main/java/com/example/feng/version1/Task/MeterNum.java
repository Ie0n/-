package com.example.feng.version1.Task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2019/3/21.
 */

public class MeterNum extends Activity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> dataList;
    private String deviceid;

//    String dv1 = new String( "同学1，学号1601030033，性别女，爱好唱歌" );
//    String dv2 = new String( "同学2，学号1601030033，性别女，爱好唱歌" );
//    String dv3 = new String( "同学3，学号1601030033，性别女，爱好唱歌" );
//    String dv4 = new String( "同学4，学号1601030033，性别女，爱好唱歌" );
//    String dv5 = new String( "同学甲，学号1601030033，性别女，爱好唱歌" );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate( savedInstanceState );
        setContentView( R.layout.meternum );

        /******
         * 设置状态栏透明
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        Intent intent_meter = getIntent();
        deviceid = intent_meter.getStringExtra("DEVICEID");

        dataList = new ArrayList<Map<String, Object>>(  );
        listView = (ListView) findViewById( R.id.lv1 );
        simpleAdapter = new SimpleAdapter( MeterNum.this,getDate(),R.layout.device_item,new String[]{"ig1","tv1","tv2"},new int[]{R.id.ig1,R.id.tv1,R.id.tv2} );
        listView.setAdapter( simpleAdapter ); //绑适配器
        listView.setOnItemClickListener( this );
        listView.setOnScrollListener( this );

        PublicData.content=null;


    }
    private List<Map<String,Object>> getDate(){
        String s = new String( "A" );
        for(int i=1;i<=9;i++){
            Map<String,Object> map = new HashMap<String, Object>(  );
            if(1 == i){
                map.put( "ig1",R.mipmap.xinlv_24dp );
                map.put( "tv1","设备编号");
                //map.put( "tv2","表计"+i+"详情" );
                map.put( "tv2",deviceid);
                dataList.add(map);
            }else if(9==i){
                map.put( "ig1",R.mipmap.xinlv_24dp );
                map.put( "tv1","提交");
                //map.put( "tv2","表计"+i+"详情" );
                map.put( "tv2","点击提交数据" );
                dataList.add(map);
            }
            else{
                map.put( "ig1",R.mipmap.xinlv_24dp );
                map.put( "tv1","表计"+(i-1));
                //map.put( "tv2","表计"+i+"详情" );
                map.put( "tv2","点击输入数据" );
                dataList.add(map);
            }
        }
        return dataList;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        switch (scrollState) {
//            case SCROLL_STATE_FLING:
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put( "ig1", R.mipmap.xinlv_24dp  );
//                map.put( "tv1", "表计" );
//                //map.put( "tv2", "查看表计详情" );
//                map.put( "tv2","点击输入数据" );
//                dataList.add( map );
//                //自动提醒刷新数据
//                simpleAdapter.notifyDataSetChanged();
//                break;
//            case SCROLL_STATE_IDLE:
//                break;
//            case SCROLL_STATE_TOUCH_SCROLL:
//                break;
//        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder( MeterNum.this);
            //设置点击ITem事件
            if(position==0){
            }
            else if(position==1){
                Intent intent_1 = new Intent();
                // 封装用户名信息
                intent_1.putExtra("DEVICE", deviceid);
                intent_1.putExtra("METER", "meter"+position);
                intent_1.setClass(MeterNum.this,ReadNumber.class);// 制定传递对象
                startActivity(intent_1);
            }
            else if(position==2){
                Intent intent_2 = new Intent();
                // 封装用户名信息
                intent_2.putExtra("DEVICE", deviceid);
                intent_2.putExtra("METER", "meter"+position);
                intent_2.setClass(MeterNum.this,ReadNumber.class);// 制定传递对象
                startActivity(intent_2);
            }
            else if(position==3){
                Intent intent_3 = new Intent();
                // 封装用户名信息
                intent_3.putExtra("DEVICE", deviceid);
                intent_3.putExtra("METER", "meter"+position);
                intent_3.setClass(MeterNum.this,ReadNumber.class);// 制定传递对象
                startActivity(intent_3);
            }
            else if(position==4){

            }
            else if(position==5){

            }else if(position==6){

            }
            else if(position==7){

            }
            else if(position==8){
            //提交数据---弹出对话框
                builder.setTitle( "提交数据" );//设置标题
                builder.setIcon( R.drawable.ic_launcher_background );
                if(PublicData.meterflag){
                    builder.setMessage("已录入完毕，是否提交？");
                }else{
                    /**在这里显示哪些表计没有填写**/
                    builder.setMessage("未填写完整！");
                }

            builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(PublicData.meterflag){
                        /**在这里包装JSON格式数据并上传服务器**/
                        Toast.makeText( MeterNum.this,"上传", Toast.LENGTH_SHORT).show();
                    }else{
                    }
                }
            } );
            builder.setNegativeButton( "取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText( MeterNum.this,"点击了取消按钮", Toast.LENGTH_SHORT).show();
                }
            } );
            AlertDialog dialog = builder.create();
            dialog.show();
            }

    }
}
