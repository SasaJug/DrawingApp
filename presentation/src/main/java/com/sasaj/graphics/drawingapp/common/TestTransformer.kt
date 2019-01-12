package com.sasaj.graphics.drawingapp.common

import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * Created by Yossi Segev on 13/11/2017.
 */
class TestTransformer<T>: Transformer<T>() {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }

}