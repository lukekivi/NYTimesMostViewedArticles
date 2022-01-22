package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleData
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.components.ArticleCardData
import com.nytimesmostviewedarticles.ui.screens.MainScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MainScreenViewModel {
    val articles: Flow<MainScreenData>
    fun updateFilter(sectionName: String)
}

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    nyTimesRepository: NyTimesRepository
) : ViewModel(), MainScreenViewModel {
    private var filter = MutableStateFlow<ArticleFilter>(ArticleFilter.None)

    override val articles: Flow<MainScreenData> = nyTimesRepository.articleDataResponse
        .combine(filter) { articleResponse, filter ->
        when (articleResponse) {
            is ArticleDataResponse.Loading -> MainScreenData.Loading
            is ArticleDataResponse.Error -> MainScreenData.Error(articleResponse.message)
            is ArticleDataResponse.Uninitialized -> {
                nyTimesRepository.updateArticleData()
                MainScreenData.Loading
            }
            is ArticleDataResponse.Success -> {
                if (articleResponse.articleDataList.isEmpty()) {
                    MainScreenData.Empty
                } else {
                    MainScreenData.Success(
                        applyFilter(articleResponse.articleDataList.map { it.toArticleCardData() })
                    )
                }
            }
        }
    }


    override fun updateFilter(sectionName: String) {
        filter.value.let {
            when (it) {
                is ArticleFilter.None -> filter.value = ArticleFilter.Section(sectionName)
                is ArticleFilter.Section -> {
                    if (it.sectionName == sectionName) {
                        filter.value = ArticleFilter.None
                    } else {
                        filter.value = ArticleFilter.Section(sectionName)
                    }
                }
            }
        }
    }

    private fun applyFilter(articleCardDataList: List<ArticleCardData>) =
        filter.value.let { articleFilter ->
            when (articleFilter) {
                is ArticleFilter.None -> articleCardDataList
                is ArticleFilter.Section -> articleCardDataList
                    .filter { it.section == articleFilter.sectionName }
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

    private sealed class ArticleFilter {
        object None: ArticleFilter()
        class Section(val sectionName: String): ArticleFilter()
    }
}

