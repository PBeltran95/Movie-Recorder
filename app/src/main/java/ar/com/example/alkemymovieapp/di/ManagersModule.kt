package ar.com.example.alkemymovieapp.di

import ar.com.example.alkemymovieapp.managers.*
import ar.com.example.alkemymovieapp.repository.MovieRepositoryImpl
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
    fun providesUpdateManager(repo: MovieRepositoryImpl): UpdateManager {
        return UpdateManagerImpl(repo)
    }

    @Provides
    fun providesRegisterManager(repo: MovieRepositoryImpl): RegisterManager {
        return RegisterManagerImpl(repo)
    }


}