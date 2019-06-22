package com.zhiyi.onepay.util;

import android.util.Log;

import com.zhiyi.onepay.AppConst;

import java.text.DateFormat;
import java.util.Date;


public class LogUtil {
    private static LogUtil instance = new LogUtil();
    private String[] logArr;
    private int index;
    private static DateFormat format = DateFormat.getDateTimeInstance();
    public static void e(String msg){
        Log.e(AppConst.TAG_LOG,msg);
        instance.appendLog(msg);
    }

    public static void e(String msg,Throwable e){
        Log.e(AppConst.TAG_LOG ,msg,e);
        instance.appendLog(msg);
    }

    public static void i(String msg){
        Log.i(AppConst.TAG_LOG,msg);
        instance.appendLog(msg);
    }

    public static void i(String msg,Throwable e){
        Log.i(AppConst.TAG_LOG ,msg,e);
        instance.appendLog(msg);
    }

    private LogUtil(){
        logArr = new String[512];
        index = 0;

    }

    private void appendLog(String msg){
        String time = format.format(new Date());
        logArr[index] = time +":"+msg+"\n";
        index = (index+1)%512;
    }

    public static String getLog(){
        return instance.readLog();
    }
    private String readLog(){
        StringBuffer sb = new StringBuffer(10240);
        for(int i=0;i<512;i++){
            int  idx = index-i;
            if(idx<0){
                idx += 512;
            }
            sb.append(logArr[idx]);
        }
        return sb.toString();
    }

}
