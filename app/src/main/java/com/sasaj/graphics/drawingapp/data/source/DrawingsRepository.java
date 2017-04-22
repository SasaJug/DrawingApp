package com.sasaj.graphics.drawingapp.data.source;

import com.sasaj.graphics.drawingapp.data.Drawing;

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

   void  getDrawings(LoadDrawingsCallback callback);

   void  getDrawing(String id, LoadDrawingCallback callback);
}
