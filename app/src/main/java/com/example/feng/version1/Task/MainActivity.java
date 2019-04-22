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
            View view = getLayoutInflater().inflate(R.layout.tab_main_content, null, false);

            ImageView icon = view.findViewById(R.id.main_tab_icon);
            TextView title = view.findViewById(R.id.main_tab_txt);
            View tab = view.findViewById(R.id.tab_bg);

            icon.setImageResource(iconID[index]);
            title.setText(titles[index]);
            tab.setBackgroundColor(getResources().getColor(R.color.white));

            mTabHost.addTab(
                    mTabHost.newTabSpec(titles[index])
                            .setIndicator(view)
                            .setContent(this)
            );
        }

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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (mTabHost != null) {
                    mTabHost.setCurrentTab(i);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if (mTabHost != null) {
                    int position = mTabHost.getCurrentTab();
                    mViewPager.setCurrentItem(position);
                }
            }
        });

    }

    @Override
    public View createTabContent(String tag) {
        View view = new View(this);
        view.setMinimumHeight(0);
        view.setMinimumWidth(0);
        return view;
    }
}
