package com.michaeludjiawan.moviedb

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.michaeludjiawan.moviedb.di.dataModule
import com.michaeludjiawan.moviedb.di.featureModule
import com.michaeludjiawan.moviedb.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    dataModule,
                    networkModule,
                    featureModule
                )
            )
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}