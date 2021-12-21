package ar.com.example.alkemymovieapp.core

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import retrofit2.http.*

interface WebService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") page:Int) : MovieList

    @GET
    suspend fun getMovieDetails(@Url movieId:String, @Query("api_key") apiKey: String) : MovieDetail

    @GET("search/movie")
    suspend fun searchMovieByTitle(@Query("api_key") apiKey: String, @Query("query") movieTitle:String): MovieList
}