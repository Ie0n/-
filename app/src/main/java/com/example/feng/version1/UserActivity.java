package com.example.feng.version1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feng.version1.adapter.UserAdapter;
import com.example.feng.version1.bean.User;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private Button edit,add;
    private Context mContext;
    private List<User> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mContext = this;
        initView();
    }

    private void initView(){
        recyclerView = findViewById(R.id.rv_user);
        add = findViewById(R.id.btn_add_user);
        edit = findViewById(R.id.btn_edit_user_info);
        add.setOnClickListener(this);
        edit.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        userList = new ArrayList<>();
        initData();
        adapter = new UserAdapter(mContext,userList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemListener(new UserAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(mContext,EditUserInfoActivity.class);

                startActivity(intent);
            }
        });
    }

    private void getData(){

    }

    private void initData(){
        userList.add(new User("张三",112441,"5465454"));
        userList.add(new User("张三",112441,"5465454"));
        userList.add(new User("张三",112441,"5465454"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_user:

                    userList.add(new User("张三",112441,"5465454"));
                    adapter.notifyDataSetChanged();

                break;

            default:
                break;
        }
    }

}
