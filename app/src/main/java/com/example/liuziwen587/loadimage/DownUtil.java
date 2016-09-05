package com.example.liuziwen587.loadimage;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuziwen on 16/8/1.
 */
public class DownUtil {

    private Handler handler = new Handler(Looper.myLooper());
    public static DownUtil instance;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "LoadTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);


    public static final Executor pool
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);


    public static DownUtil getInstance(){
        if (instance == null){
            synchronized(DownUtil.class){
                if (instance == null){
                    instance = new DownUtil();
                }
            }
        }
        return instance;
    }

    private DownUtil(){

    }

    public void execute(final String url, final DownloadImage.LoadingListener listener){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                new DownloadImage().download(url, listener);
            }
        });
    }


    public void setBitmapForView(final String url, final ProgressImageView imageView, final int width){

        final Bitmap[] bitmap = {null};
        if ((bitmap[0] = ImageLruCache.getInstance().get(DownloadImage.getNameByUrl(url))) == null){
            final File f = new File(DownloadImage.savePath + "/" + DownloadImage.getNameByUrl(url));
            if (f.exists()){
                imageView.setState(ProgressImageView.STATE_LOADING);
                bitmap[0] = BitmapUtil.getScaledBitmapFromPath(f.getAbsolutePath(), width);
                ImageLruCache.getInstance().put(DownloadImage.getNameByUrl(url), bitmap[0]);
                imageView.setBitmap(bitmap[0]);
            } else {
                execute(url, new DownloadImage.LoadingListener() {
                    @Override
                    public void handleProgress(final int progress) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (imageView.getTag().equals(url)){
                                    imageView.setProgress(progress);
                                }
                            }
                        });

                    }

                    @Override
                    public void onFinished(String name) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setState(ProgressImageView.STATE_LOADING);
                                bitmap[0] = BitmapUtil.getScaledBitmapFromPath(f.getAbsolutePath(), width);
                                ImageLruCache.getInstance().put(DownloadImage.getNameByUrl(url), bitmap[0]);
                                if (imageView.getTag().equals(url)){
                                    imageView.setBitmap(bitmap[0]);
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onFailed(String errorMsg) {

                    }
                });
            }
        } else {
            MyLog.d("lrucache reuse");
            imageView.setBitmap(bitmap[0]);
        }
    }



}
