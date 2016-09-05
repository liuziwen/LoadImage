package com.example.liuziwen587.loadimage;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by liuziwen on 16/8/1.
 */
public class ImageLruCache {

    private static ImageLruCache imageLruCache;

    private static LruCache<String, Bitmap> cache = null;

    public static ImageLruCache getInstance(){
        if (imageLruCache == null){
            synchronized (ImageLruCache.class){
                if (imageLruCache == null){
                    imageLruCache = new ImageLruCache();
                }
            }
        }
        return imageLruCache;
    }

    private ImageLruCache(){
        int maxSize = (int) (Runtime.getRuntime().maxMemory());
        MyLog.d("lrucache maxMemory = " + maxSize/1024/1024/8);
        int cacheSize = maxSize/4;
        cache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value){
                return value.getByteCount();
            }
        };
    }

    public Bitmap get(String key){
        return cache.get(key);
    }

    public void put(String key, Bitmap value){
        if (cache.get(key) == null && value != null){
            cache.put(key, value);
        }
    }

}
