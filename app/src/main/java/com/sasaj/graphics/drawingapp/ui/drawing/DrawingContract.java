package com.sasaj.graphics.drawingapp.ui.drawing;

import android.graphics.Bitmap;

/**
 * Created by DS on 4/23/2017.
 */

public interface DrawingContract {


    interface View {
        void showProgress();
        void hideProgress();
    }

    interface UserActionsListener {

        void saveDrawing(Bitmap bitmap);
    }
}
