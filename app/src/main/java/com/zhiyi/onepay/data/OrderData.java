package com.zhiyi.onepay.data;

import android.content.Intent;

import com.zhiyi.onepay.AppConst;
import com.zhiyi.onepay.util.AppUtil;


public class OrderData extends OrderDataBase {


    public String username;

    public int dianYuan;

    public OrderData(Intent intent){
        final boolean dianYuan = intent.getBooleanExtra("dianYuan",false);
        this.payType = intent.getStringExtra("type");
        this.username = intent.getStringExtra("username");
        this.money = intent.getStringExtra("money");
        this.dianYuan = dianYuan?1:0;
    }

    public boolean isPost(){
        return false;
    }

    public String getApiUrl(){
        return AppConst.NoticeUrl + "person/notify/pay?version="+AppConst.version+"&"+getOrderData();
    }

    public String getOrderData(){
        String app_id = "" + AppConst.NoticeAppId;
        this.sign = AppUtil.toMD5(app_id + AppConst.NoticeSecret + time + AppConst.version + rndStr + payType + money + username);
        return "type=" + payType
                + "&money=" + money
                + "&uname=" + username
                + "&appid=" + AppConst.NoticeAppId
                + "&rndstr=" + rndStr
                + "&sign=" + sign
                + "&time=" + time
                + "&dianyuan=" + dianYuan;
    }


    @Override
    public String getLogString() {
        return "type="+payType+",money="+money+",user="+username;
    }

}
