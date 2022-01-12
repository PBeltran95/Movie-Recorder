package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity

interface MovieDetailsCacheManager {

    suspend fun getMovieDetails(movieId:String): MovieEntity

}