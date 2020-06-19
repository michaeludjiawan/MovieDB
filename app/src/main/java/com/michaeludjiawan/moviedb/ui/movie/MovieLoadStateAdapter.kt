package com.michaeludjiawan.moviedb.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.util.toVisibility
import kotlinx.android.synthetic.main.movie_load_state_footer_view_item.view.*

class MovieLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<MovieLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): MovieLoadStateViewHolder =
        MovieLoadStateViewHolder.create(parent, retry)

}

class MovieLoadStateViewHolder(
    private val view: View,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        view.tv_movie_load_state_retry_button.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            view.tv_movie_load_state_error_msg.text = loadState.error.localizedMessage
        }
        view.tv_movie_load_state_progress_bar.visibility = toVisibility(loadState is LoadState.Loading)
        view.tv_movie_load_state_retry_button.visibility = toVisibility(loadState !is LoadState.Loading)
        view.tv_movie_load_state_error_msg.visibility = toVisibility(loadState !is LoadState.Loading)
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MovieLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_load_state_footer_view_item, parent, false)
            return MovieLoadStateViewHolder(view, retry)
        }
    }

}