package com.hucare.hshare.selpic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hushare.hucare.croppicutils.BottomPopWindow;
import com.hushare.hucare.croppicutils.CropPicUtils;

public class MainActivity1 extends AppCompatActivity {

    private CropPicUtils cropPicUtils;
    private BottomPopWindow bottomPopWindow;
    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        cropPicUtils = new CropPicUtils(this, null);
        bottomPopWindow = new BottomPopWindow(this, new BottomPopWindow.PopClick() {
            @Override
            public void firstClick() {
                cropPicUtils.doHandlerPhoto(CropPicUtils.PIC_FROM_LOCALPHOTO);
            }

            @Override
            public void secondClick() {
                cropPicUtils.doHandlerPhoto(CropPicUtils.PIC_FROM_CAMERA);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = cropPicUtils.onActivityResult(requestCode, resultCode, data);
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if ("iv1".equals(cropPicUtils.getImageTag())) {
            Glide.with(this)
                    .load("file://" + path)
                    .thumbnail(0.2f)
                    .into(iv1);
        } else {
            Glide.with(this)
                    .load("file://" + path)
                    .thumbnail(0.2f)
                    .into(iv2);
        }

    }
}
