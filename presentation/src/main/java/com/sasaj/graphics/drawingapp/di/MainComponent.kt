package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.authentication.changepassword.ForgotPasswordViewModel
import com.sasaj.graphics.drawingapp.authentication.login.LoginViewModel
import com.sasaj.graphics.drawingapp.authentication.register.RegisterViewModel
import com.sasaj.graphics.drawingapp.authentication.register.VerifyViewModel
import com.sasaj.graphics.drawingapp.drawing.DrawingNavigationViewModel
import com.sasaj.graphics.drawingapp.drawing.DrawingViewModel
import com.sasaj.graphics.drawingapp.main.DrawingListNavigationViewModel
import com.sasaj.graphics.drawingapp.main.MainViewModel
import com.sasaj.graphics.drawingapp.splash.SplashViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [
    (ApplicationModule::class),
    (RemoteRepositoryModule::class),
    (LocalRepositoryModule::class),
    (RepositoryModule::class),
    (UseCaseModule::class)
])
interface MainComponent {
    /**
     * Injects required dependencies into the specified ViewModel.
     */
    fun inject(drawingListNavigationViewModel: DrawingListNavigationViewModel)
    fun inject(drawingViewModel: DrawingViewModel)
    fun inject(splashViewModel: SplashViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(registerViewModel: RegisterViewModel)
    fun inject(verifyViewModel: VerifyViewModel)
    fun inject(forgotPasswordViewModel: ForgotPasswordViewModel)
    fun inject(authenticationNavigationViewModel: AuthenticationNavigationViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(drawingNavigationViewModel: DrawingNavigationViewModel)

    @Component.Builder
    interface Builder {
        fun build(): MainComponent

        fun applicationModule(applicationModule: ApplicationModule): Builder
    }
}