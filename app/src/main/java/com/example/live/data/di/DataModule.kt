package com.example.live.data.di

import com.example.live.data.repository.HomeContentPostsRepository
import com.example.live.data.repository.PhotosRepository
import com.example.live.data.repository.PostsRepository
import com.example.live.data.repository.ProfileUserDataRepository
import com.example.live.data.repository.SearchContentPhotosRepository
import com.example.live.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsPhotosRepository(searchContentPhotosRepository: SearchContentPhotosRepository): PhotosRepository

    @Binds
    fun bindsPostsRepository(homeContentPostsRepository: HomeContentPostsRepository): PostsRepository

    @Binds
    fun bindsUserDataRepository(profileUserDataRepository: ProfileUserDataRepository): UserDataRepository
}