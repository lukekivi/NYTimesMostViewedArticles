package com.nytimesmostviewedarticles.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import com.nytimesmostviewedarticles.datatypes.NetworkResponse
import com.nytimesmostviewedarticles.network.ArticleApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ArticleDataState {
    object Loading: ArticleDataState()
    object Empty: ArticleDataState()
    class Success(val data: List<ArticleDataForUI>): ArticleDataState()
    class Error(val message: String): ArticleDataState()
}

@HiltViewModel
class ArticleDataViewModelImpl @Inject constructor(
    private val nyTimesArticleApi: ArticleApi
    ): ViewModel(), ArticleDataViewModel {
    override lateinit var selectedArticle: ArticleDataForUI

    private val _articleDataState: MutableStateFlow<ArticleDataState> = MutableStateFlow(ArticleDataState.Loading)
    override val articleDataState: StateFlow<ArticleDataState> = _articleDataState

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            _articleDataState.emit(ArticleDataState.Loading)

            nyTimesArticleApi.getArticleDataForUi()
                .let { response ->
                    when (response) {
                        is NetworkResponse.Success -> _articleDataState.emit(
                            if (response.dataForUi.isEmpty()) {
                                ArticleDataState.Empty
                            }  else {
                                ArticleDataState.Success(response.dataForUi)
                            }
                        )
                        is NetworkResponse.Error -> _articleDataState.emit(
                            ArticleDataState.Error(response.message)
                        )
                    }
                }
        }
    }
}