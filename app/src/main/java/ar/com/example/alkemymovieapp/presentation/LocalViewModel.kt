package ar.com.example.alkemymovieapp.presentation

import androidx.lifecycle.*
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.managers.SearchManager
import ar.com.example.alkemymovieapp.managers.UpdateManager
import ar.com.example.alkemymovieapp.repository.local.LocalListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor (private val repo: LocalListRepo, private val searchManager: SearchManager, private val updateManager: UpdateManager): ViewModel() {

    fun fetchFavoriteMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getFavoriteMovies()))
        }catch (e:Throwable){
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

    fun fetchViewedMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getViewedMovies()))
        }catch (e:Throwable){
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

    fun fetchToView() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getAllToWatch()))
        }catch (e:Throwable){
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

    fun deleteFromFavorites(movieEntity: MovieEntity){
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            updateManager.deleteFromFavorites(movieEntity)
        }
    }

    fun deleteFromWatched(movieEntity: MovieEntity){
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            updateManager.deleteFromWatched(movieEntity)
        }
    }

    fun deleteFromToWatch(movieEntity: MovieEntity){
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            updateManager.deleteFromToWatch(movieEntity)
        }
    }

    private var _listSaved = MutableLiveData<List<MovieEntity>>()
    val listToFilter: LiveData<List<MovieEntity>>
        get() = _listSaved
    private var _noMatchesForQuery = MutableLiveData<Boolean>()
    val noMatchesForQuery: LiveData<Boolean>
        get() = _noMatchesForQuery


    fun searchByQuery(listToFilter: MutableList<MovieEntity>, query: String?) {

        val data = searchManager.searchByQuery(listToFilter, query)

        _noMatchesForQuery.value = data.isEmpty()
        _listSaved.value = data
    }


    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    fun verifyList(movieList: List<MovieEntity>) {
        _isEmpty.value = movieList.isEmpty()
    }
}