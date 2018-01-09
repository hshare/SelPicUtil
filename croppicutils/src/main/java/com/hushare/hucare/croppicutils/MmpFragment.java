package com.hushare.hucare.croppicutils;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 使用Fragment处理运行时权限和
 * @author huzeliang
 */
public class MmpFragment extends Fragment {

    /**
     * 选择图片工具类
     */
    private GetPicUtil getPicUtil;

    /**
     * 构造
     */
    public MmpFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getPicUtil();
    }

    public GetPicUtil getPicUtil() {
        if (getPicUtil == null){
            getPicUtil = new GetPicUtil(getActivity(), this);
        }
        return getPicUtil;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getPicUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPicUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPicUtil != null) {
            getPicUtil.onDestroy();
        }
    }
}
