package com.example.cinemateapp.models

data class Movie(
    val id: String = "",
    val title: String = "",
    val year: String = "",
    val studio: String = "",
    val posterUrl: String = "",
    val rating: Float = 0.0f
)
