package ar.com.example.alkemymovieapp.managers

import ar.com.example.alkemymovieapp.data.models.MovieEntity

class SearchMangerImpl: SearchManager {

    override fun searchByQuery(listToFilter: MutableList<MovieEntity>, query: String?): List<MovieEntity> {

        return  listToFilter.filter { it.title.lowercase().contains(query!!.lowercase().trim()) }.sortedWith(
            compareBy(String.CASE_INSENSITIVE_ORDER, {it.title})
        )
    }
}