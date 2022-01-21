package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nytimesmostviewedarticles.datatypes.ArticleDetailResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailScreenViewModel {
    fun getArticleDetail(id: String?): Flow<ArticleDetailResponse>
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository
): DetailScreenViewModel, ViewModel() {
    override fun getArticleDetail(id: String?) =
        id?.let {
            nyTimesRepository.getArticleDetailedDataResponse(id)
        } ?: flow {
            ArticleDetailResponse.Error("Error passing data between screens: null id.")
        }
}