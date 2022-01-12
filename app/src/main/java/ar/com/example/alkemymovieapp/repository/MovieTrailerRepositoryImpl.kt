package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.application.AppConstants
import ar.com.example.alkemymovieapp.data.models.MovieTrailerResponse
import ar.com.example.alkemymovieapp.data.remote.WebService
import javax.inject.Inject

class MovieTrailerRepositoryImpl @Inject constructor(private val webService: WebService) : MovieTrailerRepository {
    override suspend fun getMovieTrailer(movieId: String): MovieTrailerResponse =
        webService.getMovieVideo("movie/${movieId}/videos", AppConstants.API_KEY)
}