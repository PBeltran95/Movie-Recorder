package ar.com.example.alkemymovieapp.ui.bookmark

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentToWatchBinding
import ar.com.example.alkemymovieapp.presentation.LocalViewModel
import ar.com.example.alkemymovieapp.ui.adapters.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToWatchFragment : Fragment(R.layout.fragment_to_watch), MovieAdapter.OnClick {

    private lateinit var binding: FragmentToWatchBinding
    private val viewModel by viewModels<LocalViewModel>()
    private val movieAdapter by lazy { MovieAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentToWatchBinding.bind(view)
        setupObservers()
        showError()
    }


    private fun showError() {
        viewModel.isEmpty.observe(viewLifecycleOwner){
            binding.toWatchEmpty.isVisible = it
        }
    }

    private fun setupObservers() {
        viewModel.fetchToView().observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{}
                is Resource.Success ->{
                    val movieList = it.data
                    viewModel.verifyList(movieList)
                    setupRecyclerView(movieList)
                }
                is Resource.Failure ->{}
            }
        }
    }

    private fun setupRecyclerView(movieList: List<MovieEntity>) {
        binding.rvToWatch.adapter = movieAdapter
        movieAdapter.setMovieData(movieList)
        setupSizes()
    }

    private fun setupSizes() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvToWatch.layoutManager = object : GridLayoutManager(requireContext(), 6) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = ((height / 3) * 2)
                    lp.width = width / 6

                    return true
                }
            }
        } else {
            binding.rvToWatch.layoutManager = object : GridLayoutManager(requireContext(), 4) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.width = width / 4
                    lp.height = height / 3
                    return true
                }
            }
        }
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = ToWatchFragmentDirections.actionToWatchFragmentToDetailFragment(movieEntity.id)
        findNavController().navigate(action)
    }
}