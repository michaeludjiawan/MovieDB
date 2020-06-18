package com.michaeludjiawan.moviedb.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.michaeludjiawan.moviedb.BuildConfig
import com.michaeludjiawan.moviedb.data.api.ApiService
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { createAuthTokenInterceptor() }

    single { createOkHttpClient(get(), get()) }
    single { createGson() }

    single { createWebService<ApiService>(get(), get()) }
}

fun createOkHttpClient(
    authTokenInterceptor: Interceptor,
    connectionSpec: ConnectionSpec
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectionSpecs(arrayListOf(connectionSpec, ConnectionSpec.CLEARTEXT))
        .addInterceptor(authTokenInterceptor)
        .build()
}

fun createGson(): Gson {
    return GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .serializeNulls()
        .create()
}

fun createAuthTokenInterceptor(): Interceptor {
    return Interceptor { chain ->
        val request = chain.request()

        val newRequest = request.newBuilder().also { requestBuilder ->
            requestBuilder.header("Authorization", "Bearer ${BuildConfig.API_KEY_V4}")
        }.build()

        chain.proceed(newRequest)
    }
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, gson: Gson): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    return retrofit.create(T::class.java)
}
