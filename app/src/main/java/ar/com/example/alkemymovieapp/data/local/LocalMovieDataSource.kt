package ar.com.example.alkemymovieapp.data.local

import ar.com.example.alkemymovieapp.application.AppConstants
import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import javax.inject.Inject

class LocalMovieDataSource @Inject constructor (private val movieDao: MovieDao) {

    suspend fun getMovieDetails(movieId:String): MovieEntity = movieDao.loadMovie(movieId.toInt())

    suspend fun saveMovie(movie:MovieEntity){
        movieDao.saveMovie(movie)
    }
}