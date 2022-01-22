package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface DetailScreenViewModel {
    val getArticleDetail: Flow<ArticleDataResponse>
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository,
    savedStateHandle: SavedStateHandle
): DetailScreenViewModel, ViewModel() {
    override val getArticleDetail =
        savedStateHandle.get<String>("id")?.let { articleId ->
            nyTimesRepository.getArticleDetailedDataResponse(articleId)
        } ?: flow {
            ArticleDataResponse.Error("Error passing data between screens: null id.")
        }
}