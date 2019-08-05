package com.zhiyi.onepay.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.bugly.Bugly;
import com.zhiyi.onepay.AppConst;
import com.zhiyi.onepay.BuildConst;
import com.zhiyi.onepay.HttpJsonResponse;
import com.zhiyi.onepay.MainActivity;
import com.zhiyi.onepay.R;
import com.zhiyi.onepay.data.H5AppData;
import com.zhiyi.onepay.util.AppUtil;
import com.zhiyi.onepay.util.DBManager;
import com.zhiyi.onepay.util.LogUtil;
import com.zhiyi.onepay.util.RequestData;
import com.zhiyi.onepay.util.RequestUtils;
import com.zhiyi.onepay.util.StringUtils;
import com.zhiyi.onepay.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitActivity extends AppCompatActivity {
    private DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitNoticeParam();
        //"http://faka.ukafu.com/notify_api";
        String url = dbm.getConfig(AppConst.KeyUKFNoticeUrl);
        if(!StringUtils.isEmpty(url)){
            EditText editText = findViewById(R.id.edit_apiurl);
            editText.setText(url);
        }
        String appid = ""+AppConst.AppId;
        if(!StringUtils.isEmpty(appid)){
            EditText editText = findViewById(R.id.edit_appid);
            editText.setText(appid);
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
        Bugly.init(getApplicationContext(), BuildConst.BuglyId, false);
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
        String appid = dbm.getConfig(AppConst.KeyUKFNoticeAppId);
        String token = dbm.getConfig(AppConst.KeyUKFNoticeToken);
        RequestData post = RequestData.newInstance(AppConst.NetTypeLogin);
        try {
            post.put("device_id",uniqueId);
            if(!StringUtils.isEmpty(appid)){
                post.put(AppConst.KeyAppId,appid);
                post.put(AppConst.KeyToken,token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppConst.NoticeUrl = url;
        RequestUtils.post(url, post, new HttpJsonResponse() {
            protected void onJsonResponse(JSONObject jsonObject) {
                try {
                    if(jsonObject.has(AppConst.KeyAppId)){
                        AppConst.AppId = jsonObject.getInt(AppConst.KeyAppId);
                        dbm.setConfig(AppConst.KeyUKFNoticeAppId,""+AppConst.AppId);
                        LogUtil.e("appid:"+AppConst.AppId);
                    }
                    if(jsonObject.has(AppConst.KeyToken)){
                        AppConst.Token = jsonObject.getString(AppConst.KeyToken);
                        dbm.setConfig(AppConst.KeyUKFNoticeToken,AppConst.Token);
                    }
                    if(jsonObject.has(AppConst.KeySecret)){
                        AppConst.Secret = jsonObject.getString(AppConst.KeySecret);
                        dbm.setConfig(AppConst.KeyUKFNoticeSecret,AppConst.Secret);
                    }
                    //这里添加需要的APP列表
                    if(jsonObject.has(AppConst.KeyWebAPP)){
                        JSONArray array = jsonObject.getJSONArray(AppConst.KeyWebAPP);
                        for(int i=0;i<array.length();i++){
                            JSONObject app = array.getJSONObject(i);
                            AppConst.h5Apps.add(new H5AppData(app));
                        }
                    }
                } catch (JSONException e) {
                    LogUtil.e("login error",e);
                }
                startActivity(new Intent(InitActivity.this,MainActivity.class));
                InitActivity.this.finish();
            }
        });

    }

    private void InitNoticeParam() {
        dbm = new DBManager(this);
        AppConst.InitParams(dbm);
    }

    private void toCustomSetting(){
        startActivity(new Intent(this,SettingActivity.class));
        this.finish();
    }

}
