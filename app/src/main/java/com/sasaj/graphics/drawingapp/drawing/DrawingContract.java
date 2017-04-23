package com.sasaj.graphics.drawingapp.drawing;

import android.graphics.Bitmap;

import com.sasaj.graphics.drawingapp.data.Drawing;

import java.util.List;

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
