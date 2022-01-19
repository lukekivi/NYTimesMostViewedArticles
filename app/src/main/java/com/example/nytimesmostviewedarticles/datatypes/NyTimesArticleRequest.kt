package com.example.nytimesmostviewedarticles.datatypes

import com.squareup.moshi.Json

data class NyTimesArticleRequest(
    @Json(name = "status") val status: String,
    @Json(name = "copyright") val copyright: String,
    @Json(name = "num_results") val num_results: Int,
    @Json(name = "results") val results: List<ViewedArticle>
)

data class ViewedArticle(
    @Json(name = "uri") val uri: String,
    @Json(name = "url") val url: String,
    @Json(name = "id") val id: Long,
    @Json(name = "asset_id") val asset_id: Long,
    @Json(name = "source") val source: String,
    @Json(name = "published_date") val published_date: String,
    @Json(name = "updated") val updated: String,
    @Json(name = "section") val section: String,
    @Json(name = "subsection") val subsection: String,
    @Json(name = "nytdsection") val nytdsection: String,
    @Json(name = "adx_keywords") val adx_keywords: String,
    @Json(name = "column") val column: String?,
    @Json(name = "byline") val byline: String,
    @Json(name = "type") val type: String,
    @Json(name = "title") val title: String,
    @Json(name = "abstract") val abstract: String,
    @Json(name = "des_facet") val des_facet: List<String>,
    @Json(name = "org_facet") val org_facet: List<String>,
    @Json(name = "per_facet") val per_facet: List<String>,
    @Json(name = "geo_facet") val geo_facet: List<String>,
    @Json(name = "media") val media: List<MediaData>,
    @Json(name = "eta_id") val eta_id: Int
)

data class MediaData(
    @Json(name = "type") val type: String,
    @Json(name = "subtype") val subtype: String,
    @Json(name = "caption") val caption: String,
    @Json(name = "copyright") val copyright: String,
    @Json(name = "approved_for_syndication") val approved_for_syndication: Int,
    @Json(name = "media-metadata") val mediaMetadata: List<MediaMetaData>
)

data class MediaMetaData(
    @Json(name = "url") val url: String,
    @Json(name = "format") val format: String,
    @Json(name = "height") val height: Int,
    @Json(name = "width") val width: Int
)
