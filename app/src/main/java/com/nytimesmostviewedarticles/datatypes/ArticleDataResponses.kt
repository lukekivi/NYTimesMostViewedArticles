package com.nytimesmostviewedarticles.datatypes

/**
 * Response model for main screen article cards.
 */
sealed class SpecificArticleResponse {
    object Loading: SpecificArticleResponse()
    object NoMatch: SpecificArticleResponse()
    class Success(val articleData: ArticleData): SpecificArticleResponse()
    class Error(val message: String): SpecificArticleResponse()
}

/**
 * Response model for detail screen.
 */
sealed class ArticleDataResponse {
    object Uninitialized: ArticleDataResponse()
    object Loading: ArticleDataResponse()
    class Success(val articleDataList: List<ArticleData>): ArticleDataResponse()
    class Error(val message: String): ArticleDataResponse()
}

/**
 * Data that the app is concerned with.
 */
data class ArticleData(
    val id: String,
    val url: String,
    val publishedDate: String,
    val section: String,
    val updated: String,
    val byline: String,
    val title: String,
    val abstract: String,
    val descriptors: List<String>,
    val geographyFacets: List<String>,
    val media: MediaDataForUI?
)

/**
 * Media data needed to generate an image.
 */
data class MediaDataForUI(
    val url: String,
    val caption: String
)