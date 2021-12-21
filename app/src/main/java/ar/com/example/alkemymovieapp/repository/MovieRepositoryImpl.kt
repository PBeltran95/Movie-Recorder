package ar.com.example.alkemymovieapp.repository

import ar.com.example.alkemymovieapp.data.local.LocalMovieDataSource
import ar.com.example.alkemymovieapp.data.models.*
import ar.com.example.alkemymovieapp.data.remote.RemoteMovieDataSource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (private val remoteDataSourceRemote : RemoteMovieDataSource,
                                            private val localDataSource: LocalMovieDataSource):
    MovieRepository {

    override suspend fun getPopularMovies(page:Int): MovieList = remoteDataSourceRemote.getPopularMovies(page)


    override suspend fun getMovieDetails(movieId:String): MovieEntity {

        return if (localDataSource.elementExists(movieId)){
            localDataSource.getMovieDetails(movieId)
        }else{
            remoteDataSourceRemote.getMovieDetails(movieId).also { movie ->
                localDataSource.saveMovie(movie.toMovieEntity())
            }
            localDataSource.getMovieDetails(movieId)
        }

    }

    override suspend fun searchMovieByTitle(movieTitle: String): MovieList =
        remoteDataSourceRemote.searchMovieByTitle(movieTitle)


    override suspend fun saveFavoriteMovie(favoriteMovie: MovieEntity) {
        localDataSource.saveMovie(favoriteMovie)
    }


}