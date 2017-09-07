package com.hushare.hucare.croppicutils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 功能/模块 ：从相册或者拍照获取照片
 * 支持任意比例裁剪
 * 支持压缩，指定大小
 *
 * @author huzeliang
 * @version 1.1 2017/9/7对Android7.0进行了适配，并删除了bitmap操作
 * @see ***
 * @since ***
 */
public class CropPicUtils extends BaseBeUtil {

    /**
     * 在Fragment时，需要传入Fragment，你懂的
     */
    private Fragment fragment;
    /**
     * 上下文，不可少
     */
    private Context context;
    /**
     * 裁剪图片的比例x
     */
    private int ssX = 1;
    /**
     * 裁剪图片的比例y
     */
    private int ssY = 1;
    /**
     * 标志位，从相机获取图片
     */
    public static final int PIC_FROM_CAMERA = 0x1122;
    /**
     * 标志位，从相册获取图片
     */
    public static final int PIC_FROM_LOCALPHOTO = 0x1123;
    /**
     * 标志位，从裁剪获取图片
     */
    public static final int PIC_FROM_CAIJIAN = 0x1124;
    /**
     * 是否裁剪
     */
    private boolean isCrop = false;
    /**
     * 是否压缩
     */
    private boolean isCompress = false;
    /**
     * 忽略图片大小，小于这个大小不压缩
     */
    private int ignoreKB = 100;
    /**
     * 压缩的进度条
     */
    private ProgressDialog progressDialog;
    /**
     * 缓存路径
     */
    private static String STOREPATH = "/storage/emulated/0/Android/data/com.temp.temp/files/tempCache/";
    /**
     * 图片的tag，如果当前界面有多处需要获取图片，使用tag可以区分是哪里获取图片
     */
    private Object imageTag = "";
    /**
     * 图片回调借口
     */
    private onCompressResult onCompressResult;
    /**
     * 拍照的原始uri
     */
    private Uri cameraOutUri;

    /**
     * 获取图片的tag
     *
     * @return tag
     */
    public Object getImageTag() {
        return imageTag;
    }

    /**
     * 设置图片的tag
     *
     * @param imageTag 图片tag
     */
    public void setImageTag(Object imageTag) {
        this.imageTag = imageTag;
    }

    /**
     * @param context  上下文
     * @param fragment 如果不是fragment，就传null
     */
    public CropPicUtils(Context context, Fragment fragment) {
        super(context, "huzeliang");
        this.fragment = fragment;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("压缩图片中...");
        progressDialog.setMax(100);

        STOREPATH = context.getExternalFilesDir(null).getAbsolutePath() + "/tempCache/";

        try {
            File file2 = new File(STOREPATH);
            if (!file2.exists()) {
                file2.mkdir();
            }
        } catch (Exception e) {
        }

        super.iLog("初始化StorePath：" + STOREPATH);
    }

    /**
     * 设置是否压缩
     *
     * @param compress 是否压缩
     * @return 你懂的
     */
    public CropPicUtils setIsCompress(boolean compress) {
        isCompress = compress;
        return this;
    }

    /**
     * 设置最低图片大小
     *
     * @param ignoreKB 图片大小，单位kb
     * @return 你懂的
     */
    public CropPicUtils setIgnoreKB(int ignoreKB) {
        this.ignoreKB = ignoreKB;
        return this;
    }

    /**
     * 回调接口，不懂我也没办法啊
     */
    public interface onCompressResult {

        void onSuccess(String filePath);

        void onFailed(String errorMsg);
    }

    /**
     * 设置回调接口
     *
     * @param onCompressResult 实现的回调接口
     */
    public void setOnCompressResult(CropPicUtils.onCompressResult onCompressResult) {
        this.onCompressResult = onCompressResult;
    }

    /**
     * 设置裁剪图片的比例
     *
     * @param sX 比例x
     * @return 你懂的
     */
    public CropPicUtils setSsx(int sX) {
        this.ssX = sX;
        return this;
    }

    /**
     * 设置裁剪图片的比例
     *
     * @param sY 比例
     * @return 你懂的
     */
    public CropPicUtils setSsy(int sY) {
        this.ssY = sY;
        return this;
    }

    /**
     * 设置是否裁剪
     *
     * @param isCrop 是否裁剪
     * @return 你懂的
     */
    public CropPicUtils setIsCrop(boolean isCrop) {
        this.isCrop = isCrop;
        return this;
    }

