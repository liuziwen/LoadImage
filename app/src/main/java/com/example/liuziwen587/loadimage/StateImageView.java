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
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by liuziwen on 16/9/5.
 */
public class StateImageView extends SurfaceView implements SurfaceHolder.Callback{

    public final static int STATE_INIT = 0;
    public final static int STATE_DOWNING = 1;
    public final static int STATE_FINISH = 2;
    public final static int STATE_CANCELL = 3;
    public final static int STATE_ERROR = 4;
    public final static int STATE_LOADING = 5;

    public boolean isRun ;

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

    private Thread drawUIThread;
    private SurfaceHolder surfaceHolder;

    public StateImageView(Context context) {
        super(context);
        init();
    }

    public StateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        drawUIThread = new DrawUIThread(getHolder());

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

            } else {
                angle = 0;
                i = 0;
            }
        }

    }

    public void drawCancellState(Canvas canvas){

    }

    public void drawErrorState(Canvas canvas){

    }

    public void setImageBitmap(Bitmap b){

    }




    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        MyLog.d("stateImageView: " + "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        MyLog.d("stateImageView: " + "surfaceChanged");
        isRun = true;
        drawUIThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        MyLog.d("stateImageView: " + "surfaceDestroyed");
        isRun = false;
    }

    class DrawUIThread extends Thread{

        private SurfaceHolder holder;

        public  DrawUIThread(SurfaceHolder holder) {
            this.holder =holder;
            isRun = true;
        }

        @Override
        public void run() {
            int count = 0;
            while(isRun) {
                Canvas canvas = null;
                try {
                    synchronized (holder) {
                        canvas = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                        canvas.drawColor(Color.WHITE);
                        switch (state){
                            case STATE_INIT: drawInitAnim(canvas);break;
                            case STATE_DOWNING: drawDowningAnim(canvas);break;
                            case STATE_LOADING: drawLoadingAnim(canvas);break;
                            case STATE_FINISH:setImageBitmap(bitmap);break;
                            case STATE_ERROR: drawErrorState(canvas);break;
                            case STATE_CANCELL:drawCancellState(canvas);break;
                            default:break;
                        }

                        Thread.sleep(1000/fps);//睡眠时间为1秒
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally {
                    if(canvas!= null) {
                        holder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。

                    }
                    switch (state){
                        case STATE_INIT: isRun = true;break;
                        case STATE_DOWNING: isRun = true;break;
                        case STATE_LOADING: isRun = true;break;
                        case STATE_FINISH:isRun = false;break;
                        case STATE_ERROR: isRun = false;break;
                        case STATE_CANCELL:isRun = false;break;
                        default:break;
                    }
                }
            }
        }
    }
}
