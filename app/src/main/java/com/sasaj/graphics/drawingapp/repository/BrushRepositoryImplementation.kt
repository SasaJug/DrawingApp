package com.sasaj.graphics.drawingapp.repository

import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


class BrushRepositoryImplementation(val db: AppDatabase) : BrushRepository {

    var brush: Brush = Brush(0, 5, 1.0f, 0xff000000.toInt())

    var brushObservable: Subject<Brush> = PublishSubject.create<Brush>()

    override fun getCurrentBrush(): Observable<Brush> {
        return brushObservable.startWith(brush)
    }

    override fun setCurrentBrush(brush: Brush) {
        this.brush = brush
        brushObservable.onNext(brush)
    }


    override fun saveCurrentBrush() {

    }
}