package com.sasaj.graphics.drawingapp.main;

import com.sasaj.graphics.drawingapp.data.Drawing;

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
