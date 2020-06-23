package com.michaeludjiawan.moviedb

import androidx.paging.PagingData
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.model.Review
import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import com.michaeludjiawan.moviedb.ui.movie.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*

class FakeMovieRepository : MovieRepository {

    private val movies = ArrayList<Movie>()

    private val movie = Movie(1, "Title", Date(123456789), "Overview", "path")

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun getMovies(filter: FilterType): Flow<PagingData<Movie>> {
        return flowOf(PagingData.empty())
    }

    override suspend fun getDetail(movieId: Int): Flow<Result<Movie>> {
        if (shouldReturnError) {
            return flowOf(Result.Error(Exception()))
        }

        return flowOf(Result.Success(movie))
    }

    override fun getReview(movieId: Int): Flow<PagingData<Review>> {
        return flowOf(PagingData.empty())
    }

    override suspend fun saveAsFavorite(movie: Movie) {
        movies.add(movie)
    }

    override suspend fun removeAsFavorite(movie: Movie) {
        movies.remove(movie)
    }

    override suspend fun getFavorite(movieId: Int): Movie? {
        return movies.find { it.id == movieId }
    }

    override fun getFavorites(): Flow<List<Movie>> {
        return flowOf(movies)
    }
}