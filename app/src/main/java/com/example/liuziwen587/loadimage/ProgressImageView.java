package com.example.liuziwen587.loadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
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

    public int state = 0;
    public int progress = 0;
    public boolean isFinished = false;
    public Bitmap bitmap = null;
    public Paint p1 = null;
    public Paint p2 = null;
    public int padding = 10;
    RectF rectF = new RectF();
    Rect bound = new Rect();


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
        width = getWidth();
        height = getHeight();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (state){
            case STATE_INIT: drawInitState(canvas);break;
            case STATE_DOWNING: drawDowningAnim(canvas);break;
            case STATE_LOADING: drawLoadingAnim(canvas);break;
            case STATE_FINISH:setImageBitmap(bitmap);break;
            case STATE_ERROR: drawErrorState(canvas);break;
            case STATE_CANCELL:drawCancellState(canvas);break;
            default:break;
        }
    }

    public void setProgress(int progress) {
        state = STATE_DOWNING;
        this.progress = progress;
        if (progress >= 100) {
            isFinished = true;
        }
        invalidate();
    }

    public void drawDowningAnim(Canvas canvas){
        canvas.drawColor(Color.parseColor("#ff333333"));
        int radius = getHeight() / 2;
        p1.setColor(Color.parseColor("#FF999999"));
        p2.setColor(Color.parseColor("#FFffffff"));
        canvas.drawCircle(radius, radius, radius - padding, p1);
        float arc = (float)(progress) / 100 * 360;
        canvas.drawArc(rectF, 270, arc, true, p2);

    }

    public void drawLoadingAnim(Canvas canvas) {
        String toast = "加载中";
        canvas.drawColor(Color.WHITE);
        p1.setTextSize(height/5);
        p1.setColor(Color.BLACK);
        p1.getTextBounds(toast, 0, toast.length(), bound);
        p1.setTextAlign(Paint.Align.LEFT);
        canvas.drawColor(Color.WHITE);
        canvas.drawText(toast,(width-bound.width())/2, (height-bound.height())/2, p1);

    }

    public void drawInitState(Canvas canvas){
        setImageResource(R.mipmap.wait);
    }

    public void drawCancellState(Canvas canvas){
        String toast = "已取消";
        p1.setTextSize(height/5);
        p1.setColor(Color.BLACK);
        p1.getTextBounds(toast, 0, toast.length(), bound);
        p1.setTextAlign(Paint.Align.LEFT);
        canvas.drawColor(Color.WHITE);
        canvas.drawText(toast,(width-bound.width())/2, (height-bound.height())/2, p1);
    }

    public void drawErrorState(Canvas canvas){
        setImageResource(R.mipmap.error);
    }

}
