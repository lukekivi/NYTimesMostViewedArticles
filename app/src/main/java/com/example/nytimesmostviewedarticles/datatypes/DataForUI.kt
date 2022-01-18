package com.example.nytimesmostviewedarticles.datatypes

data class ArticleDataForUI(
    val url: String,
    val publishedDate: String,
    val updated: String,
    val section: String,
    val subsection: String,
    val byline: String,
    val type: String,
    val title: String,
    val abstract: String,
    val descriptionFacets: List<String>,
    val geographyFacets: List<String>,
    val media: MediaDataForUI
)

sealed class MediaDataForUI {
    object Unavailable: MediaDataForUI()
    data class Available(val url: String, val caption: String): MediaDataForUI()
}