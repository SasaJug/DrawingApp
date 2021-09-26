package com.sasaj.domain.common

import io.reactivex.Observable
import io.reactivex.ObservableSource

class TestTransformer<T>: Transformer<T>() {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }

}