package com.example.nytimesmostviewedarticles.viewmodel

import com.example.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import kotlinx.coroutines.flow.StateFlow

interface ArticleDataViewModel {
    val articleDataState: StateFlow<ArticleDataState>
    val sectionNames: Array<String>
    var selectedArticle: ArticleDataForUI
}