package com.michaeludjiawan.moviedb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michaeludjiawan.moviedb.data.model.Movie

@Database(
    entities = [Movie::class],
    version = 1
)
@TypeConverters(value = [DateTypeConverter::class])
abstract class AppDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}