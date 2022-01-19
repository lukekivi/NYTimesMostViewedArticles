package com.example.nytimesmostviewedarticles.network

import com.example.nytimesmostviewedarticles.datatypes.NetworkResponse

interface ArticleService {
    suspend fun getArticleDataForUi(period: QueryPeriodLength): NetworkResponse
}

/**
 * An enum or data class should be defined with a valid set of period lengths.
 */
interface QueryPeriodLength {
    val length: Int
}