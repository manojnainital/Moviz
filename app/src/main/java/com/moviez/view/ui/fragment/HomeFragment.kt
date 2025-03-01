package com.moviez.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviez.R
import com.moviez.model.data.local.MovieDao
import com.moviez.model.data.local.MovieDatabase
import com.moviez.model.data.remote.response.Movie
import com.moviez.view.adapter.MovieAdapter
import com.moviez.viewModel.MovieViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var topRatedMovieAdapter: MovieAdapter
    private lateinit var upcomingMovieAdapter: MovieAdapter
    private lateinit var movieDao: MovieDao

    // Favorite click callback
    private var onFavoriteClick: ((Movie) -> Unit)? = null

    companion object {
        fun newInstance(onFavoriteClick: (Movie) -> Unit): HomeFragment {
            val fragment = HomeFragment()
            fragment.onFavoriteClick = onFavoriteClick
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Log to verify if onFavoriteClick is null
        Log.d("HomeFragment", "onFavoriteClick is null: ${onFavoriteClick == null}")

        // Initialize Room database
        val movieDatabase = MovieDatabase.getDatabase(requireActivity())
        movieDao = movieDatabase.movieDao()

        // Initialize RecyclerView for Popular Movies
        val recyclerView1: RecyclerView = view.findViewById(R.id.recycler_view_1)
        recyclerView1.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        movieAdapter = MovieAdapter(mutableListOf(),
            onFavoriteClick = {movie ->
                toggleFavorite(movie,movieAdapter)
            },
            onItemClick = {movie ->
                val bottomSheet = MovieDetailBottomSheetFragment.newInstance(movie)
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            })
        recyclerView1.adapter = movieAdapter

        // Initialize RecyclerView for Top Rated Movies
        val recyclerView2: RecyclerView = view.findViewById(R.id.recycler_view_2)
        recyclerView2.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        topRatedMovieAdapter = MovieAdapter(mutableListOf(),
            onFavoriteClick = {movie ->
                toggleFavorite(movie,topRatedMovieAdapter)
            },
            onItemClick = {movie ->
                val bottomSheet = MovieDetailBottomSheetFragment.newInstance(movie)
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            })
        recyclerView2.adapter = topRatedMovieAdapter

        // Initialize RecyclerView for Upcoming Movies
        val recyclerView3: RecyclerView = view.findViewById(R.id.recycler_view_3)
        recyclerView3.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        upcomingMovieAdapter = MovieAdapter(mutableListOf(),
            onFavoriteClick = {movie ->
                toggleFavorite(movie,upcomingMovieAdapter)
            },
            onItemClick = {movie ->
                val bottomSheet = MovieDetailBottomSheetFragment.newInstance(movie)
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            })
        recyclerView3.adapter = upcomingMovieAdapter

        // Observe popular movies
        viewModel.popularMovies.observe(requireActivity()) { movies ->
            lifecycleScope.launch {
                movies.forEach { movie ->
                    movie.isFavorite = movieDao.isFavorite(movie.id)
                }
                movieAdapter.updateList(movies.toMutableList()) // Convert to MutableList
            }
        }

        // Observe top-rated movies
        viewModel.topRatedMovies.observe(requireActivity()) { movies ->
            lifecycleScope.launch {
                movies.forEach { movie ->
                    movie.isFavorite = movieDao.isFavorite(movie.id)
                }
                topRatedMovieAdapter.updateList(movies.toMutableList())
            }
        }

        // Observe upcoming movies
        viewModel.upcomingMovies.observe(requireActivity()) { movies ->
            lifecycleScope.launch {
                movies.forEach { movie ->
                    movie.isFavorite = movieDao.isFavorite(movie.id)
                }
                upcomingMovieAdapter.updateList(movies.toMutableList())
            }

        }

        // Observe error messages
        viewModel.errorMessage.observe(requireActivity()) { error ->
            println("Error: $error")
        }

        // Fetch popular movies
        val apiKey = "3b1520a0671f5e378efbb5ea9dccab0d"
        viewModel.fetchPopularMovies(apiKey)
        viewModel.fetchTopRatedMovies(apiKey)
        viewModel.fetchUpcomingMovies(apiKey)

        return view
    }

    private fun toggleFavorite(movie: Movie, movieAdapter: MovieAdapter) {
        // Toggle the favorite state of the movie
        movie.isFavorite = !movie.isFavorite

        // Save or remove the movie from the local database
        if (movie.isFavorite) {
            saveToFavorites(movie)
        } else {
            removeFromFavorites(movie)
        }

        // Notify the adapter of the change
        val position = movieAdapter.getCurrentList().indexOf(movie)
        movieAdapter.notifyItemChanged(position)
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