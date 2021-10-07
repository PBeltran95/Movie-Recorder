package ar.com.example.alkemymovieapp.data.models

import java.lang.Exception
import okhttp3.ResponseBody
import java.io.IOException


data class Movie(
    val id: Int = -1,
    val adult:Boolean = false,
    val backdrop_path: String = "",
    val original_title: String = "",
    val original_language: String = "",
    val overview: String = "",
    val popularity: Double = -1.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = -1.0,
    val vote_count: Int = -1
)


data class MovieList(
    val results: List<Movie> = listOf()
)

data class MovieDetail(
    val id: Int = -1,
    val adult:Boolean = false,
    val backdrop_path: String = "",
    val genres : List<Genres> = listOf(),
    val original_title: String = "",
    val original_language: String = "",
    val overview: String = "",
    val popularity: Double = -1.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = -1.0,
    val vote_count: Int = -1
)
data class Genres(
    val id:Int = -1,
    val name:String = ""
)
