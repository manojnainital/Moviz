package com.moviez.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviez.R
import com.moviez.model.data.remote.response.Movie

class MovieAdapter(
    private val movies: MutableList<Movie>,
    private val onFavoriteClick: (Movie) -> Unit,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    // Method to update the list
    fun updateList(newList: List<Movie>) {
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }

    // Method to get the current list
    fun getCurrentList(): List<Movie> {
        return movies
    }

    // Method to update favorite status
    fun updateFavorites(favoriteIds: List<Int>) {
        movies.forEach { movie ->
            movie.isFavorite = favoriteIds.contains(movie.id)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onFavoriteClick, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class MovieViewHolder(
        itemView: View,
        private val onFavoriteClick: (Movie) -> Unit,
        private val onItemClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.movie_poster)
        private val title: TextView = itemView.findViewById(R.id.movie_title)
        private val rating: TextView = itemView.findViewById(R.id.movie_rating)
        private val favorite: ImageView = itemView.findViewById(R.id.iv_favorite)

        fun bind(movie: Movie) {
            // Load movie poster using Glide
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .into(poster)

            // Set movie title
            title.text = movie.title

            // Set movie rating
            rating.text = "⭐ ${movie.vote_average}"

            // Set favorite icon state
            favorite.setImageResource(
                if (movie.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_empty
            )

            // Handle favorite button click
            favorite.setOnClickListener {
                onFavoriteClick(movie)
            }

            itemView.setOnClickListener {
                onItemClick(movie) // Handle item click
            }
        }
    }
}