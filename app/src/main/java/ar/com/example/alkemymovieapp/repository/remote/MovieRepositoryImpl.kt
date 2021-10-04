package ar.com.example.alkemymovieapp.repository.remote

import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.data.models.MovieList
import ar.com.example.alkemymovieapp.data.remote.MovieDataSource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (private val remoteDataSource : MovieDataSource): MovieRepository {

    override suspend fun getPopularMovies(): MovieList = remoteDataSource.getPopularMovies()


    override suspend fun getMovieDetails(movieId:String): MovieDetail = remoteDataSource.getMovieDetails(movieId)

}