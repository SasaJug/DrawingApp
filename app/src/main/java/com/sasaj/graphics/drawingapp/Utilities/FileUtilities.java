package com.sasaj.graphics.drawingapp.Utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 7/5/2016.
 */
public class FileUtilities {
    private static final String TAG = FileUtilities.class.getSimpleName();

    public static File getImageFile() throws IOException {
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


    private static String createImageFileName() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(new Date());
        String imageFileName = "drawing_" + timeStamp + ".jpg";
        return imageFileName;
    }

    public static File getAlbumStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DrawingApp");
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

}
