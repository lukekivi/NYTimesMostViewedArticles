package com.example.nytimesmostviewedarticles.di

import com.example.nytimesmostviewedarticles.network.ArticleService
import com.example.nytimesmostviewedarticles.network.NyTimesArticleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ArticleServiceModule {
    @Provides
    fun providesArticleService(): ArticleService {
        return NyTimesArticleService()
    }
}