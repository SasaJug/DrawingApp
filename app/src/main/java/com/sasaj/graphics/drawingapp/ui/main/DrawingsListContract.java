package com.sasaj.graphics.drawingapp.ui.main;

import com.sasaj.graphics.drawingapp.domain.Drawing;

import java.util.List;

public interface DrawingsListContract {


    interface View {
        void showProgress();
        void hideProgress();
        void setDrawingsData(List<Drawing> drawings);
    }

    interface UserActionsListener {

        void getDrawings();

    }
}
