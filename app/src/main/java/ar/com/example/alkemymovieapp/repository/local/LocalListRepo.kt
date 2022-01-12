package ar.com.example.alkemymovieapp.repository.local

import ar.com.example.alkemymovieapp.data.models.MovieEntity

interface LocalListRepo {

    suspend fun getFavoriteMovies():List<MovieEntity>

    suspend fun getViewedMovies(): List<MovieEntity>

    suspend fun getAllToWatch(): List<MovieEntity>
}