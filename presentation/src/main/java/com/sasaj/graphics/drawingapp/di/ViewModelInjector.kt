package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.splash.SplashViewModel
import com.sasaj.graphics.drawingapp.viewmodel.*
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified ViewModel.
     */
    fun inject(drawingListViewModel: DrawingListViewModel)
    fun inject(drawingViewModel: DrawingViewModel)
    fun inject(splashViewModel: SplashViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(registerViewModel: RegisterViewModel)
    fun inject(verifyViewModel: VerifyViewModel)
    fun inject(changePasswordViewModel: ChangePasswordViewModel)
    fun inject(authenticationNavigationViewModel: AuthenticationNavigationViewModel)
    fun inject(mainViewModel: MainViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun applicationModule(applicationModule: ApplicationModule): Builder
    }
}