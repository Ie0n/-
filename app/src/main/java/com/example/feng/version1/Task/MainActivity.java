package com.example.feng.version1.Task;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.feng.version1.R;
import com.example.feng.version1.fragments.TableFragment;
import com.example.feng.version1.fragments.MyFragment;
import com.example.feng.version1.fragments.TaskFragment;

public class MainActivity extends AppCompatActivity implements TabHost.TabContentFactory {

    private ViewPager mViewPager;
    private TabHost mTabHost;
    private TabWidget mTabWidget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    private void initView() {
        mViewPager = findViewById(R.id.main_view_pager);
        mTabHost = findViewById(R.id.tab_host);
        mTabWidget = findViewById(android.R.id.tabs);

        mTabHost.setup();
        String titles[] = {
                "执行操作","查看设备数据","我的"
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
                TableFragment.newInstance(),
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
