package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liuziwen on 16/8/1.
 */
public class ProgressImageView extends ImageView {

    public final static int STATE_INIT = 0;
    public final static int STATE_DOWNING = 1;
    public final static int STATE_FINISH = 2;
    public final static int STATE_CANCELL = 3;
    public final static int STATE_ERROR = 4;
    public final static int STATE_LOADING = 5;

    public int state = 1;
    public int progress = 0;
    public boolean isFinished = false;
    public Bitmap bitmap = null;
    public Paint p1 = null;
    public Paint p2 = null;
    public int padding = 10;
    RectF rectF = new RectF();

    int width = 0;
    int height = 0;

    int fps = 60;

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

    public void init() {
        p1 = new Paint();
        p1.setAntiAlias(true);
        p1.setColor(Color.parseColor("#FF999999"));
        p2 = new Paint();
        p2.setAntiAlias(true);
        p2.setColor(Color.parseColor("#FFffffff"));
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        isFinished = true;
        state = STATE_FINISH;
        progress = 100;
        invalidate();
    }

    public void setState(int state) {
        this.state = state;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        } else if (widthSpecMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSpecSize, widthSpecSize);
        } else if (heightSpecMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(heightSpecSize, heightSpecSize);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

        rectF.set(padding, padding, getWidth() - padding, getHeight() - padding);
        r1 = getHeight() / 2;
        length = getWidth() / fps;
        width = getWidth();
        height = getHeight();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (state){
            case STATE_INIT: drawInitAnim(canvas);break;
            case STATE_DOWNING: drawDowningAnim(canvas);break;
            case STATE_LOADING: drawLoadingAnim(canvas);break;
            case STATE_FINISH:setImageBitmap(bitmap);break;
            case STATE_ERROR: drawErrorState(canvas);break;
            case STATE_CANCELL:drawCancellState(canvas);break;
            default:break;
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress >= 100) {
            isFinished = true;
        }
        invalidate();
    }

    public void drawDowningAnim(Canvas canvas){
        canvas.drawColor(Color.parseColor("#ff333333"));
        int radius = getHeight() / 2;
        canvas.drawCircle(radius, radius, radius - padding, p1);
        float arc = (float)(progress) / 100 * 360;
        canvas.drawArc(rectF, 270, arc, true, p2);
    }

    int r1 = getHeight() / 2;
    int r2 = 0;
    int length = getWidth() / fps;
    boolean b = true;//外缩内增
    public void drawLoadingAnim(Canvas canvas) {
        p1.setColor(Color.BLUE);
        p2.setColor(Color.GREEN);

        if (state == STATE_LOADING) {
            if (b) {
                r1 -= length;
                r2 += length;
                if (r1 <= r2) {
                    b = false;
                }
            } else {
                r1 += length;
                r2 -= length;
                if (r1 >= getWidth() || r2 <= 0) {
                    b = true;
                }
            }
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, r1, p1);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, r2, p2);

            try {
                Thread.sleep(1000 / fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            invalidate();
        }
    }

    Path path1 = new Path();
    Path path2 = new Path();
    int i = 0;
    int angle = 0;
    public void drawInitAnim(Canvas canvas){

        int dx = 3;
        int dy = 4;
        if (dx*i<width/4){

            path1.moveTo(width/4, height/8);
            path1.lineTo(width/4*3, height/8*7);
            path1.lineTo(width/4, height/8*7);
            path1.lineTo(width/4*3, height/8);
            path1.lineTo(width/4, height/8);

            p1.setStyle(Paint.Style.STROKE);
            p2.setColor(Color.BLACK);
            p2.setStyle(Paint.Style.FILL);
            canvas.drawPath(path1, p1);


            path1.reset();
            path1.moveTo(width/4 + i*dx, height/8+ i*dy);
            path1.lineTo(width/2, height/2);
            path1.lineTo(width/4*3 - i*dx, height/8 + i*dy);

            path2.reset();
            path2.moveTo(width/4, height/8*7);
            path2.lineTo(width/4*3, height/8*7);
            path2.lineTo(width/4*3 - i*dx, height/8*7 - i*dy);
            path2.lineTo(width/4+i*dx, height/8*7 - i*dy);
            canvas.drawPath(path1, p2);
            canvas.drawPath(path2, p2);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        } else {
            if (angle < 180){

                angle += 3;
                canvas.rotate(angle, width/2, height/2);
                path1.reset();
                path1.moveTo(width/4, height/8);
                path1.lineTo(width/4*3, height/8*7);
                path1.lineTo(width/4, height/8*7);
                path1.lineTo(width/4*3, height/8);
                path1.lineTo(width/4, height/8);
                path2.reset();
                path2.moveTo(width/4, height/8*7);
                path2.lineTo(width/2, height/2);
                path2.lineTo(width/4*3, height/8*7);
                canvas.drawPath(path1, p1);
                canvas.drawPath(path2, p2);

                try {
                    Thread.sleep(1000/fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                angle = 0;
                i = 0;
            }
        }

        invalidate();
    }

    public void drawCancellState(Canvas canvas){

    }

    public void drawErrorState(Canvas canvas){

    }


}
