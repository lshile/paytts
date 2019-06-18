package com.zhiyi.onepay.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2019/6/18.
 */

public class RequestData extends JSONObject{

    public static RequestData newInstance(String type){
        return new RequestData(type);
    }
    private RequestData(String type){
        try {
            this.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
