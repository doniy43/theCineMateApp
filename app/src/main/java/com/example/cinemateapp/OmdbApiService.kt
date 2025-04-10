package com.example.cinemateapp

import com.example.cinemateapp.models.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String
    ): Call<SearchResponse>
}
