package com.michaeludjiawan.moviedb.ui.movie.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.repository.MovieRepository

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val movieId = MutableLiveData<Int>()

    val movie = switchMap(movieId) { movieId ->
        liveData {
            emit(Result.Loading())
            emit(movieRepository.getDetail(movieId))
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

}
