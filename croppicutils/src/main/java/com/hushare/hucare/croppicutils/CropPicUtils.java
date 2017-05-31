package com.hushare.hucare.croppicutils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 功能/模块 ：从相册或者拍照获取照片并裁剪
 *
 * @author huzeliang
 * @version 1.0 2017/3/21
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
     * 图片的uri
     */
    private Uri photoUri;
    /**
     * 裁剪图片的比例x
     */
    private int ssX = 1;
    /**
     * 裁剪图片的比例y
     */
    private int ssY = 1;
    /**
     * 压缩图片的宽度
     */
    private int picWidth = 720;
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
     * 缓存路径
     */
    private static String STOREPATH = "/storage/emulated/0/Android/data/com.temp.temp/files/tempCache/";
    /**
     * 图片的tag，如果当前界面有多处需要获取图片，使用tag可以区分是哪里获取图片
     */
    private Object imageTag = "";

    public Object getImageTag() {
        return imageTag;
    }

    public void setImageTag(Object imageTag) {
        this.imageTag = imageTag;
    }

    /**
     * @param context
     * @param fragment 如果不是fragment，就传null
     */
    public CropPicUtils(Context context, Fragment fragment) {
        super(context, "huzeliang");
        this.fragment = fragment;
        this.context = context;
//        this.ssX = sX;
//        this.ssY = sY;
//        this.picWidth = picWidth;
//        this.isCrop = isCrop;
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
     * 设置图片压缩的最小尺寸
     *
     * @param picWidth 最小尺寸
     * @return 你懂的
     */
    public CropPicUtils setPicWidth(int picWidth) {
        this.picWidth = picWidth;
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
     * 获取裁剪后的bitmap
     *
     * @param index 裁剪的宽度
     * @return bitmap
     */
    private Bitmap getCropBitmap(int index) {
        if (photoUri != null) {
            if (fragment != null) {
                return getCompressImage(fragment.getActivity(), photoUri, index);
            } else {
                return getCompressImage(context, photoUri, index);
            }

        } else {
            return null;
        }
    }

    /**
     * 获取压缩后的bitmap
     *
     * @param context  上下文
     * @param srcPath  图片uri
     * @param picWidth 图片宽度
     * @return bitmap
     */
    public static Bitmap getCompressImage(Context context, Uri srcPath, int picWidth) {
        Bitmap bitmap = null;

        try {
            BitmapFactory.Options e = new BitmapFactory.Options();
            e.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(srcPath), new Rect(), e);
            e.inJustDecodeBounds = false;
            int w = e.outWidth;
            int h = e.outHeight;
            int be = 1;
            if (w > picWidth) {
                be = (int) (1.0D * (double) w / (double) ((float) picWidth));
            }

            if (be <= 0) {
                be = 1;
            }

            e.inSampleSize = be;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(srcPath), new Rect(), e);
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 获取压缩后图片的路径
     *
     * @param srcPath  图片路径
     * @param picWidth 图片宽度
     * @return 图片路径
     */
    public static String getCompressImage(String srcPath, int picWidth) {
        String path = "";
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options e = new BitmapFactory.Options();
            e.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(srcPath), new Rect(), e);
            e.inJustDecodeBounds = false;
            int w = e.outWidth;
            int h = e.outHeight;
            int be = 1;
            if (w > picWidth) {
                be = (int) (1.0D * (double) w / (double) ((float) picWidth));
            }

            if (be <= 0) {
                be = 1;
            }

            e.inSampleSize = be;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(srcPath), new Rect(), e);
            path = saveBitmap(bitmap, getFilePathBaseOnTime("yasuo.jpg"));
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
        }

        return path;
    }

    /**
     * 裁剪从相机获取到的图片
     */
    private void cropCmr() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        setIntentParams(intent);
        if (fragment != null) {
            fragment.startActivityForResult(intent, PIC_FROM_CAIJIAN);
        } else {
            ((Activity) context).startActivityForResult(intent, PIC_FROM_CAIJIAN);
        }
    }

    /**
     * 裁剪图片
     *
     * @param data intent
     */
    private void cropPic(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            if (cursor == null) {
                Toast.makeText(context, "解析出错...请重新选择...", Toast.LENGTH_LONG).show();
                return;
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            cropImageUriByTakePhoto(Uri.fromFile(new File(imagePath)));
        } else {
        }
    }

    /**
     * 使用uri裁剪图片
     *
     * @param uri uri
     */
    private void cropImageUriByTakePhoto(Uri uri) {
        setPhotoUri(uri);
        cropCmr();
    }

    /**
     * 设置图片uri
     *
     * @param uri uri
     */
    private void setPhotoUri(Uri uri) {
        this.photoUri = uri;
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
                if (fragment != null) {
                    fragment.startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
                } else {
                    ((Activity) context).startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
                }
            } else {
                super.iLog("doHandlerPhoto::PIC_FROM_CAMERA");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUri = Uri.fromFile(new File(getFilePathBaseOnTime(".jpg")));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
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
    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            super.iLog("onActivityResult::resultCode != Activity.RESULT_OK");
            return "";
        }
        String path = "";
        if (isCrop) {
            super.iLog("onActivityResult::isCrop == true");
            switch (requestCode) {
                case PIC_FROM_CAMERA:
                    cropCmr();
                    break;
                case PIC_FROM_CAIJIAN:
                    Bitmap bitmap = getCropBitmap(picWidth);
                    if (bitmap != null) {
                        path = saveBitmap(bitmap, getFilePathBaseOnTime("caijian.jpg"));
                    }
                    break;
                case PIC_FROM_LOCALPHOTO:
                    cropPic(data);
                    break;
                default:
                    break;
            }
        } else {
            super.iLog("onActivityResult::isCrop == false");
            switch (requestCode) {
                case PIC_FROM_CAMERA:
                    super.iLog("onActivityResult::PIC_FROM_CAMERA");
                    path = getRealFilePath(context, photoUri);
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

        }
        super.iLog("onActivityResult::path:" + path);
        return path;
    }

    /**
     * 保存bitmap
     *
     * @param bm       bitmap
     * @param filepath 保存的路径
     * @return 返回图片的路径
     */
    public static String saveBitmap(Bitmap bm, String filepath) {
        try {
            File e = new File(filepath);
            if (e.exists()) {
                e.delete();
            }

            FileOutputStream out = new FileOutputStream(e);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return filepath;
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return "";
    }


    /**
     * 设置公用参数
     *
     * @param intent intent
     */
    private void setIntentParams(Intent intent) {
        Uri uri = photoUri;
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", ssX);
        intent.putExtra("aspectY", ssY);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        photoUri = Uri.fromFile(new File(getFilePathBaseOnTime("intent.jpg")));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
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
     * @param imageName 图片名称
     * @return 图片路径
     */
    public static String getFilePathBaseOnTime(String imageName) {
        return STOREPATH + String.valueOf(System.currentTimeMillis()) + (int) (Math.random() * 1000.0D + 1.0D) + imageName;
    }


}
