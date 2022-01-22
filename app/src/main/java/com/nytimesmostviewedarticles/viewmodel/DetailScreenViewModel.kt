package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.DetailScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface DetailScreenViewModel {
    val getArticleDetail: Flow<DetailScreenData>
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository,
    savedStateHandle: SavedStateHandle
): DetailScreenViewModel, ViewModel() {
    override val getArticleDetail =
        savedStateHandle.get<String>("id")?.let { articleId ->
            nyTimesRepository.getSpecificArticleData(articleId)
        }?.map {
            when (it) {
                is SpecificArticleResponse.Loading -> DetailScreenData.Loading
                is SpecificArticleResponse.NoMatch -> DetailScreenData.NoMatch
                is SpecificArticleResponse.Error -> DetailScreenData.Error(it.message)
                is SpecificArticleResponse.Success -> DetailScreenData.Success(it.articleData)
            }
        } ?: flow {
            ArticleDataResponse.Error("Error passing data between screens: null id.")
        }
}