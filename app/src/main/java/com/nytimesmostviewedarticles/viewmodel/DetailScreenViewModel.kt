package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nytimesmostviewedarticles.datatypes.ArticleDetailResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailScreenViewModel {
    val articleDetailResponse: StateFlow<ArticleDetailResponse>
    fun updateArticleDetail(id: String)
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository
): DetailScreenViewModel, ViewModel() {
    private val _articleDetailResponse: MutableStateFlow<ArticleDetailResponse> = MutableStateFlow(ArticleDetailResponse.Loading)
    override val articleDetailResponse: StateFlow<ArticleDetailResponse> = _articleDetailResponse

    override fun updateArticleDetail(id: String) {
        viewModelScope.launch {
            if (_articleDetailResponse.value !is ArticleDetailResponse.Success ||
                (_articleDetailResponse.value as ArticleDetailResponse.Success).articleDetailedData.id != id) {
                    nyTimesRepository.getArticleDetailedDataResponse(id).collect {
                    _articleDetailResponse.emit(it)
                }
            }
        }
    }
}