package ar.com.example.alkemymovieapp.ui.utils

import android.content.Context
import ar.com.example.alkemymovieapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertUtil {

    fun makeAlert(context: Context, action: () -> Unit, action2: () -> Unit){
        MaterialAlertDialogBuilder(context, R.style.AppCompatAlertDialogStyle)
            .setTitle("Do you want to delete this movie?")
            .setNeutralButton("No") { dialog, witch -> }
            .setPositiveButton("Delete") {dialog, witch ->
                action()
                action2()
            }
            .show()
    }

}