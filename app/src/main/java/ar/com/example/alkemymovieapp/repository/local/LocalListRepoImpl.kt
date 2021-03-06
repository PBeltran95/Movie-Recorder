package ar.com.example.alkemymovieapp.repository.local

import ar.com.example.alkemymovieapp.data.local.MovieDao
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import javax.inject.Inject

class LocalListRepoImpl @Inject constructor(private val movieDao: MovieDao) : LocalListRepo {

    override suspend fun getFavoriteMovies(): List<MovieEntity> = movieDao.getAllFavorites()

    override suspend fun getViewedMovies(): List<MovieEntity> = movieDao.getAllViewed()

    override suspend fun getAllToWatch(): List<MovieEntity> = movieDao.getAllToWatch()

}