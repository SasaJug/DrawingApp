package com.sasaj.graphics.drawingapp.Utilities;

import android.graphics.BlurMaskFilter;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by User on 6/28/2016.
 */
public class CustomPaint {


    private static final String TAG = "CustomPaint";
    private int size;
    private float blur;
    private int alpha;
    private int color;
    private Paint paint;

    public CustomPaint() {
        this.size = 10;
        this.blur = 1;
        this.alpha = 255;
        this.color = 0xFF000000;

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

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
