package ar.com.example.alkemymovieapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.repository.MovieTrailerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieTrailerViewModel @Inject constructor(private val repository: MovieTrailerRepository): ViewModel() {

    fun getMovieTrailer(movieId: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getMovieTrailer(movieId)))
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


}