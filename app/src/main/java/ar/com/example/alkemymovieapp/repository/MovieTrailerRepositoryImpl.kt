package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.application.AppConstants
import ar.com.example.alkemymovieapp.core.WebService
import ar.com.example.alkemymovieapp.data.models.MovieTrailerResponse
import javax.inject.Inject

class MovieTrailerRepositoryImpl @Inject constructor(private val webService: WebService) : MovieTrailerRepository {
    override suspend fun getMovieTrailer(movieId: String): MovieTrailerResponse =
        webService.getMovieVideo("movie/${movieId}/videos", AppConstants.API_KEY)
}