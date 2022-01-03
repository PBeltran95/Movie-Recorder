package ar.com.example.alkemymovieapp.ui.trailer

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.databinding.FragmentTrailerBinding
import ar.com.example.alkemymovieapp.presentation.MovieTrailerViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailerFragment : Fragment(R.layout.fragment_trailer){

    private lateinit var binding: FragmentTrailerBinding
    private val viewModel by viewModels<MovieTrailerViewModel>()
    private val args by navArgs<TrailerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrailerBinding.bind(view)
        fetchMovieTrailer()
    }

    private fun fetchMovieTrailer() {
        viewModel.getMovieTrailer(args.movieId.toString()).observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    setupProgressBar()
                }
                is Resource.Success -> {
                    setupVideo(it.data.results[0].key)
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        }
    }

    private fun setupProgressBar() {
        with(binding){
            progressBar.isVisible = true
        }
    }

    private fun setupVideo(videoId: String) {
        binding.progressBar.isVisible = false
        viewLifecycleOwner.lifecycle.addObserver(binding.videoView)
        binding.videoView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
        binding.videoView.enterFullScreen()
    }
}
