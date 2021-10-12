package ar.com.example.alkemymovieapp.ui.details

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.*
import ar.com.example.alkemymovieapp.core.*
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentDetailBinding
import ar.com.example.alkemymovieapp.presentation.MovieViewModel
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
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    drawDetails(it.data)
                }
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    handleApiError(it)
                }
            }
        })
    }

    private fun drawDetails(data: MovieEntity) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.verticalGuideline1.setGuidelinePercent(0.35F)
            binding.horizontalGuideline3.setGuidelinePercent(0.68F)
            setGlide(requireContext(), "https://image.tmdb.org/t/p/w500/${data.backdrop_path}", binding.imgBackground)
        }

        setGlide(requireContext(), "https://image.tmdb.org/t/p/w500/${data.backdrop_path}", binding.imgBackground,true)
        setGlide(requireContext(), "https://image.tmdb.org/t/p/w500/${data.poster_path}", binding.imgMovie)

        with(binding) {
            tvGenre.text = data.genres.removeSuffix("]").removePrefix("[")
            tvTitle.text = data.title
            tvCalendar.text = getString(R.string.release_date, data.release_date)
            tvLanguage.text = getString(R.string.movie_language, data.original_language)
            tvReviews.text = getString(R.string.reviews, data.vote_average.toString(), data.vote_count)
            tvPopularity.text = getString(R.string.movie_popularity, data.popularity.toString())
            tvDescriptionDetails.text = data.overview
        }
    }
}