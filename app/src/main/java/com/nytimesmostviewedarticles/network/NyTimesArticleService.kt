package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.datatypes.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NyTimesApiService {
    /**
     * Get NY times most viewed articles from last week.
     */
    @GET("7.json")
    suspend fun getArticlesFromLastWeek(
        @Query("api-key") apiKey: String
    ): NetworkResponse
}