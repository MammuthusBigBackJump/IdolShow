package com.nd.idolshow;

import android.support.v4.util.LogWriter;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/4.
 */
public class NdLog {
    private final static String TAG = "*************";
    private final static boolean isDebug = true;

    public static void debug(String str){
        if(isDebug)
            Log.d(TAG, str);
    }

    public static void info(String str){
        if(isDebug)
            Log.i(TAG, str);
    }

    public static void warn(String str){
        if(isDebug)
            Log.w(TAG, str);
    }

    public static void error(String str){
        if(isDebug)
            Log.e(TAG, str);
    }






}
