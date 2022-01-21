package com.nytimesmostviewedarticles.di

import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.network.NyTimesApiService
import com.nytimesmostviewedarticles.network.NyTimesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ArticleServiceModule {
    @Provides
    fun providesArticleService(nyTimesApiService: NyTimesApiService): NyTimesRepository {
        return NyTimesRepositoryImpl(nyTimesApiService)
    }
}