package hhp.interactivebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hhphat on 7/2/2015.
 */
public class Animation extends View {
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();
    private twoDSprite imagesAni;
    private int width;
    private int height;
    public Animation(Context context) {
        super(context);
    }
    public Animation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public Animation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private Bitmap createScaledBitMapScreen(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap,width, height,true);
    }
    public void setSize(int w, int h){
        width = w;
        height = h;
    }
    public void setBitmap( Bitmap[] bm){
        int index = 0;
        while (index< bm.length){
            bm[index] = createScaledBitMapScreen(bm[index]);
            index++;
        }
        imagesAni = new twoDSprite(0,0,0,0,bm);
    }
    public void enableAnimated(){
        startTimer();
    }
    public void stopAnimated(){
        stopTimerTask();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        imagesAni.Draw(canvas);
    }

    private void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000
        timer.schedule(timerTask, 0, 1000); //

    }
    public void stopTimerTask() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
    private void Update(){
        imagesAni.Update();
        this.invalidate();
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Update();
                    }
                });
            }
        };
    }


    public int getViewWidth() {
        return width;
    }


    public int getViewHeight() {
        return height;
    }
}
