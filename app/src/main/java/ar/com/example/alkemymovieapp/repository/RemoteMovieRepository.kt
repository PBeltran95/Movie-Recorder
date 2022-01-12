package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList

interface RemoteMovieRepository {

    suspend fun getMovies(movieFilter: String, page:Int): MovieList

    suspend fun getMovieDetails(movieId:String): MovieDetail

    suspend fun searchMovieByTitle(movieTitle: String): MovieList
}