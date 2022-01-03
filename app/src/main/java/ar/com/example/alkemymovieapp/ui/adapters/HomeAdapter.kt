package ar.com.example.alkemymovieapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.setGlide
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.databinding.MovieItemBinding
import ar.com.example.alkemymovieapp.ui.utils.DiffUtils

class HomeAdapter(
    private val itemClickListener: OnMovieClickListener
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var movieList = mutableListOf<Movie>()
    private lateinit var context: Context


    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

    fun setData(newList: MutableList<Movie>){
        val diffUtil = DiffUtils(movieList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.movieList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        return HomeViewHolder(layoutInflater.inflate(R.layout.movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val movie = movieList[position]
        with(holder) {
            binding.root.setOnClickListener {
                itemClickListener.onMovieClick(movie)
            }
            setGlide(context,"https://image.tmdb.org/t/p/w500/${movie.poster_path}", binding.imgMovie )
            binding.tvVotes.text = movie.vote_average.toString()
            binding.tvMovieTitle.text = movie.title
        }
    }

    override fun getItemCount(): Int = movieList.size

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: MovieItemBinding = MovieItemBinding.bind(view)
    }
}