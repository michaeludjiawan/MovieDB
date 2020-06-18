package com.michaeludjiawan.moviedb.data

import retrofit2.Response

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call.invoke()
        Result.create(response)
    } catch (throwable: Throwable) {
        Result.Error(throwable)
    }
}