package ar.com.example.alkemymovieapp.ui.utils

import androidx.recyclerview.widget.DiffUtil
import ar.com.example.alkemymovieapp.data.models.Movie

class DiffUtils(
    private val oldList: MutableList<Movie>,
    private val newList: MutableList<Movie>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id &&
                oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].overview == newList[newItemPosition].overview
    }
}