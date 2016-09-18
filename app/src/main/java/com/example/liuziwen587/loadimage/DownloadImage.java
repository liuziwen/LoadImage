package com.example.liuziwen587.loadimage;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by LIUZIWEN587 on 2016-07-27.
 */
public class DownloadImage {

    public String LOG = "DownloadImage : ";

    public boolean isCancelled = false;
    public static final String savePath = Environment.getExternalStorageDirectory().getPath()+"/DownloadImage";

    public DownloadImage(){
        File dir = new File(savePath);
        if (!dir.exists()){
            dir.mkdir();
        }
    }

    public static String getNameByUrl(String url){
        String name = null;
        int length = url.lastIndexOf('/');
        if (url.length()-1-length > 25){
            name = url.substring(url.length() - 25);
        } else {
            name = url.substring(length+1);
        }
        return name;
    }

    public void download(String path, LoadingListener listener){
        MyLog.d(LOG + "url = " + path);
        String imagePath = savePath + "/" + getNameByUrl(path);
        MyLog.d(LOG + "save path = "+savePath);
//        File image = new File(imagePath);
//        if (image.exists()){
//            if (listener != null){
//                listener.handleProgress(100);
//                listener.onFinished(getNameByUrl(path));
//            }
//            return ;
//        }

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
            //conn.setRequestMethod("POST");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                MyLog.d("ResponseCode() = "+conn.getResponseCode());
                int length = conn.getContentLength();
                is = conn.getInputStream();
                output = new FileOutputStream(imagePath);
                byte data[] = new byte[1024];
                long total = 0;
                int count = 0;
                MyLog.d(LOG + "downImage length = "+length);
                while ((count = is.read(data)) > 0){
                    if (!isCancelled){
                        total += count;
                        if (listener != null){
                            if (length > 0){
                                int progress = (int) (100 * total / length);
                                //MyLog.d(LOG + "downImage progress = " + progress);
                                listener.handleProgress(progress);
                            }
                        }

                        output.write(data, 0, count);
                    } else {
                        if (listener != null)
                            listener.onCancelled();
                        MainActivity.deleteImageFile(imagePath);
                        return ;
                    }
                }
                output.flush();
                if (listener != null){
                    listener.onFinished(getNameByUrl(path));
                }
            } else {
                if (listener != null){
                    listener.onFailed("");
                    MainActivity.deleteImageFile(imagePath);
                }
                return ;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            MainActivity.deleteImageFile(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            MainActivity.deleteImageFile(imagePath);
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
