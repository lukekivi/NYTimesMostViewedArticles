package com.nytimesmostviewedarticles.viewmodels

import com.nytimesmostviewedarticles.datatypes.ArticleData
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.viewmodel.FilterOptions

object FakeRepoResults {
    val filterOption = FilterOptions.WORLD

    const val id = "id_1"
    private val articleDataOne = ArticleData(
        id = id,
        url = "",
        publishedDate = "",
        section = filterOption.apiFilterName,
        updated = "",
        byline = "",
        title = "",
        abstract = "",
        descriptors = listOf(),
        geographyFacets = listOf(),
        media = null
    )

    const val idTwo = "id_2"
    private val articleDataTwo = ArticleData(
        id = idTwo,
        url = "",
        publishedDate = "",
        section = "Politics",
        updated = "",
        byline = "",
        title = "",
        abstract = "",
        descriptors = listOf(),
        geographyFacets = listOf(),
        media = null
    )

    val articleDataSuccess = ArticleDataResponse.Success(articleDataList = listOf(articleDataOne))
    val articleDataSuccessTwo = ArticleDataResponse.Success(articleDataList = listOf(articleDataTwo))
    val articleDataSuccessMultiple = ArticleDataResponse.Success(articleDataList = listOf(articleDataOne, articleDataTwo))

    const val errorMessage = "Error"
    val articleDataError = ArticleDataResponse.Error(errorMessage)

}