package ar.com.example.alkemymovieapp.data.local

import ar.com.example.alkemymovieapp.data.models.MovieEntity
import javax.inject.Inject

class LocalMovieDataSource @Inject constructor (private val movieDao: MovieDao) {

    suspend fun elementExists(movieId:String): Boolean = movieDao.elementExists(movieId.toInt())

    suspend fun getMovieDetails(movieId:String): MovieEntity = movieDao.loadMovie(movieId.toInt())

    suspend fun saveMovie(movie:MovieEntity){ movieDao.saveMovie(movie) }

    suspend fun updateMovie(movie:MovieEntity){ movieDao.updateMovie(movie) }
}