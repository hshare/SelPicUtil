package com.hushare.hucare.croppicutils;


import android.content.Intent;

public interface ICallback {
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onDestroy();
}
