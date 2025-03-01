package com.moviez.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviez.model.data.local.MovieDao
import com.moviez.model.data.remote.response.Movie
import kotlinx.coroutines.launch

class FavoritesViewModel(private val movieDao: MovieDao) : ViewModel() {

    val favoriteMovies: LiveData<List<Movie>> = movieDao.getAllFavorites()

    init {
        favoriteMovies.observeForever { movies ->
            Log.d("FavoritesViewModel", "Retrieved ${movies.size} favorite movies")
        }
    }

    fun removeFavorite(movie: Movie) {
        viewModelScope.launch {
            movieDao.delete(movie.id)
        }
    }
}
