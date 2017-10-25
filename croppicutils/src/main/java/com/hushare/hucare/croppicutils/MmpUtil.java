package com.hushare.hucare.croppicutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 功能/模块 ：工具类
 *
 * @author huzeliang
 * @version 1.0 2017-10-25 14:55:52
 * @see ***
 * @since ***
 */
public class MmpUtil {

    /**
     * 是否是DEBUG模式
     *
     * @param context 上下文
     * @return 是否是DEBUG模式
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }


}
