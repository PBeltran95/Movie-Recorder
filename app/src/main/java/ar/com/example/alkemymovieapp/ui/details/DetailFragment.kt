package ar.com.example.alkemymovieapp.ui.details

import android.accounts.NetworkErrorException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.hide
import ar.com.example.alkemymovieapp.application.setGlide
import ar.com.example.alkemymovieapp.application.show
import ar.com.example.alkemymovieapp.application.toast
import ar.com.example.alkemymovieapp.core.*
import ar.com.example.alkemymovieapp.data.models.MovieDetail
import ar.com.example.alkemymovieapp.databinding.FragmentDetailBinding
import ar.com.example.alkemymovieapp.presentation.remote.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel by viewModels<MovieViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        fetchMovieDetails()
    }

    private fun fetchMovieDetails() {
        val movieId = args.movieId.toString()
        viewModel.fetchMovieDetails(movieId).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    drawDetails(it.data)
                }
                is Resource.Failure -> {
                    toast(requireContext(), "Error: ${it.throwable.message}")
                }
            }
        })
    }

    private fun drawDetails(data: MovieDetail) {
        setGlide(requireContext(), "https://image.tmdb.org/t/p/w500/${data.backdrop_path}", binding.imgBackground)
        setGlide(requireContext(), "https://image.tmdb.org/t/p/w500/${data.poster_path}", binding.imgMovie)
        val genresToText = mutableListOf<String>()
        data.genres.forEach { genresToText.add(it.name) }
        with(binding) {
            tvGenre.text = genresToText.toString().removeSuffix("]").removePrefix("[")
            tvTitle.text = data.title
            tvCalendar.text = "Release date:${data.release_date}"
            tvLanguage.text = "Lang: ${data.original_language}"
            tvDescriptionDetails.text = data.overview
            tvReviews.text = "Score: ${data.vote_average} (${data.vote_count} reviews)"
            tvPopularity.text = "Views: ${data.popularity}"
        }
    }
}