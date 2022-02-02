package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.Destinations
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.DetailScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val FAILURE_TO_PASS_ID_ERROR = "Error passing data between screens: null id."

interface DetailScreenViewModel {
    val getArticleDetail: Flow<DetailScreenData>
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository,
    savedStateHandle: SavedStateHandle
): DetailScreenViewModel, ViewModel() {
    override val getArticleDetail =
        savedStateHandle.get<String>(Destinations.DetailScreen.argId)?.let { articleId ->
            nyTimesRepository.getSpecificArticleData(articleId)
        }?.map {
            when (it) {
                is SpecificArticleResponse.NoMatch -> DetailScreenData.NoMatch
                is SpecificArticleResponse.Error -> DetailScreenData.Error(it.message)
                is SpecificArticleResponse.Success -> DetailScreenData.Success(it.articleData)
            }
        } ?: flowOf(DetailScreenData.Error(FAILURE_TO_PASS_ID_ERROR))
}