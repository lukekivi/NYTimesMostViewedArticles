package com.example.nytimesmostviewedarticles.datatypes

sealed class NetworkResponse {
    class Success(val dataForUi: List<ArticleDataForUI>): NetworkResponse()
    class Error(val message: String): NetworkResponse()
}