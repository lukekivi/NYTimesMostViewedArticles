package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface DetailScreenViewModel {
    fun getArticleDetail(id: String?): Flow<ArticleDataResponse>
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository
): DetailScreenViewModel, ViewModel() {
    override fun getArticleDetail(id: String?) =
        id?.let {
            nyTimesRepository.getArticleDetailedDataResponse(id)
        } ?: flow {
            ArticleDataResponse.Error("Error passing data between screens: null id.")
        }
}