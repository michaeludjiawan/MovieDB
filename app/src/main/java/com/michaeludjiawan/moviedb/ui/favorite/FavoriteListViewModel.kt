package com.michaeludjiawan.moviedb.ui.favorite

import androidx.lifecycle.ViewModel
import com.michaeludjiawan.moviedb.data.repository.MovieRepository

class FavoriteListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val favorites = movieRepository.getFavorites()

}