package ar.com.example.alkemymovieapp.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.application.AppConstants.NOW_PLAYING_MOVIES
import ar.com.example.alkemymovieapp.application.AppConstants.POPULAR_MOVIES
import ar.com.example.alkemymovieapp.application.AppConstants.TOP_RATED_MOVIES
import ar.com.example.alkemymovieapp.application.AppConstants.UPCOMING_MOVIES
import ar.com.example.alkemymovieapp.application.handleApiError
import ar.com.example.alkemymovieapp.core.Resource
import ar.com.example.alkemymovieapp.data.models.Movie
import ar.com.example.alkemymovieapp.databinding.FragmentHomeBinding
import ar.com.example.alkemymovieapp.presentation.MovieViewModel
import ar.com.example.alkemymovieapp.ui.adapters.HomeAdapter
import ar.com.example.alkemymovieapp.ui.utils.VisualUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnMovieClickListener,
    SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MovieViewModel>()
    private val myAdapter by lazy { HomeAdapter(this@HomeFragment) }
    private var commonListOfMovies: MutableList<Movie> = mutableListOf()
    private var myListToFilter: MutableList<Movie> = mutableListOf()
    private var page = 1
    private var movieFilter = POPULAR_MOVIES


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setupPage()
        fetchMovies(page, movieFilter)
        drawEmptyListError()
        setupChips()
        setupRecyclerView()
    }

    private fun setupPage() {
        viewModel.pageNumber.observe(viewLifecycleOwner){ page = it }
    }

    private fun setupChips() {
        binding.popularChip.setOnClickListener { chipClickAction() }
        binding.playingChip.setOnClickListener { chipClickAction() }
        binding.topRatedChip.setOnClickListener { chipClickAction() }
        binding.upcomingChip.setOnClickListener { chipClickAction() }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.popular_chip -> { movieFilter = POPULAR_MOVIES }
                R.id.playing_chip -> { movieFilter = NOW_PLAYING_MOVIES }
                R.id.top_rated_chip -> { movieFilter = TOP_RATED_MOVIES }
                R.id.upcoming_chip -> { movieFilter = UPCOMING_MOVIES }
            }
        }
    }

    private fun chipClickAction() {
        viewModel.resetPageValue()
        requireActivity().invalidateOptionsMenu()
        fetchMovies(page, movieFilter)
    }

    private fun fetchMovies(page: Int, movieFilter: String) {
        viewModel.fetchMovies(movieFilter, page).observe(viewLifecycleOwner) {
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
                    setupRecyclerView()
                    modifyData(movieList.toMutableList())
                }
                is Resource.Failure -> {
                    binding.progressBar.isVisible = false
                    handleApiError(it)
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
        with(inflater){
            inflate(R.menu.search_menu, menu)
            inflate(R.menu.home_options, menu)
        }
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.apply {
            queryHint = context.getString(R.string.home_query_hint)
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@HomeFragment)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.tv_page_number).apply {
            title = page.toString()
            isEnabled = false
        }
        menu.findItem(R.id.btn_back).apply { isEnabled = page != 1 }
        menu.findItem(R.id.btn_next).apply { isEnabled = page != 1000 }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.btn_next -> {
                viewModel.incrementPage()
                requireActivity().invalidateOptionsMenu()
                fetchMovies(page, movieFilter)
            }
            R.id.btn_back -> {
                viewModel.decrementPage()
                requireActivity().invalidateOptionsMenu()
                fetchMovies(page, movieFilter)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        setupSizes()
        with(binding){
            rvHome.adapter = myAdapter
            rvHome.scheduleLayoutAnimation()
            rvHome.setHasFixedSize(true)
        }
    }

    private fun setupSizes(){
        binding.rvHome.layoutManager = VisualUtils.setupSizes(resources, requireContext())
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
                is Resource.Success -> {
                    viewModel.evaluateListOfResults(it.data.results.isEmpty())
                    modifyData(it.data.results.toMutableList())
                }
                else -> {}
            }
        }
    }

    private fun drawEmptyListError() {
        viewModel.noMatchesForQuery.observe(viewLifecycleOwner) {
            binding.errorMessageAnim.isVisible = it
        }
    }
}