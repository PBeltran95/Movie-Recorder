package ar.com.example.alkemymovieapp.ui.watched

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentWatchedBinding
import ar.com.example.alkemymovieapp.presentation.LocalViewModel
import ar.com.example.alkemymovieapp.ui.adapters.MovieAdapter
import ar.com.example.alkemymovieapp.ui.utils.VisualUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchedFragment : Fragment(R.layout.fragment_watched), MovieAdapter.OnClick {

    private lateinit var binding: FragmentWatchedBinding
    private val viewModel by viewModels<LocalViewModel>()
    private val movieAdapter by lazy { MovieAdapter(this) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWatchedBinding.bind(view)
        setupRecyclerView()
        setupObservers()
        showError()
    }
    private fun showError() {
        viewModel.isEmpty.observe(viewLifecycleOwner){
            binding.watchedEmpty.isVisible = it
        }
    }

    private fun setupObservers() {
        viewModel.fetchViewedMovies().observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{}
                is Resource.Success ->{
                    val movieList = it.data
                    viewModel.verifyList(movieList)
                    sendDataToAdapter(movieList)
                }
                is Resource.Failure ->{}
            }
        }
    }

    private fun sendDataToAdapter(movieList: List<MovieEntity>) {
        movieAdapter.setMovieData(movieList)
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.adapter = movieAdapter
        setupSizes()
    }

    private fun setupSizes() {
        binding.rvFavorites.layoutManager = VisualUtils.setupSizes(resources, requireContext(), 6,3,2)
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = WatchedFragmentDirections.actionWatchedFragmentToDetailFragment(movieEntity.id)
        findNavController().navigate(action)
    }

    override fun onLongClick(movieEntity: MovieEntity) {
        VisualUtils.makeAlert(requireContext(), { viewModel.deleteFromWatched(movieEntity) }, {setupObservers()})
    }
}