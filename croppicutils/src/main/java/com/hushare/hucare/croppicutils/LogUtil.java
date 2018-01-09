package com.hushare.hucare.croppicutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * 打印LOG
 * @author huzeliang
 */
public class LogUtil {


    /**
     * 是否是debug模式
     */
    private static boolean isDebug = false;
    /**
     * logTag
     */
    private static String logTag = "hucare";

    /**
     * 设置log的Tag
     *
     * @param context 上下文
     * @param tag     tag
     */
    public static void setLogUtil(Context context, String tag) {
        isDebug = MmpUtil.isApkDebugable(context);
        logTag = tag;
    }

    /**
     * 打印log
     *
     * @param logO log内容
     */
    public static void iLog(Object logO) {
        if (isDebug) {
            Log.i(logTag, "" + logO);
        }
    }
}
