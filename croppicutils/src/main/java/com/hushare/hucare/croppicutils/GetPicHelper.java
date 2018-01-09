package com.hushare.hucare.croppicutils;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.NonNull;

/**
 * 获取图片工具类
 * @author huzeliang
 */
public class GetPicHelper {

    /**
     * fragment的tag
     */
    static final String TAG = "mmpGetPic";
    /**
     * MmpFragment
     */
    private MmpFragment mmpFragment;

    /**
     * 构造
     *
     * @param activity activity
     */
    public GetPicHelper(@NonNull Activity activity) {
        mmpFragment = getFragment(activity);
        LogUtil.setLogUtil(activity, "huzeliang");
    }

    /**
     * 获取Fragment
     *
     * @param activity activity
     * @return fragment
     */
    public MmpFragment getFragment(Activity activity) {
        MmpFragment fragment = findFragment(activity);
        if (fragment == null) {
            fragment = new MmpFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().add(fragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    /**
     * 获取Fragment
     *
     * @param activity avtivity
     * @return fragment
     */
    private MmpFragment findFragment(Activity activity) {
        return (MmpFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    /**
     * 获取GetPicUtil
     *
     * @return activity
     */
    public GetPicUtil getPicUtil1() {
        return mmpFragment.getPicUtil();
    }

    /**
     * 获取GetPicUtil
     *
     * @param activity activity
     * @return GetPicUtil
     */
    public static GetPicUtil getPicUtil(Activity activity) {
        return (new GetPicHelper(activity)).getPicUtil1();
    }
}
