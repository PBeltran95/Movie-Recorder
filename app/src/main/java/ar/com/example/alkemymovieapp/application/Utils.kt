package ar.com.example.alkemymovieapp.application

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.data.models.toMovie
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun Any.setGlide(context: Context, data: String, imgView: ImageView, centerCrop : Boolean = false) {

    val circleProgressBar =  CircularProgressDrawable(context).apply {
        setColorSchemeColors(R.color.teal_200)
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
    if (centerCrop){
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500/${data}")
            .centerCrop()
            .placeholder(circleProgressBar)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imgView)
    }else{
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500/${data}")
            .placeholder(circleProgressBar)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imgView)
    }
}


fun Fragment.toast(context: Context, text: String, longMessage: Boolean = true) {
    if (longMessage){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }else{ Toast.makeText(context, text, Toast.LENGTH_SHORT).show()}
}

fun Fragment.handleApiError(failure: Resource.Failure){
    when {
        failure.isNetworkError -> toast(requireContext(), "Check your internet connection")
        failure.errorCode == 401 -> { toast(requireContext(), "Client failed to authenticate with the server") }
        failure.errorCode == 404 -> { toast(requireContext(), "The resource you requested could not be found.") }
        else -> toast(requireContext(), "Error: ${failure.errorBody}")
    }
}

fun List<MovieEntity>.toListOfMovie(): MutableList<Movie>{
    return this.map { it.toMovie() }.toMutableList()
}