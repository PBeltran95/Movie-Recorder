package ar.com.example.alkemymovieapp.presentation.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.repository.remote.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor (private val repo: MovieRepositoryImpl):ViewModel() {

    fun fetchMovies() = liveData(viewModelScope.coroutineContext + Dispatchers.IO){
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getPopularMovies()))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    fun fetchMovieDetails(movieId:String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO){
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getMovieDetails(movieId)))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
}