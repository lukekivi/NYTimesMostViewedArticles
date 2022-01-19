package com.example.nytimesmostviewedarticles.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface ArticleDataViewModel {
    val articleDataState: StateFlow<ArticleDataState>
    val sectionNames: Array<String>
}