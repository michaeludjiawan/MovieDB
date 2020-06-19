package com.michaeludjiawan.moviedb.di

import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import com.michaeludjiawan.moviedb.data.repository.MovieRepositoryImpl
import com.michaeludjiawan.moviedb.ui.movie.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }

    viewModel { MovieListViewModel(get()) }
}