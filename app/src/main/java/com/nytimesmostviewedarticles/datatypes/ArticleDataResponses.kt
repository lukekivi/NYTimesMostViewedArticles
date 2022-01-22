package com.nytimesmostviewedarticles.datatypes

/**
 * Response model for repository caching.
 */
sealed class CachedArticleResponse {
    object NoRequest: CachedArticleResponse()
    object Empty: CachedArticleResponse()
    class Success(val articleDataList: List<ArticleData>): CachedArticleResponse()
    class Error(val message: String): CachedArticleResponse()
}

/**
 * Response model for main screen article cards.
 */
sealed class ArticleRowDataResponse {
    object Loading: ArticleRowDataResponse()
    object Empty: ArticleRowDataResponse()
    class Success(val articleRowDataList: List<ArticleRowData>): ArticleRowDataResponse()
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