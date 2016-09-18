package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    //记录已经添加的线程
    public static Set<Integer> threadSet = new HashSet<Integer>();
    public static Set<String> downedSet = new HashSet<>();
    public static Set<String> loadSet = new HashSet<>();
    public static Map<Integer, Integer> progressSet = new HashMap<>();
    public static WeakHashMap<Integer, ProgressImageView> weakHashMap = new WeakHashMap<>();

    public static void setProgress(ProgressImageView iv, int position, int progress){
        iv.setProgress(progress);
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            MyLog.d("gridview not reuse position = "+position);
            convertView = inflater.inflate(R.layout.gridview_image_cell, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ProgressImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            MyLog.d("gridview reuse position = "+position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        weakHashMap.put(position, viewHolder.imageView);
        if (threadSet.contains(position)){
            viewHolder.imageView.setState(ProgressImageView.STATE_DOWNING);
            //if ()
        } else {
            viewHolder.imageView.setState(ProgressImageView.STATE_INIT);
        }
        String imageUrl = ImageUrl.url[position];
        viewHolder.imageView.setTag(imageUrl);
        DownUtil.getInstance().setBitmapForView(imageUrl, viewHolder.imageView, 200, position);
        return convertView;
    }

    private static class ViewHolder{
        ProgressImageView imageView;
    }

}
