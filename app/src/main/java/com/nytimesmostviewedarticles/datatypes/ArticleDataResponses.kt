package com.nytimesmostviewedarticles.datatypes

/**
 * Response model for main screen article cards.
 */
sealed class ArticleRowDataResponse {
    object Loading: ArticleRowDataResponse()
    object Empty: ArticleRowDataResponse()
    class Success(val articleDataForRows: List<ArticleDataForRow>): ArticleRowDataResponse()
    class Error(val message: String): ArticleRowDataResponse()
}

/**
 * Response model for detail screen.
 */
sealed class ArticleDetailResponse {
    object Loading: ArticleDetailResponse()
    object NoMatch: ArticleDetailResponse()
    class Success(val articleDetailedData: ArticleDetailedData): ArticleDetailResponse()
    class Error(val message: String): ArticleDetailResponse()
}