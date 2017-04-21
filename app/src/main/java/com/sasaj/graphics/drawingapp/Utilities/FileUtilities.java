package com.sasaj.graphics.drawingapp.Utilities;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 7/5/2016.
 */
public class FileUtilities {

    public static File getImageFile() throws IOException {
        String filename = createImageFileName();
        File exst = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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
}
