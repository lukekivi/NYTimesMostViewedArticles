package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.datatypes.NyTimesApiResults
import retrofit2.http.GET
import retrofit2.http.Query

interface NyTimesApiService {
    @GET("7.json")
    suspend fun getArticlesFromLastWeek(
        @Query("api-key") apiKey: String
    ): NyTimesApiResults
}