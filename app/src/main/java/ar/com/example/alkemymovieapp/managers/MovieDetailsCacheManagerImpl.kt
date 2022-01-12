package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.data.models.toMovieEntity
import ar.com.example.alkemymovieapp.repository.RemoteMovieRepository
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepository
import javax.inject.Inject

class MovieDetailsCacheManagerImpl @Inject constructor (private val remoteDataSourceRemote : RemoteMovieRepository,
                                                        private val localRepository: LocalMovieRepository
): MovieDetailsCacheManager  {

    override suspend fun getMovieDetails(movieId:String): MovieEntity {

        return if (localRepository.elementExists(movieId)){
            localRepository.getMovieDetails(movieId)
        }else{
            remoteDataSourceRemote.getMovieDetails(movieId).also { movie ->
                localRepository.saveMovie(movie.toMovieEntity())
            }
            localRepository.getMovieDetails(movieId)
        }
    }
}