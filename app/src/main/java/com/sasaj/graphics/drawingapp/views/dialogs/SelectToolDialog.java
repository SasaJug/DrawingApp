package com.sasaj.graphics.drawingapp.views.dialogs;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.sasaj.graphics.drawingapp.DrawingApplication;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.Utilities.CustomPaint;
import com.sasaj.graphics.drawingapp.interfaces.ColorPicker;
import com.sasaj.graphics.drawingapp.views.misc.BrushSample;
import com.sasaj.graphics.drawingapp.views.misc.SaturationBrightnessSelector;

/**
 * Created by User on 6/25/2016.
 */
public class SelectToolDialog extends LinearLayout implements ColorPicker{

    private static final String TAG = "SelectToolDialog";
    private SaturationBrightnessSelector sbSelector;
    private CustomPaint customPaint;
    private Paint paint;
    private BrushSample brushSample;

    public SelectToolDialog(Context context) {
        super(context);
        setupDialog(context);
    }

    public SelectToolDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDialog(context);
    }

    public SelectToolDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupDialog(context);
    }

    private void setupDialog(Context context){

        this.setBackgroundColor(Color.WHITE);
        this.setOrientation(VERTICAL);
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lif.inflate(R.layout.select_tool_dialog_layout, this);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        brushSample = (BrushSample) findViewById(R.id.brush_sample);
        sbSelector = (SaturationBrightnessSelector) findViewById(R.id.sb_selector);
        sbSelector.setColorPicker(this);

        SeekBar brushSizeSeekBar = (SeekBar) findViewById(R.id.brush_size_seekbar);
        SeekBar brushBlurSeekBar = (SeekBar) findViewById(R.id.brush_blur_seekbar);
        SeekBar brushAlphaSeekBar = (SeekBar) findViewById(R.id.brush_alpha_seekbar);
        SeekBar brushColorSeekBar = (SeekBar) findViewById(R.id.brush_color_seekbar);

        customPaint = DrawingApplication.getPaint();
        paint = DrawingApplication.getPaint().getPaint();

        brushSizeSeekBar.setProgress(customPaint.getSize());
        brushSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                customPaint.setSize(progress);
                brushSample.setDrawPaint(paint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        brushBlurSeekBar.setProgress((int) customPaint.getBlur());
        brushBlurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                customPaint.setBlur(progress);
                brushSample.setDrawPaint(paint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        brushAlphaSeekBar.setProgress(customPaint.getAlpha());
        brushAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                customPaint.setAlpha(progress);
                brushSample.setDrawPaint(paint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        brushColorSeekBar.setProgress((int)setChosenColor(customPaint.getColor())[0]);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sbSelector.setColor(setChosenColor(customPaint.getColor()));
            }
        });
        thread.start();


        brushColorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int color = Color.HSVToColor(getColor((float) progress));
                sbSelector.setColor(getColor((float)progress));
                customPaint.setColor(color);
                brushSample.setDrawPaint(paint);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        brushSample.setDrawPaint(paint);
        float[] hsv;
        hsv = new float[3];
        Color.colorToHSV(customPaint.getColor(), hsv);
    }

    private float[] getColor(float progress){
        float hue = progress;
        float saturation = sbSelector.getSaturation();
        float brightness = sbSelector.getBrightness();
        Log.e(TAG, progress +" "+hue + " " + saturation + " " + brightness);

        float[] hsv;
        hsv = new float[]{hue, saturation, brightness};
        int color = Color.HSVToColor(hsv);

        return hsv;
    }

    private float[] setChosenColor(int color){
        float[] hsv;
        hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv;
    }

    @Override
    public void takeColor(int color) {
        customPaint.setColor(color);
        brushSample.setDrawPaint(paint);
        Log.e(TAG,"called");
    }
}





















