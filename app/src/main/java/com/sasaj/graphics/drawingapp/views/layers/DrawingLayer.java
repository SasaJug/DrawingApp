package com.sasaj.graphics.drawingapp.views.layers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.utils.PaintWrapper;

/**
 * Created by User on 6/25/2016.
 */
public class DrawingLayer extends View {

    private static final String TAG = DrawingLayer.class.getSimpleName();

    private Context mContext;
    private Path mDrawPath;
    private Paint mCanvasPaint;
    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;
    private Paint paint;

    public DrawingLayer(Context context) {
        super(context);
        this.mContext = context;
        setupLayer();
    }

    public DrawingLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setupLayer();
    }

    public DrawingLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setupLayer();
    }

    private void setupLayer() {
        mDrawPath = new Path();
//        paint = PaintWrapper.getInstance().getPaint();
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setPaint(Paint paint) {
        Log.e(TAG, "setPaint: ");
        this.paint = paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, paint);
    }

    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();
        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                mDrawPath.moveTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
                mDrawPath.lineTo(touchX, touchY);
                mDrawCanvas.drawPath(mDrawPath, paint);
                mDrawPath.reset();
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    public Bitmap getBitmapFromView() {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
