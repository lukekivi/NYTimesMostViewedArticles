package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleData
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.components.ArticleCardData
import com.nytimesmostviewedarticles.ui.screens.MainScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MainScreenViewModel {
    val articles: Flow<MainScreenData>
}

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    nyTimesRepository: NyTimesRepository
) : ViewModel(), MainScreenViewModel {
    override val articles: Flow<MainScreenData> = nyTimesRepository.articleDataResponse.map { articleDataResponse ->
        when (articleDataResponse) {
            is ArticleDataResponse.Loading -> MainScreenData.Loading
            is ArticleDataResponse.Error -> MainScreenData.Error(articleDataResponse.message)
            is ArticleDataResponse.Uninitialized -> {
                nyTimesRepository.updateArticleData()
                MainScreenData.Loading
            }
            is ArticleDataResponse.Success -> {
                if (articleDataResponse.articleDataList.isEmpty()) {
                    MainScreenData.Empty
                } else {
                    MainScreenData.Success(
                        articleDataResponse.articleDataList.map { it.toArticleCardData() }
                    )
                }
            }
        }
    }

    private fun ArticleData.toArticleCardData() = ArticleCardData(
        id = id,
        publishedDate = publishedDate,
        section = section,
        title = title,
        descriptors = descriptors,
        media = media
    )
}
