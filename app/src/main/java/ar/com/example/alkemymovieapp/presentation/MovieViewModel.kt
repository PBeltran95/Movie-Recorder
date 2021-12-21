package ar.com.example.alkemymovieapp.presentation

import android.util.Log
import androidx.lifecycle.*
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
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

    fun searchMovieByTitle(movieTitle: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.searchMovieByTitle(movieTitle)))
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

    private var _noMatchesForQuery = MutableLiveData<Boolean>()
    val noMatchesForQuery: LiveData<Boolean>
        get() = _noMatchesForQuery

    fun evaluateListOfResults(empty: Boolean) {
        _noMatchesForQuery.value = empty
    }

    fun saveFavoriteMovie(favoriteMovie: MovieEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.saveFavoriteMovie(favoriteMovie)
        }
    }

}