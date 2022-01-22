package com.nytimesmostviewedarticles.datatypes

/**
 * Article data for article cards in main screen lazy column.
 */
data class ArticleRowData(
    val id: String,
    val publishedDate: String,
    val section: String,
    val title: String,
    val descriptors: List<String>,
    val media: MediaDataForUI?
)

/**
 * Article data for detail screen.
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