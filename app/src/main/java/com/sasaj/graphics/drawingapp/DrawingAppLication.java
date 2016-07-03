package com.sasaj.graphics.drawingapp;

import android.app.Application;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;

import com.sasaj.graphics.drawingapp.Utilities.CustomPaint;

/**
 * Created by User on 6/26/2016.
 */
public class DrawingApplication extends Application{

    private static CustomPaint sPaint;

    public DrawingApplication() {
        super();
    }

    public static CustomPaint getPaint() {
        if(sPaint == null){
            sPaint = new CustomPaint();
        }
        return sPaint;
    }

    public static void setPaint(CustomPaint paint) {
        sPaint = paint;
    }
}
