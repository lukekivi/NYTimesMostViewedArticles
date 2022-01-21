package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.datatypes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

// NY times articles API key
private const val API_KEY = "nKLx7rAx32IP9qsHdVcachu1zsGEcWu7"

// We only want "image" media from the API
private const val MEDIA_TYPE_OF_CONCERN = "image"

interface NyTimesRepository {
    fun getArticleDataForRows(): Flow<ArticleRowDataResponse>
    fun getArticleDetailedDataResponse(id: String): Flow<ArticleDataResponse>
}

@Singleton
class NyTimesRepositoryImpl @Inject constructor(
    private val nyTimesApiService: NyTimesApiService
) : NyTimesRepository {
    private var articleData: List<ArticleData>? = null

    override fun getArticleDataForRows() = flow {
        if (articleData == null) {
            emit(ArticleRowDataResponse.Loading)
            updateArticleDetailedData()
        }

        if (articleData!!.isEmpty()) {
            emit(ArticleRowDataResponse.Empty)
        } else {
            emit(
                ArticleRowDataResponse.Success(
                    articleRowData = articleData!!.map { it.toArticleDataForRow() }
                )
            )
        }
    }.catch { e ->
        articleData = listOf()
        emit(
            ArticleRowDataResponse.Error(
                e.message ?: "Unknown error has occurred"
            )
        )
    }.flowOn(Dispatchers.IO)


    override fun getArticleDetailedDataResponse(id: String) = flow {
        if (articleData == null) {
            emit(ArticleDataResponse.Loading)
            updateArticleDetailedData()
        }

        emit(
            articleData!!.firstOrNull { it.id == id }?.let {
                ArticleDataResponse.Success(articleData = it)
            } ?: ArticleDataResponse.NoMatch
        )
    }.catch {
        articleData = listOf()
        emit(
            ArticleDataResponse.Error(
                message = it.message ?: "Unknown error has occurred"
            )
        )
    }.flowOn(Dispatchers.IO)


    private suspend fun updateArticleDetailedData() {
        articleData = nyTimesApiService
            .getArticlesFromLastWeek(API_KEY)
            .results
            .map { it.toArticleDetailedData() }
    }


    private fun ArticleData.toArticleDataForRow() = ArticleRowData(
        id = id,
        publishedDate = publishedDate,
        section = section,
        title = title,
        descriptors = descriptors,
        media = media
    )


    private fun ViewedArticle.toArticleDetailedData(): ArticleData {
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
            publishedDate = published_date,
            section = section,
            updated = updated,
            byline = byline,
            title = title,
            abstract = abstract,
            descriptors = listOf(
                section,
                subsection
            ) + des_facet,
            geographyFacets = geo_facet,
            media = mediaDataForUI
        )
    }
}
