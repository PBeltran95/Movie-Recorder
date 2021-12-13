package ar.com.example.alkemymovieapp.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
    }
}