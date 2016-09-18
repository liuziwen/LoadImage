package com.example.liuziwen587.loadimage;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by liuziwen on 16/9/14.
 */
public class LoadAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    WeakReference<ProgressImageView> progressImageView;
    public LoadAsyncTask(ProgressImageView iv){
        progressImageView = new WeakReference<ProgressImageView>(iv);
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        final String url = params[0];
        Bitmap bitmap = null;
        if ((bitmap = ImageLruCache.getInstance().get(DownloadImage.getNameByUrl(url))) == null){
            String path = DownloadImage.savePath + DownloadImage.getNameByUrl(url);
            File file = new File(path);
            if (file.exists()){
                bitmap = BitmapUtil.getScaledBitmapFromPath(path, 200);
            }else {
            }
        } else {

        }
        return null;
    }
}
