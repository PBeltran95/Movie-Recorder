package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepository
import javax.inject.Inject

class RegisterManagerImpl @Inject constructor(private val repo: LocalMovieRepository) :
    RegisterManager {

    override suspend fun saveOrDeleteToWatch(movieEntity: MovieEntity, booleanValue: Boolean) {
        repo.updateMovie(movieEntity.also {
            it.watchLater = booleanValue
        })
    }

    override suspend fun saveOrDeleteFavorite(movieEntity: MovieEntity, booleanValue: Boolean) {
        repo.updateMovie(movieEntity.also {
            it.isFavorite = booleanValue
            it.viewed = true
        })
    }

    override suspend fun saveOrDeleteViewed(movieEntity: MovieEntity, booleanValue: Boolean) {
        repo.updateMovie(movieEntity.also {
            it.viewed = booleanValue
        })
    }
}
