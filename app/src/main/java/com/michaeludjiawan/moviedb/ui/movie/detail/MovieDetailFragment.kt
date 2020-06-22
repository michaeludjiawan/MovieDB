package com.michaeludjiawan.moviedb.ui.movie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.ui.common.BaseFragment
import com.michaeludjiawan.moviedb.ui.movie.MovieLoadStateAdapter
import com.michaeludjiawan.moviedb.util.DateUtil
import com.michaeludjiawan.moviedb.util.toImageUrl
import com.michaeludjiawan.moviedb.util.toVisibility
import kotlinx.android.synthetic.main.movie_detail_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment() {

    private val viewModel by viewModel<MovieDetailViewModel>()

    private val args by navArgs<MovieDetailFragmentArgs>()

    private val reviewPagingAdapter = ReviewPagingAdapter()

    private var reviewJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultToolbar(args.movieName)
        viewModel.setMovieId(args.movieId)

        initRecyclerView()
        initObservers()
        initFavorite()
    }

    private fun initRecyclerView() {
        reviewPagingAdapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { reviewPagingAdapter.retry() },
            footer = MovieLoadStateAdapter { reviewPagingAdapter.retry() }
        )

        reviewPagingAdapter.addLoadStateListener { loadState ->
            if (isDetached) return@addLoadStateListener

            if (loadState.append.endOfPaginationReached) {
                tv_movie_detail_review_label.text = getString(
                    if (reviewPagingAdapter.itemCount == 0) R.string.review_empty_label else R.string.review_exist_label
                )
            }

            if (loadState.refresh !is LoadState.NotLoading) {
                rv_movie_detail_review_list.visibility = View.GONE
                pb_movie_detail_review_progress.visibility = toVisibility(loadState.refresh is LoadState.Loading)
                btn_movie_detail_review_retry.visibility = toVisibility(loadState.refresh is LoadState.Error)
            } else {
                rv_movie_detail_review_list.visibility = View.VISIBLE
                pb_movie_detail_review_progress.visibility = View.GONE
                btn_movie_detail_review_retry.visibility = View.GONE

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    else -> null
                }

                errorState?.let {
                    Toast.makeText(requireContext(), "Error: ${it.error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        rv_movie_detail_review_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewPagingAdapter
        }
    }

    private fun initObservers() {
        viewModel.movie.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    pb_movie_detail_progress_bar.visibility = View.VISIBLE
                    btn_movie_detail_retry.visibility = View.GONE
                    ll_movie_detail_content.visibility = View.GONE
                }
                is Result.Success -> {
                    ll_movie_detail_content.visibility = View.VISIBLE
                    btn_movie_detail_retry.visibility = View.GONE
                    pb_movie_detail_progress_bar.visibility = View.GONE

                    result.data?.let { loadData(it) }
                }
                is Result.Error -> {
                    btn_movie_detail_retry.visibility = View.VISIBLE
                    pb_movie_detail_progress_bar.visibility = View.GONE
                    ll_movie_detail_content.visibility = View.GONE
                }
            }
        })

        btn_movie_detail_retry.setOnClickListener {
            viewModel.refresh()
        }

        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            reviewJob?.cancel()
            reviewJob = viewLifecycleOwner.lifecycleScope.launch {
                reviewPagingAdapter.submitData(it)
            }
        })
    }

    private fun initFavorite() {
        viewLifecycleOwner.lifecycleScope.launch {
            iv_movie_detail_fav.isSelected = viewModel.isFavorite()
        }

        iv_movie_detail_fav.setOnClickListener {
            val nextState = !iv_movie_detail_fav.isSelected
            iv_movie_detail_fav.isSelected = nextState
            viewModel.setFavorite(nextState)
        }
    }

    private fun loadData(movie: Movie) {
        Glide.with(requireContext())
            .load(movie.posterPath.toImageUrl())
            .apply(RequestOptions().placeholder(R.color.black_12))
            .into(iv_movie_detail_poster)

        tv_movie_detail_title.text = movie.title
        tv_movie_detail_release_date.text = DateUtil.format(movie.releaseDate)
        tv_movie_detail_overview.text = movie.overview
    }

}