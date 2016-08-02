package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Timer timer = null;
    ProgressImageView piv;
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
        piv = (ProgressImageView) findViewById(R.id.progress);
        DownUtil.getInstance().getBitmap(ImageUrl.url[2],piv);

//        timer = new Timer();
//        timer.schedule(timerTask, 1000, 100);

    }

    static Context context;
    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
