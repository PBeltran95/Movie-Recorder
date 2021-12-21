package ar.com.example.alkemymovieapp.di

import android.content.Context
import androidx.room.Room
import ar.com.example.alkemymovieapp.application.AppConstants
import ar.com.example.alkemymovieapp.core.WebService
import ar.com.example.alkemymovieapp.data.local.AppDatabase
import ar.com.example.alkemymovieapp.data.local.MovieDao
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepo
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepoImpl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Retrofit
    @Singleton
    @Provides
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()


    @Provides
    fun providesWebService(retrofit: Retrofit) = retrofit.create(WebService::class.java)


    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java,
        "movie_table").build()

    @Provides
    fun providesDao(db:AppDatabase) = db.movieDao()

    @Provides
    fun providesLocalMovieRepoImpl(dao: MovieDao) : LocalMovieRepo {
        return LocalMovieRepoImpl(dao)
    }

}