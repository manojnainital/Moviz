package com.moviez.view.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviez.R
import com.moviez.model.data.local.MovieDao
import com.moviez.model.data.local.MovieDatabase
import com.moviez.model.data.remote.response.Movie
import com.moviez.view.adapter.MovieAdapter
import com.moviez.view.ui.fragment.MovieDetailBottomSheetFragment
import com.moviez.viewModel.SearchViewModel
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: MovieAdapter
    private lateinit var tvMessage: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize Room database
        val movieDatabase = MovieDatabase.getDatabase(this)
        movieDao = movieDatabase.movieDao()

        // Initialize views
        tvMessage = findViewById(R.id.tv_message)
        recyclerView = findViewById(R.id.search_results_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchAdapter = MovieAdapter(mutableListOf(),
            onFavoriteClick = {movie ->
//                toggleFavorite(movie,searchAdapter)
            },
            onItemClick = {movie ->
//                val bottomSheet = MovieDetailBottomSheetFragment.newInstance(movie)
//                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            })
        recyclerView.adapter = searchAdapter

        // Set up back button
        val backButton: ImageButton = findViewById(R.id.ivBackButton)
        backButton.setOnClickListener {
            hideKeyboard() // Hide the keyboard
            onBackPressedDispatcher.onBackPressed()
        }

        // Set up search input
        val searchInput: EditText = findViewById(R.id.et_search)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim()
                if (!query.isNullOrEmpty()) {
                    showLoading() // Show loading message
                    searchViewModel.searchMovies(query) // Perform search
                } else {
                    showMessage("No Results Found") // Show empty state message
                    searchAdapter.updateList(mutableListOf()) // Clear the adapter list
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Observe search results
        searchViewModel.searchResults.observe(this) { movies ->
            if (movies.isEmpty()) {
                showMessage("No Results Found") // Show empty state message
            } else {
                hideMessage() // Hide message and show RecyclerView
                lifecycleScope.launch {
                    movies.forEach { movie ->
                        movie.isFavorite = movieDao.isFavorite(movie.id)
                    }
                    searchAdapter.updateList(movies.toMutableList()) // Update the adapter list
                }

            }
        }

        // Observe error messages
        searchViewModel.errorMessage.observe(this) { error ->
            showMessage("Error: $error") // Show error message
        }
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoading() {
        tvMessage.text = "Loading, please wait..."
        tvMessage.visibility = TextView.VISIBLE
        recyclerView.visibility = RecyclerView.GONE
    }

    private fun showMessage(message: String) {
        tvMessage.text = message
        tvMessage.visibility = TextView.VISIBLE
        recyclerView.visibility = RecyclerView.GONE
    }

    private fun hideMessage() {
        tvMessage.visibility = TextView.GONE
        recyclerView.visibility = RecyclerView.VISIBLE
    }

    private fun toggleFavorite(movie: Movie) {
        movie.isFavorite = !movie.isFavorite

        if(movie.isFavorite){
            saveToFavorites(movie)
        }else {
            removeFromFavorites(movie)
        }
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