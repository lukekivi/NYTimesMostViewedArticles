package com.nytimesmostviewedarticles.di

import com.nytimesmostviewedarticles.network.ArticleApi
import com.nytimesmostviewedarticles.network.NyTimesApiService
import com.nytimesmostviewedarticles.network.NyTimesArticleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ArticleServiceModule {
    @Provides
    fun providesArticleService(nyTimesApiService: NyTimesApiService): ArticleApi {
        return NyTimesArticleApi(nyTimesApiService)
    }
}