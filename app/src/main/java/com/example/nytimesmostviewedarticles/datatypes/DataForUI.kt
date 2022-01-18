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
    val media: MediaDataForUI?
)

data class MediaDataForUI(
    val url: String,
    val caption: String,
    val width: Int
)