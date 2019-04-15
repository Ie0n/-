package com.example.feng.version1.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.feng.version1.LoginActivity;
import com.example.feng.version1.R;
import com.example.feng.version1.UserActivity;
import com.example.feng.version1.bean.User;

public class MyFragment extends Fragment implements View.OnClickListener {

    private Button logout,manager;
    private Context mContext;
    private User user;
    private TextView user_name_text,user_id_text;

    public static MyFragment newInstance() {

        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        user = User.getInstance();
        initView(view);
        //拿到user
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initView(View view){
        logout = view.findViewById(R.id.logout);
        manager = view.findViewById(R.id.manager);
        user_name_text = view.findViewById(R.id.text_user_name);
        user_id_text = view.findViewById(R.id.text_user_id);
        logout.setOnClickListener(this);
        manager.setOnClickListener(this);
        if (user.getAdmin() == 1){
            manager.setVisibility(View.VISIBLE);
        }
        user_id_text.setText(String.valueOf(user.getuserNo()));
        user_name_text.setText(user.getUserName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                getActivity().finish();
                startActivity(new Intent(mContext,LoginActivity.class));
                break;
            case R.id.manager:
                startActivity(new Intent(mContext,UserActivity.class));
                break;
        }
    }
}
