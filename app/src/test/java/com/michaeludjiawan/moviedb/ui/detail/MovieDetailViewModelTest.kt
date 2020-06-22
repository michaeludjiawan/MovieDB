package com.michaeludjiawan.moviedb.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.michaeludjiawan.moviedb.MainCoroutineRule
import com.michaeludjiawan.moviedb.awaitNextValue
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.model.Review
import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import com.michaeludjiawan.moviedb.ui.movie.detail.MovieDetailViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

    private lateinit var viewModel: MovieDetailViewModel

    private val movieRepository = mock<MovieRepository>()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val movie = Movie(1, "Title", Date(123456789), "Overview", "path")

    @Before
    fun setup() {
        viewModel = MovieDetailViewModel(movieRepository)
    }

    @Test
    fun setMovieId_fetchDetail_loadSuccess() = runBlockingTest {
        whenever(movieRepository.getDetail(1)).thenReturn(
            flowOf(Result.Success(movie))
        )
        viewModel.movie.observeForever { }

        viewModel.setMovieId(1)

        assertTrue((viewModel.movie.awaitNextValue() as Result.Success).data == movie)
    }

    @Test
    fun setMovieId_detailLoaded_loadSuccess() = runBlockingTest {
        whenever(movieRepository.getDetail(1)).thenReturn(
            flowOf(Result.Success(movie))
        )
        viewModel.movie.observeForever { }

        viewModel.setMovieId(1)

        assertTrue(viewModel.isLoaded())
    }

    @Test
    fun setMovieId_fetchReviews_loadSuccess() = runBlockingTest {
        val data = PagingData.empty<Review>()
        whenever(movieRepository.getReview(1)).thenReturn(flowOf(data))
        viewModel.reviews.observeForever { }

        viewModel.setMovieId(1)

        assertTrue(viewModel.reviews.awaitNextValue() == data)
    }

    @Test
    fun isFavorite_validId_localSuccess() = runBlockingTest {
        whenever(movieRepository.getFavorite(1)).thenReturn(movie)
        viewModel.setMovieId(1)

        val isFavorite = viewModel.isFavorite()

        assertTrue(isFavorite)
    }

    @Test
    fun isFavorite_validId_localEmpty() = runBlockingTest {
        whenever(movieRepository.getFavorite(1)).thenReturn(null)
        viewModel.setMovieId(1)

        val isFavorite = viewModel.isFavorite()

        assertFalse(isFavorite)
    }

    @Test
    fun isFavorite_invalidId_throwException() = runBlockingTest {
        try {
            viewModel.isFavorite()
            fail("Should have thrown NullPointerException")
        } catch (e: Exception) {
        }
    }
}