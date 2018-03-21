package com.hushare.hucare.croppicutils;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 使用Fragment处理运行时权限和
 *
 * @author huzeliang
 */
public class MmpFragment extends Fragment {

    /**
     * 选择图片工具类
     */
    private ICallback iCallback;

    /**
     * 构造
     */
    public MmpFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public ICallback getiCallbackWrapper() {
        if (iCallback == null) {
            iCallback = new CallBackWrapper();
        }
        return iCallback;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        iCallback.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iCallback.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iCallback != null) {
            iCallback.onDestroy();
        }
    }
}
