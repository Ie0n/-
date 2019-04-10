package com.example.feng.version1.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Viemitem.Bottom;
import com.example.feng.version1.db.MyDatabaseHelper;
import com.example.feng.version1.db.myDatabasedeviceHelper;
import com.example.feng.version1.fragment.CountFragment;
import com.example.feng.version1.fragment.MyFragment;
import com.example.feng.version1.fragment.TaskFragment;
import com.example.feng.version1.localtoservice;
import com.yzq.testzxing.zxing.android.CaptureActivity;

import static com.example.feng.version1.Public.PublicData.content;

public class MainActivity extends AppCompatActivity implements TabHost.TabContentFactory {
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

    private ViewPager mViewPager;
    private TabHost mTabHost;
    private TabWidget mTabWidget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /******
         * 设置状态栏透明
         * **/
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        this.getSupportFragmentManager().findFragmentByTag(TaskFragment.class.getSimpleName()).onActivityResult(requestCode,resultCode,data);
//
//    }

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
        Toast.makeText(MainActivity.this,"itemdev is :"+itemdev,Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this,"PublicData.devitemchoice is :"+PublicData.devitemchoice,Toast.LENGTH_SHORT).show();
        if(null != itemdev){
            if(itemdev.equals(PublicData.devitemchoice)){
                itemurl=null;
                content=null;
                Intent intent_dev1 = new Intent();
                // 封装设备名信息
                intent_dev1.putExtra("DEVICEID",itemdev);
                intent_dev1.setClass(MainActivity.this,MeterNum.class);// 制定传递对象
                startActivity(intent_dev1);
                itemdev =null;
            }
            content=null;
        }
       return itemdev;
    }

    private void initView() {
        mViewPager = findViewById(R.id.main_view_pager);
        mTabHost = findViewById(R.id.tab_host);
        mTabWidget = findViewById(android.R.id.tabs);

        mTabHost.setup();
        String titles[] = {
                "任务","数据统计","我的"
        };
        int iconID[] = {
            R.drawable.ic_main_tab_selector_task,R.drawable.ic_main_tab_selector_data
                ,R.drawable.ic_main_tab_selector_my
        };


        for (int index = 0; index < titles.length; index++) {
            // 加载Tab每个标签的布局
            View view = getLayoutInflater().inflate(R.layout.tab_main_content, null, false);

            ImageView icon = view.findViewById(R.id.main_tab_icon);
            TextView title = view.findViewById(R.id.main_tab_txt);
            View tab = view.findViewById(R.id.tab_bg);

            icon.setImageResource(iconID[index]);
            title.setText(titles[index]);
            // 设置布局的整体颜色
            tab.setBackgroundColor(getResources().getColor(R.color.white));

            // 为TabHost添加Tab
            mTabHost.addTab(
                    // 参数为Tag，是Tab的标记
                    mTabHost.newTabSpec(titles[index])
                            // 将视图指定为选项卡指示符。
                            .setIndicator(view)
                            // 设置标签要显示的内容，
                            // 但此处因为是用Fragment做的ViewPager，所以此处设置为空了，参数可以是View的Id
                            .setContent(this)
            );
        }

        // 4个Fragment资源。
        final Fragment[] fragments = new Fragment[]{
                TaskFragment.newInstance(),
                CountFragment.newInstance(),
                MyFragment.newInstance()
        };
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }
            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        // 设置ViewPager改变Tab跟着改变
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // 设置当前的Tab与ViewPager的位置相对应
                if (mTabHost != null) {
                    mTabHost.setCurrentTab(i);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        // 设置Tab改变ViewPager也跟着改变
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if (mTabHost != null) {
                    // 获取当前Tab的位置
                    int position = mTabHost.getCurrentTab();
                    mViewPager.setCurrentItem(position);
                }
            }
        });

    }

    public TabWidget getTabWidget() {
        return mTabWidget;
    }

    @Override
    public View createTabContent(String tag) {
        View view = new View(this);
        // 将view隐藏起来，因为这里用ViewPager显示内容
        view.setMinimumHeight(0);
        view.setMinimumWidth(0);
        return view;
    }
}
