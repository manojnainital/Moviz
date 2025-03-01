package com.moviez.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviez.model.data.remote.RetrofitClient
import com.moviez.model.data.remote.response.Movie
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> get() = _searchResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

//    fun searchMovies(query: String) {
//        viewModelScope.launch {
//            try {
//                val apiKey = "3b1520a0671f5e378efbb5ea9dccab0d" // Replace with your API key
//                val response = RetrofitClient.tmdbApiService.searchMovies(apiKey, query)
//                if (response.isSuccessful) {
//                    _searchResults.value = response.body()?.results ?: emptyList()
//                } else {
//                    _errorMessage.value = "Failed to fetch search results: ${response.errorBody()?.string()}"
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Network Error: ${e.message}"
//            }
//        }
//    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val apiKey = "3b1520a0671f5e378efbb5ea9dccab0d" // Replace with your API key
            val response = RetrofitClient.tmdbApiService.getSearchMovies(apiKey,query)
            try {
                _searchResults.value = response.results
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch search results: ${e.message}"
            }
        }
    }
}