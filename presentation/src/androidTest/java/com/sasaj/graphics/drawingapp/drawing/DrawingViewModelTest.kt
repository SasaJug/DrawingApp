package com.sasaj.graphics.drawingapp.drawing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sasaj.domain.BrushRepository
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.entities.Optional
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.domain.usecases.SaveDrawing
import com.sasaj.graphics.drawingapp.authentication.login.LoginViewState
import com.sasaj.graphics.drawingapp.common.TestTransformer
import com.sasaj.graphics.drawingapp.common.UIException
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*


@Suppress("UNCHECKED_CAST")
@RunWith(AndroidJUnit4::class)
class DrawingViewModelTest {


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var drawingRepository: DrawingRepository
    private lateinit var brushRepository: BrushRepository
    private lateinit var saveDrawing: SaveDrawing
    private lateinit var getBrush: GetBrush
    private lateinit var saveBrush: SaveBrush
    private lateinit var drawingViewModel: DrawingViewModel

    private lateinit var viewStateObserver: Observer<DrawingViewState>
    private lateinit var errorObserver: Observer<UIException?>


    @Before
    fun setUp() {
        drawingRepository = mock(DrawingRepository::class.java)
        brushRepository = mock(BrushRepository::class.java)
        saveDrawing = SaveDrawing(TestTransformer(), drawingRepository)
        getBrush = GetBrush(TestTransformer(), brushRepository)
        saveBrush = SaveBrush(TestTransformer(),brushRepository)
        drawingViewModel = DrawingViewModel(saveDrawing, getBrush, saveBrush)

        viewStateObserver = mock(Observer::class.java) as Observer<DrawingViewState>
        errorObserver = mock(Observer::class.java) as Observer<UIException?>

        drawingViewModel.drawingLiveData.observeForever(viewStateObserver)
        drawingViewModel.errorState.observeForever(errorObserver)

    }


    @Test
    @UiThreadTest
    fun noLastBrush() {
        val argument = ArgumentCaptor.forClass(DrawingViewState::class.java)
        `when`(brushRepository.getCurrentBrush()).thenReturn(Observable.just(Optional.empty()))
        drawingViewModel.getLastBrush()


        Mockito.verify(viewStateObserver, Mockito.times(3)).onChanged(argument.capture())

        Assert.assertEquals(null, argument.allValues[2].brush)
        verifyZeroInteractions(errorObserver)
    }
}