package com.jkkg.hhtx.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.jkkg.hhtx.app.Constants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author   wildma
 * Github   https://github.com/wildma
 * Date     2018/6/24
 * Desc     ${图片相关工具类}
 */

public class ImageUtils {

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format) {
        return save(src, FileUtils.getFileByPath(filePath), format, false);
    }

    /**
     * 保存图片
     *
     * @param src    源图片
     * @param file   要保存到的文件
     * @param format 格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, CompressFormat format) {
        return save(src, file, format, false);
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @param recycle  是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format, boolean recycle) {
        return save(src, FileUtils.getFileByPath(filePath), format, recycle);
    }

    /**
     * 保存图片
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src) || !FileUtils.createOrExistsFile(file)) {
            return false;
        }
        System.out.println(src.getWidth() + ", " + src.getHeight());
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(os);
        }
        return ret;
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * 将byte[]转换成Bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromByte(byte[] bytes, int width, int height) {
        final YuvImage image = new YuvImage(bytes, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length);
        if (!image.compressToJpeg(new Rect(0, 0, width, height), 100, os)) {
            return null;
        }
        byte[] tmp = os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        return bmp;
    }

    /**
     *   //这个方法是把地址生成二维码
     * Bitmap  qr = CodeUtils.createQRCode(coin.getAddr(), ScreenSizeUtil.dp2px(this, 175));
     *
     */

    /**
     * 在这里先获取到控件的视图，然后在用bitmap保存到相册
     * 使用View的缓存功能，截取指定区域的View
     */
    public static  Bitmap screenShotView(View view) {
        //开启缓存功能
        view.setDrawingCacheEnabled(true);
        //创建缓存
        view.buildDrawingCache();
        //获取缓存Bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }


    /**
     * 将文件保存到公共的媒体文件夹
     * 这里的filename单纯的指文件名，不包含路径
     * @param bitmap
     *  String fileType=imageUrl.substring(imageUrl.lastIndexOf(".")+1,imageUrl.length());
     *  String newFileName = System.currentTimeMillis() + "."+fileType;
     */
    public static void savePicture(Context context,Bitmap bitmap) {
        try {
            if (null == bitmap) {
                ToastUtil.show("保存失败：图片没有找到");
                return;
            }
            String fileName = System.currentTimeMillis() + ".png";
            //Android Q和以下版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //设置保存参数到ContentValues中
                ContentValues contentValues = new ContentValues();
                //设置文件名
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                //RELATIVE_PATH是相对路径不是绝对路径
                //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/miduo");
                //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/signImage");
                String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
                //设置文件类型
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/"+fileType);
                //执行insert操作，向系统文件夹中添加文件
                //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                if (uri != null) {
                    //若生成了uri，则表示该文件添加成功
                    //使用流将内容写入该uri中即可
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        ToastUtil.show("保存成功!");
                    }
                }
            } else {
                //建立指定文件夹
                File foder = new File(Environment.getExternalStorageDirectory(), Constants.FilePath);
                if (!foder.exists()) {
                    foder.mkdirs();
                }
                File myCaptureFile = new File(foder, fileName);
                if (!myCaptureFile.exists()) {
                    myCaptureFile.createNewFile();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                //压缩保存到本地
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                bos.flush();
                bos.close();

                // 把文件插入到系统图库
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, myCaptureFile.getAbsolutePath());
                String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/"+fileType);
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if(uri!=null){
                    // 通知图库更新
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    context.sendBroadcast(mediaScanIntent);

                    ToastUtil.show("保存成功!");
                }else{
                    ToastUtil.show("保存失败!");
                }
            }
        } catch (Exception e) {
            ToastUtil.show( "保存失败：" + e.getMessage());
        }
    }
}
