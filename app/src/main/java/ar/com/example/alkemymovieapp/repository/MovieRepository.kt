package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.models.*

interface MovieRepository {

    suspend fun getPopularMovies():MovieList

    suspend fun getMovieDetails(movieId:String): MovieEntity
}