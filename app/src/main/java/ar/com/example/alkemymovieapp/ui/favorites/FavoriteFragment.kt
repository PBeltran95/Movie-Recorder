package ar.com.example.alkemymovieapp.ui.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentFavoriteBinding
import ar.com.example.alkemymovieapp.presentation.LocalViewModel
import ar.com.example.alkemymovieapp.ui.adapters.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite), MovieAdapter.OnClick, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentFavoriteBinding
    private val adapter by lazy { MovieAdapter(this) }
    private val viewModel by viewModels<LocalViewModel>()
    private var commonListOfMovies: MutableList<MovieEntity> = mutableListOf()
    private var myListToFilter: MutableList<MovieEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
                    val movieList = it.data
                    commonListOfMovies.apply {
                        clear()
                        addAll(movieList)
                    }
                    myListToFilter.apply {
                        clear()
                        addAll(movieList)
                    }
                    setupRecyclerView(it.data)
                }
                is Resource.Failure ->{}
            }
        }

        viewModel.listToFilter.observe(viewLifecycleOwner) { filteredList ->
            modifyData(filteredList.toMutableList())
        }
        viewModel.noMatchesForQuery.observe(viewLifecycleOwner) {
            //binding.errorMessageAnim.isVisible = it
        }
    }

    private fun setupRecyclerView(data: List<MovieEntity>) {
        binding.rvFavorites.adapter = adapter
        adapter.setMovieData(data)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@FavoriteFragment)
        }
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(movieEntity.id)
        findNavController().navigate(action)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()) {
            drawFiltrated(query)
        } else {
            //binding.errorMessageAnim.isVisible = false
            modifyData(commonListOfMovies)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isNotEmpty()) {
            drawFiltrated(newText)
        } else {
            //binding.errorMessageAnim.isVisible = false
            modifyData(commonListOfMovies)
        }
        return true
    }

    private fun drawFiltrated(query: String?) {
        viewModel.searchByQuery(myListToFilter, query)
    }

    private fun modifyData(data: MutableList<MovieEntity>) {
        adapter.setMovieData(data)
    }
}