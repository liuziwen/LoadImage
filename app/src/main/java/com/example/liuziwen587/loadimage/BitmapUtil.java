package com.example.liuziwen587.loadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by liuziwen on 16/8/1.
 */
public class BitmapUtil {

    public static Bitmap getScaledBitmapFromPath(String path, int length){
        System.out.println("decode bitmap path = "+path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;

        System.out.println("decode bitmap before length = "+width);

        int l = Math.min(height, width);
        if (l > length){
            options.inSampleSize = l / length;
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = ImageCrop(bitmap);

        System.out.println("decode bitmap after length = "+bitmap.getWidth());

        return bitmap;
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }
}
