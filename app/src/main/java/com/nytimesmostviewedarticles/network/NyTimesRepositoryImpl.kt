package com.nytimesmostviewedarticles.network

import android.util.Log
import com.nytimesmostviewedarticles.datatypes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private const val TAG = "NyTimesRepositoryImpl"

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
    private val uid = Random.nextInt()

    override fun getArticleDataForRows() = flow {
        if (articleData == null) {
            Log.d(TAG, "row -> article data is null ($uid)")
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
        Log.d(TAG, e.stackTraceToString())
        articleData = listOf()
        emit(
            ArticleRowDataResponse.Error(
                e.message ?: "Unknown error has occurred"
            )
        )
    }.flowOn(Dispatchers.IO)


    override fun getArticleDetailedDataResponse(id: String) = flow {
        Log.d(TAG, "getArticleDetailedDataResponse(): $uid")

        if (articleData == null) {
            Log.d(TAG, "article data is null")
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
        Log.d(TAG, "updateArticleDetailedData()")
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
