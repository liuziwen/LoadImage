package com.example.liuziwen587.loadimage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liuziwen on 16/9/9.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_image_cell, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = ImageUrl.url[position];
        holder.imageView.setTag(imageUrl);
        //DownUtil.getInstance().setBitmapForView(imageUrl, holder.imageView, 200);
    }

    @Override
    public int getItemCount() {
        return ImageUrl.url.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ProgressImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView = (ProgressImageView) view.findViewById(R.id.image);
        }
    }
}
