package com.sasaj.graphics.drawingapp.repository;

import android.graphics.Bitmap;

import com.sasaj.graphics.drawingapp.domain.Drawing;

import java.util.List;

/**
 * Created by DS on 4/21/2017.
 */

public interface DrawingsRepository {


    interface LoadDrawingsCallback {

        void onDrawingsLoaded(List<Drawing> drawings);

        void onDataNotAvailable();
    }

    interface LoadDrawingCallback {

        void onDrawingLoaded(Drawing drawings);

        void onDataNotAvailable();
    }


    interface SaveDrawingCallback {

        void onDrawingSaved(Drawing drawings);

        void onError();
    }

    void getDrawings(LoadDrawingsCallback callback);

    void getDrawing(String id, LoadDrawingCallback callback);

    void saveDrawing(Bitmap bitmap, SaveDrawingCallback callback);
}
