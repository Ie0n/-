package com.example.feng.version1.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Task.MainActivity;
import com.example.feng.version1.Viemitem.Bottom;
import com.yzq.testzxing.zxing.android.CaptureActivity;

import static android.app.Activity.RESULT_OK;
import static com.example.feng.version1.Public.PublicData.content;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView userid;
    private LinearLayout device1;
    private LinearLayout device2;
    private LinearLayout device3;
    private Button upload;
    private Bottom bottom;

    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

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
        device1 = view.findViewById(R.id.linear1);
        device1.setOnClickListener(this);
        device2 = view.findViewById(R.id.linear2);
        device2.setOnClickListener(this);
        device3 = view.findViewById(R.id.linear3);
        device3.setOnClickListener(this);
        upload=view.findViewById(R.id.uploading);
        upload.setOnClickListener(this);
        //底部标签栏
//        bottom = view.findViewById(R.id.b1);
//        bottom.activity_from = mContext;
//        bottom.task.setBackgroundColor(Color.parseColor("#50AAAAAA"));
//
//        /*********注册广播（监听服务器是否连接成功）*********/
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(PublicData.CONNECTSUCCESS);
//        filter.addAction(PublicData.CONNECTDEFAULT);
//        registerReceiver(mBroadcastReceiver,filter);
//
//        setupService();//启动服务
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                Toast.makeText(mContext,content,Toast.LENGTH_SHORT).show();
                if (PublicData.devitemchoice != null){
                    //查询SQLite数据库
//                    querydeviceid(content);
                }

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
            case R.id.linear1:
                PublicData.devitemchoice = PublicData.device1;
                Intent intent = new Intent(mContext,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.linear2:

                break;

            case R.id.linear3:

                break;
            case R.id.uploading:
                Toast.makeText(mContext, "完成情况是...", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
