package com.nytimesmostviewedarticles.network

import com.nytimesmostviewedarticles.datatypes.MediaData
import com.nytimesmostviewedarticles.datatypes.MediaMetaData
import com.nytimesmostviewedarticles.datatypes.NetworkResponse
import com.nytimesmostviewedarticles.datatypes.ViewedArticle
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

object TestNetworkResults {

    val id = "id_1"

    val success = NetworkResponse(
        results = listOf(
            ViewedArticle(
                url = "url_1",
                id = id,
                published_date = "published_data_1",
                updated = "updated_1",
                section = "section_1",
                subsection = "subsection_1",
                column = null,
                byline = "byline_1",
                title = "title_1",
                abstract = "abstract_1",
                des_facet = listOf("des_facet_1"),
                geo_facet = listOf("geo_facet_1"),
                media = listOf(
                    MediaData(
                        type = "image",
                        caption = "caption_1",
                        mediaMetadata = listOf(
                            MediaMetaData(url = "media_url_1", height = 1, width = 1)
                        )
                    )
                )
            )
        )
    )

    val idTwo = "id_2"

    val successTwo = NetworkResponse(
        results = listOf(
            ViewedArticle(
                url = "url_2",
                id = idTwo,
                published_date = "published_data_",
                updated = "updated_2",
                section = "section_2",
                subsection = "subsection_2",
                column = null,
                byline = "byline_2",
                title = "title_2",
                abstract = "abstract_2",
                des_facet = listOf("des_facet_2"),
                geo_facet = listOf("geo_facet_2"),
                media = listOf(
                    MediaData(
                        type = "image",
                        caption = "caption_2",
                        mediaMetadata = listOf(
                            MediaMetaData(url = "media_url_2", height = 2, width = 2)
                        )
                    )
                )
            )
        )
    )

    val exception = HttpException(
        Response.error<Any>(
            401, ResponseBody.create(MediaType.get("text/plain"), "Test incorrect API key")
        )
    )
}