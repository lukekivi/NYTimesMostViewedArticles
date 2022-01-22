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

private const val DEFAULT_ERROR_MESSAGE = "Unknown error has occurred"

interface NyTimesRepository {
    fun getArticleDataForRows(): Flow<ArticleRowDataResponse>
    fun getArticleDetailedDataResponse(id: String): Flow<ArticleDataResponse>
}

@Singleton
class NyTimesRepositoryImpl @Inject constructor(
    private val nyTimesApiService: NyTimesApiService
) : NyTimesRepository {
    private var cachedArticleData: CachedArticleData = CachedArticleData.NoRequest

    override fun getArticleDataForRows() = flow {
        var articleDataList: List<ArticleData>? = null

        cachedArticleData.let { response ->
            when (response) {

                is CachedArticleData.NoRequest -> {
                    emit(ArticleRowDataResponse.Loading)
                    val data = getArticleData()

                    if (data.isEmpty()) {
                        cachedArticleData = CachedArticleData.Empty
                        emit(ArticleRowDataResponse.Empty)
                    } else {
                        cachedArticleData = CachedArticleData.Success(data)
                        articleDataList = data
                    }
                }

                is CachedArticleData.Success -> {
                    articleDataList = response.articleDataList
                }

                is CachedArticleData.Empty -> {
                    emit(ArticleRowDataResponse.Empty)
                }

                is CachedArticleData.Error -> {
                    emit(ArticleRowDataResponse.Error(response.message))
                }
            }
        }

        // Emit data if request was successful
        articleDataList?.let { dataList ->
            emit(
                ArticleRowDataResponse.Success(
                    dataList.map { it.toArticleDataForRow() }
                )
            )
        }
    }.catch { e ->
        cachedArticleData = CachedArticleData.Error(e.message ?: DEFAULT_ERROR_MESSAGE)

        emit(
            ArticleRowDataResponse.Error(
                e.message ?: DEFAULT_ERROR_MESSAGE
            )
        )
    }.flowOn(Dispatchers.IO)


    override fun getArticleDetailedDataResponse(id: String) = flow {
        var articleDataList: List<ArticleData>? = null

        cachedArticleData.let { response ->
            when (response) {

                is CachedArticleData.NoRequest -> {
                    emit(ArticleDataResponse.Loading)
                    val data = getArticleData()

                    if (data.isEmpty()) {
                        cachedArticleData = CachedArticleData.Empty
                        articleDataList = emptyList()
                    } else {
                        cachedArticleData = CachedArticleData.Success(data)
                        articleDataList = data
                    }
                }

                is CachedArticleData.Success -> {
                    articleDataList = response.articleDataList
                }

                is CachedArticleData.Empty -> {
                    articleDataList = emptyList()
                }

                is CachedArticleData.Error -> {
                    emit(ArticleDataResponse.Error(response.message))
                }
            }
        }

        /**
         * articleDataList is non-null if request was successful.
         * Elvis operator is hit if there was no match which includes
         * an empty response from API.
         */
        articleDataList?.let { datalist ->
            datalist.firstOrNull {
                it.id == id
            }?.let {
                emit(ArticleDataResponse.Success(it))
            } ?: emit(ArticleDataResponse.NoMatch)
        }
    }.catch { e ->
        cachedArticleData = CachedArticleData.Error(e.message ?: DEFAULT_ERROR_MESSAGE)

        emit(
            ArticleDataResponse.Error(
                e.message ?: DEFAULT_ERROR_MESSAGE
            )
        )
    }.flowOn(Dispatchers.IO)


    private suspend fun getArticleData() = nyTimesApiService
            .getArticlesFromLastWeek(API_KEY)
            .results
            .map { it.toArticleDetailedData() }



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
