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

    public boolean isCancelled = false;
    public static final String savePath = "/sdcard/DownloadImage";

    public DownloadImage(){
        File dir = new File(savePath);
        if (!dir.exists()){
            dir.mkdir();
        }
    }

    public static String getNameByUrl(String url){
        String name = null;
        int length = url.lastIndexOf('/');
        if (url.length()-1-length > 20){
            name = url.substring(url.length() - 20);
        } else {
            name = url.substring(length+1);
        }
        return name+".jpg";
    }

    public void download(String path, LoadingListener listener){
        System.out.println("downImage url = "+path);
        String imagePath = savePath + "/" + getNameByUrl(path);
        System.out.println("save path = "+savePath);
        File image = new File(imagePath);
        if (image.exists()){
            if (listener != null){
                listener.handleProgress(100);
                listener.onFinished(getNameByUrl(path));
            }
            return ;
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
            conn.setRequestMethod("POST");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int length = conn.getContentLength();
                is = conn.getInputStream();
                output = new FileOutputStream(imagePath);
                byte data[] = new byte[4096];
                long total = 0;
                int count = 0;
                System.out.println("downImage length = "+length);
                while ((count = is.read(data)) > 0){
                    if (!isCancelled){
                        total += count;
                        if (listener != null){
                            if (length > 0){
                                int progress = (int) (100 * total / length);
                                System.out.println("downImage progress = " + progress);
                                listener.handleProgress(progress);
                            }
                        }

                        output.write(data, 0, count);
                    } else {
                        if (listener != null)
                            listener.onCancelled();
                        return ;
                    }
                }
                output.flush();
                if (listener != null)
                    listener.onFinished(getNameByUrl(path));
            } else {

                return ;
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
        return ;

    }

    public void cancell(){
        isCancelled = true;
    }

    public boolean deleteImage(String path){
        File file = new File(path);
        if (file.exists()){
            return file.delete();
        } else {
            return true;
        }
    }

    public interface LoadingListener{
        void handleProgress(int progress);
        void onFinished(String name);
        void onCancelled();
        void onFailed(String errorMsg);
    }



}
