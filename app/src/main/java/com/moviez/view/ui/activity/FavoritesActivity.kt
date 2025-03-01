package com.moviez.view.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviez.R
import com.moviez.view.adapter.MovieAdapter
import com.moviez.view.ui.fragment.MovieDetailBottomSheetFragment
import com.moviez.viewModel.FavoritesViewModel
import com.moviez.viewModel.FavoritesViewModelFactory
import com.moviez.viewModel.MyApplication

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // Set up back button
        val backButton: ImageView = findViewById(R.id.ivBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.favorites_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this,3)

        favoritesAdapter = MovieAdapter(mutableListOf(),
            onFavoriteClick = {movie ->

            },
            onItemClick = {movie ->
//                val bottomSheet = MovieDetailBottomSheetFragment.newInstance(movie)
//                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            })
        recyclerView.adapter = favoritesAdapter

        // Obtain the MovieDao instance from your database
        val movieDao = (application as MyApplication).database.movieDao()

        // Create the ViewModel using the factory
        val factory = FavoritesViewModelFactory(movieDao)
        favoritesViewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)

        // Observe favorite movies from the ViewModel
        favoritesViewModel.favoriteMovies.observe(this) { movies ->
            Log.d("FavoritesActivity", "Observed ${movies.size} favorite movies")
            favoritesAdapter.updateList(movies.toMutableList())
        }
    }
}
