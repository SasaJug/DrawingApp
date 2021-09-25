package com.sasaj.graphics.drawingapp.authentication.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.SignUp
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
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
@RunWith(AndroidJUnit4::class)
class RegisterViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var userRepository: UserRepository

    private lateinit var signupUseCase: SignUp
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var viewStateObserver: Observer<RegisterViewState>
    private lateinit var errorObserver: Observer<UIException?>

    @Before
    fun setUp() {
        userRepository = Mockito.mock(UserRepository::class.java)
        signupUseCase = SignUp(TestTransformer(), userRepository)
        registerViewModel = RegisterViewModel(signupUseCase)
        viewStateObserver = Mockito.mock(Observer::class.java) as Observer<RegisterViewState>
        errorObserver = Mockito.mock(Observer::class.java) as Observer<UIException?>
        registerViewModel.registerLiveData.observeForever(viewStateObserver)
        registerViewModel.errorState.observeForever(errorObserver)
    }

    @Test
    @UiThreadTest
    fun signUpNoUsernameEmailAndPassword() {
        val argument = ArgumentCaptor.forClass(UIException::class.java)

        // Any values except empty strings should cause test to fail.
        registerViewModel.register("", "", "", "")

        Mockito.verify(errorObserver).onChanged(argument.capture())
        Assert.assertEquals(UIException.EMPTY_USERNAME
                or UIException.EMPTY_EMAIL
                or UIException.EMPTY_PASSWORD
                or UIException.EMPTY_CONFIRM_PASSWORD, argument.value.errorCode)
        Assert.assertEquals(IllegalArgumentException()::class.java, argument.value.cause!!::class.java)
    }


    @Test
    @UiThreadTest
    fun registerSuccess() {
        val argument = ArgumentCaptor.forClass(RegisterViewState::class.java)
        val map = HashMap<String, String>()
        map["email"] = "email"
        Mockito.`when`(userRepository.signUp("user", "pass", map)).thenReturn(Observable.just(true))
        registerViewModel.register("user", "email", "pass", "pass")

        Mockito.verify(viewStateObserver, Mockito.times(3)).onChanged(argument.capture())

        Assert.assertEquals(false, argument.allValues[0].isConfirmed)
        Assert.assertEquals(false, argument.allValues[1].isConfirmed)
        Assert.assertEquals(true, argument.allValues[2].isConfirmed)
    }

}