package com.michaeludjiawan.moviedb.data.repository

import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.api.ApiService
import com.michaeludjiawan.moviedb.data.local.MovieDao
import com.michaeludjiawan.moviedb.data.model.Movie
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class MovieRepositoryImplTest {

    private lateinit var movieRepository: MovieRepositoryImpl

    private val testDispatcher = TestCoroutineDispatcher()

    private val apiService = mock<ApiService>()
    private val movieDao = mock<MovieDao>()

    private val localMovie = Movie(1, "Title", Date(123456789), "Overview", "path")
    private val remoteMovie = Movie(2, "Title", Date(123456789), "Overview", "path")

    @Before
    fun setup() {
        movieRepository = MovieRepositoryImpl(apiService, movieDao, testDispatcher)
    }

    @Test
    fun getDetail_verifyCalled() = runBlockingTest {
        movieRepository.getDetail(1).collect()

        verify(movieDao).getMovie(1)
        verify(apiService).getMovieDetail(1)
    }

    @Test
    fun getDetail_emptyLocal_onlyEmitRemote() = runBlockingTest {
        val response: Response<Movie> = Response.success(remoteMovie)
        whenever(apiService.getMovieDetail(1)).thenReturn(response)

        val result = movieRepository.getDetail(1).take(1).toList()

        assertTrue((result[0] as Result.Success).data == remoteMovie)
    }

    @Test
    fun getDetail_bothLocalRemoteExist_emitAll() = runBlockingTest {
        val response: Response<Movie> = Response.success(remoteMovie)
        whenever(apiService.getMovieDetail(1)).thenReturn(response)
        whenever(movieDao.getMovie(1)).thenReturn(localMovie)

        val result = movieRepository.getDetail(1).take(2).toList()

        assertTrue((result[0] as Result.Success).data == localMovie)

        assertTrue((result[1] as Result.Success).data == remoteMovie)
    }

}