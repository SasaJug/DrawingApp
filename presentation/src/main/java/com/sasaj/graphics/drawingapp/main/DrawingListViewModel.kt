package com.sasaj.graphics.drawingapp.main

import com.sasaj.domain.entities.Drawing
import com.sasaj.domain.usecases.GetDrawings
import com.sasaj.domain.usecases.SyncDrawings
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class DrawingListViewModel : BaseViewModel() {


    @Inject
    lateinit var syncDrawings: SyncDrawings

    @Inject
    lateinit var getDrawings: GetDrawings

    fun getDrawings(): Observable<List<Drawing>> {
        return getDrawings.getDrawings()
    }

    fun syncDrawings(): Observable<Boolean> {
        return syncDrawings.syncDrawings()
    }
}
