package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.application.AppConstants.API_KEY
import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import ar.com.example.alkemymovieapp.data.remote.WebService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteMovieRepositoryImpl @Inject constructor(private val webService: WebService):
    RemoteMovieRepository {

    override suspend fun getMovies(movieFilter: String, page:Int):MovieList = webService.getMovies(movieFilter, API_KEY, page)

    override suspend fun getMovieDetails(movieId:String):MovieDetail = webService.getMovieDetails("movie/${movieId}", API_KEY)

    override suspend fun searchMovieByTitle(movieTitle: String): MovieList = webService.searchMovieByTitle(API_KEY, movieTitle)

}