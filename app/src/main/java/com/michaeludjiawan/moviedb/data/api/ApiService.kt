package com.michaeludjiawan.moviedb.data.api

import com.michaeludjiawan.moviedb.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("movie/{filter_type}")
    suspend fun getMovies(
        @Path(value = "filter_type") filterType: String
    ): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path(value = "movie_id") movieId: Int
    ): Response<Movie>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path(value = "movie_id") movieId: Int
    ): Response<ReviewListResponse>

}