package com.moviez.model.data.remote

import com.moviez.model.data.remote.response.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): PopularMoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): PopularMoviesResponse // Using same response structure

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): PopularMoviesResponse // Using same response structure

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): PopularMoviesResponse  // Using same response structure
}
