package com.sasaj.graphics.drawingapp.repository;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.sasaj.graphics.drawingapp.domain.Drawing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sjugurdzija on 4/21/2017.
 */

public class DrawingsRepositoryImplementation implements DrawingsRepository {

    private static final String TAG = DrawingsRepositoryImplementation.class.getSimpleName();
    private static DrawingsRepositoryImplementation INSTANCE;

    private DrawingsRepositoryImplementation() {
    }

    public synchronized static DrawingsRepositoryImplementation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DrawingsRepositoryImplementation();
        }
        return INSTANCE;
    }

    @Override
    public List<Drawing> getDrawings() {
        File dir = getAlbumStorageDir();
        final List<Drawing> drawings = new ArrayList<>();
        File[] list = dir.listFiles();
        if (list != null && list.length > 0) {
            for (File file : list) {
                Drawing drawing = new Drawing(file.getAbsolutePath(), file.lastModified());
                drawings.add(drawing);
            }

        }
        return drawings;
    }

    @Override
    public void saveDrawing(final Bitmap bitmap) {
        try {
            File imageFile;
            imageFile = getImageFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();


        } catch (IOException e) {
            Log.e("error", e.getMessage());
        }
    }

    private File getImageFile() throws IOException {
        String filename = createImageFileName();
        File exst = getAlbumStorageDir();
        String exstPath = exst.getPath();
        File dir = new File(exstPath);

        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException se) {
                //handle it
            }
        }
        File file = new File(dir, filename);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }


    private String createImageFileName() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(new Date());
        String imageFileName = "drawing_" + timeStamp + ".jpg";
        return imageFileName;
    }

    private File getAlbumStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DrawingApp");
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

}
