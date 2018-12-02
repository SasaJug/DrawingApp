package com.sasaj.graphics.drawingapp.repository

import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject


class BrushRepositoryImplementation(val db: AppDatabase) : BrushRepository {

    var brush: Brush = Brush(0, 5, 1.0f, 0xff000000.toInt())
    private var brushSubject: Subject<Brush> = BehaviorSubject.create<Brush>()

    init {
        db.brushDao().getLastSaved()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { br ->
                    brushSubject.onNext(br)
                    brush = br
                }
    }

    override fun getBrushFlowable(): Flowable<Brush> {
        return brushSubject.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getCurrentBrush(): Brush {
        return brush
    }

    override fun setCurrentBrush(brush: Brush) {
        this.brush = brush
        saveCurrentBrush()
        brushSubject.onNext(brush)
    }

    override fun saveCurrentBrush() {
        db.brushDao().insert(brush)
    }
}