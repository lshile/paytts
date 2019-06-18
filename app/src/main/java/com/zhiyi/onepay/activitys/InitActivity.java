package com.zhiyi.onepay.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyi.onepay.AppConst;
import com.zhiyi.onepay.IHttpResponse;
import com.zhiyi.onepay.MainActivity;
import com.zhiyi.onepay.R;
import com.zhiyi.onepay.util.AppUtil;
import com.zhiyi.onepay.util.DBManager;
import com.zhiyi.onepay.util.RequestData;
import com.zhiyi.onepay.util.RequestUtils;
import com.zhiyi.onepay.util.StringUtils;
import com.zhiyi.onepay.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class InitActivity extends AppCompatActivity {
    private DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbm = new DBManager(this);
        String url = dbm.getConfig(AppConst.KeyUKFNoticeUrl);
        if(!StringUtils.isEmpty(url)){
            EditText editText = findViewById(R.id.edit_apiurl);
            editText.setText(url);
        }
        Button btn_ok = findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitActivity.this.onOkClick();
            }
        });
        Button btn_setting = findViewById(R.id.button_custom);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitActivity.this.toCustomSetting();
            }
        });
    }

    private void onOkClick(){
        EditText etx = findViewById(R.id.edit_apiurl);
        String url = etx.getText().toString();
        if(StringUtils.isEmpty(url)){
            ToastUtil.show(this,"地址为空");
            return;
        }
        AppConst.NoticeUrl = url;
        dbm.setConfig(AppConst.KeyUKFNoticeUrl,url);
        String uniqueId = AppUtil.getUniqueId(this);
        RequestData post = RequestData.newInstance(AppConst.NetTypeLogin);
        try {
            post.put("device_id",uniqueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtils.post(url, post, new IHttpResponse() {
            @Override
            public void OnHttpData(String data) {
                sendBroadcast(new Intent(InitActivity.this,MainActivity.class));
                InitActivity.this.finish();
            }

            @Override
            public void OnHttpDataError(IOException e) {

            }
        });

    }

    private void toCustomSetting(){
        sendBroadcast(new Intent(this,SettingActivity.class));
        this.finish();
    }

}
