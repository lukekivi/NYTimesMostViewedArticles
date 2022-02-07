package com.nytimesmostviewedarticles.di

import com.nytimesmostviewedarticles.network.NetworkConnectionReceiver
import com.nytimesmostviewedarticles.network.NetworkConnectionReceiverImpl
import com.nytimesmostviewedarticles.network.NetworkConnectionService
import com.nytimesmostviewedarticles.network.NetworkConnectionServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
interface NetworkConnectionServiceModule {

    @Binds
    fun bindsNetworkConnectionService(networkConnectionService: NetworkConnectionServiceImpl): NetworkConnectionService

    @Binds
    fun bindsNetworkConnectionReceiver(networkConnectionCallback: NetworkConnectionReceiverImpl): NetworkConnectionReceiver
}