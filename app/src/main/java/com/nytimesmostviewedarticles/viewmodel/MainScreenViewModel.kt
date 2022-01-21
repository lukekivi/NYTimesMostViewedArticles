package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nytimesmostviewedarticles.datatypes.ArticleRowDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MainScreenViewModel {
    val articleDataState: StateFlow<ArticleRowDataResponse>
}

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository
) : ViewModel(), MainScreenViewModel {
    private val _articleDataState: MutableStateFlow<ArticleRowDataResponse> =
        MutableStateFlow(ArticleRowDataResponse.Loading)
    override val articleDataState: StateFlow<ArticleRowDataResponse> = _articleDataState

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            nyTimesRepository.getArticleDataForRows()
                .collect { _articleDataState.emit(it) }
        }
    }
}
