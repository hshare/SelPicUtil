package com.hucare.hshare.selpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hushare.hucare.croppicutils.BottomPopWindow;
import com.hushare.hucare.croppicutils.CropPicUtils;
import com.hushare.hucare.croppicutils.GetPicHelper;
import com.hushare.hucare.croppicutils.GetPicUtil;

public class MainActivity2 extends AppCompatActivity {

    GetPicHelper getPicHelper;
    GetPicUtil getPicUtil;
    private BottomPopWindow bottomPopWindow;
    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        getPicHelper = new GetPicHelper(this);
        getPicUtil = getPicHelper.getPicUtil();
        getPicUtil.setIsCompress(true).setIsCrop(false);
        getPicUtil.setOnCompressResult(new GetPicUtil.onCompressResult() {
            @Override
            public void onSuccess(String filePath) {
                if (TextUtils.isEmpty(filePath)) {
                    return;
                }
                if ("iv1".equals(getPicUtil.getImageTag())) {
                    Glide.with(MainActivity2.this)
                            .load(filePath)
                            .thumbnail(0.2f)
                            .into(iv1);
                } else {
                    Glide.with(MainActivity2.this)
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
                getPicUtil.doHandlerPhoto(CropPicUtils.PIC_FROM_LOCALPHOTO);
            }

            @Override
            public void secondClick() {
                getPicUtil.doHandlerPhoto(CropPicUtils.PIC_FROM_CAMERA);
            }
        });
        bottomPopWindow.initTitleWindow("相册", "拍照");
    }

    public void onHuClick1(View v) {
        bottomPopWindow.showTitlePop(v);
        getPicUtil.setImageTag("iv1");
    }

    public void onHuClick2(View v) {
        bottomPopWindow.showTitlePop(v);
        getPicUtil.setImageTag("iv2");
    }

}
