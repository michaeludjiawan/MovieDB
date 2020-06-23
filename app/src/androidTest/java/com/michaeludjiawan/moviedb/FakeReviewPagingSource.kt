package com.michaeludjiawan.moviedb

import androidx.paging.PagingSource
import com.michaeludjiawan.moviedb.data.model.Review

class FakeReviewPagingSource: PagingSource<Int, Review>() {

    private val reviews = ArrayList<Review>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return LoadResult.Page(
            data = reviews,
            prevKey = null,
            nextKey = null
        )
    }

    fun add(review: Review) {
        reviews.add(review)
    }
}