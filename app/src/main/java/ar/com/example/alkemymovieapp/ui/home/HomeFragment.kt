package ar.com.example.alkemymovieapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import ar.com.example.alkemymovieapp.presentation.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.isVisible
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.toast


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnMovieClickListener,
    SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MovieViewModel>()
    private val myAdapter by lazy { HomeAdapter(this@HomeFragment) }
    private var commonListOfMovies: MutableList<Movie> = mutableListOf()
    private var myListToFilter: MutableList<Movie> = mutableListOf()
    var swipedTimes = 0
    var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchMovies(page)
        listenScroll()
        returnToPageOne()
    }

    private fun returnToPageOne() {
        binding.swipeToRefresh.setOnRefreshListener {
            page = 1
            fetchMovies(page)
            binding.swipeToRefresh.isRefreshing = false
        }
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
                    toast(requireContext(), "Showing page: $page", false)
                }
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    handleApiError(it)
                }
            }
        })
    }

    private fun setupRecyclerView(movieList: MutableList<Movie>) {


        binding.rvHome.adapter = myAdapter
        myAdapter.setData(movieList)
        binding.rvHome.scheduleLayoutAnimation()
        binding.rvHome.setHasFixedSize(true)



        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            binding.rvHome.layoutManager = object : GridLayoutManager(requireContext(), 6) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = ((height / 3) * 2)
                    lp.width = width / 6

                    return true
                }

            }
        } else {
            // portrait
            binding.rvHome.layoutManager = object : GridLayoutManager(requireContext(), 3) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {

                    lp.height = height / 3
                    return true
                }
            }
        }


    }
    private fun listenScroll(){
        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    swipedTimes++
                    changePage()
                }
            }
        })
    }

    private fun changePage() {
        if (swipedTimes != 0) {
            if (swipedTimes % 2 == 0) {
                page++
                fetchMovies(page)
            }else{toast(requireContext(), "Swipe Down to change to page: ${page +1}")}
            when (page){
                1000 -> toast(requireContext(), "Last page")
                1001 -> {page = 1
                        fetchMovies(page)
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
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@HomeFragment)
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
        viewModel.searchByQuery(myListToFilter, query)
        viewModel.listTofilter.observe(viewLifecycleOwner, Observer { filteredList ->
            modifyData(filteredList.toMutableList())
        })
        viewModel.noMatchesForQuery.observe(viewLifecycleOwner, Observer {
            binding.errorMessageAnim.isVisible = it
        })
    }
}