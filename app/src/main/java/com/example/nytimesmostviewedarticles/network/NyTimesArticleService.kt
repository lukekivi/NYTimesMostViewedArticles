package com.example.nytimesmostviewedarticles.network

import android.util.Log
import com.example.nytimesmostviewedarticles.Constants
import com.example.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import com.example.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.example.nytimesmostviewedarticles.datatypes.NyTimesArticleRequest
import com.example.nytimesmostviewedarticles.datatypes.NetworkResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
class NyTimesArticleService: ArticleService {
    private val mediaTypeOfConcern = "image" // We only want "image" media from the API

    private val baseUrl = "https://api.nytimes.com/svc/mostpopular/v2/viewed/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()

    private val nyTimesApi by lazy { retrofit.create(NyTimesApiService::class.java) }

    private interface NyTimesApiService {
        @GET("{endpoint}")
        suspend fun getArticles(
            @Path("endpoint") endpoint: String,
            @Query("api-key") apiKey: String
        ): NyTimesArticleRequest
    }

    override suspend fun getArticleDataForUi(period: QueryPeriodLength): NetworkResponse {
        val endpoint = "${period.length}.json"

        return try {
            nyTimesApi.getArticles(endpoint, Constants.API_KEY)
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
                        type = articleData.type,
                        title = articleData.title,
                        abstract = articleData.abstract,
                        descriptionFacets = articleData.des_facet,
                        geographyFacets = articleData.geo_facet,
                        media = mediaDataForUI
                    )
                }.let {
                    NetworkResponse.Success(it)
                }

        } catch (e: Exception) {
            Log.d("NETTY", e.stackTraceToString())
            NetworkResponse.Error(e.message ?: "Unknown error has occurred.")
        }
    }

}

/**
 * The NY Times API offers three query period lengths.
 */
enum class NyTimesArticlePeriod(override val length: Int): QueryPeriodLength {
    DAY(1), WEEK(7), MONTH(30)
}
