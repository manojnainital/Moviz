package com.moviez.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviez.model.data.remote.RetrofitClient
import com.moviez.model.data.remote.response.Movie
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> get() = _upcomingMovies

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.tmdbApiService.getPopularMovies(apiKey)
                _popularMovies.value = response.results
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch movies: ${e.message}"
            }
        }
    }

    fun fetchTopRatedMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.tmdbApiService.getTopRatedMovies(apiKey)
                _topRatedMovies.value = response.results
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch top-rated movies: ${e.message}"
            }
        }
    }

    fun fetchUpcomingMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.tmdbApiService.getUpcomingMovies(apiKey)
                _upcomingMovies.value = response.results
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch upcoming movies: ${e.message}"
            }
        }
    }
}