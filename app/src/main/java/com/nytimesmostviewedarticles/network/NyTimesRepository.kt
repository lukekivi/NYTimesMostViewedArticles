package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.datatypes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// NY times articles API key
private const val API_KEY = "nKLx7rAx32IP9qsHdVcachu1zsGEcWu7"

// We only want "image" media from the API
private const val MEDIA_TYPE_OF_CONCERN = "image"

private const val DEFAULT_ERROR_MESSAGE = "Unknown error has occurred"

interface NyTimesRepository {
    val articleDataResponse: StateFlow<ArticleDataResponse>

    /**
     * Get data about a specific article via it's [id]. If there is no data make an API call.
     */
    fun getSpecificArticleData(id: String): Flow<SpecificArticleResponse>

    /**
     * Get up to date article data from the the API.
     */
    fun updateArticleData()
}

@Singleton
class NyTimesRepositoryImpl
@Inject constructor(
    private val nyTimesApiService: NyTimesApiService,
    private val coroutineScope: CoroutineScope,
) : NyTimesRepository {
    private val _articleDataResponse =
        MutableStateFlow<ArticleDataResponse>(ArticleDataResponse.Uninitialized)
    override val articleDataResponse: StateFlow<ArticleDataResponse> = _articleDataResponse

    override fun updateArticleData() {
        coroutineScope.launch {
            try {
                _articleDataResponse.emit(
                    ArticleDataResponse.Success(
                        nyTimesApiService
                            .getArticlesFromLastWeek(API_KEY)
                            .results
                            .map { it.toArticleData() })
                )

            } catch (e: Exception) {
                _articleDataResponse.emit(
                    ArticleDataResponse.Error(
                        e.message ?: DEFAULT_ERROR_MESSAGE
                    )
                )
            }
        }
    }

    override fun getSpecificArticleData(id: String): Flow<SpecificArticleResponse> {
        // Refresh article data if need be
        when (_articleDataResponse.value) {
            is ArticleDataResponse.Error,
            is ArticleDataResponse.Uninitialized -> updateArticleData()
            is ArticleDataResponse.Success -> Unit
        }

        // Return results mapped to SpecificDataResponse
        return _articleDataResponse.map { articleDataResponse ->
            when (articleDataResponse) {
                is ArticleDataResponse.Success -> {
                    articleDataResponse.articleDataList.firstOrNull { it.id == id }
                        ?.let { SpecificArticleResponse.Success(it) }
                        ?: SpecificArticleResponse.NoMatch
                }
                is ArticleDataResponse.Uninitialized -> SpecificArticleResponse.NoMatch
                is ArticleDataResponse.Error -> {
                    SpecificArticleResponse.Error(articleDataResponse.message)
                }
            }
        }
    }

    private fun ViewedArticle.toArticleData(): ArticleData {
        val media = media
            .firstOrNull { it.type == MEDIA_TYPE_OF_CONCERN }

        /**
         * Data is organized from smallest to largest.
         * The largest being a reasonable size. (440 x 293)
         */
        val mediaDataForUI = media
            ?.mediaMetadata
            ?.lastOrNull()
            ?.let {
                MediaDataForUI(
                    url = it.url,
                    caption = media.caption
                )
            }

        return ArticleData(
            id = id,
            url = url,
            publishedDate = publishedDate,
            section = section,
            updated = updated,
            byline = byline,
            title = title,
            abstract = abstract,
            descriptors = listOf(
                section,
                subsection
            ) + desFacet,
            geographyFacets = geoFacet,
            media = mediaDataForUI
        )
    }
}
