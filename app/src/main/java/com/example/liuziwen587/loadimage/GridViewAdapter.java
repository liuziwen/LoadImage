package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
            convertView = inflater.inflate(R.layout.gridview_image_cell, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (StateImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            MyLog.d("gridview reuse");
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setState(StateImageView.STATE_LOADING);
//        String imageUrl = ImageUrl.serverUrl + ImageUrl.url[position];
//        viewHolder.imageView.setTag(imageUrl);
//        DownUtil.getInstance().setBitmapForView(imageUrl, viewHolder.imageView, 200);
        return convertView;
    }

    private class ViewHolder{
        StateImageView imageView;
    }
}
