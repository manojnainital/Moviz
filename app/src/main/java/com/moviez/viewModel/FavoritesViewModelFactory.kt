package com.moviez.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moviez.model.data.local.MovieDao

class FavoritesViewModelFactory(private val movieDao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(movieDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}