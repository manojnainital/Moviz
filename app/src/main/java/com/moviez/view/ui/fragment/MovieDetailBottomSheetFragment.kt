package com.moviez.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moviez.R
import com.moviez.model.data.remote.response.Movie

class MovieDetailBottomSheetFragment : BottomSheetDialogFragment() {

    private var movie: Movie? = null

    companion object {
        private const val ARG_MOVIE = "arg_movie"

        fun newInstance(movie: Movie): MovieDetailBottomSheetFragment {
            val fragment = MovieDetailBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable(ARG_MOVIE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail_bottom_sheet,container,false)

        val moviePoster: ImageView = view.findViewById(R.id.moviePoster)
        val movieTitle: TextView = view.findViewById(R.id.movieTitle)
        val movieReleaseDate: TextView = view.findViewById(R.id.movieReleaseDate)
        val movieRating: TextView = view.findViewById(R.id.movieRating)
        val movieOverview: TextView = view.findViewById(R.id.movieOverview)


        Glide.with(requireActivity())
            .load("https://image.tmdb.org/t/p/w1280${movie?.poster_path}")
            .into(moviePoster)

        movieTitle.text = movie?.title
        movieRating.text = "⭐ ${movie?.vote_average}"
        movieReleaseDate.text = movie?.release_date
        movieOverview.text = movie?.overview

        return view
    }
}
