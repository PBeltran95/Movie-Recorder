package ar.com.example.alkemymovieapp.core

import retrofit2.Response

sealed class Resource<out T>{

    class Loading<out T>: Resource<T>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Failure<out T>(val throwable: Throwable): Resource<T>()



}
