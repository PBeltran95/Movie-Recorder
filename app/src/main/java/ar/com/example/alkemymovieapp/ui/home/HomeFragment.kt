package ar.com.example.alkemymovieapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.databinding.FragmentHomeBinding
import ar.com.example.alkemymovieapp.presentation.remote.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.isVisible
import ar.com.example.alkemymovieapp.R


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnMovieClickListener, SearchView.OnQueryTextListener{

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var myAdapter: HomeAdapter
    private var commonListOfMovies: MutableList<Movie> = mutableListOf()
    private var myListToFilter: MutableList<Movie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModel.fetchMovies().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val movieList = it.data.results
                    commonListOfMovies.apply {
                        clear()
                        addAll(movieList)
                    }
                    myListToFilter.apply {
                        clear()
                        addAll(movieList)
                    }
                    setupRecyclerView(commonListOfMovies)
                }
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    handleApiError(it)
                }
            }
        })
    }

    private fun setupRecyclerView(movieList: MutableList<Movie>) {

        myAdapter = HomeAdapter(movieList, this@HomeFragment)
        binding.rvHome.adapter = myAdapter

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu,  menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@HomeFragment)
        }
    }

    private fun modifyData(data: MutableList<Movie>){
        myAdapter.setData(data)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()){
            drawFiltrated(query)
        }else{
            binding.errorMessageAnim.isVisible = false
            modifyData(commonListOfMovies)}
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isNotEmpty()){
            drawFiltrated(newText)
        }else{
            binding.errorMessageAnim.isVisible = false
            modifyData(commonListOfMovies)}
        return true
    }

    private fun drawFiltrated(query: String?){
        viewModel.searchByQuery(myListToFilter,  query)
        viewModel.listTofilter.observe(viewLifecycleOwner, Observer { filteredList ->
                modifyData(filteredList.toMutableList())
        })
        viewModel.noMatchesForQuery.observe(viewLifecycleOwner, Observer {
            binding.errorMessageAnim.isVisible = it
        })
    }
}