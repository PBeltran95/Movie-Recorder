package ar.com.example.alkemymovieapp.data.local

import androidx.room.*
import ar.com.example.alkemymovieapp.data.models.MovieEntity


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieEntity)

    @Query("SELECT * FROM movieentity WHERE id = :movie_id")
    suspend fun loadMovie(movie_id:Int):MovieEntity

    @Query("SELECT EXISTS (SELECT 1 FROM movieentity WHERE id = :movie_id)")
    suspend fun elementExists(movie_id: Int): Boolean

    @Query("SELECT * FROM movieentity WHERE isFavorite = 1")
    suspend fun getAllFavorites(): List<MovieEntity>


}