package ar.com.example.alkemymovieapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.databinding.FragmentHomeBinding
import ar.com.example.alkemymovieapp.presentation.remote.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView


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
                    setupRecyclerView(movieList)
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })
    }

    private fun setupRecyclerView(movieList: List<Movie>) {
        binding.rvHome.adapter = HomeAdapter(movieList, this@HomeFragment)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            binding.rvHome.layoutManager = object : GridLayoutManager(requireContext(),2) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.height = height * 2
                    lp.width = width / 2
                    return true
                }
            }
        } else {
            // portrait
            binding.rvHome.layoutManager = object : GridLayoutManager(requireContext(),3) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    // landscape
                    lp.height = height / 3
                    return true
                }
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id)
        findNavController().navigate(action)
    }

}