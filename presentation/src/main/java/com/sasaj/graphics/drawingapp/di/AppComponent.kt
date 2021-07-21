package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.authentication.changepassword.di.ForgotPasswordModule
import com.sasaj.graphics.drawingapp.authentication.changepassword.di.ForgotPasswordSubcomponent
import com.sasaj.graphics.drawingapp.authentication.login.di.LoginModule
import com.sasaj.graphics.drawingapp.authentication.login.di.LoginSubcomponent
import com.sasaj.graphics.drawingapp.authentication.register.di.RegisterModule
import com.sasaj.graphics.drawingapp.authentication.register.di.RegisterSubcomponent
import com.sasaj.graphics.drawingapp.authentication.register.di.VerifyModule
import com.sasaj.graphics.drawingapp.authentication.register.di.VerifySubcomponent
import com.sasaj.graphics.drawingapp.splash.di.SplashModule
import com.sasaj.graphics.drawingapp.splash.di.SplashSubcomponent
import dagger.Component
import javax.inject.Singleton
//
//@Singleton
//@Component(modules = [
//    (ApplicationModule::class),
//    (RemoteRepositoryModule::class),
//    (LocalRepositoryModule::class),
//    (RepositoryModule::class)
//])
//interface AppComponent {
//    fun plus(splashModule: SplashModule): SplashSubcomponent
//    fun plus(loginModule: LoginModule): LoginSubcomponent
//    fun plus(registerModule: RegisterModule): RegisterSubcomponent
//    fun plus(verifyModule: VerifyModule): VerifySubcomponent
//    fun plus(forgotPasswordModule: ForgotPasswordModule): ForgotPasswordSubcomponent
//    fun plus(mainModule: MainModule): MainSubcomponent
//}