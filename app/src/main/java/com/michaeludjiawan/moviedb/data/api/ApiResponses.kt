package com.michaeludjiawan.moviedb.data.api

import com.google.gson.annotations.SerializedName
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.model.Review

data class MovieListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val movies: List<Movie>
)

data class ReviewListResponse(
    @SerializedName("id") val id: String,
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val movies: List<Review>
)