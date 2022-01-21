package com.nytimesmostviewedarticles.datatypes

/**
 * Response model for main screen article cards.
 */
sealed class ArticleRowDataResponse {
    object Loading: ArticleRowDataResponse()
    object Empty: ArticleRowDataResponse()
    class Success(val articleRowData: List<ArticleRowData>): ArticleRowDataResponse()
    class Error(val message: String): ArticleRowDataResponse()
}

/**
 * Response model for detail screen.
 */
sealed class ArticleDataResponse {
    object Loading: ArticleDataResponse()
    object NoMatch: ArticleDataResponse()
    class Success(val articleData: ArticleData): ArticleDataResponse()
    class Error(val message: String): ArticleDataResponse()
}