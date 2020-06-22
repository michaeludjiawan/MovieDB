package com.michaeludjiawan.moviedb.data.api

import com.michaeludjiawan.moviedb.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/{filter_type}")
    suspend fun getMovies(
        @Path(value = "filter_type", encoded = true) filterType: String,
        @Query("page") page: Int
    ): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path(value = "movie_id") movieId: Int
    ): Response<Movie>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path(value = "movie_id") movieId: Int,
        @Query("page") page: Int
    ): Response<ReviewListResponse>

}