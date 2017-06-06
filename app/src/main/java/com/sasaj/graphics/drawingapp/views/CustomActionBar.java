package com.sasaj.graphics.drawingapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sasaj.graphics.drawingapp.R;

/**
 * Created by User on 6/25/2016.
 */
public class CustomActionBar extends LinearLayout {

    private ImageButton mSelectTool;
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

        this.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        mSelectTool = (ImageButton) findViewById(R.id.select_tool);
        mSelectTool.setOnClickListener(buttonClick);

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
                    mDelegate.startToolsDialog();
            }else if (v == mSave){
                    mDelegate.startSaveDialog();
            }
        }
    };

    public interface Delegate{
        void startToolsDialog();
        void startSaveDialog();
    }

}
