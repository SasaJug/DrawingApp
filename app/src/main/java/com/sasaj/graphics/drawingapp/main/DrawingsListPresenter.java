package com.sasaj.graphics.drawingapp.main;

import com.sasaj.graphics.drawingapp.data.Drawing;
import com.sasaj.graphics.drawingapp.data.source.DrawingsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DS on 4/21/2017.
 */

public class DrawingsListPresenter implements DrawingsListContract.UserActionsListener {
    private DrawingsListContract.View view;
    private DrawingsRepository repository;

    public DrawingsListPresenter(DrawingsListContract.View view, DrawingsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getDrawings() {
        view.showProgress();
        repository.getDrawings(new DrawingsRepository.LoadDrawingsCallback() {
            @Override
            public void onDrawingsLoaded(List<Drawing> drawings) {
                view.hideProgress();
                view.setDrawingsData(drawings);
            }

            @Override
            public void onDataNotAvailable() {
                view.hideProgress();
            }
        });
    }
}
