package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepository
import javax.inject.Inject

class UpdateManagerImpl @Inject constructor(private val repo: LocalMovieRepository): UpdateManager {
    override suspend fun deleteFromFavorites(movieEntity: MovieEntity) {
        repo.updateMovie(movieEntity.also { it.isFavorite = false })
    }

    override suspend fun deleteFromWatched(movieEntity: MovieEntity) {
        repo.updateMovie(movieEntity.also { it.viewed = false })
    }

    override suspend fun deleteFromToWatch(movieEntity: MovieEntity) {
        repo.updateMovie(movieEntity.also { it.watchLater = false })
    }
}