package com.sasaj.graphics.drawingapp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by DS on 4/22/2017.
 */

public class DrawingApplication extends Application {


    private static Context appContext;
    private static Handler applicationHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
        applicationHandler = new Handler(appContext.getMainLooper());
    }

    public static Context getContext() {
        return appContext;
    }

    public static Handler getApplicationHandler() {
        return applicationHandler;
    }


}
