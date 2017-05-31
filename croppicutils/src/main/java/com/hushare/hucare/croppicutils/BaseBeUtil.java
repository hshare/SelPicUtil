package com.hushare.hucare.croppicutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * 功能/模块 ：工具类的基础类
 *
 * @author huzeliang
 * @version 1.0 2017/3/1
 * @see ***
 * @since ***
 */
public class BaseBeUtil {

    /**
     * 是否是debug模式，不是就不输出log
     */
    private boolean isDebug = false;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 日志的tag
     */
    private String logTag = "huzeliang";

    BaseBeUtil(Context context, String logTag) {
        this.context = context;
        isDebug = isApkDebugable();
        this.logTag = logTag;
    }

    /**
     * 是否是debug模式
     *
     * @return 是否是debug模式
     */
    private boolean isApkDebugable() {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 日志工具
     *
     * @param logString 打印的数据
     */
    protected void iLog(String logString) {
        if (isDebug) {
            Log.i(logTag, "" + logString);
        }
    }
}
