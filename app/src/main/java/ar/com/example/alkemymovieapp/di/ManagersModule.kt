package ar.com.example.alkemymovieapp.di

import ar.com.example.alkemymovieapp.managers.MakeUriManager
import ar.com.example.alkemymovieapp.managers.MakeUriManagerImpl
import ar.com.example.alkemymovieapp.managers.SearchManager
import ar.com.example.alkemymovieapp.managers.SearchMangerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ManagersModule {

    @Provides
    fun providesMakeUriManager(): MakeUriManager {
        return MakeUriManagerImpl()
    }

    @Provides
    fun providesSearchManager(): SearchManager {
        return SearchMangerImpl()
    }


}