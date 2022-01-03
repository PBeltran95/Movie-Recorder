package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.models.MovieTrailerResponse

interface MovieTrailerRepository {

    suspend fun getMovieTrailer(movieId: String): MovieTrailerResponse

}