package ar.com.example.alkemymovieapp.ui.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object VisualUtils {

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

     fun setupSizes(resources: Resources, context: Context, spanCount: Int = 5, d:Int = 3, t:Int = 4 ): RecyclerView.LayoutManager{
         val layoutManager: RecyclerView.LayoutManager?
         if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = object : GridLayoutManager(context, spanCount) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = ((height / d) * t)
                    lp.width = width / spanCount

                    return true
                }
            }
            return layoutManager
        } else {
            layoutManager = object : GridLayoutManager(context, 3) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.height = ((height / 3) * 3) / 2
                    return true
                }
            }
            return layoutManager
        }
    }
}