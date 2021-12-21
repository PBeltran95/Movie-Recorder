package ar.com.example.alkemymovieapp.repository.local

import ar.com.example.alkemymovieapp.data.local.MovieDao
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import javax.inject.Inject

class LocalMovieRepoImpl @Inject constructor(private val movieDao: MovieDao) : LocalMovieRepo {

    override suspend fun getFavoriteMovies(): List<MovieEntity> = movieDao.getAllFavorites()

}