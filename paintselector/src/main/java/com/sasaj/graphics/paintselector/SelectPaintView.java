package com.sasaj.graphics.paintselector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.interfaces.PaintSelector;
import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.utils.PaintWrapper;
import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.utils.SimpleOnSeekBarChangeListener;

/**
 * Created by User on 6/25/2016.
 */
public class SelectPaintView extends LinearLayout implements PaintSelector {

    private static final String TAG = "SelectPaintView";

    private static final int SIZE = 1;
    private static final int BLUR = 2;
    private static final int ALPHA = 3;
    private static final int HUE = 4;

    private SaturationBrightnessSelector sbSelector;
    private BrushSample brushSample;
    private SeekBar brushSizeSeekBar;
    private SeekBar brushBlurSeekBar;
    private SeekBar brushAlphaSeekBar;
    private SeekBar brushColorSeekBar;
    private PaintWrapper paintWrapper;


    public SelectPaintView(Context context) {
        super(context);
        init(context);
    }

    public SelectPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setOrientation(VERTICAL);
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lif.inflate(R.layout.select_paint_view_layout, this);

        paintWrapper = PaintWrapper.getInstance();

        brushSample = (BrushSample) findViewById(R.id.brush_sample);
        sbSelector = (SaturationBrightnessSelector) findViewById(R.id.sb_selector);
        sbSelector.setColorPicker(this);

        brushSizeSeekBar = (SeekBar) findViewById(R.id.brush_size_seekbar);
        brushBlurSeekBar = (SeekBar) findViewById(R.id.brush_blur_seekbar);
        brushAlphaSeekBar = (SeekBar) findViewById(R.id.brush_alpha_seekbar);
        brushColorSeekBar = (SeekBar) findViewById(R.id.brush_color_seekbar);


        brushSizeSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setProgressBar(SIZE, progress);
            }
        });

        brushBlurSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setProgressBar(BLUR, progress);
            }
        });

        brushAlphaSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setProgressBar(ALPHA, progress);
            }

        });

        brushColorSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setProgressBar(HUE, progress);
            }

        });

        setViews();
    }

    @Override
    public int getColor() {
        return paintWrapper.getColor();
    }

    @Override
    public void setColor(int color) {
        paintWrapper.setColor(color);
        paintWrapper.setAlpha(brushAlphaSeekBar.getProgress());
        brushSample.setPaint(paintWrapper.getPaint());
    }

    private void setViews() {
        brushSample.setPaint(paintWrapper.getPaint());
        brushSizeSeekBar.setProgress(paintWrapper.getSize());
        brushBlurSeekBar.setProgress((int) paintWrapper.getBlur());
        brushAlphaSeekBar.setProgress(paintWrapper.getAlpha());
        brushColorSeekBar.setProgress((int) paintWrapper.getHsv()[0]);
    }

    private void setProgressBar(int which, int value){

        switch(which){

            case SIZE:
                paintWrapper.setSize(value);
                brushSample.setPaint(paintWrapper.getPaint());
                break;

            case BLUR:
                if (value == 0) {
                    value = 1;
                }
                paintWrapper.setBlur(value);
                brushSample.setPaint(paintWrapper.getPaint());
                break;

            case ALPHA:
                paintWrapper.setAlpha(value);
                brushSample.setPaint(paintWrapper.getPaint());
                break;

            case HUE:
                sbSelector.setHue((float) value);
                break;

            default:

        }
    }
}





















