package com.hucare.hshare.selpic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hushare.hucare.croppicutils.BottomPopWindow;
import com.hushare.hucare.croppicutils.GetPicUtil;

public class MainActivity1 extends AppCompatActivity {

    private GetPicUtil cropPicUtils;
    private BottomPopWindow bottomPopWindow;
    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        cropPicUtils = new GetPicUtil(this, null).setIsCompress(true).setIsCrop(false);
        cropPicUtils.setOnCompressResult(new GetPicUtil.onCompressResult() {
            @Override
            public void onSuccess(String filePath) {
                if (TextUtils.isEmpty(filePath)) {
                    return;
                }
                if ("iv1".equals(cropPicUtils.getImageTag())) {
                    Glide.with(MainActivity1.this)
                            .load(filePath)
                            .thumbnail(0.2f)
                            .into(iv1);
                } else {
                    Glide.with(MainActivity1.this)
                            .load(filePath)
                            .thumbnail(0.2f)
                            .into(iv2);
                }
            }

            @Override
            public void onFailed(String errorMsg) {

            }
        });
        bottomPopWindow = new BottomPopWindow(this, new BottomPopWindow.PopClick() {
            @Override
            public void firstClick() {
                cropPicUtils.doHandlerPhotoFromLocalPhoto();
            }

            @Override
            public void secondClick() {
                cropPicUtils.doHandlerPhotoFromCamera();
            }
        });
        bottomPopWindow.initTitleWindow("相册", "拍照");
    }

    public void onHuClick1(View v) {
        bottomPopWindow.showTitlePop(v);
        cropPicUtils.setImageTag("iv1");
    }

    public void onHuClick2(View v) {
        bottomPopWindow.showTitlePop(v);
        cropPicUtils.setImageTag("iv2");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        cropPicUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cropPicUtils.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cropPicUtils.onActivityResult(requestCode, resultCode, data);
    }
}
