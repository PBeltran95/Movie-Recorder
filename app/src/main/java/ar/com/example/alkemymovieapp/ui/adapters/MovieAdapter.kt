package ar.com.example.alkemymovieapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.setGlide
import ar.com.example.alkemymovieapp.application.toListOfMovie
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.MovieItemBinding
import ar.com.example.alkemymovieapp.ui.utils.DiffUtils

class MovieAdapter(private val movieClick: OnClick): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    interface OnClick{
        fun onMovieClick(movieEntity:MovieEntity)
    }

    private lateinit var context: Context
    private var movieList: List<MovieEntity> = listOf()

    fun setMovieData(newList: List<MovieEntity>){
        val diffUtils = DiffUtils(movieList.toListOfMovie(), newList.toListOfMovie())
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        this.movieList = newList
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return MovieViewHolder(layoutInflater.inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movieList[position]
        with(holder){
            setGlide(context, item.poster_path, binding.imgMovie)
            binding.root.setOnClickListener { movieClick.onMovieClick(item) }
            binding.tvMovieTitle.text = item.title
        }
    }

    override fun getItemCount(): Int = movieList.size

    inner class MovieViewHolder(view:View): RecyclerView.ViewHolder(view){
        val binding = MovieItemBinding.bind(view)
    }
}