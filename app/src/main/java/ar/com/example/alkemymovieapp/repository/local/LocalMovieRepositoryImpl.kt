package ar.com.example.alkemymovieapp.repository.local

import ar.com.example.alkemymovieapp.data.local.MovieDao
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import javax.inject.Inject

class LocalMovieRepositoryImpl @Inject constructor (private val movieDao: MovieDao):
    LocalMovieRepository {

    override suspend fun elementExists(movieId:String): Boolean = movieDao.elementExists(movieId.toInt())

    override suspend fun getMovieDetails(movieId:String): MovieEntity = movieDao.loadMovie(movieId.toInt())

    override suspend fun saveMovie(movie:MovieEntity){ movieDao.saveMovie(movie) }

    override suspend fun updateMovie(movie:MovieEntity){ movieDao.updateMovie(movie) }
}