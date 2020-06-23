package com.michaeludjiawan.moviedb.ui.movie.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.michaeludjiawan.moviedb.FakeMovieRepository
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.repository.MovieRepository
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class MovieDetailFragmentTest {

    companion object {
        private lateinit var movieRepository: FakeMovieRepository

        @BeforeClass
        @JvmStatic
        fun injectModules() = loadModules
        private val loadModules by lazy {
            loadKoinModules(
                module {
                    viewModel { MovieDetailViewModel(get()) }
                    factory<MovieRepository> { movieRepository }
                }
            )
        }
    }

    @Before
    fun setup() {
        movieRepository = FakeMovieRepository()
    }

    @Test
    fun detail_loadSuccess() {
        launchFragment()
        checkContentDetailDisplayed()
    }

    @Test
    fun detail_loadError() {
        movieRepository.setReturnError(true)
        launchFragment()
        checkErrorDetailDisplayed()
    }

    @Test
    fun detail_retrySuccess() {
        movieRepository.setReturnError(true)
        launchFragment()
        checkErrorDetailDisplayed()

        movieRepository.setReturnError(false)

        onView(withId(R.id.btn_movie_detail_retry)).perform(ViewActions.click())

        checkContentDetailDisplayed()
    }

    @Test
    fun detail_retryError() {
        movieRepository.setReturnError(true)
        launchFragment()
        checkErrorDetailDisplayed()

        onView(withId(R.id.btn_movie_detail_retry)).perform(ViewActions.click())

        checkErrorDetailDisplayed()
    }

    private fun launchFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)

        val bundle = Bundle().apply {
            putInt("movieId", 10)
            putString("movieName", "Title 10")
        }

        val scenario = launchFragmentInContainer<MovieDetailFragment>(bundle)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    private fun checkContentDetailDisplayed() {
        onView(withId(R.id.ll_movie_detail_content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.btn_movie_detail_retry)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        onView(withId(R.id.pb_movie_detail_progress_bar)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    private fun checkErrorDetailDisplayed() {
        onView(withId(R.id.btn_movie_detail_retry)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.ll_movie_detail_content)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        onView(withId(R.id.ll_movie_detail_content)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }
}