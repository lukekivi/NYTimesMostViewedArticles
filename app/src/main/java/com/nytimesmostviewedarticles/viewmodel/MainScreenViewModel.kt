package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.datatypes.ArticleRowDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MainScreenViewModel {
    val articles: Flow<ArticleRowDataResponse>
}

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    nyTimesRepository: NyTimesRepository
) : ViewModel(), MainScreenViewModel {
    override val articles = nyTimesRepository.getArticleDataForRows()

}
