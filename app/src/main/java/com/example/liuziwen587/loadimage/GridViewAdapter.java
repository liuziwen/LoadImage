package com.example.liuziwen587.loadimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by liuziwen on 16/8/16.
 */
public class GridViewAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater = null;
    public GridViewAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //记录已经添加的从网上下载图片的线程, 避免重复下载
    public static Set<Integer> threadSet = new HashSet<Integer>();
    //记录下载好的图片,下载完后再从sd卡上加载
    public static Set<String> downedSet = new HashSet<>();
    //记录正在从sd卡加载图片的线程
    public static Set<String> loadSet = new HashSet<>();
    //记录position位置的imageView
    public static WeakHashMap<Integer, ProgressImageView> weakHashMap = new WeakHashMap<>();


    @Override
    public int getCount() {
        return ImageUrl.url.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.gridview_image_cell, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ProgressImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        weakHashMap.put(position, viewHolder.imageView);
        if (threadSet.contains(position)){
            viewHolder.imageView.setState(ProgressImageView.STATE_DOWNING);
        } else {
            viewHolder.imageView.setState(ProgressImageView.STATE_INIT);
        }
        String imageUrl = ImageUrl.url[position];
        viewHolder.imageView.setTag(imageUrl);
        DownUtil.getInstance().setBitmapForView(imageUrl, viewHolder.imageView, 200, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(context);
                d.setTitle("重新加载");
                Bitmap bitmap = ImageLruCache.getInstance().get(ImageUrl.url[position]);
                if (bitmap == null){
                    d.setIcon(new BitmapDrawable(bitmap));
                } else {
                    d.setIcon(R.mipmap.ic_launcher);
                }

                String msg = "图片下载线程是否已添加:"+threadSet.contains(position)+"\n"+
                        " 图片是否已下到sd卡:"+downedSet.contains(ImageUrl.url[position]);
                d.setMessage(msg);
//                d.setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
                d.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                d.show();

            }
        });
        return convertView;
    }

    private static class ViewHolder{
        ProgressImageView imageView;
    }

}
