package com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.utils;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by User on 6/28/2016.
 */
public class PaintWrapper {

    private static final String TAG = "PaintWrapper";
    private static PaintWrapper instance;

    private int size;
    private float blur;
    private int alpha;
    private int color;
    private float[] hsv;

    private Paint paint;

    private PaintWrapper() {
        size = 10;
        blur = 1;
        alpha = 255;
        color = 0xFF000000;
        hsv = new float[3];

        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(size);
        paint.setAlpha(alpha);
        paint.setMaskFilter(new BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL));

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        paint.setStrokeWidth(size);
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
        paint.setMaskFilter(new BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL));

    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        paint.setAlpha(alpha);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    public float[] getHsv() {
        Color.colorToHSV(color, hsv);
        Log.e(TAG, "getHsv() called with: " + hsv[0]+hsv[1]+hsv[2]);
        return hsv;
    }

    public void setHsv(float[] hsv) {
        this.hsv = hsv;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public static PaintWrapper getInstance() {
        if(instance == null){
            instance = new PaintWrapper();
        }
        return instance;
    }
}
