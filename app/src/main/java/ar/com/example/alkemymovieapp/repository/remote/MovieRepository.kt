package ar.com.example.alkemymovieapp.repository.remote

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList

interface MovieRepository {

    suspend fun getPopularMovies():MovieList

    suspend fun getMovieDetails(movieId:String): MovieDetail

}