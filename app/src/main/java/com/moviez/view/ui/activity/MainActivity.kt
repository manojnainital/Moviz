package com.moviez.view.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.moviez.R
import com.moviez.model.data.local.MovieDao
import com.moviez.model.data.local.MovieDatabase
import com.moviez.model.data.remote.response.Movie
import com.moviez.view.ui.fragment.HomeFragment
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, HomeFragment.newInstance { movie ->
                    toggleFavorite(movie)
                })
            }
        }

        // Initialize Room database
        val movieDatabase = MovieDatabase.getDatabase(this)
        movieDao = movieDatabase.movieDao()

        // Set up SearchView click listener
        val searchView = findViewById<ImageView>(R.id.search_view)
        searchView.setOnClickListener {
            openSearchActivity(SearchActivity::class.java)
        }

        val ivFavorite = findViewById<ImageView>(R.id.ivFavorite)
        ivFavorite.setOnClickListener {
            openSearchActivity(FavoritesActivity::class.java)
        }
    }

    private fun toggleFavorite(movie: Movie) {
        movie.isFavorite = !movie.isFavorite
        if (movie.isFavorite) {
            saveToFavorites(movie)
        } else {
            removeFromFavorites(movie)
        }
    }


    private fun <T : Activity> openSearchActivity(activityClass: Class<T>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun saveToFavorites(movie: Movie) {
        lifecycleScope.launch {
            movieDao.insert(movie)
        }
    }

    private fun removeFromFavorites(movie: Movie) {
        lifecycleScope.launch {
            movieDao.delete(movie.id)
        }
    }
}