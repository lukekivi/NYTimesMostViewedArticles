package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleRowDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MainScreenViewModel {
    fun getArticles(): Flow<ArticleRowDataResponse>
}

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository
) : ViewModel(), MainScreenViewModel {
    override fun getArticles() = nyTimesRepository.getArticleDataForRows()

}
