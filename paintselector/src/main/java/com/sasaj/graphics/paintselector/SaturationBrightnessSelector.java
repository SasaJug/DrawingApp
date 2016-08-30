package com.sasaj.graphics.paintselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.interfaces.PaintSelector;
import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.utils.PaintWrapper;

/**
 * Created by User on 7/17/2016.
 */
public class SaturationBrightnessSelector extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private static final String TAG = "SaturationBrightness";
    private int sourceWidth = 0;
    private int sourceHeight = 0;
    volatile private Bitmap targetBitmap;

    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public float hue;
    public float saturation;
    public float brightness;
    public PaintSelector picker;
    private float cx;
    private float cy;
    private Paint circlePaint;

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

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(5);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.ROUND);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void setColorPicker(PaintSelector picker){
        this.picker = picker;

        int color = picker.getColor();
        float[] hsv = getHsvFromColor(color);
        hue = hsv[0];
        saturation = hsv[1];
        brightness = hsv[2];
        cx = saturation/0.005f;
        cy = (1 - brightness)/0.005f;
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
        targetBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        setMeasuredDimension(width, height);
    }


    public void setHue(float hue){
        this.hue = hue;

        float[] hsv;
        hsv = new float[]{this.hue, saturation, brightness};
        int color = Color.HSVToColor(hsv);
        picker.setColor(color);
        
        postInvalidate();
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
                float circleHue = hue + 180; if(circleHue > 360) circleHue = circleHue - 360;
                int circleColor = Color.HSVToColor(new float []{circleHue, 1- cx * 0.005f, cy*0.005f});
                circlePaint.setColor(circleColor);
                if(cx<0) cx=0; if(cx>sourceWidth) cx = sourceWidth; if (cy < 0) cy = 0; if(cy > sourceHeight) cy = sourceHeight;
                canvas.drawCircle(cx, cy, 10, circlePaint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cx = event.getX();
        cy = event.getY();
        saturation = event.getX()*0.005f;
        brightness = 1 - event.getY()*0.005f;

        int color = getColorFromHsv();
        picker.setColor(color);
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

    private float[] getHsvFromColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv;
    }

    private int getColorFromHsv(){
        float[] hsv;
        hsv = new float[]{hue, saturation, brightness};
        return Color.HSVToColor(hsv);
    }
}

















