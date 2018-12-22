package com.sasaj.graphics.drawingapp

import android.app.Application
import android.util.Log
import com.sasaj.graphics.drawingapp.authentication.changepassword.di.ForgotPasswordModule
import com.sasaj.graphics.drawingapp.authentication.changepassword.di.ForgotPasswordSubcomponent
import com.sasaj.graphics.drawingapp.authentication.login.di.LoginModule
import com.sasaj.graphics.drawingapp.authentication.login.di.LoginSubcomponent
import com.sasaj.graphics.drawingapp.authentication.register.di.RegisterModule
import com.sasaj.graphics.drawingapp.authentication.register.di.RegisterSubcomponent
import com.sasaj.graphics.drawingapp.authentication.register.di.VerifyModule
import com.sasaj.graphics.drawingapp.authentication.register.di.VerifySubcomponent
import com.sasaj.graphics.drawingapp.di.*
import com.sasaj.graphics.drawingapp.splash.di.SplashModule
import com.sasaj.graphics.drawingapp.splash.di.SplashSubcomponent
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by sjugurdzija on 4/22/2017.
 */

class DrawingApplication : Application() {

    private var splashComponent: SplashSubcomponent? = null
    private var loginComponent: LoginSubcomponent? = null
    private var registerComponent: RegisterSubcomponent? = null
    private var verifyComponent:VerifySubcomponent? = null
    private var forgotPasswordComponent:ForgotPasswordSubcomponent? = null
    private var mainComponent: MainSubcomponent? = null

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { e -> Log.e("App", e.message, e) }
        appComponent = DaggerAppComponent.builder()
                .applicationModule(ApplicationModule(applicationContext))
                .remoteRepositoryModule(RemoteRepositoryModule())
                .localRepositoryModule(LocalRepositoryModule())
                .repositoryModule(RepositoryModule())
                .build()
    }


    fun createSplashComponenet(): SplashSubcomponent {
        splashComponent = appComponent.plus(SplashModule())
        return splashComponent!!
    }

    fun releaseSplashComponent() {
        splashComponent = null
    }


    fun createLoginComponenet(): LoginSubcomponent {
        loginComponent = appComponent.plus(LoginModule())
        return loginComponent!!
    }

    fun releaseLoginComponent() {
        loginComponent = null
    }

    fun createRegisterComponenet(): RegisterSubcomponent {
        registerComponent = appComponent.plus(RegisterModule())
        return registerComponent!!
    }

    fun releaseRegisterComponent() {
        registerComponent = null
    }

    fun createVerifyComponenet(): VerifySubcomponent {
        verifyComponent = appComponent.plus(VerifyModule())
        return verifyComponent!!
    }

    fun releaseVerifyComponent() {
        verifyComponent = null
    }

    fun createForgotPasswordComponenet(): ForgotPasswordSubcomponent {
        forgotPasswordComponent = appComponent.plus(ForgotPasswordModule())
        return forgotPasswordComponent!!
    }

    fun releaseforgotPasswordComponent() {
        forgotPasswordComponent = null
    }

    fun createMainComponenet(): MainSubcomponent {
        mainComponent = appComponent.plus(MainModule())
        return mainComponent!!
    }

    fun releaseMainComponent() {
        mainComponent = null
    }
}
