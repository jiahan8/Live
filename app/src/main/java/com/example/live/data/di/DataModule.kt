package com.example.live.data.di

import com.example.live.data.repository.LiveRepository
import com.example.live.data.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsLiveRepository(photoRepository: PhotoRepository): LiveRepository

}