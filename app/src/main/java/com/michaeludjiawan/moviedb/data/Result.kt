package com.michaeludjiawan.moviedb.data

import retrofit2.Response

sealed class Result<out T : Any> {
    data class Loading<out T : Any>(val data: T?) : Result<Nothing>()
    data class Success<out T : Any>(val data: T?) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()

    companion object {
        fun <T : Any> create(throwable: Throwable): Result<T> {
            return Error(throwable)
        }

        fun <T : Any> create(response: Response<T>): Result<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                Success(body)
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string().orEmpty()

                val exception = ServerFailedException(errorCode, errorBody)
                Error(exception)
            }
        }
    }
}

class ServerFailedException(code: Int, message: String, fullErrorBody: String = "") :
    Exception(message) {
    var code: Int
        private set

    var errorBody: String
        private set

    init {
        this.code = code
        this.errorBody = fullErrorBody
    }
}