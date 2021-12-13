package ar.com.example.alkemymovieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavController()
        hideBottomNavBar()
    }

    private fun hideBottomNavBar() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.detailFragment -> binding.bottomNavigationBar.isVisible = false
                else -> binding.bottomNavigationBar.isVisible = true
            }
        }
    }

    private fun initNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationBar.setupWithNavController(navController)
    }
}