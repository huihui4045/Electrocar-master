package saiyi.com.xiande.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件操作
 *
 * @author ZLJ
 */
public class FileUtils {

    /**
     * 保存Bitmap到SD卡
     *
     * @param mBitmap
     */
    public static void saveBitmap(Bitmap mBitmap) {
        Date dt = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = sdf.format(dt);
        //
        if (!SDCardUtils.isSDCardEnable()) {
            throw new RuntimeException("SDCard is not enable");
        }
        //
        saveBitmap(mBitmap, SDCardUtils.getSDCardPath(), fileName);
    }

    /**
     * 保存Drawable到SD卡
     */
    public static void saveDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new RuntimeException("Drawable is null");
        }
        Bitmap mBitmap = drawable2Bitmap(drawable);
        saveBitmap(mBitmap);
    }

    /**
     * 保存成png
     *
     * @param drawable
     * @param path     类似于 /sdcard/Note
     * @param saveName 保存的名称
     */
    public static void saveDrawable(Drawable drawable, String path, String saveName) {
        if (drawable == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(saveName)) {
            return;
        }
        Bitmap mBitmap = drawable2Bitmap(drawable);
        saveBitmap(mBitmap, path, saveName);
    }

    /**
     * 保存成png
     *
     * @param mBitmap
     * @param path     类似于 /sdcard/Note
     * @param saveName 保存的名称
     */
    public static void saveBitmap(Bitmap mBitmap, String path, String saveName) {
        if (mBitmap == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(saveName)) {
            return;
        }
        //
        File dir = new File(path + File.separator);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        //
        File f = new File(dir, saveName + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * drawable2Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

}
