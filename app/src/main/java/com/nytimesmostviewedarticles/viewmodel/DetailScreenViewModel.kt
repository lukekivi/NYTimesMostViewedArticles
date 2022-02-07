package com.nytimesmostviewedarticles.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.Destinations
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.DetailScreenContent
import com.nytimesmostviewedarticles.ui.screens.DetailScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val FAILURE_TO_PASS_ID_ERROR = "Error passing data between screens: null id."

interface DetailScreenViewModel {
    val detailScreenContentFlow: Flow<DetailScreenContent>
    fun refreshAppData()
}

@HiltViewModel
class DetailScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository,
    savedStateHandle: SavedStateHandle
) : DetailScreenViewModel, ViewModel() {

    private val isLoadingFlow = MutableStateFlow(false)

    private val specificArticleResponseFlow =
        savedStateHandle.get<String>(Destinations.DetailScreen.ArgId)?.let { articleId ->
            nyTimesRepository.getSpecificArticleData(articleId)
        }?.onEach {
            /**
             * Whenever article data is updated loading has completed.
             */
            isLoadingFlow.value = false
        }
            ?: flow {
                isLoadingFlow.value = false
                emit(SpecificArticleResponse.Error(FAILURE_TO_PASS_ID_ERROR))
            }

    override val detailScreenContentFlow: Flow<DetailScreenContent> = combine(
        specificArticleResponseFlow, isLoadingFlow
    ) { specificArticleResponse, isLoading ->
        val detailScreenData = when (specificArticleResponse) {
            is SpecificArticleResponse.Uninitialized -> {
                refreshAppData()
                DetailScreenData.Uninitialized
            }
            is SpecificArticleResponse.NoMatch -> DetailScreenData.NoMatch
            is SpecificArticleResponse.Error -> DetailScreenData.Error(specificArticleResponse.message)
            is SpecificArticleResponse.Success -> DetailScreenData.Success(specificArticleResponse.articleData)
        }

        DetailScreenContent(
            detailScreenData = detailScreenData,
            isLoading = isLoading
        )
    }

    override fun refreshAppData() {
        isLoadingFlow.value = true
        nyTimesRepository.updateArticleData()
    }
}