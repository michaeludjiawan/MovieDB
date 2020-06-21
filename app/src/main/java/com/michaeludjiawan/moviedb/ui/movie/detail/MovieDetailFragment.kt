package com.michaeludjiawan.moviedb.ui.movie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.ui.common.BaseFragment
import com.michaeludjiawan.moviedb.util.DateUtil
import com.michaeludjiawan.moviedb.util.toImageUrl
import kotlinx.android.synthetic.main.movie_detail_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment() {

    private val viewModel by viewModel<MovieDetailViewModel>()

    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultToolbar(args.movieName)

        viewModel.setMovieId(args.movieId)

        viewModel.movie.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    pb_movie_detail_progress_bar.visibility = View.VISIBLE
                    btn_movie_detail_retry.visibility = View.GONE
                    cv_movie_detail_content.visibility = View.GONE
                }
                is Result.Success -> {
                    cv_movie_detail_content.visibility = View.VISIBLE
                    btn_movie_detail_retry.visibility = View.GONE
                    pb_movie_detail_progress_bar.visibility = View.GONE

                    result.data?.let { loadData(it) }
                }
                is Result.Error -> {
                    btn_movie_detail_retry.visibility = View.VISIBLE
                    pb_movie_detail_progress_bar.visibility = View.GONE
                    cv_movie_detail_content.visibility = View.GONE
                }
            }
        })
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