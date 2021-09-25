package com.sasaj.data.di

import com.sasaj.data.repositories.*
import com.sasaj.domain.BrushRepository
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsDrawingRepository(drawingRepositoryImpl: DrawingRepositoryImpl): DrawingRepository


    @Binds
    @Singleton
    fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository


    @Binds
    @Singleton
    fun bindsBrushRepository(brushRepositoryImpl: BrushRepositoryImpl): BrushRepository
}