package com.sasaj.graphics.drawingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.sasaj.graphics.drawingapp.Utilities.FileUtilities;
import com.sasaj.graphics.drawingapp.views.CustomActionBar;
import com.sasaj.graphics.drawingapp.views.dialogs.SelectToolDialog;
import com.sasaj.graphics.drawingapp.views.layers.DrawingLayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public void saveImageToMemory(Bitmap bitmap) {
        SaveDrawing saveDrawingTask = new SaveDrawing();
        saveDrawingTask.execute(bitmap);
    }


    CustomActionBar.Delegate mDelegate = new CustomActionBar.Delegate() {

        @Override
        public void startDialog(int option) {

            mDialogContainer.setVisibility(View.VISIBLE);
            switch (option) {
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

        @Override
        public void startSaveDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

