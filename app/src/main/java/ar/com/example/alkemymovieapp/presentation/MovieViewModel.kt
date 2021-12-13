package ar.com.example.alkemymovieapp.presentation

import androidx.lifecycle.*
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repo: MovieRepositoryImpl) : ViewModel() {

    fun fetchMovies(page:Int) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getPopularMovies(page)))
        } catch (e: Throwable) {
            when (e) {
                is HttpException -> {
                    emit(Resource.Failure(false, e.code(), e.response()?.errorBody()))
                }
                else -> {
                    emit(Resource.Failure(true, null, null))
                }
            }
        }
    }

    fun fetchMovieDetails(movieId: String) =
        liveData<Resource<MovieEntity>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(repo.getMovieDetails(movieId)))
            } catch (e: Throwable) {
                when (e) {
                    is HttpException -> {
                        emit(Resource.Failure(false, e.code(), e.response()?.errorBody()))
                    }
                    else -> {
                        emit(Resource.Failure(true, null, null))
                    }
                }
            }
        }

    private var _listSaved = MutableLiveData<List<Movie>>()
    val listTofilter: LiveData<List<Movie>>
        get() = _listSaved
    private var _noMatchesForQuery = MutableLiveData<Boolean>()
    val noMatchesForQuery: LiveData<Boolean>
        get() = _noMatchesForQuery


    fun searchByQuery(listToFilter: MutableList<Movie>, query: String?) {

        val data = listToFilter.filter {

            it.title.lowercase().contains(query!!.lowercase().trim())
        }

        _noMatchesForQuery.value = data.isEmpty()
        _listSaved.value = data
    }

    fun saveFavoriteMovie(favoriteMovie: MovieEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.saveFavoriteMovie(favoriteMovie)
        }
    }

}