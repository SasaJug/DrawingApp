package com.sasaj.graphics.drawingapp.authentication.register.di

import com.sasaj.graphics.drawingapp.authentication.register.RegisterFragment
import dagger.Subcomponent


@RegisterScope
@Subcomponent(modules = [RegisterModule::class])
interface RegisterSubcomponent {
    fun inject(registerFragment: RegisterFragment)
}