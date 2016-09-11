package com.sasaj.graphics.paintselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by User on 6/25/2016.
 */
public class BrushSample extends View {

    private static final String TAG = "BrushSample";
    Path path;
    Paint paint;

    public BrushSample(Context context) {
        super(context);
        init();
    }

    public BrushSample(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrushSample(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    public void setPaint(Paint paint) {
        this.paint = paint;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width/4;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path = createPath(w, h);
    }

    private Path createPath(int w, int h) {
        Path path = new Path();
        float widthUnit = w/15;
        float heightUnit = h/6;

        path.moveTo(widthUnit * 2, heightUnit * 3);
        path.quadTo(widthUnit*4, heightUnit*2, widthUnit*6,  heightUnit*3);
        path.quadTo( widthUnit*8, heightUnit*4, widthUnit*10,  heightUnit*3);
        path.quadTo(widthUnit*11.5f,  heightUnit*2.25f, widthUnit*13,  heightUnit*2.75f);
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(path != null && paint != null){
            canvas.drawPath(path, paint);
        }
    }
}
