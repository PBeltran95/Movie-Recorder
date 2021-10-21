package ar.com.example.alkemymovieapp.core

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import retrofit2.http.*

interface WebService {

    @GET("popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") page:Int) : MovieList

    @GET
    suspend fun getMovieDetails(@Url movieId:String, @Query("api_key") apiKey: String) : MovieDetail
}