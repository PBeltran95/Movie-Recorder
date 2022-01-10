package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity

interface UpdateManager {

    suspend fun deleteFromFavorites(movieEntity: MovieEntity)

    suspend fun deleteFromWatched(movieEntity: MovieEntity)

    suspend fun deleteFromToWatch(movieEntity: MovieEntity)

}
