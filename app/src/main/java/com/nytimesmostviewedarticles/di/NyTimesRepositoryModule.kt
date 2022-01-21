package com.nytimesmostviewedarticles.di

import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.network.NyTimesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface NyTimesRepositoryModule {

    @Binds
    fun providesArticleService(nyTimesRepo: NyTimesRepositoryImpl): NyTimesRepository
}