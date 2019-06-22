package com.zhiyi.onepay;

import com.zhiyi.onepay.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2019/6/21.
 */

public abstract class HttpJsonResponse implements IHttpResponse {
    @Override
    public void OnHttpData(String data) {
        if (data.charAt(0) == '{' || data.charAt(0) == '[') {
            try {
                JSONObject jdata = new JSONObject(data);
                if (jdata != null) {
                    if(jdata.has("code")){
                        int code = jdata.getInt("code");
                        if (code > 0) {
                            String msg = jdata.has("msg")?jdata.getString("msg"):"未提供消息属性:msg";
                            onError(code,msg);
                        } else {
                            onJsonResponse(jdata.getJSONObject("data"));
                        }
                    }else{
                        LogUtil.e("json not code "+data);
                    }
                } else {
                    LogUtil.e("is not json " + data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onError(int code,String msg){
        LogUtil.e("json response error,code:"+code+",msg:"+msg);
    }

    protected abstract void onJsonResponse(JSONObject data);

    @Override
    public void OnHttpDataError(IOException e) {
        LogUtil.e(e.getMessage());
    }
}
