package com.example.feng.version1.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.AddEquipmentActivity;
import com.example.feng.version1.R;
import com.example.feng.version1.Task.SelectTabActivity;
import com.yzq.testzxing.zxing.android.CaptureActivity;

import static android.app.Activity.RESULT_OK;
import static com.example.feng.version1.Public.PublicData.content;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView userid;
    private RelativeLayout add;
    private RelativeLayout input;

    private static final String TAG = "-dd";

    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_ADD = 0x0000;
    private static final int REQUEST_CODE_INPUT = 0x0001;

    public static TaskFragment newInstance() {

        Bundle args = new Bundle();

        TaskFragment fragment = new TaskFragment();
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
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        initView(view);

        return view;
    }
    private void initView(View view){
        add = view.findViewById(R.id.r1);
        add.setOnClickListener(this);
        input = view.findViewById(R.id.r2);
        input.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        //录入信息
        if (requestCode == REQUEST_CODE_INPUT && resultCode == RESULT_OK) {
            if (data != null) {
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                String name = "aa";
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                Toast.makeText(mContext,content,Toast.LENGTH_SHORT).show();
                Log.d(TAG,content);
                if (content.equals("004020000000000000269621")){
                    Intent intent_dev1 = new Intent();
                    intent_dev1.putExtra("device",name);
                    intent_dev1.setClass(mContext,SelectTabActivity.class);// 制定传递对象
                    startActivity(intent_dev1);
                }

            }
        }
        //添加设备
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK){
            if (data != null){
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                startActivity(new Intent(mContext,AddEquipmentActivity.class));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 按下设备键扫码，根据url查询得到设备号
             * 若与publicdata中设备号一致，则进入MeterNum.class
             * */
            case R.id.r1:
                Intent intent = new Intent(mContext,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INPUT);
                break;

            case R.id.r2:
                Intent intent2 = new Intent(mContext,
                        CaptureActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_ADD);
                break;
            default:
                break;
        }
    }
}
