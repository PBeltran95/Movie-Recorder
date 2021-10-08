package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.local.LocalMovieDataSource
import ar.com.example.alkemymovieapp.data.models.*
import ar.com.example.alkemymovieapp.data.remote.RemoteMovieDataSource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (private val remoteDataSourceRemote : RemoteMovieDataSource,
                                            private val localDataSource: LocalMovieDataSource):
    MovieRepository {

    override suspend fun getPopularMovies(): MovieList = remoteDataSourceRemote.getPopularMovies()


    override suspend fun getMovieDetails(movieId:String): MovieEntity {
        remoteDataSourceRemote.getMovieDetails(movieId).also { movie ->
            localDataSource.saveMovie(movie.toMovieEntity())
        }

        return localDataSource.getMovieDetails(movieId)
    }

}