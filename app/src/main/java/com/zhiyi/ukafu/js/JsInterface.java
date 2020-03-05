package com.zhiyi.ukafu.js;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.zhiyi.ukafu.AppConst;
import com.zhiyi.ukafu.activitys.WebViewActivity;
import com.zhiyi.ukafu.util.AppUtil;
import com.zhiyi.ukafu.util.DBManager;
import com.zhiyi.ukafu.util.LogUtil;
import com.zhiyi.ukafu.util.SystemProgramUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.Util;

public final class JsInterface {

    private WebView webView;
    private WebViewActivity activity;
    DBManager dbManager;
    private static JsInterface instance;
    public JsInterface(WebView webview, WebViewActivity activity) {
        this.webView = webview;
        this.activity = activity;
        callback = "alert";
        dbManager = new DBManager(this.activity);
        instance = this;
    }

    public static JsInterface getInstance(){
        return instance;
    }

    @JavascriptInterface
    public String getToken() {
        return AppConst.AppId + "-" + AppConst.Token;
    }

    @JavascriptInterface
    public boolean isBoot() {
        return false;
    }

    @JavascriptInterface
    public void setBoot(boolean a) {

    }


    @JavascriptInterface
    public void appendLog(int level, String log) {
        if (level > 1) {
            LogUtil.e("js log=>" + log);
        } else {
            LogUtil.i("js log=>" + log);
        }
    }

    @JavascriptInterface
    public void setCookie(String cookie) {
        AppConst.Cookie = cookie;
    }

    @JavascriptInterface
    public String getConfig(String name) {
        return dbManager.getConfig(name);
    }

    public void startNotifyMonitor() {
        if (!AppUtil.isNotifycationEnabled(this.activity)) {
            AppUtil.openNotificationListenSettings(this.activity);
        }
        activity.openService();
    }

    @JavascriptInterface
    public boolean setConfig(String name, String value) {
        LogUtil.i("set config " + name + "=" + value);
        boolean bvalue = "true".equals(value.toLowerCase());
        if ("auto".equals(name)) {
            if (bvalue) {
                this.startNotifyMonitor();
            } else {
                activity.stopService();
            }
        }
        if (AppConst.KeyUKFNoticeUrl.equals(name)) {
            AppConst.NoticeUrl = value;
            AppConst.inited = true;
        } else if (AppConst.KeyUKFNoticeSecret.equals(name)) {
            AppConst.Secret = value;
        } else if (AppConst.KeyUKFVoice.equals(name)) {
            AppConst.checkNewOrder = bvalue;
        }
        dbManager.setConfig(name, value);
        return bvalue;
    }


    private String callback;

    @JavascriptInterface
    public void selectQrcode(String callback) {
        this.callback = callback;
        SystemProgramUtils.zhaopian(activity);

    }


    public void onQrcodeload(Intent intent) {
        if (callback != null) {
            JSONObject jsonObject = new JSONObject();
            for (String key : intent.getExtras().keySet()) {
                try {
                    jsonObject.put(key, intent.getExtras().get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callWebJs(callback, jsonObject);
        }
    }

    public void callWebJs(String funcName, JSONObject data) {
        callWebJs(funcName, data.toString());
    }

    public void callWebJs(String funcName, String data) {
        String jsCallback = "javascript:" + funcName + "(" + data + ")";
        LogUtil.i("call js interface:" + jsCallback);
        webView.loadUrl(jsCallback);
    }
}
