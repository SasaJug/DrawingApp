package com.sasaj.graphics.drawingapp.ui.views.layers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sjugurdzija on 6/25/2016
 */
public class DrawingView extends View {

    private static final String TAG = DrawingView.class.getSimpleName();
    private static final float TOUCH_TOLERANCE = 4;

    private Path drawPath;
    private Paint canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Paint paint;
    private float x;
    private float y;

    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawPath = new Path();
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

        Drawable bgDrawable = getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(drawCanvas);
        } else {
            drawCanvas.drawColor(Color.WHITE);
        }
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
            default:
        }
        return true;
    }

    private void touchStart(float x, float y) {
        drawPath.moveTo(x, y);
        this.x = x;
        this.y = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - this.x);
        float dy = Math.abs(y - this.y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawPath.quadTo(this.x, this.y, (x + this.x) / 2, (y + this.y) / 2);
            this.x = x;
            this.y = y;
        }
    }

    private void touchUp() {
        drawCanvas.drawPath(drawPath, paint);
        drawPath.reset();
    }


    public Bitmap getBitmapFromView() {
        return canvasBitmap;
    }
}
