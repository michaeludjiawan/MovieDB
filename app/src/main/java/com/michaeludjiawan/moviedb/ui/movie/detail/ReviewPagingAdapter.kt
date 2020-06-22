package com.michaeludjiawan.moviedb.ui.movie.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.michaeludjiawan.moviedb.R
import com.michaeludjiawan.moviedb.data.model.Review
import com.michaeludjiawan.moviedb.util.formatHtml
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewPagingAdapter : PagingDataAdapter<Review, RecyclerView.ViewHolder>(REVIEW_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as ReviewViewHolder).bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ReviewViewHolder.create(parent)

    companion object {
        val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean = oldItem.id == newItem.id
        }
    }

}

class ReviewViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(review: Review) {
        with(view) {
            tv_review_content.text = review.content.formatHtml
            tv_review_author.text = review.author
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): ReviewViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
            return ReviewViewHolder(view)
        }
    }

}