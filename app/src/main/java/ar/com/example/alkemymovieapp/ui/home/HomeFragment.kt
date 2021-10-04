package ar.com.example.alkemymovieapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.toast
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.databinding.FragmentHomeBinding
import ar.com.example.alkemymovieapp.presentation.remote.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MovieViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModel.fetchMovies().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val movieList = it.data.results
                    initRecyclerView(movieList)
                }
                is Resource.Failure -> {
                    toast(requireContext(), "Error: ${it.throwable.message}")
                }
            }
        })
    }

    private fun initRecyclerView(movieList: List<Movie>) {
        binding.rvHome.adapter = HomeAdapter(movieList, this@HomeFragment)
    }

    override fun onMovieClick(movie: Movie) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id)
        findNavController().navigate(action)
    }

}