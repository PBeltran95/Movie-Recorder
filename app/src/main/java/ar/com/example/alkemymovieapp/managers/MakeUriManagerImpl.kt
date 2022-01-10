package ar.com.example.alkemymovieapp.managers

import android.net.Uri

class MakeUriManagerImpl: MakeUriManager {


    override fun makeUri(uri: String): Uri? {
        val formattedTitle = uri.replace("[-,:. ]".toRegex(), "+")
        return Uri.parse("https://www.youtube.com/results?search_query=${formattedTitle.lowercase()}trailer")
    }
}