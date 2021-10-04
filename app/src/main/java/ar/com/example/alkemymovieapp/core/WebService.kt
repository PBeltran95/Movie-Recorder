package ar.com.example.alkemymovieapp.core

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WebService {

    @GET("popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String) : MovieList

    @GET
    suspend fun getMovieDetails(@Url movieId:String, @Query("api_key") apiKey: String) : MovieDetail
}