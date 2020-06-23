package com.michaeludjiawan.moviedb

import android.app.Application
import com.michaeludjiawan.moviedb.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication : Application() {

    override fun onCreate() {
        startKoin {
            androidContext(this@TestApplication)
            modules(listOf(networkModule))
        }
    }

}