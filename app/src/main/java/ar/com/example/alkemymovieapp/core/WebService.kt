package ar.com.example.alkemymovieapp.core

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import ar.com.example.alkemymovieapp.data.models.MovieTrailerResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WebService {

    @GET
    suspend fun getPopularMovies(@Url movieFilter:String, @Query("api_key") apiKey: String, @Query("page") page:Int) : MovieList

    @GET
    suspend fun getMovieDetails(@Url movieId:String, @Query("api_key") apiKey: String) : MovieDetail

    @GET("search/movie")
    suspend fun searchMovieByTitle(@Query("api_key") apiKey: String, @Query("query") movieTitle:String): MovieList

    @GET
    suspend fun getMovieVideo(@Url movieId: String, @Query("api_key") apiKey: String): MovieTrailerResponse
}