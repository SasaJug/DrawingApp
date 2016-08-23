package com.sasaj.graphics.drawingapp.views.misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.interfaces.ColorPicker;

/**
 * Created by User on 7/17/2016.
 */
public class SaturationBrightnessSelector extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private static final String TAG = "SaturationBrightness";
    Paint paint = new Paint();
    private int sourceWidth = 0;
    private int sourceHeight = 0;
    volatile private Bitmap targetBitmap;

    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public float hue;
    public float saturation;
    public float brightness;
    public ColorPicker picker;


    public SaturationBrightnessSelector(Context context) {
        super(context);
        init();
    }

    public SaturationBrightnessSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SaturationBrightnessSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    public void setColorPicker(ColorPicker picker){
        this.picker = picker;
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
        postInvalidate();
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {

            }
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sourceWidth = w;
        sourceHeight = h;
        saturation = w * 0.005f;
        brightness = 1 - h * 0.005f;
        targetBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        setMeasuredDimension(width, height);
    }

    public void setColor(float[] hsv) {
        if (targetBitmap != null) {

            hue = hsv[0];
            saturation = hsv[1];
            brightness = hsv[2];


            postInvalidate();
        }
    }

    private void makeNewImage(float hue) {
        int[] pixels = new int[sourceWidth * sourceHeight];
        float[] hsv;
        hsv = new float[3];

        for (int i = 0; i < sourceWidth; i++) {
            for (int j = 0; j < sourceHeight; j++) {

                hsv[0] = hue;
                hsv[1] = i * 0.005f;
                hsv[2] = 1 - j * 0.005f;

                int color = Color.HSVToColor(hsv);
                pixels[i + sourceWidth * j] = color;
            }
        }
        targetBitmap.setPixels(pixels, 0, sourceWidth, 0, 0, sourceWidth, sourceHeight);
    }

    @Override
    public void run() {
        while (running){
            if (!holder.getSurface().isValid()){
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            if (targetBitmap != null && canvas != null) {
                makeNewImage(hue);
                canvas.drawBitmap(targetBitmap, 0, 0, null);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        saturation = event.getX()*0.005f;
        brightness = 1 - event.getY()*0.005f;

//        int action = event.getAction();
//
//        switch (action) {
//
//            case MotionEvent.ACTION_DOWN:
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                break;
//
//            case MotionEvent.ACTION_UP:
//                break;
//
//            default:
//                return false;
//        }

        float[] hsv;
        hsv = new float[]{(float)hue, (float)saturation, (float)brightness};
        int color = Color.HSVToColor(hsv);

        picker.takeColor(color);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }

    public float getSaturation() {

        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}

















