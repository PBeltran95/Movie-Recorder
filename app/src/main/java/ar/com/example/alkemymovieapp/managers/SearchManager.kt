package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity

interface SearchManager {

    fun searchByQuery(listToFilter: MutableList<MovieEntity>, query: String?): List<MovieEntity>


}