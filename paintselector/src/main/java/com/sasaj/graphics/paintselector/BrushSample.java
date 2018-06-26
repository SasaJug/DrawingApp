package com.sasaj.graphics.paintselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
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
    private Paint bgPaint;

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
        int height = width / 4;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path = createPath(w, h);
        bgPaint = createCheckerBoard(h / 6);
    }

    private Path createPath(int w, int h) {
        Path path = new Path();
        float widthUnit = w / 15;
        float heightUnit = h / 6;

        path.moveTo(widthUnit * 2, heightUnit * 3);
        path.quadTo(widthUnit * 4, heightUnit * 2, widthUnit * 6, heightUnit * 3);
        path.quadTo(widthUnit * 8, heightUnit * 4, widthUnit * 10, heightUnit * 3);
        path.quadTo(widthUnit * 11.5f, heightUnit * 2.25f, widthUnit * 13, heightUnit * 2.75f);
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bgPaint != null)
            canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
        if (path != null && paint != null) {
            canvas.drawPath(path, paint);
        }
    }

    private Paint createCheckerBoard(int pixelSize) {
        Bitmap bitmap = Bitmap.createBitmap(pixelSize * 2, pixelSize * 2, Bitmap.Config.ARGB_8888);

        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(0x22000000);

        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect(0, 0, pixelSize, pixelSize);
        canvas.drawRect(rect, fill);
        rect.offset(pixelSize, pixelSize);
        canvas.drawRect(rect, fill);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.REPEAT, BitmapShader.TileMode.REPEAT));
        return paint;
    }
}
