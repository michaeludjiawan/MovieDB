package com.michaeludjiawan.moviedb.data.repository

import androidx.paging.PagingSource
import com.michaeludjiawan.moviedb.data.Result
import com.michaeludjiawan.moviedb.data.api.ApiService
import com.michaeludjiawan.moviedb.data.model.Movie
import com.michaeludjiawan.moviedb.data.safeApiCall
import com.michaeludjiawan.moviedb.ui.movie.FilterType
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class MoviePagingSource(
    private val apiService: ApiService,
    private val filterType: FilterType
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val response = safeApiCall { apiService.getMovies(filterType.toString().toLowerCase(Locale.US), position) }
            if (response is Result.Success) {
                val movies = response.data?.movies.orEmpty()
                LoadResult.Page(
                    data = movies,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (movies.isEmpty()) null else position + 1
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