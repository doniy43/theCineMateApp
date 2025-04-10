package com.example.cinemateapp.models

data class SearchResponse(
    val Search: List<SearchResult>?,
    val totalResults: String?,
    val Response: String?,
    val Error: String?
)
