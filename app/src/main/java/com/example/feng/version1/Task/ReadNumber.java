package com.example.feng.version1.Task;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.Public.PublicData;
import com.example.feng.version1.R;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

public class ReadNumber extends AppCompatActivity implements View.OnClickListener{
    private String device,tab;
    private String meterid;
    private Button speechr;
    private Button textin;
    private EditText editxt;
    private TextView tabTv,devTv,txtTv;
    /**辅助变量**/
    private String d,m,result_speech,result_edit;

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

        //初始化SDK
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5c7b8620");
        findview();
        //设置文本框内容
        devTv.setText(device);
        tabTv.setText(tab);
    }
    private void findview(){
        devTv = findViewById(R.id.device_read);
        tabTv = findViewById(R.id.tab_read);

        speechr=(Button)findViewById(R.id.yuyin);
        speechr.setOnClickListener(this);

        textin=(Button)findViewById(R.id.textinput);
        textin.setOnClickListener(this);

        editxt=(EditText)findViewById(R.id.edit_nn);
        txtTv=(TextView)findViewById(R.id.resultnum);

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
                if(null != result_edit){
                    Toast.makeText(ReadNumber.this,result_edit,Toast.LENGTH_SHORT).show();
                }else if( null != result_speech ){
                    Toast.makeText(ReadNumber.this,result_speech,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
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
