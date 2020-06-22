package com.michaeludjiawan.moviedb.data.repository

import androidx.paging.PagingSource
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.api.ApiService
import com.michaeludjiawan.moviedb.data.model.Review
import com.michaeludjiawan.moviedb.data.safeApiCall
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val apiService: ApiService,
    private val movieId: Int
) : PagingSource<Int, Review>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val position = params.key ?: 1
        return try {
            val response = safeApiCall { apiService.getMovieReviews(movieId, position) }
            if (response is Result.Success) {
                val reviews = response.data?.reviews.orEmpty()
                LoadResult.Page(
                    data = reviews,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (reviews.isEmpty()) null else position + 1
                )
            } else {
                return LoadResult.Error(Exception())
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}