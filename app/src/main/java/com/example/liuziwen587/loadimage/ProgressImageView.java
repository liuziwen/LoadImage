package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by liuziwen on 16/8/1.
 */
public class ProgressImageView extends ImageView {

    public static int STATE_INIT = 0;
    public static int STATE_ING = 1;
    public static int STATE_FINISH = 2;
    public static int STATE_CANCELL = 3;
    public static int STATE_ERROR = 4;

    public int state = 0;
    public int progress = 0;
    public boolean isFinished = false;
    public Bitmap bitmap = null;
    public Paint p1 = null;
    public Paint p2 = null;
    public int padding = 10;
    RectF rectF = new RectF();

    public ProgressImageView(Context context) {
        super(context);
        init();
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        p1 = new Paint();
        p1.setAntiAlias(true);
        p1.setColor(Color.parseColor("#FF999999"));
        p2 = new Paint();
        p2.setAntiAlias(true);
        p2.setColor(Color.parseColor("#FFffffff"));
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        isFinished = true;
        progress = 100;
        invalidate();
    }

    public void setState(int state){
        this.state = state;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectF.set(padding, padding, getWidth() - padding, getHeight() - padding);
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (!isFinished){
            System.out.println("onDraw progress = " + progress);
            canvas.drawColor(Color.parseColor("#ff333333"));
            int radius = getHeight() / 2;
            canvas.drawCircle(radius, radius, radius - padding, p1);
            float arc = (float)(progress) / 100 * 360;
            canvas.drawArc(rectF, 270, arc, true, p2);
        } else {
            setImageBitmap(bitmap);
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress >= 100){
            isFinished = true;
        }
        invalidate();
    }



}
