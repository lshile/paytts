package com.zhiyi.onepay;

import android.webkit.JavascriptInterface;

public final class JsInterface {

    @JavascriptInterface
    public String getToken(){
        return AppConst.Token;
    }
}
