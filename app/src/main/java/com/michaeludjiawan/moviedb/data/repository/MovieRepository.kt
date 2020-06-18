package com.michaeludjiawan.moviedb.data.repository

import androidx.paging.PagingData
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.model.Review
import com.michaeludjiawan.moviedb.ui.movie.FilterType
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(filter: FilterType): Flow<PagingData<Movie>>
    suspend fun getDetail(movieId: Int): Result<Movie>
    fun getReview(movieId: Int): Flow<PagingData<Review>>
}