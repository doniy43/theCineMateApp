package com.example.cinemateapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemateapp.models.SearchResponse
import com.example.cinemateapp.models.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OmdbApiService::class.java)

    fun searchMovies(apiKey: String, query: String) {
        api.searchMovies(apiKey, query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful && response.body()?.Search != null) {
                    _searchResults.value = response.body()!!.Search!!
                } else {
                    _searchResults.value = emptyList()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _searchResults.value = emptyList()
            }
        })
    }
}
