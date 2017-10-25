package com.hushare.hucare.croppicutils;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.NonNull;

/**
 * 功能/模块 ：***
 *
 * @author huzeliang
 * @version 1.0 ${date} ${time}
 * @see ***
 * @since ***
 */
public class GetPicHelper {

    static final String TAG = "mmpGetPic";
    private MmpFragment mmpFragment;


    public GetPicHelper(@NonNull Activity activity) {
        mmpFragment = getFragment(activity);
        LogUtil.setLogUtil(activity, "huzeliang");
    }

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

    private MmpFragment findFragment(Activity activity) {
        return (MmpFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public GetPicUtil getPicUtil() {
        return mmpFragment.getPicUtil();
    }
}
