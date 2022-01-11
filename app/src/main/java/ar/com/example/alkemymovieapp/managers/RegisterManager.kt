package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity

interface RegisterManager {

    suspend fun saveOrDeleteToWatch(movieEntity: MovieEntity, booleanValue: Boolean)

    suspend fun saveOrDeleteFavorite(movieEntity: MovieEntity, booleanValue: Boolean)

    suspend fun saveOrDeleteViewed(movieEntity: MovieEntity, booleanValue: Boolean)

}