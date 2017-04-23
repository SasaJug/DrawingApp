package com.sasaj.graphics.drawingapp.data.source;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.sasaj.graphics.drawingapp.Utilities.FileUtilities;
import com.sasaj.graphics.drawingapp.Utilities.Utilities;
import com.sasaj.graphics.drawingapp.data.Drawing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DS on 4/21/2017.
 */

public class DrawingsRepositoryImplementation implements DrawingsRepository {

    private static final String TAG = DrawingsRepositoryImplementation.class.getSimpleName();
    private static DrawingsRepositoryImplementation INSTANCE;

    private DrawingsRepositoryImplementation(){
    }

    public synchronized static DrawingsRepositoryImplementation getInstance (){
        if(INSTANCE == null){
            INSTANCE = new DrawingsRepositoryImplementation();
        }
        return INSTANCE;
    }

    @Override
    public void getDrawings(final LoadDrawingsCallback callback) {
        Utilities.looperThread.postRunnable(new Runnable() {
            @Override
            public void run() {
                File dir = FileUtilities.getAlbumStorageDir();
                final List<Drawing> drawings = new ArrayList<>();
                File[] list = dir.listFiles();
                if (list != null && list.length > 0) {
                    for (File file : list) {
                        Drawing drawing = new Drawing();
                        drawing.setImagePath(file.getAbsolutePath());
                        drawings.add(drawing);
                        Log.e(TAG, "run: " + file.getAbsolutePath());
                    }
                    Utilities.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDrawingsLoaded(drawings);
                        }
                    });
                } else {
                    callback.onDataNotAvailable();
                }
            }
        });
    }

    @Override
    public void getDrawing(String id, LoadDrawingCallback callback) {

    }

    @Override
    public void saveDrawing(final Bitmap bitmap, final  SaveDrawingCallback callback) {
        Utilities.looperThread.postRunnable(new Runnable() {
            @Override
            public void run() {
                File imageFile;
                try {
                    imageFile = FileUtilities.getImageFile();
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();

                    final Drawing drawing = new Drawing();
                    drawing.setImagePath(imageFile.getAbsolutePath());
                    Utilities.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDrawingSaved(drawing);
                        }
                    });


                } catch (IOException e) {
                    Log.e("error", e.getMessage());
                    e.printStackTrace();
                    Utilities.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError();
                        }
                    });
                }
            }
        });
    }
}