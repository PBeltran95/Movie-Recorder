package ar.com.example.alkemymovieapp.repository.local

import ar.com.example.alkemymovieapp.data.models.MovieEntity

interface LocalMovieRepository {

    suspend fun elementExists(movieId:String): Boolean
    suspend fun getMovieDetails(movieId:String): MovieEntity
    suspend fun saveMovie(movie: MovieEntity)
    suspend fun updateMovie(movie: MovieEntity)

}