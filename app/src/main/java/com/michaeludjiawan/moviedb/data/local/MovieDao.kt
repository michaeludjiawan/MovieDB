package com.michaeludjiawan.moviedb.data.local

import androidx.room.*
import com.michaeludjiawan.moviedb.data.model.Movie

/*
Used to store favorite movies.
 */
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Delete
    suspend fun remove(movie: Movie)

    @Query("SELECT * FROM Movie WHERE :id == id")
    suspend fun getMovie(id: Int): Movie?

}