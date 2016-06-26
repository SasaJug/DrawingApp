package com.sasaj.graphics.drawingapp;

import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.sasaj.graphics.drawingapp.views.CustomActionBar;
import com.sasaj.graphics.drawingapp.views.dialogs.SelectToolDialog;
import com.sasaj.graphics.drawingapp.views.layers.DrawingLayer;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mDialogContainer;
    public Paint currentPaint;
    private com.sasaj.graphics.drawingapp.views.layers.DrawingLayer drawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawing = (DrawingLayer) findViewById(R.id.drawing);

        mDialogContainer = (FrameLayout) findViewById(R.id.dialog_container);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        CustomActionBar customActionBar = new com.sasaj.graphics.drawingapp.views.CustomActionBar(this);
        getSupportActionBar().setCustomView(customActionBar);
        customActionBar.setDelegate(mDelegate);
        Toolbar parent = (Toolbar) customActionBar.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    CustomActionBar.Delegate mDelegate = new CustomActionBar.Delegate() {

        @Override
        public void startDialog(int option) {

            mDialogContainer.setVisibility(View.VISIBLE);
            switch(option){
                case CustomActionBar.SELECT_TOOL_OPTION:
                    SelectToolDialog selectToolDialog = new SelectToolDialog(MainActivity.this);
                    mDialogContainer.addView(selectToolDialog);
                    break;
                default:
                    mDialogContainer.removeAllViews();
            }
        }

        @Override
        public void removeActiveDialog() {
            mDialogContainer.removeAllViews();
        }

        @Override
        public void setPaint(Paint paint) {
            MainActivity.this.currentPaint = paint;
        }
    };
}
