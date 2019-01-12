package com.sasaj.graphics.drawingapp.authentication.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.LogIn
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
import org.mockito.Mockito.*


@Suppress("UNCHECKED_CAST")
@RunWith(AndroidJUnit4::class)
class LoginViewModelInstrumentationTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var userRepository: UserRepository

    private lateinit var loginUseCase: LogIn
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var viewStateObserver: Observer<LoginViewState>
    private lateinit var errorObserver: Observer<UIException?>

    @Before
    @UiThreadTest
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        loginUseCase = LogIn(TestTransformer(), userRepository)
        loginViewModel = LoginViewModel(loginUseCase)
        viewStateObserver = mock(Observer::class.java) as Observer<LoginViewState>
        errorObserver = mock(Observer::class.java) as Observer<UIException?>
        loginViewModel.loginLiveData.observeForever(viewStateObserver)
        loginViewModel.errorState.observeForever(errorObserver)
    }

    @Test
    @UiThreadTest
    fun logInNoUsernameAndPassword() {
        val argument = ArgumentCaptor.forClass(UIException::class.java)
        loginViewModel.logIn("", "")

        verify(errorObserver).onChanged(argument.capture())
        Assert.assertEquals(UIException.EMPTY_USERNAME or UIException.EMPTY_PASSWORD, argument.value.errorCode)
        Assert.assertEquals(IllegalArgumentException()::class.java, argument.value.cause!!::class.java)
    }


    @Test
    @UiThreadTest
    fun logInNoUsername() {
        val argument = ArgumentCaptor.forClass(UIException::class.java)
        loginViewModel.logIn("", "pass")

        verify(errorObserver).onChanged(argument.capture())
        Assert.assertEquals(UIException.EMPTY_USERNAME, argument.value.errorCode)
    }


    @Test
    @UiThreadTest
    fun logInNoPassword() {
        val argument = ArgumentCaptor.forClass(UIException::class.java)
        loginViewModel.logIn("user", "")

        verify(errorObserver).onChanged(argument.capture())
        Assert.assertEquals(UIException.EMPTY_PASSWORD, argument.value.errorCode)
    }

    @Test
    @UiThreadTest
    fun logInSuccess() {
        val argument = ArgumentCaptor.forClass(LoginViewState::class.java)
        `when`(userRepository.logIn(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Observable.just(true))
        loginViewModel.logIn("user", "pass")

        verify(viewStateObserver, times(3)).onChanged(argument.capture())

        Assert.assertEquals(false, argument.allValues[0].completed)
        Assert.assertEquals(false, argument.allValues[1].completed)
        Assert.assertEquals(true, argument.allValues[2].completed)
    }


    @Test
    @UiThreadTest
    fun logInFailed() {
        val argument = ArgumentCaptor.forClass(LoginViewState::class.java)
        val argumentError = ArgumentCaptor.forClass(UIException::class.java)
        `when`(userRepository.logIn(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Observable.error(UIException()))
        loginViewModel.logIn("user", "pass")

        verify(viewStateObserver, times(3)).onChanged(argument.capture())

        Assert.assertEquals(false, argument.allValues[0].completed)
        Assert.assertEquals(false, argument.allValues[1].completed)
        Assert.assertEquals(false, argument.allValues[2].completed)

        verify(errorObserver).onChanged(argumentError.capture())
        Assert.assertEquals(-1, argumentError.value.errorCode)
        Assert.assertEquals("Something went wrong", argumentError.value.message)
    }

}