package com.sasaj.graphics.drawingapp.views.dialogs;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.sasaj.graphics.drawingapp.DrawingApplication;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.views.misc.BrushSample;

/**
 * Created by User on 6/25/2016.
 */
public class SelectToolDialog extends LinearLayout {

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
//        layoutParams.setMargins(30, 30, 30, 30);
        this.setLayoutParams(layoutParams);



        final BrushSample brushSample = (BrushSample) findViewById(R.id.brush_sample);
        SeekBar brushSizeSeekBar = (SeekBar) findViewById(R.id.brush_size_seekbar);
        SeekBar brushBlurSeekBar = (SeekBar) findViewById(R.id.brush_blur_seekbar);
        SeekBar brushAlphaSeekBar = (SeekBar) findViewById(R.id.brush_alpha_seekbar);
        SeekBar brushColorSeekBar = (SeekBar) findViewById(R.id.brush_color_seekbar);

        brushSizeSeekBar.setProgress((int) DrawingApplication.getPaint().getStrokeWidth());
        brushSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DrawingApplication.getPaint().setStrokeWidth(progress);
                brushSample.setDrawPaint(DrawingApplication.getPaint());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        brushBlurSeekBar.setProgress();
        brushBlurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    progress = 1;
                }
                DrawingApplication.getPaint().setMaskFilter(new BlurMaskFilter(progress, BlurMaskFilter.Blur.NORMAL));
                brushSample.setDrawPaint(DrawingApplication.getPaint());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        brushAlphaSeekBar.setProgress(DrawingApplication.getPaint().getAlpha());
        brushAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DrawingApplication.getPaint().setAlpha(progress);
                brushSample.setDrawPaint(DrawingApplication.getPaint());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        brushColorSeekBar.setProgress(DrawingApplication.getPaint().getAlpha());
        brushColorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int color = getColor(progress);
                DrawingApplication.getPaint().setColor(color);
                brushSample.setDrawPaint(DrawingApplication.getPaint());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        brushSample.setDrawPaint(DrawingApplication.getPaint());
    }

    private int getColor(int progress){
        int color = 0;
        int red = 0;
        int green = 0;
        int blue = 0;

        if(progress <= 255){
            red = 255;
            green = progress % 255;
            blue = 0;
        }else if(progress <= 510 && progress > 255){
            red = 255 - progress % 255;
            green = 255;
            blue = 0;
        }else if(progress <= 765 && progress > 510){
            red = 0;
            green = 255;
            blue = progress % 255;
        }else if(progress <= 1020 && progress > 765){
            red = 0;
            green = 255 - progress % 255;
            blue = 255;
        }else if(progress <= 1275 && progress > 1020){
            red = progress % 255;
            green = 0;
            blue = 255;
        }else if(progress <=1530 && progress > 1275){
            red = 255;
            green = 0;
            blue = 255 - progress % 255;
        }

        color = Color.argb(255, red,green, blue);

        return color;
    }
}
