package com.sasaj.graphics.drawingapp;

import android.app.Application;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;

/**
 * Created by User on 6/26/2016.
 */
public class DrawingApplication extends Application{

    private static Paint sPaint;


    public DrawingApplication() {
        super();
    }

    public static Paint getPaint() {
        if(sPaint == null){
            int blur = 5;
            sPaint = new Paint();
            sPaint.setColor(0xFF660000);
            sPaint.setAntiAlias(true);
            sPaint.setStrokeWidth(20);
            sPaint.setMaskFilter(new BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL));
            sPaint.setStyle(Paint.Style.STROKE);
            sPaint.setStrokeJoin(Paint.Join.ROUND);
            sPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        return sPaint;
    }

    public static void setPaint(Paint paint) {
        sPaint = paint;
    }
}
