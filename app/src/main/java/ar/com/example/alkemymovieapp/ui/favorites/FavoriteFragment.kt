package ar.com.example.alkemymovieapp.ui.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentFavoriteBinding
import ar.com.example.alkemymovieapp.presentation.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding
    private val adapter by lazy { FavoriteAdapter() }
    private val viewModel by viewModels<LocalViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.fetchFavoriteMovies().observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{}
                is Resource.Success ->{
                    setupRecyclerView(it.data)
                }
                is Resource.Failure ->{}
            }
        }
    }

    private fun setupRecyclerView(data: List<MovieEntity>) {
        binding.rvFavorites.adapter = adapter
        adapter.setFavoriteData(data)
        setupSizes()
    }

    private fun setupSizes() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            binding.rvFavorites.layoutManager = object : GridLayoutManager(requireContext(), 6) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = ((height / 3) * 2)
                    lp.width = width / 6

                    return true
                }
            }
        } else {
            // portrait
            binding.rvFavorites.layoutManager = object : GridLayoutManager(requireContext(), 3) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = height / 3
                    return true
                }
            }
        }
    }
}