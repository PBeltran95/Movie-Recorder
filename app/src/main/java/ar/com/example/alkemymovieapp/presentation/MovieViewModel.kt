package ar.com.example.alkemymovieapp.presentation

import android.net.Uri
import androidx.lifecycle.*
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.managers.MakeUriManager
import ar.com.example.alkemymovieapp.managers.MovieDetailsCacheManager
import ar.com.example.alkemymovieapp.managers.RegisterManager
import ar.com.example.alkemymovieapp.repository.RemoteMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repo: RemoteMovieRepository, private val uriManager: MakeUriManager,
                                         private val registerManager: RegisterManager, private val movieDetailsManager: MovieDetailsCacheManager )
    : ViewModel() {

    fun fetchMovies(movieFilter: String, page:Int) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getMovies(movieFilter, page)))
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
                emit(Resource.Success(movieDetailsManager.getMovieDetails(movieId)))
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

    fun saveOrDeleteToWatch(toWatch: Boolean, favoriteMovie: MovieEntity) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            registerManager.saveOrDeleteToWatch(favoriteMovie, toWatch)
        }
    }

    fun saveOrDeleteViewed(viewed: Boolean, favoriteMovie: MovieEntity) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            registerManager.saveOrDeleteViewed(favoriteMovie, viewed)
        }
    }

    fun saveOrDeleteFavorite(isFavorite: Boolean, favoriteMovie: MovieEntity) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            registerManager.saveOrDeleteFavorite(favoriteMovie, isFavorite)
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