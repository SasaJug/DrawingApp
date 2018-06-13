package com.sasaj.graphics.drawingapp.ui.drawing.di;

import com.sasaj.graphics.drawingapp.ui.drawing.DrawingFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by DS on 6/6/2017.
 */

@Singleton
@Component(modules = {PaintModule.class})
public interface PaintComponent {
    void inject(DrawingFragment fragment);
}

