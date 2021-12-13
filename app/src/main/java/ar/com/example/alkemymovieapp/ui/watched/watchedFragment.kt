package ar.com.example.alkemymovieapp.ui.watched

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ar.com.example.alkemymovieapp.R
import ar.com.example.alkemymovieapp.databinding.FragmentWatchedBinding

class WatchedFragment : Fragment(R.layout.fragment_watched) {

    private lateinit var binding: FragmentWatchedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWatchedBinding.bind(view)
    }
}