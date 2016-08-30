package com.sasaj.graphics.drawingapp.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sasaj.graphics.drawingapp.R;

/**
 * Created by User on 6/25/2016.
 */
public class CustomActionBar extends LinearLayout {

    public static final int NONE = 0;
    public static final int SELECT_TOOL_OPTION = 1;
    public static final int SELECT_BACKGROUND_OPTION = 2;
    public static final int SELECT_LAYER_OPTION = 3;
    public static final int SAVE_OPTION = 4;

    public int mCurrentOption = NONE;

    private ImageButton mSelectTool;
    private ImageButton mSelectBackground;
    private ImageButton mSelectLayer;
    private ImageButton mSave;

    private Delegate mDelegate;

    public CustomActionBar(Context context) {
        super(context);
        initialize(context);
    }

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lif.inflate(R.layout.action_bar_layout, this);

        this.setBackgroundColor(Color.WHITE);

        mSelectTool = (ImageButton) findViewById(R.id.select_tool);
        mSelectTool.setOnClickListener(buttonClick);

        mSelectBackground = (ImageButton) findViewById(R.id.select_background);
        mSelectBackground.setOnClickListener(buttonClick);

        mSelectLayer = (ImageButton) findViewById(R.id.select_layer);
        mSelectLayer.setOnClickListener(buttonClick);

        mSave = (ImageButton) findViewById(R.id.save);
        mSave.setOnClickListener(buttonClick);
    }


    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }


    View.OnClickListener buttonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v == mSelectTool){
                if(mCurrentOption == SELECT_TOOL_OPTION){
                    mCurrentOption = NONE;
                    mSelectTool.getDrawable().clearColorFilter();
                    mDelegate.removeActiveDialog();
                }else{
                    mCurrentOption = SELECT_TOOL_OPTION;
                    mSelectTool.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    mDelegate.removeActiveDialog();
                    mDelegate.startDialog(SELECT_TOOL_OPTION);
                }
            }else if (v == mSave){
                    mDelegate.startSaveDialog();
            }

        }
    };

    public static interface Delegate{
        public void startDialog(int option);
        public void removeActiveDialog();
        public void setPaint(Paint paint);
        public void startSaveDialog();
    }

}
