package com.sasaj.graphics.drawingapp.views.layers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sasaj.graphics.drawingapp.DrawingApplication;
import com.sasaj.graphics.drawingapp.R;

/**
 * Created by User on 6/25/2016.
 */
public class DrawingLayer extends View {

    private Context mContext;
    private Path mDrawPath;
    private Paint mDrawPaint;
    private Paint mCanvasPaint;
    private int mPaintColor = 0xFF660000;
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

    private void setupLayer(){

        mDrawPath = new Path();
//
//        mDrawPaint = new Paint();
//        mDrawPaint.setColor(mPaintColor);
//        mDrawPaint.setAntiAlias(true);
//        mDrawPaint.setStrokeWidth(20);
//        mDrawPaint.setStyle(Paint.Style.STROKE);
//        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
//        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        paint = DrawingApplication.getPaint();
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCanvasBitmap = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
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
                mDrawCanvas.drawPath(mDrawPath, DrawingApplication.getPaint());
                mDrawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


}
