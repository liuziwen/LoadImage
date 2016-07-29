package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LIUZIWEN587 on 2016-07-27.
 */
public class DownloadImage {

    public Context context;
    public boolean isCancelled = false;
    public final String savePath = "/sdcard/DownloadImage";
    public int CPUCount = 2;

    public DownloadImage(Context con){
        this.context = con;
        File dir = new File(savePath);
        if (!dir.exists()){
            dir.mkdir();
        }
    }

    public boolean download(String path, LoadingProgressListener listener){
        String imagePath = savePath + "/" + path;
        File image = new File(imagePath);
        if (image.exists()){
            return true;
        }

        URL url = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        OutputStream output = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int length = conn.getContentLength();
                is = conn.getInputStream();
                output = new FileOutputStream(savePath);
                byte data[] = new byte[4096];
                long total = 0;
                int count = 0;
                while ((count = is.read(data)) != -1){
                    if (!isCancelled){
                        total += count;
                        if (listener != null){
                            if (length > 0){
                                listener.handleProgress((int) (100 * total / length));
                            }
                        }

                        output.write(data, 0, count);
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (is != null)
                    is.close();
            } catch (IOException ignored) {
            }
            if (conn != null)
                conn.disconnect();
        }
        return true;

    }

    public boolean deleteImage(String path){
        File file = new File(path);
        if (file.exists()){
            return file.delete();
        } else {
            return true;
        }
    }

    public interface LoadingProgressListener{
        void handleProgress(int progress);
    }

    ExecutorService pool = Executors.newFixedThreadPool(CPUCount);
}
