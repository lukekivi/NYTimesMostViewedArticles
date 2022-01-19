package com.example.nytimesmostviewedarticles.datatypes

import com.squareup.moshi.Json

data class NyTimesArticleRequest(
    @Json(name = "results") val results: List<ViewedArticle>
)

data class ViewedArticle(
    @Json(name = "url") val url: String,
    @Json(name = "published_date") val published_date: String,
    @Json(name = "updated") val updated: String,
    @Json(name = "section") val section: String,
    @Json(name = "subsection") val subsection: String,
    @Json(name = "column") val column: String?,
    @Json(name = "byline") val byline: String,
    @Json(name = "title") val title: String,
    @Json(name = "abstract") val abstract: String,
    @Json(name = "des_facet") val des_facet: List<String>,
    @Json(name = "geo_facet") val geo_facet: List<String>,
    @Json(name = "media") val media: List<MediaData>,
)

data class MediaData(
    @Json(name = "type") val type: String,
    @Json(name = "caption") val caption: String,
    @Json(name = "media-metadata") val mediaMetadata: List<MediaMetaData>
)

data class MediaMetaData(
    @Json(name = "url") val url: String,
    @Json(name = "height") val height: Int,
    @Json(name = "width") val width: Int
)