    /**
     * 裁剪工具
     *
     * @param normalUri
     */
    private void cropUtil(Uri normalUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri ttempUri = null;
        Uri tempUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
//            tempUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(getFilePathBaseOnTime("intent.jpg")));
            ttempUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(getRealFilePath(context, normalUri)));
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
//            tempUri = Uri.fromFile(new File(getFilePathBaseOnTime("intent.jpg")));
            ttempUri = normalUri;
        }
        //照片 截取输出的outputUri，只能使用 Uri.fromFile，不能用FileProvider.getUriForFile
        tempUri = Uri.fromFile(new File(getFilePathBaseOnTime("intent.jpg")));
        intent.setDataAndType(ttempUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", ssX);
        intent.putExtra("aspectY", ssY);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        if (fragment != null) {
            fragment.startActivityForResult(intent, PIC_FROM_CAIJIAN);
        } else {
            ((Activity) context).startActivityForResult(intent, PIC_FROM_CAIJIAN);
        }
    }

    /**
     * 图片处理逻辑
     *
     * @param type 选择类型，是从相册还是相机还是裁剪
     */
    public void doHandlerPhoto(int type) {
        try {
            if (type == PIC_FROM_LOCALPHOTO) {
                super.iLog("doHandlerPhoto::PIC_FROM_LOCALPHOTO");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (Build.VERSION.SDK_INT >= 24) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                if (fragment != null) {
                    fragment.startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
                } else {
                    ((Activity) context).startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
                }
            } else {
                super.iLog("doHandlerPhoto::PIC_FROM_CAMERA");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Uri temp = null;
                    File file = new File(getFilePathBaseOnTime(".jpg"));
                    cameraOutUri = Uri.fromFile(file);
                    if (Build.VERSION.SDK_INT >= 24) {
                        temp = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    } else {
                        temp = Uri.fromFile(file);
                    }

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, temp);
                    cameraIntent.putExtra("return-data", true);
                    if (fragment != null) {
                        fragment.startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
                    } else {
                        ((Activity) context).startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
                    }

                } else {
                    super.iLog("doHandlerPhoto::无SD卡");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            super.iLog("doHandlerPhoto::处理图片出现错误");
        }
    }

    /**
     * 需要在Activity或者Fragment中的onActivityResult调用此方法，获取图片路径
     *
     * @param requestCode 你懂的
     * @param resultCode  你懂的
     * @param data        你懂的
     * @return 图片路径
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            super.iLog("onActivityResult::resultCode != Activity.RESULT_OK");
        }
        String path = "";
        if (isCrop) {
            super.iLog("onActivityResult::isCrop == true");
            switch (requestCode) {
                case PIC_FROM_CAMERA:
                    cropUtil(cameraOutUri);
                    break;
                case PIC_FROM_CAIJIAN:
                    if (data != null) {
                        path = getRealFilePath(context, data.getData());
                        if (isCompress) {
                            compressPic(context, path);
                        } else {
                            if (onCompressResult != null) {
                                if (TextUtils.isEmpty(path)) {
                                    onCompressResult.onFailed("获取图片失败");
                                } else {
                                    onCompressResult.onSuccess(path);
                                }
                            }
                        }
                    }
                    break;
                case PIC_FROM_LOCALPHOTO:
                    if (data != null) {
                        cropUtil(data.getData());
                    }
                    break;
                default:
                    break;
            }
        } else {
            super.iLog("onActivityResult::isCrop == false");
            switch (requestCode) {
                case PIC_FROM_CAMERA:
                    super.iLog("onActivityResult::PIC_FROM_CAMERA");
                    path = getRealFilePath(context, cameraOutUri);
                    break;
                case PIC_FROM_LOCALPHOTO:
                    if (data != null) {
                        super.iLog("onActivityResult::PIC_FROM_LOCALPHOTO");
                        path = getRealFilePath(context, data.getData());
                    }
                    break;
                default:
                    break;
            }
            if (isCompress) {
                compressPic(context, path);
            } else {
                if (onCompressResult != null) {
                    if (TextUtils.isEmpty(path)) {
                        onCompressResult.onFailed("获取图片失败");
                    } else {
                        onCompressResult.onSuccess(path);
                    }
                }
            }

        }
        super.iLog("onActivityResult::path:" + path);


    }

    /**
     * 压缩图片
     *
     * @param context
     * @param inputPath
     */
    private void compressPic(Context context, String inputPath) {
        Luban.with(context)
                .load(inputPath)                                   // 传人要压缩的图片列表
                .ignoreBy(ignoreKB)                                  // 忽略不压缩图片的大小
                .setTargetDir(STOREPATH)                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        progressDialog.show();
                        iLog("onStart");
                    }

                    @Override
                    public void onSuccess(File file) {
                        progressDialog.dismiss();
                        iLog("onSuccess:" + file.getAbsolutePath());
                        iLog("onSuccess:" + file.getPath());

                        if (onCompressResult != null) {
                            onCompressResult.onSuccess(file.getAbsolutePath());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        iLog("onError");
                        if (onCompressResult != null) {
                            onCompressResult.onFailed("压缩图片失败了...");
                        }
                    }
                }).launch();    //启动压缩
    }

    /**
     * 通过uri获取图片的路径
     *
     * @param context 上下文
     * @param uri     uri
     * @return 图片路径
     */
    private String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            super.iLog("getRealFilePath: url == null");
            return null;
        }
        super.iLog("getRealFilePath: url == " + uri);
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
            super.iLog("getRealFilePath: scheme == null");
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            super.iLog("getRealFilePath: ContentResolver.SCHEME_FILE");
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            super.iLog("getRealFilePath: ContentResolver.SCHEME_CONTENT");
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            } else {
                super.iLog("getRealFilePath: null != cursor");
            }
        }
        return data;
    }

    /**
     * 获取格式化后的图片路径
     *
     * @param imageName 图片名称
     * @return 图片路径
     */
    public static String getFilePathBaseOnTime(String imageName) {
        return STOREPATH + String.valueOf(System.currentTimeMillis()) + (int) (Math.random() * 1000.0D + 1.0D) + imageName;
    }


}
