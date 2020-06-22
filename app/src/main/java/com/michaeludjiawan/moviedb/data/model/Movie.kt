package com.michaeludjiawan.moviedb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Movie(
    @SerializedName("id") @PrimaryKey val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("release_date") val releaseDate: Date,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String
)