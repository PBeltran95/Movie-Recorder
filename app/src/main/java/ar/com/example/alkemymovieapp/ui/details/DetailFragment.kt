package ar.com.example.alkemymovieapp.ui.details

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.application.setGlide
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentDetailBinding
import ar.com.example.alkemymovieapp.presentation.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var favoriteMovie: MovieEntity
    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteMovie = MovieEntity()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        fetchMovieDetails()
        setButtons()
    }

    private fun setButtons() {
        with(binding) {
            cvFavorite.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isChecked) {
                    saveOrDeleteFavorite(true)
                } else saveOrDeleteFavorite(false)
            }
            cvViewed.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isChecked){
                    saveOrDeleteViewed(true)
                }else saveOrDeleteViewed(false)
            }
            cvBookmark.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isChecked){
                    saveOrDeleteToWatch(true)
                }else saveOrDeleteToWatch(false)
            }
        }
    }

    private fun saveOrDeleteToWatch(toWatch: Boolean) {
        viewModel.updateMovie(favoriteMovie.also {
            it.watchLater = toWatch
        })
    }

    private fun saveOrDeleteViewed(viewed: Boolean) {
        viewModel.updateMovie(favoriteMovie.also {
            it.viewed = viewed
        })
    }

    private fun saveOrDeleteFavorite(isFavorite: Boolean) {
        viewModel.updateMovie(favoriteMovie.also {
            it.isFavorite = isFavorite
            it.viewed = true
        })
    }

    private fun fetchMovieDetails() {
        val movieId = args.movieId.toString()
        viewModel.fetchMovieDetails(movieId).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    favoriteMovie = it.data
                    drawDetails(it.data)
                }
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    handleApiError(it)
                }
            }
        }
    }

    private fun drawDetails(data: MovieEntity) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.verticalGuideline1.setGuidelinePercent(0.35F)
            binding.horizontalGuideline3.setGuidelinePercent(0.68F)
            setGlide(
                requireContext(),
                "https://image.tmdb.org/t/p/w500/${data.backdrop_path}",
                binding.imgBackground
            )
        }

        setGlide(
            requireContext(),
            "https://image.tmdb.org/t/p/w500/${data.backdrop_path}",
            binding.imgBackground,
            true
        )
        setGlide(
            requireContext(),
            "https://image.tmdb.org/t/p/w500/${data.poster_path}",
            binding.imgMovie
        )

        with(binding) {
            tvGenre.text = data.genres.removeSuffix("]").removePrefix("[")
            tvTitle.text = data.title
            tvCalendar.text = getString(R.string.release_date, data.release_date)
            tvLanguage.text = getString(R.string.movie_language, data.original_language)
            tvReviews.text =
                getString(R.string.reviews, data.vote_average.toString(), data.vote_count)
            tvPopularity.text = getString(R.string.movie_popularity, data.popularity.toString())
            tvDescriptionDetails.text = data.overview
            cvFavorite.isChecked = data.isFavorite
            cvViewed.isChecked = data.viewed
            cvBookmark.isChecked = data.watchLater

            btnTrailer.setOnClickListener {
                setTrailerIntent(data.title)
            }
            btnShare.setOnClickListener {
                shareData(data.title)
            }
        }

    }

    private fun shareData(title: String) {
        val sendApp:Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, formatTitle(title).toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendApp, null)
        startActivity(shareIntent)
    }

    private fun setTrailerIntent(title: String) {
        val trailerIntent = Intent(Intent.ACTION_VIEW, formatTitle(title))
        startActivity(trailerIntent)
    }

    private fun formatTitle(title: String): Uri {
        var url: Uri = Uri.EMPTY
        viewModel.makeUri(title)
        viewModel.uri.observe(viewLifecycleOwner){
            url = it
        }
        return url
    }

}