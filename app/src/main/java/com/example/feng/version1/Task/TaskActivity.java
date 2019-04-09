package com.example.feng.version1.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.MainActivity;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Viemitem.Bottom;
import com.example.feng.version1.db.MyDatabaseHelper;
import com.example.feng.version1.db.myDatabasedeviceHelper;
import com.example.feng.version1.localtoservice;
import com.yzq.testzxing.zxing.android.CaptureActivity;

import static com.example.feng.version1.Public.PublicData.content;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener{
    /**************基本变量**************/
    private String username;
    private String password;
    private TextView userid;
    private LinearLayout device1;
    private LinearLayout device2;
    private LinearLayout device3;
    private Button upload;
    private Bottom bottom;

    private Cursor cursor;
    /**扫码相关**/
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private String itemurl,itemdev,queryresult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        /******
         * 设置状态栏透明
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        Intent intent_task = getIntent();
        findview();
        username = intent_task.getStringExtra("USERNAME");
        password = intent_task.getStringExtra("PASSWORD");
        if (null != username || null != password ) {
            userid.setText(username);//设置文本框内容
            Toast.makeText(this, "id"+username, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "password"+password, Toast.LENGTH_SHORT).show();

        }
    }
    private void findview(){
        userid=(TextView)findViewById(R.id.usrid);
        device1 = (LinearLayout)findViewById(R.id.linear1);
        device1.setOnClickListener(this);
        device2 = (LinearLayout)findViewById(R.id.linear2);
        device2.setOnClickListener(this);
        device3 = (LinearLayout)findViewById(R.id.linear3);
        device3.setOnClickListener(this);
        upload=(Button)findViewById(R.id.uploading);
        upload.setOnClickListener(this);
        //底部标签栏
        bottom = (Bottom)findViewById(R.id.b1);
        bottom.activity_from = TaskActivity.this;
       bottom.task.setBackgroundColor(Color.parseColor("#50AAAAAA"));

        /*********注册广播（监听服务器是否连接成功）*********/
        IntentFilter filter = new IntentFilter();
        filter.addAction(PublicData.CONNECTSUCCESS);
        filter.addAction(PublicData.CONNECTDEFAULT);
        registerReceiver(mBroadcastReceiver,filter);

        setupService();//启动服务
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 按下设备键扫码，根据url查询得到设备号
             * 若与publicdata中设备号一致，则进入MeterNum.class
             * */
            case R.id.linear1:
                PublicData.devitemchoice = PublicData.device1;
                Intent intent = new Intent(TaskActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.linear2:

                break;

            case R.id.linear3:

                break;
            case R.id.uploading:
                Toast.makeText(this, "完成情况是...", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
    public String querydeviceid(String u){
        if(PublicData.dbdevice != null){
            cursor = PublicData.dbdevice.query("DEURL",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    itemurl = cursor.getString(cursor.getColumnIndex("url"));
                    if(itemurl.equals(u)){
                        itemdev = cursor.getString(cursor.getColumnIndex("dev"));
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        //相等则进行下一步
        Toast.makeText(TaskActivity.this,"itemdev is :"+itemdev,Toast.LENGTH_SHORT).show();
        //Toast.makeText(TaskActivity.this,"PublicData.devitemchoice is :"+PublicData.devitemchoice,Toast.LENGTH_SHORT).show();
        if(null != itemdev){
            if(itemdev.equals(PublicData.devitemchoice)){
                itemurl=null;
                content=null;
                Intent intent_dev1 = new Intent();
                // 封装设备名信息
                intent_dev1.putExtra("DEVICEID",itemdev);
                intent_dev1.setClass(TaskActivity.this,MeterNum.class);// 制定传递对象
                startActivity(intent_dev1);
                itemdev =null;
            }
            content=null;
        }
       return itemdev;
    }

    private void setupService() {
        // TODO Auto-generated method stub

        /***********************在这里创建两个本地SQLite数据库*************************/
        PublicData.myDatabaseHelper=new MyDatabaseHelper(TaskActivity.this,"BookHHHH.db",null,1);
        PublicData.db = PublicData.myDatabaseHelper.getWritableDatabase();
        PublicData.myDatabasedeviceHelper=new myDatabasedeviceHelper(TaskActivity.this,"deviceurl.db",null,1);
        PublicData.dbdevice = PublicData.myDatabasedeviceHelper.getWritableDatabase();
        Intent addserviceIntent = new Intent(this,AddItemService.class);
        startService(addserviceIntent);
        /**********************在这里初始化连接服务器***************************/
        Intent serviceIntent = new Intent(this,localtoservice.class);
        startService(serviceIntent);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            //服务器已连接
            if (PublicData.CONNECTSUCCESS.equals(intent.getAction())) {
                //Toast.makeText(TaskActivity.this,"SERVICE_connnnn",Toast.LENGTH_SHORT).show();
            }
            //服务器未连接
            else if (PublicData.CONNECTDEFAULT.equals(intent.getAction())) {

                //Toast.makeText(TaskActivity.this,"SERVICE_losssss",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                Toast.makeText(TaskActivity.this,content,Toast.LENGTH_SHORT).show();
                if (PublicData.devitemchoice != null){
                   //查询SQLite数据库
                    querydeviceid(content);
                }

            }
        }
    }
}
