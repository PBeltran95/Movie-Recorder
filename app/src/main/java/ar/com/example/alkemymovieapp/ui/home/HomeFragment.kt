package ar.com.example.alkemymovieapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.application.toast
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.databinding.FragmentHomeBinding
import ar.com.example.alkemymovieapp.presentation.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnMovieClickListener,
    SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MovieViewModel>()
    private val myAdapter by lazy { HomeAdapter(this@HomeFragment) }
    private var commonListOfMovies: MutableList<Movie> = mutableListOf()
    private var myListToFilter: MutableList<Movie> = mutableListOf()
    var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchMovies(page)
        drawEmptyListError()
        lastPageAdvice()
    }

    private fun fetchMovies(page: Int) {
        viewModel.fetchMovies(page).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.rvHome.isVisible = false
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    binding.rvHome.isVisible = true
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

    override fun onMovieClick(movie: Movie) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        with(inflater){
            inflate(R.menu.search_menu, menu)
            inflate(R.menu.home_options, menu)
        }
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@HomeFragment)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.tv_page_number).apply {
            title = page.toString()
            isEnabled = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btn_next -> {
                page++
                requireActivity().invalidateOptionsMenu()
                fetchMovies(page)
            }
            R.id.btn_back -> {
                page--
                requireActivity().invalidateOptionsMenu()
                fetchMovies(page)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(movieList: MutableList<Movie>) {
        setupSizes()
        with(binding){
            rvHome.adapter = myAdapter
            myAdapter.setData(movieList)
            rvHome.scheduleLayoutAnimation()
            rvHome.setHasFixedSize(true)
        }
    }

    private fun setupSizes(){
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvHome.layoutManager = object : GridLayoutManager(requireContext(), 6) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = ((height / 3) * 2)
                    lp.width = width / 6

                    return true
                }
            }
        } else {
            binding.rvHome.layoutManager = object : GridLayoutManager(requireContext(), 3) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.height = height / 3
                    return true
                }
            }
        }
    }

    private fun lastPageAdvice() {
        when (page) {
            1000 -> toast(requireContext(), getString(R.string.last_page))
            1001 -> {
                page = 1
                fetchMovies(page)
            }
        }
    }

    private fun modifyData(data: MutableList<Movie>) {
        myAdapter.setData(data)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.isNotEmpty()) {
            drawFiltrated(query)
        } else {
            binding.errorMessageAnim.isVisible = false
            modifyData(commonListOfMovies)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isNotEmpty()) {
            drawFiltrated(newText)
        } else {
            binding.errorMessageAnim.isVisible = false
            modifyData(commonListOfMovies)
        }
        return true
    }

    private fun drawFiltrated(query: String?) {
        viewModel.searchMovieByTitle(query!!).observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.evaluateListOfResults(it.data.results.isEmpty())
                    modifyData(it.data.results.toMutableList())
                }
                is Resource.Failure -> {}
            }
        }
    }

    private fun drawEmptyListError() {
        viewModel.noMatchesForQuery.observe(viewLifecycleOwner, Observer {
            binding.errorMessageAnim.isVisible = it
        })
    }
}