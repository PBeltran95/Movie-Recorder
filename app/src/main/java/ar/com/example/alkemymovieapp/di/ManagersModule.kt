package ar.com.example.alkemymovieapp.di

import ar.com.example.alkemymovieapp.managers.*
import ar.com.example.alkemymovieapp.repository.RemoteMovieRepository
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepository
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

    @Provides
    fun providesDetailsCacheManager(localMovieRepository: LocalMovieRepository, remoteMovieRepository: RemoteMovieRepository): MovieDetailsCacheManager {
        return MovieDetailsCacheManagerImpl(remoteMovieRepository, localMovieRepository)
    }

    @Provides
    fun providesUpDateManager(repo: LocalMovieRepository): UpdateManager {
        return UpdateManagerImpl(repo)
    }

    @Provides
    fun providesRegisterManager(repo: LocalMovieRepository): RegisterManager {
        return RegisterManagerImpl(repo)
    }


}