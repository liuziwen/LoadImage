package com.example.liuziwen587.loadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.Iterator;
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


    public static DownUtil getInstance() {
        if (instance == null) {
            synchronized (DownUtil.class) {
                if (instance == null) {
                    instance = new DownUtil();
                }
            }
        }
        return instance;
    }

    private DownUtil() {

    }

    public void execute(final String url, final DownloadImage.LoadingListener listener) {
        pool.execute(new Thread(url) {
            @Override
            public void run() {
                new DownloadImage().download(url, listener);
            }
        });
    }


    public void setBitmapForView(final String url, final ProgressImageView imageView, final int width, final int position) {
        final Bitmap[] bitmap = {null};
        if ((bitmap[0] = ImageLruCache.getInstance().get(DownloadImage.getNameByUrl(url))) == null) {
            final File f = new File(DownloadImage.savePath + "/" + DownloadImage.getNameByUrl(url));
            if (f.exists()) {
                if (imageView != null && !GridViewAdapter.threadSet.contains(position)) {
                    if (!GridViewAdapter.loadSet.contains(url)) {
                        imageView.setState(ProgressImageView.STATE_LOADING);
                        GridViewAdapter.loadSet.add(url);
                        new LoadFromSDCardThread(url, imageView, width).start();
                    }
                }

            } else {
                if (GridViewAdapter.threadSet.contains(position)) {
                    return;
                }
                GridViewAdapter.threadSet.add(position);
                execute(url, new DownloadImage.LoadingListener() {
                    @Override
                    public void handleProgress(final int progress) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ProgressImageView imageView = GridViewAdapter.weakHashMap.get(position);
                                if (imageView != null && imageView.getTag().equals(url)) {
                                    imageView.setProgress(progress);
                                } else {
                                    //MyLog.d("progress null");
                                }
                            }
                        });

                    }

                    @Override
                    public void onFinished(String name) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                GridViewAdapter.threadSet.remove(position);
                                GridViewAdapter.downedSet.add(url);
                                ProgressImageView imageView = GridViewAdapter.weakHashMap.get(position);
                                if (imageView != null && imageView.getTag().equals(url)) {
                                    imageView.setState(ProgressImageView.STATE_LOADING);
                                    GridViewAdapter.loadSet.add(url);
                                    new LoadFromSDCardThread(url, imageView, width).start();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ProgressImageView imageView = GridViewAdapter.weakHashMap.get(position);
                                if (imageView != null && imageView.getTag().equals(url)) {
                                    imageView.setState(ProgressImageView.STATE_CANCELL);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ProgressImageView imageView = GridViewAdapter.weakHashMap.get(position);
                                if (imageView != null && imageView.getTag().equals(url)) {
                                    imageView.setState(ProgressImageView.STATE_ERROR);
                                }
                            }
                        });
                    }
                });
            }
        } else {
            if (imageView != null) {
                imageView.setBitmap(bitmap[0]);
            }
        }
    }

    public class LoadFromSDCardThread extends Thread {
        String url;
        ProgressImageView iv;
        int width;

        public LoadFromSDCardThread(String url, ProgressImageView iv, int width) {
            this.iv = iv;
            this.url = url;
            this.width = width;
            setName(url);
        }

        @Override
        public void run() {
            final Bitmap bitmap = BitmapUtil.getScaledBitmapFromPath(DownloadImage.savePath + "/" + DownloadImage.getNameByUrl(url), width);
            ImageLruCache.getInstance().put(DownloadImage.getNameByUrl(url), bitmap);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (iv != null && iv.getTag().equals(url)) {
                        if (bitmap != null) {
                            iv.setBitmap(bitmap);
                        } else {
                            iv.setBitmap(BitmapFactory.decodeResource(iv.getResources(), R.mipmap.ic_launcher));
                        }
                    }
                    GridViewAdapter.loadSet.remove(url);
                }
            });
        }
    }


}
