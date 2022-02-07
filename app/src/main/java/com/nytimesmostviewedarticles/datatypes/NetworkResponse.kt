package com.nytimesmostviewedarticles.datatypes

import com.squareup.moshi.Json


data class NetworkResponse(
    @Json(name = "results") val results: List<ViewedArticle>
)

data class ViewedArticle(
    @Json(name = "url") val url: String,
    @Json(name = "id") val id: String,
    @Json(name = "published_date") val publishedDate: String,
    @Json(name = "updated") val updated: String,
    @Json(name = "section") val section: String,
    @Json(name = "subsection") val subsection: String,
    @Json(name = "column") val column: String?,
    @Json(name = "byline") val byline: String,
    @Json(name = "title") val title: String,
    @Json(name = "abstract") val abstract: String,
    @Json(name = "des_facet") val desFacet: List<String>,
    @Json(name = "geo_facet") val geoFacet: List<String>,
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
