package com.michaeludjiawan.moviedb.data.local

import androidx.room.*
import com.michaeludjiawan.moviedb.data.model.Movie
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM Movie")
    fun getMovies(): Flow<List<Movie>>
}