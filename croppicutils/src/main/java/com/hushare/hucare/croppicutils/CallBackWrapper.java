package com.hushare.hucare.croppicutils;


import android.content.Intent;

public class CallBackWrapper implements ICallback {

    ICallback iCallback;

    protected void attachCallBackWrapper(ICallback base) {
        if (iCallback != null) {
            throw new IllegalStateException("iCallback already set");
        }
        iCallback = base;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (iCallback == null) {
            return;
        }
        iCallback.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (iCallback == null) {
            return;
        }
        iCallback.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (iCallback == null) {
            return;
        }
        iCallback.onDestroy();
    }
}
