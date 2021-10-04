package ar.com.example.alkemymovieapp.application

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun Any.setGlide(context: Context, data: String, imgView: ImageView) {
    Glide.with(context)
        .load("https://image.tmdb.org/t/p/w500/${data}")
        .centerCrop()
        .into(imgView)
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Fragment.toast(context: Context, text: String) {

    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}