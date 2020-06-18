package com.michaeludjiawan.moviedb.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("release_date") val releaseDate: Date,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String
)