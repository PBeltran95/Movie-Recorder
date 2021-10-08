package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.data.models.MovieList

interface MovieRepository {

    suspend fun getPopularMovies():MovieList

    suspend fun getMovieDetails(movieId:String): MovieEntity

}