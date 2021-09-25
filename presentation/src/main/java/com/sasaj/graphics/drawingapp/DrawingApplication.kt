package com.sasaj.graphics.drawingapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by sjugurdzija on 4/22/2017.
 */

@HiltAndroidApp
class DrawingApplication : Application() {

//    private var splashComponent: SplashSubcomponent? = null
//    private var loginComponent: LoginSubcomponent? = null
//    private var registerComponent: RegisterSubcomponent? = null
//    private var verifyComponent:VerifySubcomponent? = null
//    private var forgotPasswordComponent:ForgotPasswordSubcomponent? = null
//    private var mainComponent: MainSubcomponent? = null

//    companion object {
//        lateinit var appComponent: AppComponent
//    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { e -> Log.e("App", e.message, e) }
//        appComponent = DaggerAppComponent.builder()
//                .applicationModule(ApplicationModule(applicationContext))
//                .remoteRepositoryModule(RemoteRepositoryModule())
//                .localRepositoryModule(LocalRepositoryModule())
//                .repositoryModule(RepositoryModule())
//                .build()
    }
//
//
//    fun createSplashComponent(): SplashSubcomponent {
//        splashComponent = appComponent.plus(SplashModule())
//        return splashComponent!!
//    }
//
//    fun releaseSplashComponent() {
//        splashComponent = null
//    }
//
//
//    fun createLoginComponent(): LoginSubcomponent {
//        loginComponent = appComponent.plus(LoginModule())
//        return loginComponent!!
//    }
//
//    fun releaseLoginComponent() {
//        loginComponent = null
//    }
//
//    fun createRegisterComponent(): RegisterSubcomponent {
//        registerComponent = appComponent.plus(RegisterModule())
//        return registerComponent!!
//    }
//
//    fun releaseRegisterComponent() {
//        registerComponent = null
//    }
//
//    fun createVerifyComponent(): VerifySubcomponent {
//        verifyComponent = appComponent.plus(VerifyModule())
//        return verifyComponent!!
//    }
//
//    fun releaseVerifyComponent() {
//        verifyComponent = null
//    }
//
//    fun createForgotPasswordComponent(): ForgotPasswordSubcomponent {
//        forgotPasswordComponent = appComponent.plus(ForgotPasswordModule())
//        return forgotPasswordComponent!!
//    }
//
//    fun releaseForgotPasswordComponent() {
//        forgotPasswordComponent = null
//    }
//
//    fun createMainComponent(): MainSubcomponent {
//        mainComponent = appComponent.plus(MainModule())
//        return mainComponent!!
//    }
//
//    fun releaseMainComponent() {
//        mainComponent = null
//    }
}
