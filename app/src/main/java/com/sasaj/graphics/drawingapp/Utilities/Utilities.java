package com.sasaj.graphics.drawingapp.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sasaj.graphics.drawingapp.DrawingApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by DS on 4/22/2017.
 */

public class Utilities {
    private static final Object lock = new Object();
    public static LooperThread looperThread = new LooperThread("looperThread");

    public static void runOnUiThread(Runnable runnable) {
        synchronized (lock) {
            DrawingApplication.getApplicationHandler().post(runnable);
        }
    }

    public static void hideKeyboard(View view) {
        ((InputMethodManager) DrawingApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        ((InputMethodManager) DrawingApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static String getLocalDate(String utcDate) {

        SimpleDateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat localDateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        localDateFormatter.setTimeZone(TimeZone.getDefault());

        String date = "";
        try {
            date = localDateFormatter.format(utcFormatter.parse(utcDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getLocalTime(String utcDate) {

        SimpleDateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat localTimeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);
        localTimeFormatter.setTimeZone(TimeZone.getDefault());

        String time = "";
        try {
            time = localTimeFormatter.format(utcFormatter.parse(utcDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
