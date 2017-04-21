package com.sasaj.graphics.drawingapp.main;

import com.sasaj.graphics.drawingapp.data.source.DrawingsRepository;

/**
 * Created by DS on 4/21/2017.
 */

public class DrawingsListPresenter implements DrawingsListContract.UserActionsListener{
    private DrawingsListContract.View view;
    private DrawingsRepository repository;

    public DrawingsListPresenter(DrawingsListContract.View view, DrawingsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getDrawings() {

    }
}
