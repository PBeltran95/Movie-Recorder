package ar.com.example.alkemymovieapp.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.setGlide
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.MovieItemBinding

class FavoriteAdapter(private val movieClick: OnClick): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    interface OnClick{
        fun onFavoriteClick(movieEntity:MovieEntity)
    }

    private lateinit var context: Context
    private var favoriteList: List<MovieEntity> = listOf()

    fun setFavoriteData(newList: List<MovieEntity>){
        this.favoriteList = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return FavoriteViewHolder(layoutInflater.inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favoriteList[position]
        with(holder){
            setGlide(context, item.poster_path, binding.imgMovie)
            binding.root.setOnClickListener { movieClick.onFavoriteClick(item) }
        }
    }

    override fun getItemCount(): Int = favoriteList.size

    inner class FavoriteViewHolder(view:View): RecyclerView.ViewHolder(view){
        val binding = MovieItemBinding.bind(view)
    }
}