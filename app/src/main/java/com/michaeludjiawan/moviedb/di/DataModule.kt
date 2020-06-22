package com.michaeludjiawan.moviedb.di

import android.content.Context
import androidx.room.Room
import com.michaeludjiawan.moviedb.data.local.AppDb
import org.koin.dsl.module

val dataModule = module {
    single { createDatabase(get()) }

    single { (get() as AppDb).movieDao() }
}

fun createDatabase(context: Context): AppDb {
    return Room.databaseBuilder(context, AppDb::class.java, "app_db")
        .fallbackToDestructiveMigration()
        .build()
}