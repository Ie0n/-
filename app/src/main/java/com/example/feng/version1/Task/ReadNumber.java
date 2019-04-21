package com.example.feng.version1.Task;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.MessageEvent;
import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.example.feng.version1.Util.Utils;
import com.example.feng.version1.bean.StatusResponse;
import com.example.feng.version1.bean.User;
import com.example.feng.version1.http.HttpRequest;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReadNumber extends AppCompatActivity implements View.OnClickListener, Callback {
    private String device, tab;
    private String meterid;
    private Button speechr;
    private Button textin;
    private EditText editxt;
    private TextView tabTv, devTv, txtTv;
    /**
     * 辅助变量
     **/
    private String d, m, result_speech, result_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_number);
        /******
         * 设置状态栏透明
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        Intent intent_task = getIntent();
        device = intent_task.getStringExtra("device");
        tab = intent_task.getStringExtra("tab");
        meterid = intent_task.getStringExtra("meterid");
        //初始化SDK
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5c7b8620");
        findview();
        //设置文本框内容
        devTv.setText(device);
        tabTv.setText(tab);
    }

    private void findview() {
        devTv = findViewById(R.id.device_read);
        tabTv = findViewById(R.id.tab_read);

        speechr = (Button) findViewById(R.id.yuyin);
        speechr.setOnClickListener(this);

        textin = (Button) findViewById(R.id.textinput);
        textin.setOnClickListener(this);

        editxt = (EditText) findViewById(R.id.edit_nn);
        txtTv = (TextView) findViewById(R.id.resultnum);

    }

    public void initSpeech(final Context context) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    //返回的result为识别后的汉字,直接赋值到TextView上即可
                    result_speech = parseVoice(recognizerResult.getResultString());
                    //设置文本框内容
                    txtTv.setText(result_speech);
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yuyin:
                initSpeech(this);
                break;
            case R.id.textinput:
                /**在这里写上传数据逻辑**/
                result_edit = editxt.getText().toString();
                String result = result_edit;
                if (result_speech == null && result_edit.equals("")) {
                    Toast.makeText(this, "请语音或手动录入参数", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(result)) {
                    result = result_speech;
                }
                if (result_speech != null && !result_edit.equals("")) {
                    result = result_edit;
                }
                try {
                    updateResult(result);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "请输入数字", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    private void updateResult(String result) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String entry = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        RequestBody body = new FormBody.Builder()
                .add("userNo", String.valueOf(User.getInstance().getuserNo()))
                .add("meterId", String.valueOf(meterid))
                .add("data", result)
                .add("entryTime", entry)
                .build();
        String url = PublicData.DOMAIN + "/api/user/entryData";
        HttpRequest.getInstance().post(url, body, this, PublicData.getCookie(this));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Utils.ToastTextThread(this, e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            String body = PublicData.clearChar(response.body().string());
            Log.d("res-", body);
            Gson gson = new Gson();
            StatusResponse r = gson.fromJson(body, StatusResponse.class);
            if (r.getStatus() == 1200) {
                Utils.ToastTextThread(this, r.getStatusinfo().getMessage());
                EventBus.getDefault().post(new MessageEvent());
                ReadNumber.this.finish();
            } else {
                Utils.ToastTextThread(this, r.getStatusinfo().getMessage());
            }
        } else {

        }
    }


    /**
     * 语音对象封装
     */
    public class Voice {

        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }

}
