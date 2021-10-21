package ar.com.example.alkemymovieapp.data.remote

import ar.com.example.alkemymovieapp.application.AppConstants
import ar.com.example.alkemymovieapp.core.WebService
import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteMovieDataSource @Inject constructor(private val webService: WebService){

    suspend fun getPopularMovies(page:Int):MovieList = webService.getPopularMovies(AppConstants.API_KEY, page)

    suspend fun getMovieDetails(movieId:String):MovieDetail = webService.getMovieDetails(movieId,AppConstants.API_KEY)

}