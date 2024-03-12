package com.example.live.network.di

import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.retrofit.LiveRetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun bindsLiveNetworkDataSource(liveRetrofitNetwork: LiveRetrofitNetwork): LiveNetworkDataSource

}