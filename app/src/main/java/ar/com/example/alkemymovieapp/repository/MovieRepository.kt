package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.data.models.MovieList

interface MovieRepository {

    suspend fun getPopularMovies(page:Int, movieFilter: String):MovieList

    suspend fun getMovieDetails(movieId:String): MovieEntity

    suspend fun searchMovieByTitle(movieTitle:String): MovieList

    suspend fun saveFavoriteMovie(favoriteMovie: MovieEntity)

    suspend fun updateMovie(movie: MovieEntity)
}