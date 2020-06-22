package com.michaeludjiawan.moviedb.ui.movie.detail

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val movieId = MutableLiveData<Int>()

    private var resultMovie: Movie? = null

    val movie = switchMap(movieId) { movieId ->
        liveData {
            emit(Result.Loading())

            movieRepository.getDetail(movieId).collectLatest { result ->
                if (result is Result.Success) {
                    resultMovie = result.data
                }

                emit(result)
            }
        }
    }

    val reviews = switchMap(movieId) { movieId ->
        movieId?.let {
            movieRepository.getReview(it).asLiveData()
        }
    }

    fun setMovieId(movieId: Int) {
        this.movieId.value = movieId
    }

    fun refresh() {
        this.movieId.value = this.movieId.value
    }

    suspend fun isFavorite(): Boolean {
        val movieId = movieId.value!!

        val savedMovie = movieRepository.getFavorite(movieId)
        return savedMovie != null
    }

    fun setFavorite(isFavorite: Boolean) = viewModelScope.launch {
        val movie = resultMovie ?: throw IllegalStateException("No movie data.")

        if (isFavorite) {
            movieRepository.saveAsFavorite(movie)
        } else {
            movieRepository.removeAsFavorite(movie)
        }
    }

    // Convenient method to check if data already loaded whether from local or remote
    fun isLoaded() = resultMovie != null

}
