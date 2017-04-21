package com.sasaj.graphics.drawingapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sasaj.graphics.drawingapp.Utilities.FileUtilities;
import com.sasaj.graphics.drawingapp.views.CustomActionBar;
import com.sasaj.graphics.drawingapp.views.fragments.SelectPaintDialogFragment;
import com.sasaj.graphics.drawingapp.views.layers.DrawingLayer;
import com.sasaj.graphics.paintselector.SelectPaintView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawingActivity extends AppCompatActivity{

    public static final String ORIENTATION = "ORIENTATION";
    public static final int LANDSCAPE = 0;
    public static final int PORTRAIT = 1;

    public Paint currentPaint;
    private com.sasaj.graphics.drawingapp.views.layers.DrawingLayer drawing;

    public static Intent createIntent (Context context, int orientation){
        Intent intent = new Intent(context, DrawingActivity.class);
        intent.putExtra(ORIENTATION, orientation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setScreenOrientation(getIntent().getIntExtra(ORIENTATION, PORTRAIT));

        setContentView(R.layout.activity_drawing);
        drawing = (DrawingLayer) findViewById(R.id.drawing);

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

    public void saveImageToMemory(Bitmap bitmap) {
        SaveDrawing saveDrawingTask = new SaveDrawing();
        saveDrawingTask.execute(bitmap);
    }


    private SelectPaintView selectToolDialog;
    CustomActionBar.Delegate mDelegate = new CustomActionBar.Delegate() {

        @Override
        public void startDialog(int option) {

            switch (option) {
                case CustomActionBar.SELECT_TOOL_OPTION:
//                    selectToolDialog = new SelectPaintView(DrawingActivity.this);
//                    mDialogContainer.addView(selectToolDialog);
//
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT);
//                    selectToolDialog.setLayoutParams(layoutParams);
                    DialogFragment newFragment = SelectPaintDialogFragment.newInstance(new Bundle());
                    newFragment.show(getFragmentManager(), "com.sasaj.graphics.drawingapp.dialog");

                    break;

                default:

            }
        }


        @Override
        public void setPaint(Paint paint) {
            DrawingActivity.this.currentPaint = paint;
        }


        @Override
        public void startSaveDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrawingActivity.this);

            // set title
            alertDialogBuilder.setTitle("DrawingApp");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Save image?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveImageToMemory(drawing.getBitmapFromView());
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    };

    private void setScreenOrientation(int orientation){
        if(orientation == PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private class SaveDrawing extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            File imageFile;
            try {
                imageFile = FileUtilities.getImageFile();
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

    }
}

