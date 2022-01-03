package ar.com.example.alkemymovieapp.data.models

data class MovieTrailerResponse(
    val id: Int = 0,
    val results: List<MovieTrailerData>
)

data class MovieTrailerData(
    val name: String = "",
    val key: String = "",
    val site: String = "",
    val official: Boolean = false
)