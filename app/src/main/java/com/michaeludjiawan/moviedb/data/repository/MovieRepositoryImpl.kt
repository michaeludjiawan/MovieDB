package com.michaeludjiawan.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.api.ApiService
import com.michaeludjiawan.moviedb.data.local.MovieDao
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.model.Review
import com.michaeludjiawan.moviedb.data.safeApiCall
import com.michaeludjiawan.moviedb.ui.movie.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getMovies(filter: FilterType): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MoviePagingSource(apiService, filter) }
        ).flow

    override suspend fun getDetail(movieId: Int): Result<Movie> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getMovieDetail(movieId) }
        }

    override fun getReview(movieId: Int): Flow<PagingData<Review>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ReviewPagingSource(apiService, movieId) }
        ).flow

    override suspend fun saveAsFavorite(movie: Movie) = movieDao.insert(movie)

    override suspend fun removeAsFavorite(movie: Movie) = movieDao.remove(movie)

    override suspend fun getFavorite(movieId: Int): Movie? = movieDao.getMovie(movieId)

    override fun getFavorites(): Flow<List<Movie>> = movieDao.getMovies()

}