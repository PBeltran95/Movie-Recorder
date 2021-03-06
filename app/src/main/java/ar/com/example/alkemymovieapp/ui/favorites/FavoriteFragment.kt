package ar.com.example.alkemymovieapp.ui.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.MovieEntity
import ar.com.example.alkemymovieapp.databinding.FragmentFavoriteBinding
import ar.com.example.alkemymovieapp.presentation.LocalViewModel
import ar.com.example.alkemymovieapp.ui.adapters.MovieAdapter
import ar.com.example.alkemymovieapp.ui.utils.VisualUtils
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
        setupRecyclerView()
        setupObservers()
        showError()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.adapter = adapter
        setupSizes()
    }

    private fun showError() {
        viewModel.isEmpty.observe(viewLifecycleOwner){
            if (it){
                binding.progressBar.isVisible = false
            }
            binding.favoritesEmpty.isVisible = it
        }
    }

    private fun setupObservers() {
        viewModel.fetchFavoriteMovies().observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{
                    setupProgressBar()
                }
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
                    viewModel.verifyList(movieList)
                    manageAnimState(movieList)
                    modifyData(movieList.toMutableList())
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }
            }
        }

        viewModel.listToFilter.observe(viewLifecycleOwner) { filteredList ->
            modifyData(filteredList.toMutableList())
        }
        viewModel.noMatchesForQuery.observe(viewLifecycleOwner) {
            binding.errorMessageAnim.isVisible = it
        }
    }

    private fun setupProgressBar() {
        with(binding){
            progressBar.isVisible = true
            rvFavorites.isVisible = false
        }
    }

    private fun manageAnimState(data: List<MovieEntity>) {
        with(binding){
            if (data.isNotEmpty()){
                favoritesEmpty.isVisible = false
                progressBar.isVisible = false
                rvFavorites.isVisible = true
            }
        }
    }

    private fun setupSizes() {
        binding.rvFavorites.layoutManager = VisualUtils.setupSizes(resources, requireContext(), 6,3,2)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.apply {
            queryHint = context.getString(R.string.favorite_query_hint)
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@FavoriteFragment)
        }
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(movieEntity.id)
        findNavController().navigate(action)
    }

    override fun onLongClick(movieEntity: MovieEntity) {
        VisualUtils.makeAlert(requireContext(), { viewModel.deleteFromFavorites(movieEntity) }, {setupObservers()})
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()) {
            binding.favoritesEmpty.isVisible = false
            filterList(query)
        } else {
            binding.errorMessageAnim.isVisible = false
            showError()
            modifyData(commonListOfMovies)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isNotEmpty()) {
            binding.favoritesEmpty.isVisible = false
            filterList(newText)
        } else {
            binding.errorMessageAnim.isVisible = false
            showError()
            modifyData(commonListOfMovies)
        }
        return true
    }

    private fun filterList(query: String?) {
        viewModel.searchByQuery(myListToFilter, query)
    }

    private fun modifyData(data: MutableList<MovieEntity>) {
        adapter.setMovieData(data)
    }
}