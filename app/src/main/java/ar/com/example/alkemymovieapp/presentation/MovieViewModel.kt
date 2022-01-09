package ar.com.example.alkemymovieapp.presentation

import android.net.Uri
import androidx.lifecycle.*
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.managers.MakeUriManager
import ar.com.example.alkemymovieapp.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repo: MovieRepositoryImpl, private val uriManager: MakeUriManager) : ViewModel() {

    fun fetchMovies(page:Int, movieFilter: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getPopularMovies(page, movieFilter)))
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

    private val _noMatchesForQuery = MutableLiveData<Boolean>()
    val noMatchesForQuery: LiveData<Boolean>
        get() = _noMatchesForQuery

    fun evaluateListOfResults(empty: Boolean) {
        _noMatchesForQuery.value = empty
    }

    fun updateMovie(favoriteMovie: MovieEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.updateMovie(favoriteMovie)
        }
    }

    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri>
        get() = _uri


    fun makeUri(title:String) {
        _uri.value = uriManager.makeUri(title)
    }

    private val _pageNumber = MutableLiveData(1)
    val pageNumber: LiveData<Int>
        get() = _pageNumber

    fun incrementPage() {
        _pageNumber.value = _pageNumber.value?.plus(1)
    }
    fun decrementPage() {
        if (_pageNumber.value == 0){
            _pageNumber.value = 1
        }else {
            _pageNumber.value = _pageNumber.value?.minus(1)
        }
    }

    fun resetPageValue() {
        _pageNumber.value = 1
    }

}