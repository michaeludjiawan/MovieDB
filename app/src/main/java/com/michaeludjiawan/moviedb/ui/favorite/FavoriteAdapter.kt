package com.michaeludjiawan.moviedb.ui.favorite

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.ui.movie.MovieViewHolder

class FavoriteAdapter(
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {

    private val favorites = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent, onItemClick)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    fun add(list: List<Movie>) {
        favorites.clear()
        favorites.addAll(list)
        notifyDataSetChanged()
    }
}