package com.example.liuziwen587.loadimage;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.GridView;

import java.io.File;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(this));
    }

    public void onResume(){
        super.onResume();
//        deleteImageFile(DownloadImage.savePath);
//        ImageLruCache.getInstance().clear();
//        GridViewAdapter.downedSet.clear();
//        GridViewAdapter.threadSet.clear();
    }
    public void onDestroy(){
        super.onDestroy();
//        deleteImageFile(DownloadImage.savePath);
    }

    public static boolean deleteImageFile(String path) {
        File file = new File(path);
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files){
                if (file.isDirectory()){
                    deleteImageFile(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
        } else {
            file.delete();
        }
        return true;
    }

    static Context context;
    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
