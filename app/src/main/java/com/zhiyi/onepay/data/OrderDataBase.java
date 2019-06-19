package com.zhiyi.onepay.data;

import com.zhiyi.onepay.AppConst;
import com.zhiyi.onepay.util.AppUtil;
import com.zhiyi.onepay.util.RequestData;

/**
 * Created by Administrator on 2019/5/6.
 */

public abstract class OrderDataBase {

    protected String payType;
    protected String name;
    protected String money;
    protected String sign;
    protected String rndStr;
    protected long time;

    OrderDataBase(){
        time = System.currentTimeMillis()/1000 - AppConst.DetaTime;
        rndStr = AppUtil.randString(16);
    }

    public abstract boolean isPost();

    public abstract String getApiUrl();

    public abstract RequestData getOrderData();

    public abstract String getLogString();
}
