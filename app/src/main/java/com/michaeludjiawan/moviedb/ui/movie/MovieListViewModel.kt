package com.michaeludjiawan.moviedb.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun getMovies(filterType: FilterType): Flow<PagingData<Movie>> {
        return movieRepository.getMovies(filterType).cachedIn(viewModelScope)
    }

}