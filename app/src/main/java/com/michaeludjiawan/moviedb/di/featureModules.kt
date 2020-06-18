package com.michaeludjiawan.moviedb.di

import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import com.michaeludjiawan.moviedb.data.repository.MovieRepositoryImpl
import org.koin.dsl.module

val featureModules = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}