package com.sasaj.graphics.drawingapp.drawing.di;

import android.graphics.Paint;

import com.sasaj.graphics.paintselector.com.sasaj.graphics.paintselector.utils.PaintWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DS on 6/6/2017.
 */

@Module
public class PaintModule {

    public PaintModule() {
    }

    @Provides
    @Singleton
    Paint providesPaint(){
        return PaintWrapper.getInstance().getPaint();
    }
}
