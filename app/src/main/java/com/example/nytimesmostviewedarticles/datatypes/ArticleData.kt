package com.example.nytimesmostviewedarticles.datatypes

import com.squareup.moshi.Json

data class ArticleData(
    val uri: String,
    val url: String,
    val id: Int,
    val asset_id: Int,
    val source: String,
    val published_date: String,
    val updated: String,
    val section: String,
    val subsection: String,
    val nytdsection: String,
    val adx_keywords: String,
    val column: String?,
    val byline: String,
    val type: String,
    val title: String,
    val abstract: String,
    val des_facet: List<String>,
    val org_facet: List<String>,
    val per_facet: List<String>,
    val geo_facet: List<String>,
    val media: List<MediaData>,
    val eta_id: Int
)

data class MediaData(
    val type: String,
    val subtype: String,
    val caption: String,
    val copyright: String,
    val approved_for_syndication: Int,
    @Json(name = "media-metadata") val media_metadata: List<MediaMetaData>
)

data class MediaMetaData(
    val url: String,
    val format: String,
    val height: Int,
    val width: Int
)

data class ArticleDataForUI(
    val url: String,
    val publishedDate: String,
    val section: String,
    val subsection: String,
    val byline: String,
    val type: String,
    val title: String,
    val abstract: String,
    val descriptionFacets: List<String>,
    val media: MediaMetaData
)
