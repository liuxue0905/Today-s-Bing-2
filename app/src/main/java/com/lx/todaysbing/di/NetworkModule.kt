package com.lx.todaysbing.di

import com.lx.todaysbing.api.BingApisService
import com.lx.todaysbing.api.BingService
import com.lx.todaysbing.api.GalleryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideBingService(): BingService {
        return BingService.create()
    }

    @Singleton
    @Provides
    fun provideGalleryService(): GalleryService {
        return GalleryService.create()
    }

    @Singleton
    @Provides
    fun provideBingApisService(): BingApisService {
        return BingApisService.create()
    }
}