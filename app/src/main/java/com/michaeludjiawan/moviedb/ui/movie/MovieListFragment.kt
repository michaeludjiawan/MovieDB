package com.michaeludjiawan.moviedb.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.ui.common.BaseFragment
import com.michaeludjiawan.moviedb.ui.common.FilterTypeBottomDialog
import com.michaeludjiawan.moviedb.util.toVisibility
import kotlinx.android.synthetic.main.movie_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class MovieListFragment : BaseFragment() {

    private val viewModel by viewModel<MovieListViewModel>()

    private val filterTypeDialog by lazy { FilterTypeBottomDialog() }

    private var getMoviesJob: Job? = null

    private val onMovieClick: (Movie) -> Unit = {
        val direction = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(it.id, it.title)
        findNavController().navigate(direction)
    }

    private val movieAdapter = MoviePagingAdapter(onMovieClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultToolbar(getString(R.string.app_name), showBackButton = false)

        initRecyclerView()
        initFilterDialog()

        getMovies(FilterType.POPULAR)
    }

    private fun initRecyclerView() {
        movieAdapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { movieAdapter.retry() },
            footer = MovieLoadStateAdapter { movieAdapter.retry() }
        )

        movieAdapter.addLoadStateListener { loadState ->
            if (isDetached) return@addLoadStateListener

            if (loadState.refresh !is LoadState.NotLoading) {
                rv_movie_list.visibility = View.GONE
                pb_movie_progress_bar.visibility = toVisibility(loadState.refresh is LoadState.Loading)
                btn_movie_retry.visibility = toVisibility(loadState.refresh is LoadState.Error)
            } else {
                rv_movie_list.visibility = View.VISIBLE
                pb_movie_progress_bar.visibility = View.GONE
                btn_movie_retry.visibility = View.GONE

                val errorState = when {
                    loadState.append is LoadState.Error -> {
                        loadState.append as LoadState.Error
                    }
                    loadState.prepend is LoadState.Error -> {
                        loadState.prepend as LoadState.Error
                    }
                    else -> {
                        null
                    }
                }
                errorState?.let {
                    Toast.makeText(requireContext(), "Error: ${it.error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        rv_movie_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }

        lifecycleScope.launch {
            movieAdapter.dataRefreshFlow.collect {
                rv_movie_list.scrollToPosition(0)
            }
        }

        btn_movie_retry.setOnClickListener { movieAdapter.retry() }
    }

    private fun initFilterDialog() {
        filterTypeDialog.valueSelected.observe(viewLifecycleOwner, Observer { filterType ->
            getMovies(filterType)
        })

        btn_movie_list_category.setOnClickListener {
            filterTypeDialog.show(parentFragmentManager, "")
        }
    }

    private fun getMovies(filterType: FilterType) {
        getMoviesJob?.cancel()
        getMoviesJob = lifecycleScope.launch {
            viewModel.getMovies(filterType).collectLatest { movieAdapter.submitData(it) }
        }
    }

}