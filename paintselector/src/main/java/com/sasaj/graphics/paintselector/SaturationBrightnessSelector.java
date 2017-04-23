package com.sasaj.graphics.paintselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.interfaces.PaintSelector;

/**
 * Created by User on 7/17/2016.
 */
public class SaturationBrightnessSelector extends View{

    private static final String TAG = "SaturationBrightness";
    private int sourceWidth = 0;
    private int sourceHeight = 0;

    public float hue;
    public float saturation;
    public float brightness;
    public PaintSelector picker;
    private float cx;
    private float cy;
    private Paint backgroundPaint;
    private Paint circlePaint;
    private float factor;

    final float[] hsv = { 1f, 1f, 1f };
    private Shader brightnessGradient;
    private Shader saturationGradient;

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

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        factor = 0;

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
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sourceWidth = w;
        sourceHeight = h;
        factor = 1/(float)w;
        cx = saturation/factor;
        cy = (1 - brightness)/factor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }


    public void setHue(float hue){
        this.hue = hue;

        hsv[0] = hue;
        picker.setColor(getColorFromHsv());

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (backgroundPaint == null) {
            backgroundPaint = new Paint();
            brightnessGradient = new LinearGradient(0f, 0f, 0f, sourceHeight, 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
        }
        int rgb = Color.HSVToColor(hsv);
        saturationGradient = new LinearGradient(0f, 0f, sourceWidth, 0f, 0xffffffff, rgb, Shader.TileMode.CLAMP);
        ComposeShader shader = new ComposeShader(brightnessGradient, saturationGradient, PorterDuff.Mode.MULTIPLY);
        backgroundPaint.setShader(shader);
        canvas.drawRect(0f, 0f, this.getMeasuredWidth(), this.getMeasuredHeight(), backgroundPaint);

        float circleHue = hue + 180; if(circleHue > 360) circleHue = circleHue - 360;
        int circleColor = Color.HSVToColor(new float []{circleHue, 1- cx * factor, cy*factor});
        circlePaint.setColor(circleColor);
        if(cx<0) cx=0; if(cx > sourceWidth) cx = sourceWidth; if (cy < 0) cy = 0; if(cy > sourceHeight) cy = sourceHeight;
        canvas.drawCircle(cx, cy, 10, circlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cx = event.getX();
        cy = event.getY();
        saturation = cx*factor;
        brightness = 1 - cy*factor;

        int color = getColorFromHsv();
        picker.setColor(color);
        invalidate();
        return true;
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

















