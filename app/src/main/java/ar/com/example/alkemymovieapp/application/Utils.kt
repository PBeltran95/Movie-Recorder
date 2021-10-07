package ar.com.example.alkemymovieapp.application

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.com.example.alkemymovieapp.core.Resource
import com.bumptech.glide.Glide

fun Any.setGlide(context: Context, data: String, imgView: ImageView, centerCrop : Boolean = false) {
    if (centerCrop){
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500/${data}")
            .centerCrop()
            .into(imgView)
    }else{
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500/${data}")
            .into(imgView)
    }
}


fun Fragment.toast(context: Context, text: String) {

    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun Fragment.handleApiError(failure: Resource.Failure){
    when {
        failure.isNetworkError -> toast(requireContext(), "Check your internet connection")
        failure.errorCode == 401 -> { toast(requireContext(), "Client failed to authenticate with the server") }
        failure.errorCode == 404 -> { toast(requireContext(), "The resource you requested could not be found.") }
        else -> toast(requireContext(), "Error: ${failure.errorBody}")
    }
}