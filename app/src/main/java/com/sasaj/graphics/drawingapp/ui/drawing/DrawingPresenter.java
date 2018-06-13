package com.sasaj.graphics.drawingapp.ui.drawing;

import android.graphics.Bitmap;

import com.sasaj.graphics.drawingapp.domain.Drawing;
import com.sasaj.graphics.drawingapp.repository.DrawingsRepository;

/**
 * Created by DS on 4/23/2017.
 */

public class DrawingPresenter implements DrawingContract.UserActionsListener {
    DrawingContract.View view;
    DrawingsRepository repository;

    public DrawingPresenter(DrawingContract.View view, DrawingsRepository repository) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void saveDrawing(Bitmap bitmap) {
        view.showProgress();
        repository.saveDrawing(bitmap, new DrawingsRepository.SaveDrawingCallback() {
            @Override
            public void onDrawingSaved(Drawing drawings) {
                view.hideProgress();
            }

            @Override
            public void onError() {
                view.hideProgress();
            }
        });
    }
}
