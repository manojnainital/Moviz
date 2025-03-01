package com.moviez.model.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moviez.model.data.local.GenreIdsConverter
import kotlinx.android.parcel.Parcelize

data class PopularMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

@Entity(tableName = "favorite_movies")
@TypeConverters(GenreIdsConverter::class)
@Parcelize
data class Movie(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    @TypeConverters(GenreIdsConverter::class)
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var isFavorite: Boolean = false
): Parcelable
