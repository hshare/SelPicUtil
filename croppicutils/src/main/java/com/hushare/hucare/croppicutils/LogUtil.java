package com.hushare.hucare.croppicutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * 功能/模块 ：打印LOG
 *
 * @author huzeliang
 * @version 1.0 2017-10-25 14:54:58
 * @see ***
 * @since ***
 */
public class LogUtil {

    private static boolean isDebug = false;
    private static String logTag = "hucare";

    public static void setLogUtil(Context context, String tag) {
        isDebug = MmpUtil.isApkDebugable(context);
        logTag = tag;
    }

    public static void iLog(Object logO) {
        if (isDebug) {
            Log.i(logTag, "" + logO);
        }
    }
}
