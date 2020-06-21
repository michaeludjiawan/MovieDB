package com.michaeludjiawan.moviedb.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.util.DateUtil
import com.michaeludjiawan.moviedb.util.toImageUrl
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviePagingAdapter(
    private val onItemClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(MOVIE_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as MovieViewHolder).bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MovieViewHolder.create(parent, onItemClick)

    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        }
    }

}

class MovieViewHolder(
    private val view: View,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Movie) {
        with(view) {
            Glide.with(view.context)
                .load(movie.posterPath.toImageUrl())
                .apply(RequestOptions().placeholder(R.color.black_12))
                .into(iv_movie_poster)

            tv_movie_title.text = movie.title
            tv_movie_release_date.text = DateUtil.format(movie.releaseDate)
            tv_movie_overview.text = movie.overview

            setOnClickListener { onItemClick(movie) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (Movie) -> Unit
        ): MovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
            return MovieViewHolder(view, onItemClick)
        }
    }

}