package com.zhiyi.onepay.data;

import android.content.Intent;
import android.util.Log;

import com.zhiyi.onepay.AppConst;
import com.zhiyi.onepay.util.AppUtil;
import com.zhiyi.onepay.util.RequestData;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MapOrderData extends OrderDataBase{
    private HashMap<String, String> map;
    private RequestData data;
    public MapOrderData(Intent intent){
        super();
        Serializable data = intent.getSerializableExtra("data");
        map = (HashMap<String, String>) data;
        map.put("time",""+time);
        map.put("rndStr",rndStr);
        map.put("appid",""+AppConst.AppId);
        payType = map.get("paytype");
        name = map.get("name");
    }
    @Override
    public boolean isPost() {
        return true;
    }

    @Override
    public String getApiUrl() {
        return AppConst.NoticeUrl;
    }

    @Override
    public RequestData getOrderData() {
        if(data == null){
            data = RequestData.newInstance(AppConst.NetTypeNotify);
            StringBuilder sb = new StringBuilder();
            Set<String> keys = map.keySet();
            String[] list = new String[keys.size()];
            list = keys.toArray(list);
            Arrays.sort(list);
            try {
                for (String key:list) {
                    String value = map.get(key);
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                    sb.append("&");
                    data.put(key,value);
                }
                String tmp = sb.toString();
                String sign = AppUtil.toMD5(tmp+AppConst.Secret);
                data.put("sign",sign);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    @Override
    public String getLogString() {
        return "type="+payType+",money="+money;
    }
}
