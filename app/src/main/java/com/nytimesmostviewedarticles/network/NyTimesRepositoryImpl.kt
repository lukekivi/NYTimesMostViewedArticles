package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.datatypes.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

// NY times articles API key
private const val API_KEY = "nKLx7rAx32IP9qsHdVcachu1zsGEcWu7"

// We only want "image" media from the API
private const val mediaTypeOfConcern = "image"

interface NyTimesRepository {
    suspend fun getArticleDataForRows(): Flow<ArticleRowDataResponse>
    suspend fun getArticleDetailedDataResponse(id: String): Flow<ArticleDetailResponse>
}

@Singleton
class NyTimesRepositoryImpl @Inject constructor(
    private val nyTimesApiService: NyTimesApiService
) : NyTimesRepository {
    private var articleDetailedData: List<ArticleDetailedData>? = null

    override suspend fun getArticleDataForRows() = flow {
        if (articleDetailedData == null) {
            emit(ArticleRowDataResponse.Loading)
            updateArticleDetailedData()
        }

        if (articleDetailedData!!.isEmpty()) {
            emit(ArticleRowDataResponse.Empty)
        } else {
            emit(
                ArticleRowDataResponse.Success(
                    articleDataForRows = articleDetailedData!!.map { it.toArticleDataForRow() }
                )
            )
        }
    }.catch {
        articleDetailedData = listOf()
        emit(
            ArticleRowDataResponse.Error(
                message = it.message ?: "Unknown error has occurred"
            )
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun getArticleDetailedDataResponse(id: String) = flow {
        if (articleDetailedData == null) {
            emit(ArticleDetailResponse.Loading)
            updateArticleDetailedData()
        }

        emit(
            articleDetailedData!!.firstOrNull { it.id == id }?.let {
                ArticleDetailResponse.Success(articleDetailedData = it)
            } ?: ArticleDetailResponse.NoMatch
        )

    }.catch {
        articleDetailedData = listOf()
        emit(
            ArticleDetailResponse.Error(
                message = it.message ?: "Unknown error has occurred"
            )
        )
    }.flowOn(Dispatchers.IO)


    private suspend fun updateArticleDetailedData() {
        articleDetailedData = nyTimesApiService
            .getArticlesFromLastWeek(API_KEY)
            .results
            .map { it.toArticleDetailedData() }
    }

    private fun ArticleDetailedData.toArticleDataForRow() = ArticleDataForRow(
        id = id,
        publishedDate = publishedDate,
        section = section,
        title = title,
        descriptors = descriptors,
        media = media
    )

    private fun ViewedArticle.toArticleDetailedData(): ArticleDetailedData {
        val mediaDataForUI = try {
            val media = media
                .first { it.type == mediaTypeOfConcern }

            /**
             * Data is organized from smallest to largest.
             * The largest being a reasonable size. (440 x 293)
             */
            val mediaMetaData = media.mediaMetadata.last()

            MediaDataForUI(
                url = mediaMetaData.url,
                caption = media.caption
            )
        } catch (e: NoSuchElementException) {
            null
        }

        return ArticleDetailedData(
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
