package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.Constants
import com.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import com.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.nytimesmostviewedarticles.datatypes.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

private const val mediaTypeOfConcern = "image" // We only want "image" media from the API

interface ArticleApi {
    suspend fun getArticleDataForUi(): NetworkResponse
}

@Singleton
class NyTimesArticleApi @Inject constructor(
    private val nyTimesApiService: NyTimesApiService
) : ArticleApi {
    override suspend fun getArticleDataForUi(): NetworkResponse {
        return try {
            nyTimesApiService.getArticlesFromLastWeek(Constants.API_KEY)
                .results
                .map { articleData ->
                    val mediaDataForUI = try {
                        val media = articleData.media
                            .first { it.type == mediaTypeOfConcern }

                        /**
                         * Data is organized from smallest to largest.
                         * The largest being a reasonable size. (440 x 293)
                         */
                        val mediaMetaData = media.mediaMetadata.last()

                        MediaDataForUI(
                            url = mediaMetaData.url,
                            caption = media.caption,
                            width = mediaMetaData.width
                        )
                    } catch (e: NoSuchElementException) {
                        null
                    }

                    ArticleDataForUI(
                        url = articleData.url,
                        publishedDate = articleData.published_date,
                        updated = articleData.updated,
                        section = articleData.section,
                        subsection = articleData.subsection,
                        byline = articleData.byline,
                        title = articleData.title,
                        abstract = articleData.abstract,
                        descriptionFacets = listOf(articleData.section, articleData.subsection) + articleData.des_facet,
                        geographyFacets = articleData.geo_facet,
                        media = mediaDataForUI
                    )
                }.let {
                    NetworkResponse.Success(it)
                }
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Unknown error has occurred.")
        }
    }
}
