package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.GridView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Timer timer = null;
    private ProgressImageView piv;
    private GridView gridView;
    int progress = 0;

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            piv.setProgress(++progress);
            if (progress>=100){
                piv.setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                timer.cancel();
            }
        }
    };

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(runnable);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
//        StateImageView siv = (StateImageView) findViewById(R.id.progress);
//        siv.setState(StateImageView.STATE_INIT);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(this));
        //DownUtil.getInstance().getBitmap(ImageUrl.url[2],piv);

//        timer = new Timer();
//        timer.schedule(timerTask, 1000, 100);

    }


    public void onDestroy(){
        super.onDestroy();
        deleteImageFile(DownloadImage.savePath);
    }

    public boolean deleteImageFile(String path) {
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
